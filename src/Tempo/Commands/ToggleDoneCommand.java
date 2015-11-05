package Tempo.Commands;

import java.util.ArrayList;

import Tempo.Logic.Calendar;
import Tempo.Logic.IndexStore;

public class ToggleDoneCommand implements Command {
	private Calendar cal;
	private IndexStore indexStore;
	private int idx;
	private boolean isDoneCmd;
	
	private static final String MSG_DONE_ERR_NOT_EVENT = "Error: Index provided is not a valid task!";

	public ToggleDoneCommand(Calendar cal, IndexStore indexStore, int idx, boolean isDoneCmd) {
		this.cal = cal;
		this.indexStore = indexStore;
		this.idx = idx;
		this.isDoneCmd = isDoneCmd;
	}
	
	public Result execute() {
		if(isTask() || isFloatingTask()){
			if(isDoneCmd){
				return cal.markTaskAsDone(idx);
			}else{
				return cal.markTaskAsUndone(idx);
			}
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
