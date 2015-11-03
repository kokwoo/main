package Tempo.Commands;

import java.util.*;

import Tempo.Logic.Calendar;
import Tempo.Logic.IndexStore;

public class RemoveCommand implements Command {
	private Calendar cal;
	private IndexStore indexStore;
	private int idx;
	
	private static final String MSG_INVALID_ID = "Error: Index provided is invalid!";
	
	public RemoveCommand(Calendar cal, IndexStore indexStore, int idx) {
		this.cal = cal;
		this.indexStore = indexStore;
		this.idx = idx;
	}
	
	public ArrayList<String> execute(){
		if (isEvent()) {
			return cal.removeEvent(idx);
		} else if (isFloatingTask()) {
			return cal.removeFloatingTask(idx);
		} else if (isTask()) {
			return cal.removeTask(idx);
		} else {
			return handleInvalidRemove();
		}
	}
	
	private ArrayList<String> handleInvalidRemove() {
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(MSG_INVALID_ID);
		return feedback;
	}
	
	private boolean isEvent(){
		return indexStore.isEvent(idx);
	}
	
	private boolean isFloatingTask(){
		return indexStore.isFloatingTask(idx);
	}

	private boolean isTask() {
		return indexStore.isTask(idx);
	}
}
