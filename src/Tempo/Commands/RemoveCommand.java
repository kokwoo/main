package Tempo.Commands;

import Tempo.Logic.Calendar;
import Tempo.Logic.*;

public class RemoveCommand implements Command {
	private Calendar cal;
	private IndexStore indexStore;
	private int idx;
	private boolean isSeries;
	
	private static final String MSG_INVALID_ID = 
						"Error: Index provided is invalid!";
	
	public RemoveCommand(Calendar cal, IndexStore indexStore, 
						int idx, boolean isSeries) {
		this.cal = cal;
		this.indexStore = indexStore;
		this.idx = idx;
		this.isSeries = isSeries;
	}
	
	public Result execute(){
		saveCommand();
		
		if (isEvent()) {
			return executeRemoveEvent();
		} else if (isFloatingTask()) {
			return executeRemoveFloating();
		} else if (isTask()) {
			return executeRemoveTask();
		} else {
			return handleInvalidRemove();
		}
	}
	
	private Result executeRemoveEvent() {
		return cal.removeEvent(idx, isSeries);
	}
	
	private Result executeRemoveFloating() {
		return cal.removeFloatingTask(idx, isSeries);
	}
	
	private Result executeRemoveTask() {
		return cal.removeTask(idx, isSeries);
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
	
	private void saveCommand() {
		cal.saveCmd((Command) new RemoveCommand(
				cal, indexStore, idx, isSeries));
	}
}
