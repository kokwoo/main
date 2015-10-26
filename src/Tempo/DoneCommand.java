package Tempo;

import java.util.ArrayList;

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
	
	public ArrayList<String> execute() {
		if(isTask() || isFloatingTask()){
			return cal.markTaskAsDone(idx);
		}else{
			return handleInvalidDone();
		}
	}
	
	private ArrayList<String> handleInvalidDone() {
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(MSG_DONE_ERR_NOT_EVENT);
		return feedback;
	}
	
	private boolean isFloatingTask(){
		return indexStore.isFloatingTask(idx);
	}

	private boolean isTask() {
		return indexStore.isTask(idx);
	}
}
