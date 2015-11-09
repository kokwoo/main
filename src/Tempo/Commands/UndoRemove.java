//@@author A0127047J
package Tempo.Commands;

import java.util.*;

import Tempo.Data.*;
import Tempo.Logic.Calendar;

public class UndoRemove implements Command {
	private static Calendar cal = Calendar.getInstance();
	
	private static final String CMD_UNDO = "undo <remove %1$s %2$s>";
	private static final String OBJ_EVENT = "event";
	private static final String OBJ_TASK = "task";
	private static final String OBJ_FLOATING = "floating task";
	
	private String objType;
	private String nameOfPrevObj;
		
	private Event prevModEvent;
	private Task prevModTask;
	private FloatingTask prevModFloating;
	
	private ArrayList<CalendarObject> prevModEvents;
	private ArrayList<CalendarObject> prevModTasks;
	
	private boolean isEvent = false;
	private boolean isTask = false;
	private boolean isFloatingTask = false;
	private boolean isEventsSeries = false;
	
	public UndoRemove(Event event) {
		prevModEvent = event;
		isEvent = true;
		objType = OBJ_EVENT;
	}
	
	public UndoRemove(Task task) {
		prevModTask = task;
		isTask = true;
		objType = OBJ_TASK;
	}
	
	public UndoRemove(FloatingTask floatingTask) {
		prevModFloating = floatingTask;
		isFloatingTask = true;
		objType = OBJ_FLOATING;
	}
	
	public UndoRemove(ArrayList<CalendarObject> series, 
					  boolean isEventsSeries) {
		this.isEventsSeries = isEventsSeries;
		initialiseSeries(series);
	}
	
	public Result execute() {
		Result result;
		
		if (isEvent) {
			result = executeRemoveEvent();
		} else if (isTask) {
			result = executeRemoveTask();
		} else if (isFloatingTask) {
			result = executeRemoveFloating();
		} else if (isEventsSeries) {
			result = executeRemoveEventsSeries();
		} else {
			result = executeRemoveTasksSeries();
		}
		
		initialiseNameOfPrevObj();
		String command = String.format(CMD_UNDO, objType, 
									   nameOfPrevObj);
		result.setCommand(command);
				
		return result;
	}
	
	private Result executeRemoveEvent() {
		return cal.addBackEvent(prevModEvent);
	}
	
	private Result executeRemoveTask() {
		return cal.addBackTask(prevModTask);
	}
	
	private Result executeRemoveFloating() {	
		return cal.addBackFloating(prevModFloating);
	}
	
	private Result executeRemoveEventsSeries() {
		return cal.addBackRecurrEvent(prevModEvents);
	}
	
	private Result executeRemoveTasksSeries() {
		return cal.addBackRecurrTask(prevModTasks);
	}
	
	private void initialiseSeries(ArrayList<CalendarObject> series) {
		if (isEventsSeries) {
			prevModEvents = series;
			objType = OBJ_EVENT;
		} else {
			prevModTasks = series;
			objType = OBJ_TASK;
		}
	}
	
	private void initialiseNameOfPrevObj() {
		if (isEvent) {
			setNameOfPrevObj(prevModEvent.getName());
		} else if (isTask) {
			setNameOfPrevObj(prevModTask.getName());
		} else if (isFloatingTask) {
			setNameOfPrevObj(prevModFloating.getName());
		} else if (isEventsSeries) {
			Event event = (Event) prevModEvents.get(0);
			setNameOfPrevObj(event.getName());
		} else {
			Task task = (Task) prevModTasks.get(0);
			setNameOfPrevObj(task.getName());
		}
	}
	
	private void setNameOfPrevObj(String name) {
		nameOfPrevObj = name;
	}
}
