package Tempo.Commands;

import Tempo.Logic.Calendar;
import Tempo.Logic.*;

public class ToggleDoneCommand implements Command {
	private Calendar cal;
	private IndexStore indexStore;
	private int idx;
	private boolean isDoneCmd;
	
	private static final String MSG_ERROR = 
			"Error: Index provided is not a valid task!";

	public ToggleDoneCommand(Calendar cal, IndexStore indexStore, 
							int idx, boolean isDoneCmd) {
		this.cal = cal;
		this.indexStore = indexStore;
		this.idx = idx;
		this.isDoneCmd = isDoneCmd;
	}
	
	public Result execute() {
		saveCommand();
		
		if(isTask() || isFloatingTask()){
			return executeDoneOrUndoneCmd();
		} else {
			return handleInvalidDone();
		}
	}
	
	private Result executeDoneOrUndoneCmd() {
		if(isDoneCmd){
			return cal.markTaskAsDone(idx);
		} else {
			return cal.markTaskAsUndone(idx);
		}
	}
	
	private Result handleInvalidDone() {
		return new Result(MSG_ERROR, false, null);
	}
	
	private boolean isFloatingTask(){
		return indexStore.isFloatingTask(idx);
	}

	private boolean isTask() {
		return indexStore.isTask(idx);
	}
	
	private void saveCommand() {
		cal.saveCmd((Command) new ToggleDoneCommand(cal, indexStore, 
													idx, isDoneCmd));
	}
}
