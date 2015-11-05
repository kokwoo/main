package Tempo.Commands;

import java.util.ArrayList;

import Tempo.Logic.Calendar;
import Tempo.Logic.IndexStore;

public class DoneCommand implements Command {
	private Calendar cal;
	private IndexStore indexStore;
	private int idx;
	
	private static final String MSG_DONE_ERR_NOT_EVENT = "Error: Index provided is not a valid task!";

	public DoneCommand(Calendar cal, IndexStore indexStore, int idx) {
		this.cal = cal;
		this.indexStore = indexStore;
		this.idx = idx;
	}
	
	public Result execute() {
		if(isTask() || isFloatingTask()){
			return cal.markTaskAsDone(idx);
		}else{
			return handleInvalidDone();
		}
	}
	
	private Result handleInvalidDone() {
		return new Result(MSG_DONE_ERR_NOT_EVENT, false, null);
	}
	
	private boolean isFloatingTask(){
		return indexStore.isFloatingTask(idx);
	}

	private boolean isTask() {
		return indexStore.isTask(idx);
	}
}
