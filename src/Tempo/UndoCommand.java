package Tempo;

import java.util.ArrayList;

public class UndoCommand implements Command{
	private Calendar calendar;
	
	public UndoCommand(Calendar calendar){
		this.calendar  = calendar;
	}

	public ArrayList<String> execute() {
		return calendar.undo();
	}
	
}
