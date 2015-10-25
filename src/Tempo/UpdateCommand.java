package Tempo;

import java.util.*;

public class UpdateCommand implements Command {
	private static Calendar cal;
	private static IndexStore indexStore;
	private int idx;
	private ArrayList<String> fields;
	private ArrayList<String> newValues;
	
	public UpdateCommand(Calendar cal, IndexStore indexStore, int idx, ArrayList<String> fields, ArrayList<String> newValues) {
		this.cal = cal;
		this.indexStore = indexStore;
		this.idx = idx;
		this.fields = fields;
		this.newValues = newValues;
	}
	
	@Override
	public ArrayList<String> execute() {
		if (isEvent()) {
			return cal.updateEvent(idx, fields, newValues);
		} else if (isFloatingTask()) {
			return cal.updateFloatingTask(idx, fields, newValues);
		} else {
			return cal.updateTask(idx, fields, newValues);
		}
	}
	
	private boolean isEvent() {
		return indexStore.isEvent(idx);
	}
	
	private boolean isFloatingTask() {
		return indexStore.isFloatingTask(idx);
	}
	
}
