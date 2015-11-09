package Tempo.Commands;

import Tempo.Logic.Calendar;
import Tempo.Data.*;
import Tempo.Logic.*;

//@@author A0127047J
public class UndoAdd implements Command {
	private static Calendar cal = Calendar.getInstance();
	private static IndexStore idxStore = IndexStore.getInstance();
	
	private static final String CMD_UNDO = "undo <add %1$s %2$s>";
	private static final String OBJ_EVENT = "event";
	private static final String OBJ_TASK = "task";
	private static final String OBJ_FLOATING = "floating task";
	
	private int prevModIndex;
	private String nameOfPrevObj;
	private String objType;
	
	private boolean isEvent = false;
	private boolean isTask = false;
	private boolean isSeries = false;
	
	public UndoAdd(int prevModIndex, boolean isEvent, boolean isTask, 
					boolean isSeries) {
		this.prevModIndex = prevModIndex;
		this.isEvent = isEvent;
		this.isTask = isTask;
		this.isSeries = isSeries;
		initialiseNameOfPrevObj();
	}
	
	public Result execute() {
		Result result;
		
		if (isEvent) {
			objType = OBJ_EVENT;
			result = executeRemoveEvent();
		} else if (isTask) {
			objType = OBJ_TASK;
			result = executeRemoveTask();
		} else {
			objType = OBJ_FLOATING;
			result = executeRemoveFloating();
		}
			
		String cmd = String.format(CMD_UNDO, objType, nameOfPrevObj);
		result.setCommand(cmd);
		removeUndoUndoCommand();
				
		return result;
	}
	
	private Result executeRemoveEvent() {
		return cal.removeEvent(prevModIndex, isSeries);
	}
	
	private Result executeRemoveTask() {
		return cal.removeTask(prevModIndex, isSeries);
	}
	
	private Result executeRemoveFloating() {
		return cal.removeFloatingTask(prevModIndex, isSeries);
	}
	
	private void removeUndoUndoCommand() {
		cal.removeLastUndo();
	}
	
	private void initialiseNameOfPrevObj() {
		if (isEvent) {
			Event event = (Event) idxStore.getEventById(prevModIndex);
			setNameOfPrevObj(event.getName());
		} else {
			FloatingTask task = (FloatingTask) idxStore.getTaskById(
														prevModIndex);
			setNameOfPrevObj(task.getName());
		}
	}
	
	private void setNameOfPrevObj(String name) {
		nameOfPrevObj = name;
	}

}
