package Tempo.Commands;

import java.util.*;

import Tempo.Logic.Calendar;
import Tempo.Logic.IndexStore;

public class RemoveCommand implements Command {
	private Calendar cal;
	private IndexStore indexStore;
	private int idx;
	private boolean removeSeries;
	
	private static final String MSG_INVALID_ID = "Error: Index provided is invalid!";
	
	public RemoveCommand(Calendar cal, IndexStore indexStore, int idx, boolean removeSeries) {
		this.cal = cal;
		this.indexStore = indexStore;
		this.idx = idx;
		this.removeSeries = removeSeries;
	}
	
	public Result execute(){
		if (isEvent()) {
			return cal.removeEvent(idx, removeSeries);
		} else if (isFloatingTask()) {
			return cal.removeFloatingTask(idx, removeSeries);
		} else if (isTask()) {
			return cal.removeTask(idx, removeSeries);
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
