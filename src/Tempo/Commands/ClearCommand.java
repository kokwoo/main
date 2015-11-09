package Tempo.Commands;

import Tempo.Logic.Calendar;

//@@author A0125962B
public class ClearCommand implements Command{
	private Calendar cal;
	
	public ClearCommand(Calendar cal){
		this.cal = cal;
	}
	
	public Result execute() {
		saveCommand();
		return cal.clearFile();
	}
	
	private void saveCommand() {
		cal.saveCmd((Command) new ClearCommand(cal));
	}

}
