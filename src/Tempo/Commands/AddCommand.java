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
	private static final String DEFAULT_NAME = "<NO NAME>";
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
		String name = replaceNullName(params.get(0));
		String start = replaceNullStart(params.get(1));
		String end = replaceNullEnd(params.get(2));
		
		String command = String.format(ADD_EVENT, name);
		
		HashMap<String, ArrayList<FloatingTask>> result = cal.addEvent(name, start, end);
		
		return new Result(command, true, result);
	}
	
	private Result addRecurringEvent(){
		
	}
	
	private ArrayList<String> addTask() {
		String name = replaceNullName(params.get(0));
		String dueDate = params.get(1);
		return cal.addTask(name, dueDate);
	}
	
	private Result addRecurringTask(){
		
	}
	
	private ArrayList<String> addFloatingTask() {
		String name = replaceNullName(params.get(0));
		return cal.addFloatingTask(name);
	}
	
	private String replaceNullName(String name) {
		if (isEmpty(name)) {
			return DEFAULT_NAME;
		}
		return name;
	}
	
	private String replaceNullStart(String dateWithTime) {
		if (isEmpty(dateWithTime)) { 
			// replace with current date and time
			return dateTimeFormat.format(getCurrDateTime());
		}
		return dateWithTime;
	}
	
	private String replaceNullEnd(String dateWithTime) {
		if (isEmpty(dateWithTime)) {
			String currTime = timeFormat.format(getCurrDateTime());
			String currDate = dateFormat.format(getCurrDateTime());
			
			currTime = addTwoHours(currTime);
			
			return currDate + DELIMETER_SPACE + currTime;
		}
		return dateWithTime;
	}
	
	private String addTwoHours(String time) {
		String[] params = time.trim().split(DELIMETER_COLON);
		int updatedHour = Integer.valueOf(params[1]) + 2;
		
		if (updatedHour >= NUM_HOURS_IN_A_DAY) {
			updatedHour -= NUM_HOURS_IN_A_DAY;
		}
	
		return updatedHour + DELIMETER_COLON + params[1];
	}
	
	private boolean isEmpty(String input) {
		return (input == null || input == BLANK);
	}
		
	private Date getCurrDateTime() {
		return new Date();
	}
}
