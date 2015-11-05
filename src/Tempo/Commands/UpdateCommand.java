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
	private boolean isSeries;
	
	public UpdateCommand(Calendar cal, IndexStore indexStore, int idx, ArrayList<String> fields, ArrayList<String> newValues, boolean isSeries) {
		this.cal = cal;
		this.indexStore = indexStore;
		this.idx = idx;
		this.fields = fields;
		this.newValues = newValues;
		this.isSeries = isSeries;
	}
	
	@Override
	public Result execute() {
		if (isEvent()) {
			return cal.updateEvent(idx, fields, newValues, isSeries);
		} else if (isFloatingTask()) {
			return cal.updateFloatingTask(idx, fields, newValues, isSeries);
		} else if (isTask()){
			return cal.updateTask(idx, fields, newValues, isSeries);
		} else {
			return handleInvalidUpdate();
		}
	}
	
	private Result handleInvalidUpdate() {
		return new Result(MSG_INVALID_ID, false, null);
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