package Tempo.Commands;

import java.util.*;

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
	private static final int LENGTH_ADD_FLOATING_TASK_PARAMS = 1;
	
	private static final String ADD_EVENT = "Add Event %1$s";
	private static final String ADD_TASK = "Add Task %1$s";
	private static final String ADD_FLOATINGTASK = "Add Floating Task %1$s";
	
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
		} else if (params.size() == LENGTH_ADD_FLOATING_TASK_PARAMS){
			return addFloatingTask();		
		}else{
			return new Result(commandString, false, null);
		}
	}
	
	private Result addEvent() {
		String name = params.get(0);
		String start = params.get(1);
		String end = params.get(2);
		
		String command = String.format(ADD_EVENT, name);
		
		 HashMap<String, ArrayList<FloatingTask>> result = cal.addEvent(name, start, end);
		
		return new Result(command, true, result);
	}
	
	private Result addRecurringEvent(){
		
	}
	
	private ArrayList<String> addTask() {
		String name = params.get(0);
		String dueDate = params.get(1);
		return cal.addTask(name, dueDate);
	}
	
	private Result addRecurringTask(){
		
	}
	
	private ArrayList<String> addFloatingTask() {
		String name = params.get(0);
		return cal.addFloatingTask(name);
	}
}
