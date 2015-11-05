package Tempo.Commands;

import java.util.*;

import Tempo.CalendarObjects.*;
import Tempo.Logic.Calendar;

public class UndoRemove implements Command {
	private static Calendar calendar = Calendar.getInstance();
	
	private static final String CMD = "Undo remove %1$s";
	
	private int prevModIndex;
	
	private Event prevModEvent;
	private Task prevModTask;
	private FloatingTask prevModFloating;
	
	private ArrayList<CalendarObject> prevModEvents;
	private ArrayList<CalendarObject> prevModTasks;
	
	private boolean isEvent = false;
	private boolean isTask = false;
	private boolean isFloatingTask = false;
	private boolean isEventsSeries = false;
	
	public UndoRemove(Event event) {
		prevModEvent = event;
		isEvent = true;
	}
	
	public UndoRemove(Task task) {
		prevModTask = task;
		isTask = true;
	}
	
	public UndoRemove(FloatingTask floatingTask) {
		prevModFloating = floatingTask;
		isFloatingTask = true;
	}
	
	public UndoRemove(ArrayList<CalendarObject> series, boolean isEventsSeries) {
		this.isEventsSeries = isEventsSeries;
		initialiseSeries(series);
	}
	
	public Result execute() {
		Result result;
		
		if (isEvent) {
			result = calendar.addBackEvent(prevModEvent);
		} else if (isTask) {
			result = calendar.addBackTask(prevModTask);
		} else if (isFloatingTask) {
			result = calendar.addBackFloating(prevModFloating);
		} else if (isEventsSeries) {
			result = calendar.addBackRecurrEvent(prevModEvents);
		} else {
			result = calendar.addBackRecurrTask(prevModTasks);
		}
			
		String command = result.getCommandPerformed();
		String name = getLastWord(command);
		result.setCommand(String.format(CMD, name));
		
		return result;
	}
	
	private void initialiseSeries(ArrayList<CalendarObject> series) {
		if (isEventsSeries) {
			prevModEvents = series;
		} else {
			prevModTasks = series;
		}
	}
	
	private String getLastWord(String text) {
		String[] params = text.trim().split(" ");
		return params[params.length-1];
	}
}
