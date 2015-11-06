package Tempo.Commands;

import Tempo.Logic.Calendar;
import Tempo.Logic.*;
import Tempo.CalendarObjects.*;

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
	
	public UndoAdd(int prevModIndex, boolean isEvent, boolean isTask, boolean isSeries) {
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
			result = cal.removeEvent(prevModIndex, isSeries);
		} else if (isTask) {
			objType = OBJ_TASK;
			result = cal.removeTask(prevModIndex, isSeries);
		} else {
			objType = OBJ_FLOATING;
			result = cal.removeFloatingTask(prevModIndex, isSeries);
		}
			
		String command = String.format(CMD_UNDO, objType, nameOfPrevObj);
		result.setCommand(command);
		
		removeUndoUndoCommand();
				
		return result;
	}
	
	private void removeUndoUndoCommand() {
		cal.removeLastUndo();
	}
	
	private void initialiseNameOfPrevObj() {
		if (isEvent) {
			Event event = (Event) idxStore.getEventById(prevModIndex);
			nameOfPrevObj = event.getName();
		} else {
			FloatingTask task = (FloatingTask) idxStore.getTaskById(prevModIndex);
			nameOfPrevObj = task.getName();
		}
	}

}
