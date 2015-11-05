package Tempo.Commands;

import java.util.*;
import Tempo.CalendarObjects.*;

public class UndoUpdate implements Command {
	
	private Event prevModEvent;
	private Task prevModTask;
	private FloatingTask prevModFloating;
	
	private ArrayList<CalendarObject> prevModEvents;
	private ArrayList<CalendarObject> prevModTasks;

	private boolean isEvent = false;
	private boolean isTask = false;
	private boolean isFloatingTask = false;
	private boolean isEventsSeries = false;
	
	public UndoUpdate(Event event) {
		
	}
	
	public UndoUpdate(Task task) {
		
	}
	
	public UndoUpdate(FloatingTask floatingTask) {
		
	}
	
	public UndoUpdate(ArrayList<CalendarObject> series, boolean isEventsSeries) {
		
	}

	public Result execute() {
		return null;
	}
}
