package Tempo.Commands;

import java.util.*;

import Tempo.Data.*;
import Tempo.Logic.Calendar;

//@@author A0127047J
public class UndoClear implements Command {
	private static Calendar cal = Calendar.getInstance();
	
	private static final String CMD_UNDO = "undo <%1$s>";
	
	private ArrayList<CalendarObject> events;
	private ArrayList<CalendarObject> tasks;
	private ArrayList<CalendarObject> floatingTasks;
	
	public UndoClear(ArrayList<CalendarObject> events, 
					 ArrayList<CalendarObject> tasks, 
					 ArrayList<CalendarObject> floatingTasks) {
		this.events = events;
		this.tasks = tasks;
		this.floatingTasks = floatingTasks;
	}
	
	public Result execute() {
		Result result = executeUndoClear();
		
		String cmd = String.format(CMD_UNDO, result.getCmdPerformed());
		result.setCommand(cmd);
		return result;
	}
	
	public Result executeUndoClear() {
		return cal.addBackAll(events, tasks, floatingTasks);
	}

}
