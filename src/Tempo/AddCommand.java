package Tempo;

import java.util.*;

public class AddCommand implements Command {
	private Calendar cal;
	private ArrayList<String> params;
	
	private static final int LENGTH_ADD_EVENT_PARAMS = 3;
	private static final int LENGTH_ADD_TASK_PARAMS = 2;
	private static final int LENGTH_ADD_FLOATING_TASK_PARAMS = 1;
	
	public AddCommand(Calendar cal, ArrayList<String> params) {
		this.cal = cal;
		this.params = params;
	}
	
	@Override
	public ArrayList<String> execute() {
		if (params.size() == LENGTH_ADD_EVENT_PARAMS) {
			return addEvent();
		} else if (params.size() == LENGTH_ADD_TASK_PARAMS) {
			return addTask();
		} else if (params.size() == LENGTH_ADD_FLOATING_TASK_PARAMS){
			return addFloatingTask();		
		}else{
			//This shouldn't happen
			return null;
		}
	}
	
	private ArrayList<String> addEvent() {
		String name = params.get(0);
		String start = params.get(1);
		String end = params.get(2);
		return cal.addEvent(name, start, end);
	}
	
	private ArrayList<String> addTask() {
		String name = params.get(0);
		String dueDate = params.get(1);
		return cal.addTask(name, dueDate);
	}
	
	private ArrayList<String> addFloatingTask() {
		String name = params.get(0);
		return cal.addFloatingTask(name);
	}
}
