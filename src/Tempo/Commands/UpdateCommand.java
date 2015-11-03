package Tempo.Commands;

import java.util.*;

import Tempo.Logic.Calendar;
import Tempo.Logic.IndexStore;

public class UpdateCommand implements Command {
	private static Calendar cal;
	private static IndexStore indexStore;
	
	private static final String MSG_INVALID_ID = "Error: Index provided is invalid!";
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
		} else if (isTask()){
			return cal.updateTask(idx, fields, newValues);
		} else {
			return handleInvalidUpdate();
		}
	}
	
	private ArrayList<String> handleInvalidUpdate() {
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(MSG_INVALID_ID);
		return feedback;
	}
	
	private boolean isEvent() {
		return indexStore.isEvent(idx);
	}
	
	private boolean isFloatingTask() {
		return indexStore.isFloatingTask(idx);
	}
	
	private boolean isTask() {
		return indexStore.isTask(idx);
	}
		
}