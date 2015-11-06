package Tempo.Commands;

import Tempo.Logic.Calendar;

public class RedoCommand implements Command{
	private Calendar cal;
	
	public RedoCommand(Calendar cal){
		this.cal = cal;
	}
	
	public Result execute(){
		//Szeying, feel free to change this as you like! :)
		return cal.redo();
	}

}
