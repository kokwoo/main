package Tempo.Commands;

import Tempo.Logic.Calendar;

//@@author A0127047J
public class UndoCommand implements Command {
	private Calendar cal;
	
	public UndoCommand(Calendar cal) {
		this.cal  = cal;
	}

	public Result execute() {
		return cal.undo();
	}
	
}
