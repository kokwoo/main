package Tempo.Commands;

import java.util.*;
import Tempo.CalendarObjects.*;
import Tempo.Logic.Calendar;

public class UndoUpdate implements Command {
	private static Calendar calendar = Calendar.getInstance();
	
	private static final String CMD_UNDO = "undo <update %1$s %2$s>";
	private static final String OBJ_EVENT = "event";
	private static final String OBJ_TASK = "task";
	private static final String OBJ_FLOATING = "floating task";
	
	private String objType;
	private String nameOfPrevObj;
	
	private int prevModIndex;
	private Event prevModEvent;
	private Task prevModTask;
	private FloatingTask prevModFloating;
	
	private ArrayList<CalendarObject> prevModEvents;
	private ArrayList<CalendarObject> prevModTasks;

	private boolean isEvent = false;
	private boolean isTask = false;
	private boolean isFloatingTask = false;
	private boolean isEventsSeries = false;
	
	public UndoUpdate(Event event) {
		prevModEvent = event;
		prevModIndex = event.getIndex();
		isEvent = true;
		objType = OBJ_EVENT;
	}
	
	public UndoUpdate(Task task) {
		prevModTask = task;
		prevModIndex = task.getIndex();
		isTask = true;
		objType = OBJ_TASK;
	}
	
	public UndoUpdate(FloatingTask floatingTask) {
		prevModFloating = floatingTask;
		prevModIndex = floatingTask.getIndex();
		isFloatingTask = true;
		objType = OBJ_FLOATING;
	}
	
	public UndoUpdate(ArrayList<CalendarObject> series, boolean isEventsSeries) {
		this.isEventsSeries = isEventsSeries;
		initialiseSeries(series);
	}
	
	private void initialiseSeries(ArrayList<CalendarObject> series) {
		if (isEventsSeries) {
			prevModEvents = series;
			Event event = (Event) series.get(0);
			prevModIndex = event.getIndex();
			objType = OBJ_EVENT;
		} else {
			prevModTasks = series;
			Task task = (Task) series.get(0);
			prevModIndex = task.getIndex();
			objType = OBJ_TASK;
		}
	}

	public Result execute() {
		initialiseNameOfPrevObj();
		Result result;
		
		if (isEvent) {
			result = undoUpdateEvent();
		} else if (isTask) {
			result = undoUpdateTask();
		} else if (isFloatingTask) {
			result = undoUpdateFloating();
		} else if (isEventsSeries) {
			result = undoUpdateEventsSeries();
		} else {
			result = undoUpdateTasksSeries();
		}
		
		String command = String.format(CMD_UNDO, objType, 
									   nameOfPrevObj);
		result.setCommand(command);
		return result;
	}
	
	private Result undoUpdateEvent() {
		Result result;
		result = calendar.removeEvent(prevModIndex, isEventsSeries);
		removeUndoUndoCommand();
		result = calendar.addBackEvent(prevModEvent);
		return result;
	}
	
	private Result undoUpdateTask() {
		Result result;
		result = calendar.removeTask(prevModIndex, isEventsSeries);
		removeUndoUndoCommand();
		result = calendar.addBackTask(prevModTask);
		return result;
	}
	
	private Result undoUpdateFloating() {
		Result result;
		result = calendar.removeFloatingTask(prevModIndex, isEventsSeries);
		removeUndoUndoCommand();
		result = calendar.addBackFloating(prevModFloating);
		return result;
	}
	
	private Result undoUpdateEventsSeries() {
		Result result;
		result = calendar.removeEvent(prevModIndex, isEventsSeries);
		removeUndoUndoCommand();
		result = calendar.addBackRecurrEvent(prevModEvents);
		return result;
	}
	
	private Result undoUpdateTasksSeries() {
		Result result;
		result = calendar.removeTask(prevModIndex, isEventsSeries);
		removeUndoUndoCommand();
		result = calendar.addBackRecurrTask(prevModTasks);
		return result;
	}
	
	private void removeUndoUndoCommand() {
		calendar.removeLastUndo();
	}
	
	private void initialiseNameOfPrevObj() {
		if (isEvent) {
			nameOfPrevObj = prevModEvent.getName();
		} else if (isTask) {
			nameOfPrevObj = prevModTask.getName();
		} else if (isFloatingTask) {
			nameOfPrevObj = prevModFloating.getName();	
		} else if (isEventsSeries) {
			Event event = (Event) prevModEvents.get(0);
			nameOfPrevObj = event.getName();
		} else {
			Task task = (Task) prevModTasks.get(0);
			nameOfPrevObj = task.getName();
		}
	}

}
