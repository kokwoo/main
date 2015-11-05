package Tempo.Commands;

import Tempo.Logic.Calendar;

public class UndoDone implements Command {
	private static Calendar cal = Calendar.getInstance();
	private static final String CMD = "Undo done %1$s";
	
	private int prevModIndex;
	private boolean isFloating;
	
	public UndoDone(int prevModIndex, boolean isFloating) {
		this.prevModIndex = prevModIndex;
		this.isFloating = isFloating;
	}
	
	public Result execute() {
		Result result;
		
		if(isFloating) {
			result = cal.markFloatingTaskAsUndone(prevModIndex);
		} else {
			result = cal.markTaskAsUndone(prevModIndex); 
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
