package Tempo.Commands;

import Tempo.Logic.Calendar;

//@@author A0127047J
public class RedoCommand implements Command{
	private Calendar cal;
	private static final String CMD_REDO = "redo <%1$s>";
	
	public RedoCommand(Calendar cal){
		this.cal = cal;
	}
	
	public Result execute(){
		Result result = cal.redo();
		String cmd = String.format(CMD_REDO, 
					 result.getCmdPerformed());
		result.setCommand(cmd);
		return result;
	}

}
