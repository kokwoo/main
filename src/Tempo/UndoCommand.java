package Tempo;

import java.util.ArrayList;

public class UndoCommand implements Command{
	private Calendar cal;
	
	public UndoCommand(Calendar cal){
		this.cal  = cal;
	}

	public ArrayList<String> execute() {
		return cal.undo();
	}
	
}
