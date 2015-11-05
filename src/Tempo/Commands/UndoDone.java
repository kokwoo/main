package Tempo.Commands;

import Tempo.Logic.Calendar;

public class UndoDone implements Command {
	private static Calendar cal = Calendar.getInstance();
	private static final String CMD = "Undo done %1$s";
	
	private int prevModIndex;
	private boolean isFloating;
	private boolean isDoneCmd = false;
	
	public UndoDone(int prevModIndex, boolean isFloating, boolean isDoneCmd) {
		this.prevModIndex = prevModIndex;
		this.isFloating = isFloating;
		this.isDoneCmd = isDoneCmd;
	}
	
	public Result execute() {
		Result result;
		
		if (isDoneCmd) {
			if(isFloating) {
				result = cal.markFloatingTaskAsUndone(prevModIndex);
			} else {
				result = cal.markTaskAsUndone(prevModIndex); 
			}
		} else {
			if (isFloating) {
				result = cal.markFloatingTaskAsDone(prevModIndex);
			} else {
				result = cal.markTaskAsDone(prevModIndex);
			}
		}
		
		String command = result.getCommandPerformed();
		String name = getLastWord(command);
		result.setCommand(String.format(CMD, name));
		
		return result;
	}
	
	private String getLastWord(String text) {
		String[] params = text.trim().split(" ");
		return params[params.length-1];
	}
}
