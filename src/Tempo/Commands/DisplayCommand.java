package Tempo.Commands;

import java.text.ParseException;
import java.util.ArrayList;

import Tempo.Logic.Display;

public class DisplayCommand implements Command {
	private Display display;
	private String displayType;

	// display arguments
	private final String ARG_EVENTS = "events";
	private final String ARG_UPCOMING_EVENTS = "upcoming events";
	private final String ARG_TASKS = "tasks";
	private final String ARG_UNDONE_TASKS = "undone tasks";
	private final String ARG_MISSED_TASKS = "missed tasks";
	private final String ARG_TODAY = "today";
	private final String ARGS_ALL = "all";

	private final String DISPLAY_ERROR = "Error: The command entered is invalid!";

	public DisplayCommand(Display display, String displayType) {
		this.display = display;
		this.displayType = displayType;
	}

	public Result execute() {
		switch (displayType) {
		case (ARG_EVENTS):
			return display.getEvents();
		case (ARG_TASKS):
			return display.getTasks();
		case (ARG_UPCOMING_EVENTS):
			return display.getUpcomingEvents();
		case (ARG_UNDONE_TASKS):
			return display.getUndoneTasks();
		case (ARG_MISSED_TASKS):
			return display.getMissedTasks();
		case (ARG_TODAY):
			return display.getToday();
		case (ARGS_ALL):
			return display.getAll();
		default:
			Result result = new Result("display", false, null);
			return result;
//			ArrayList<String> returnList = new ArrayList<String>();
//			returnList.add(DISPLAY_ERROR);
//			return returnList;

		}
	}

}
