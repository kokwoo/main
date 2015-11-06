package Tempo.Commands;

import Tempo.Logic.Calendar;

public class RedoCommand implements Command{
	private Calendar cal;
	
	public RedoCommand(Calendar cal){
		this.cal = cal;
	}
	
	public Result execute(){
		System.out.println("Attempted to execute redo"); // debug
		return cal.redo();
	}

}
