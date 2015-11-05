package Tempo.Commands;

import Tempo.Logic.Calendar;

public class UndoAdd implements Command {
	private static Calendar calendar = Calendar.getInstance();
	
	private static final String CMD = "Undo add %1$s";
	
	private int prevModIndex;
	
	private boolean isEvent = false;
	private boolean isTask = false;
	private boolean isSeries = false;
	
	public UndoAdd(int prevModIndex, boolean isEvent, boolean isTask, boolean isSeries) {
		this.prevModIndex = prevModIndex;
		this.isEvent = isEvent;
		this.isTask = isTask;
		this.isSeries = isSeries;
	}
	
	public Result execute() {
		Result result;
		
		if (isEvent) {
			result = calendar.removeEvent(prevModIndex, isSeries);
		} else if (isTask) {
			result = calendar.removeTask(prevModIndex, isSeries);
		} else {
			result = calendar.removeFloatingTask(prevModIndex, isSeries);
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
