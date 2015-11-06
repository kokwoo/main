package Tempo.Commands;

import Tempo.Logic.Calendar;

public class ClearCommand implements Command{
	private Calendar cal;
	
	public ClearCommand(Calendar cal){
		this.cal = cal;
	}
	
	public Result execute() {
		return cal.clearFile();
	}

}
