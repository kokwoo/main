package Tempo;

import java.util.*;

public class RemoveCommand implements Command {
	private Calendar calendar;
	private IndexStore indexStore;
	private int idx;
	
	private final String DISPLAY_ERROR = "Error: The display command entered is invalid!";
	
	public RemoveCommand(Calendar calendar, IndexStore indexStore, int idx) {
		this.calendar = calendar;
		this.indexStore = indexStore;
		this.idx = idx;
	}
	
	public ArrayList<String> execute(){
		if(idx != -1){
			if (isEvent(idx)) {
				return calendar.removeEvent(idx);
			} else if (isFloatingTask(idx)) {
				return calendar.removeFloatingTask(idx);
			} else {
				return calendar.removeTask(idx);
			}
		}else{
			ArrayList<String> returnArray = new ArrayList<String>();
			
		}
	}
	
	private boolean isEvent(int idx){
		return indexStore.isEvent(idx);
	}
	
	private boolean isFloatingTask(int idx){
		return indexStore.isFloatingTask(idx);
	}
}
