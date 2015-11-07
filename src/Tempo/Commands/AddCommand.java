package Tempo.Commands;

import java.util.*;
import java.text.*;
import Tempo.Logic.Calendar;
import Tempo.Logic.*;

public class AddCommand implements Command {
	private Calendar cal;
	private ArrayList<String> cmdArgs;
	private boolean isRecurring = false;
	private String recurringFrequency;
	private String recurrenceEndDate;
	
	private static final int LENGTH_ADD_EVENT_PARAMS = 3;
	private static final int LENGTH_ADD_TASK_PARAMS = 2;
	
	private static final String ADD_EVENT = "add Event %1$s";
	private static final String ADD_TASK = "add Task %1$s";
	private static final String ADD_FLOATINGTASK = "add Floating Task %1$s";
	private static final String DELIMETER_TIME = ":";
	private static final String DELIMETER_DATE = "/";
	private static final String STR_EMPTY = "";
	
	private static final String INVALID_DATE = "Error: Date(s) entered is invalid!";
	private static final String END_EARLIER_STRING = "Error: End time is earlier than start time!";
	private static final String BLANK = "";
	
	private DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private DateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	public AddCommand(Calendar cal, ArrayList<String> params) {
		this.cal = cal;
		this.cmdArgs = params;
	}
	
	public AddCommand(Calendar cal, ArrayList<String> params, boolean isRecurring, String recurringType, String recurrenceEndDate) {
		this.cal = cal;
		this.cmdArgs = params;
		this.isRecurring = isRecurring;
		this.recurringFrequency = recurringType;
		this.recurrenceEndDate = recurrenceEndDate;
	}
	
	@Override
	public Result execute() {
		saveCommand();
		if (cmdArgs.size() == LENGTH_ADD_EVENT_PARAMS) {
			if(cmdArgs.get(1) == null || cmdArgs.get(2) == null){
				return new Result(INVALID_DATE, true, true, null);
			}
			return executeAddEvent();
		} else if (cmdArgs.size() == LENGTH_ADD_TASK_PARAMS) {
			if(cmdArgs.get(1) == null){
				return new Result(INVALID_DATE, true, true, null);
			}
			return executeAddTask();
		} else {
			return addFloatingTask();		
		}
	}
	
	private Result executeAddEvent() {
		if (isRecurring) {
			return addRecurringEvent();
		} else {
			return addEvent();
		}
	}
	
	private Result executeAddTask() {
		if (isRecurring) {
			return addRecurringTask();
		} else {
			return addTask();
		}
	}
	
	private Result addEvent() {
		String name = cmdArgs.get(0);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_EVENT, STR_EMPTY), false, null);
		}
		
		String start = replaceNullStart(cmdArgs.get(1));
		String end = replaceNullEnd(start, cmdArgs.get(2));
			
		Result result = cal.addEvent(name, start, end);
		
		return result;
	}
	
	private Result addRecurringEvent(){
		String name = cmdArgs.get(0);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_EVENT, STR_EMPTY), false, null);
		}
		
		String start = replaceNullStart(cmdArgs.get(1));
		String end = replaceNullEnd(start, cmdArgs.get(2));
			
		Result result = cal.addRecurringEvent(name, start, end, recurringFrequency, recurrenceEndDate);
		
		return result;
	}
	
	private Result addTask() {
		String name = cmdArgs.get(0);
		String dueDate = cmdArgs.get(1);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_TASK, STR_EMPTY), false, null);
		}
		
		return cal.addTask(name, dueDate);
	}
	
	private Result addRecurringTask(){
		String name = cmdArgs.get(0);
		String dueDate = cmdArgs.get(1);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_TASK, STR_EMPTY), false, null);
		}
		
		return cal.addRecurringTask(name, dueDate, recurringFrequency, recurrenceEndDate);
	}
	
	private Result addFloatingTask() {
		String name = cmdArgs.get(0);
		
		if (!hasValidName(name)) {
			return new Result(String.format(ADD_FLOATINGTASK, STR_EMPTY), false, null);
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
		return getDateTimeStr(startDate);
	}
	
	private boolean isEmptyInput(String input) {
		return (input == null || input == STR_EMPTY);
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
	
	private String getDateStr(String dateStr) {
		return dateFormat.format(dateStr);
	}
	
	private String getTimeStr(String dateStr) {
		return timeFormat.format(dateStr);
	}
	
	private String getDateTimeStr(GregorianCalendar date) {
		return dateTimeFormat.format(date);
	}
		
	private GregorianCalendar getCalendarDate(String dateStr) {
		String date = getDateStr(dateStr);
		String[] dateParams = date.trim().split(DELIMETER_DATE);
		
		int year = Integer.valueOf(dateParams[2]);
		int month = Integer.valueOf(dateParams[1]);
		int day = Integer.valueOf(dateParams[0]);
		
		String time = getTimeStr(dateStr);
		String[] timeParams = time.trim().split(DELIMETER_TIME);
		
		int hour = Integer.valueOf(timeParams[0]);
		int min = Integer.valueOf(timeParams[1]);
		
		return new GregorianCalendar(year, month, day, hour, min);
	}
	
	private void saveCommand() {
		if (isRecurring) {
			cal.saveCmd((Command) new AddCommand(cal, cmdArgs, isRecurring, recurringFrequency, recurrenceEndDate));
		} else {
			cal.saveCmd((Command) new AddCommand(cal, cmdArgs));
		}
	}
}
