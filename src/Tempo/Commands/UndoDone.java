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
	
	public UndoDone(int prevModIndex, boolean isFloating, boolean isDoneCmd) {
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
			if(isFloating) {
				result = cal.markFloatingTaskAsUndone(prevModIndex);
			} else {
				result = cal.markTaskAsUndone(prevModIndex); 
			}
		} else {
			cmdType = CMD_UNDONE;
			if (isFloating) {
				result = cal.markFloatingTaskAsDone(prevModIndex);
			} else {
				result = cal.markTaskAsDone(prevModIndex);
			}
		}
		
		String nameOfItem = getName(result.getCommandPerformed());
		String command = String.format(CMD_UNDO, cmdType, objType, nameOfItem);
		result.setCommand(command);
		
		removeUndoUndoCommand();
		
		return result;
	}
	
	private void removeUndoUndoCommand() {
		cal.removeLastUndo();
	}
	
	private String getName(String feedbackStr) {
		String[] params = feedbackStr.trim().split(" task ");
		return params[1].trim();
	}
}
