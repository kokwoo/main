//@@author A0127047J
package Tempo.Commands;

import Tempo.Logic.Calendar;

public class UndoDone implements Command {
	private static Calendar cal = Calendar.getInstance();
	
	private static final String CMD_UNDO = "undo <%1$s %2$s %3$s>";
	private static final String CMD_DONE = "done";
	private static final String CMD_UNDONE = "undone";
	
	private static final String OBJ_TASK = "task";
	private static final String OBJ_FLOATING = "floating task";
	
	private String cmdType;
	private String objType;
	
	private int prevModIndex;
	private boolean isFloating;
	private boolean isDoneCmd = false;
	
	public UndoDone(int prevModIndex, boolean isFloating, 
					boolean isDoneCmd) {
		this.prevModIndex = prevModIndex;
		this.isFloating = isFloating;
		
		if(isFloating) {
			objType = OBJ_FLOATING;
		} else {
			objType = OBJ_TASK;
		}
		
		this.isDoneCmd = isDoneCmd;
	}
	
	public Result execute() {
		Result result;
		
		if (isDoneCmd) {
			cmdType = CMD_DONE;
			result = executeMarkUndone();
		} else {
			cmdType = CMD_UNDONE;
			result = executeMarkDone();
		}
		
		String nameOfItem = getName(result.getCmdPerformed());
		String command = String.format(CMD_UNDO, cmdType, objType, 
									   nameOfItem);
		result.setCommand(command);
		
		removeUndoUndoCommand();
		
		return result;
	}
	
	private Result executeMarkUndone() {
		if(isFloating) {
			return cal.markFloatingTaskAsUndone(prevModIndex);
		} else {
			return cal.markTaskAsUndone(prevModIndex); 
		}
	}
	
	private Result executeMarkDone() {
		if (isFloating) {
			return cal.markFloatingTaskAsDone(prevModIndex);
		} else {
			return cal.markTaskAsDone(prevModIndex);
		}
	}
	
	private void removeUndoUndoCommand() {
		cal.removeLastUndo();
	}
	
	private String getName(String feedbackStr) {
		String[] params = feedbackStr.trim().split(" task ");
		return params[1].trim();
	}
}
