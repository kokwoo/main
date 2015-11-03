package Tempo.Commands;

import java.util.*;
import java.text.*;

import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;
import Tempo.Logic.Calendar;

public class AddCommand implements Command {
	private String commandString;
	private Calendar cal;
	private ArrayList<String> params;
	private boolean isRecurring;
	private String recurringType;
	private String recurrenceEndDate;
	
	private static final int LENGTH_ADD_EVENT_PARAMS = 3;
	private static final int LENGTH_ADD_TASK_PARAMS = 2;
	private static final int NUM_HOURS_IN_A_DAY = 24;
	
	private static final String ADD_EVENT = "Add Event %1$s";
	private static final String ADD_TASK = "Add Task %1$s";
	private static final String ADD_FLOATINGTASK = "Add Floating Task %1$s";
	private static final String DELIMETER_SPACE = " ";
	private static final String DELIMETER_COLON = ":";
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
		if (params.size() == LENGTH_ADD_EVENT_PARAMS) {
			return addEvent();
		} else if (params.size() == LENGTH_ADD_TASK_PARAMS) {
			return addTask();
		} else {
			return addFloatingTask();		
		}
	}
	
	private Result addEvent() {
		String name = params.get(0);
		
		if (!hasValidName(name)) {
			return Result(String.format(ADD_EVENT, BLANK), false, null);
		}
		
		String start = replaceNullStart(params.get(1));
		String end = replaceNullEnd(start, params.get(2));
	
		String command = String.format(ADD_EVENT, name);
		
		HashMap<String, ArrayList<FloatingTask>> result = cal.addEvent(name, start, end);
		
		return new Result(command, true, result);
	}
	
	private Result addRecurringEvent(){
		return new Result(null, false, null); // TODO:
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
		// TODO: after we rename our Calendar class	
		return "";
	}
	
	private boolean isEmptyInput(String input) {
		return (input == null || input == BLANK);
	}
	
	private boolean hasValidName(String name) {
		return isEmptyInput(name);
	}
		
	private Date getCurrDateTime() {
		return new Date();
	}
	
	private String getCurrDateTimeStr() {
		return dateTimeFormat.format(getCurrDateTime());
	}
	
	private String getCurrDateStr() {
		return dateFormat.format(getCurrDateTime());
	}
	
	private String getCurrTimeStr() {
		return timeFormat.format(getCurrDateTime());
	}
}
