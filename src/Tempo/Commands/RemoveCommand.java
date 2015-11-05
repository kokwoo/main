package Tempo.Commands;

import java.util.*;

import Tempo.Logic.Calendar;
import Tempo.Logic.IndexStore;

public class RemoveCommand implements Command {
	private Calendar cal;
	private IndexStore indexStore;
	private int idx;
	private boolean isSeries;
	
	private static final String MSG_INVALID_ID = "Error: Index provided is invalid!";
	
	public RemoveCommand(Calendar cal, IndexStore indexStore, int idx, boolean isSeries) {
		this.cal = cal;
		this.indexStore = indexStore;
		this.idx = idx;
		this.isSeries = isSeries;
	}
	
	public Result execute(){
		if (isEvent()) {
			return cal.removeEvent(idx, isSeries);
		} else if (isFloatingTask()) {
			return cal.removeFloatingTask(idx, isSeries);
		} else if (isTask()) {
			return cal.removeTask(idx, isSeries);
		} else {
			return handleInvalidRemove();
		}
	}
	
	private Result handleInvalidRemove() {
		return new Result(MSG_INVALID_ID, false, null);
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
