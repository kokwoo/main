package Tempo.Commands;

import java.util.*;
import java.text.*;
import Tempo.Logic.Calendar;
import Tempo.Logic.*;

public class AddCommand implements Command {
	private Calendar cal;
	private ArrayList<String> params;
	private boolean isRecurring = false;
	private String recurringType;
	private String recurrenceEndDate;
	
	private static final int LENGTH_ADD_EVENT_PARAMS = 3;
	private static final int LENGTH_ADD_TASK_PARAMS = 2;
	
	private static final String ADD_EVENT = "Add Event %1$s";
	private static final String ADD_TASK = "Add Task %1$s";
	private static final String ADD_FLOATINGTASK = "Add Floating Task %1$s";
	private static final String DELIMETER_TIME = ":";
	private static final String DELIMETER_DATE = "/";
	private static final String BLANK = "";
	
	private DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private DateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	public AddCommand(Calendar cal, ArrayList<String> params) {
		this.cal = cal;
		this.params = params;
	}
	
	public AddCommand(Calendar cal, ArrayList<String> params, boolean isRecurring, String recurringType, String recurrenceEndDate) {
		this.cal = cal;
		this.params = params;
		this.isRecurring = isRecurring;
		this.recurringType = recurringType;
		this.recurrenceEndDate = recurrenceEndDate;
	}
	
	@Override
	public Result execute() {
		if (isRecurring) {
			cal.saveCmd((Command) new AddCommand(cal, params, isRecurring, recurringType, recurrenceEndDate));
		} else {
			cal.saveCmd((Command) new AddCommand(cal, params));
		}
		if (params.size() == LENGTH_ADD_EVENT_PARAMS) {
			if(isRecurring){
				return addRecurringEvent();
			}else{
				return addEvent();
			}
		} else if (params.size() == LENGTH_ADD_TASK_PARAMS) {
			if(isRecurring){
				return addRecurringTask();
			}else{
				return addTask();
			}
		} else {
			return addFloatingTask();		
		}
	}
	
	private Result addEvent() {
		String name = params.get(0);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_EVENT, BLANK), false, null);
		}
		
		String start = replaceNullStart(params.get(1));
		String end = replaceNullEnd(start, params.get(2));
			
		Result result = cal.addEvent(name, start, end);
		
		return result;
	}
	
	private Result addRecurringEvent(){
		String name = params.get(0);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_EVENT, BLANK), false, null);
		}
		
		String start = replaceNullStart(params.get(1));
		String end = replaceNullEnd(start, params.get(2));
			
		Result result = cal.addRecurringEvent(name, start, end, recurringType, recurrenceEndDate);
		
		return result;
	}
	
	private Result addTask() {
		String name = params.get(0);
		String dueDate = params.get(1);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_TASK, BLANK), false, null);
		}
		
		return cal.addTask(name, dueDate);
	}
	
	private Result addRecurringTask(){
		String name = params.get(0);
		String dueDate = params.get(1);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_TASK, BLANK), false, null);
		}
		
		return cal.addRecurringTask(name, dueDate, recurringType, recurrenceEndDate);
	}
	
	private Result addFloatingTask() {
		String name = params.get(0);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_FLOATINGTASK, BLANK), false, null);
		}
		
		return cal.addFloatingTask(name);
	}
	
	private String replaceNullStart(String dateWithTime) {
		if (isEmptyInput(dateWithTime)) { 
			return getCurrDateTimeStr();
		}
		return dateWithTime;
	}
	
	private String replaceNullEnd(String start, String end) {
		if (isEmptyInput(end)) {
			return addTwoHours(start);
		}
		return end;
	}
	
	private String addTwoHours(String start) {
		GregorianCalendar startDate = getCalendarDate(start);
		startDate.add(GregorianCalendar.HOUR, 2);
		return dateTimeFormat.format(startDate);
	}
	
	private boolean isEmptyInput(String input) {
		return (input == null || input == BLANK);
	}
	
	private boolean hasValidName(String name) {
		return !isEmptyInput(name);
	}
		
	private CurrentTime getCurrDateTime() {
		return new CurrentTime();
	}
	
	private String getCurrDateTimeStr() {
		CurrentTime curr = getCurrDateTime();
		return curr.getDateAndTime();
	}
		
	private GregorianCalendar getCalendarDate(String dateStr) {
		String date = dateFormat.format(dateStr);
		String[] dateParams = date.trim().split(DELIMETER_DATE);
		
		int year = Integer.valueOf(dateParams[2]);
		int month = Integer.valueOf(dateParams[1]);
		int day = Integer.valueOf(dateParams[0]);
		
		String time = timeFormat.format(dateStr);
		String[] timeParams = time.trim().split(DELIMETER_TIME);
		
		int hour = Integer.valueOf(timeParams[0]);
		int min = Integer.valueOf(timeParams[1]);
		
		return new GregorianCalendar(year, month, day, hour, min);
	}
}
