package Tempo.Commands;

import Tempo.Logic.Display;

//@@author A0125962B
public class DisplayCommand implements Command {
	private Display display;
	private String displayType;

	// display arguments
	private final String ARG_EVENTS = "events";
	//private final String ARG_UPCOMING_EVENTS = "upcoming events";
	private final String ARG_TASKS = "tasks";
	private final String ARG_UNDONE_TASKS = "undone tasks";
	private final String ARG_MISSED_TASKS = "missed tasks";
	private final String ARG_TODAY = "today";
	private final String ARGS_ALL = "all";
	private final String ARG_EVENTS_TODAY = "events today";
	private final String ARG_EVENTS_UPCOMING = "upcoming events";
	private final String ARG_PAST_EVENTS = "past events";
	private final String ARG_TASKS_TODAY = "tasks today";
	private final String ARG_UPCOMING_TASKS = "upcoming tasks";
	private final String ARG_DONE_TASKS = "done tasks";
	private final String ARG_UNDONE_FTASKS = "undone floating tasks";
	private final String ARG_DONE_FTASKS = "done floating tasks";
	

//	private final String DISPLAY_ERROR = "Error: The command entered is invalid!";

	public DisplayCommand(Display display, String displayType) {
		this.display = display;
		this.displayType = displayType;
	}
	
	//The list of things that can be displayed
	/*getEventsToday
	getUpcomingEvents
	getPastEvents
	getTasksToday
	getUpcomingTasks
	getMissedTasks
	getUndoneTasks
	getDoneTasks
	getUndoneFloatingTasks
	getDoneFloatingTasks
	getAll
	getEvents
	getTasks
	getToday
	*/
	
	public Result execute() {
		display = Display.getInstance();
		
		switch (displayType) {
		case (ARG_EVENTS_TODAY):
			return display.getEventsToday();
		case (ARG_EVENTS_UPCOMING):
			return display.getUpcomingEvents();
		case (ARG_PAST_EVENTS):
			return display.getPastEvents();
		case (ARG_TASKS_TODAY):
			return display.getTasksToday();
		case (ARG_UPCOMING_TASKS):
			return display.getUpcomingTasks();
		case (ARG_MISSED_TASKS):
			return display.getMissedTasks();	
		case (ARG_UNDONE_TASKS):
			return display.getUndoneTasks();
		case (ARG_DONE_TASKS):
			return display.getDoneTasks();		
		case (ARG_UNDONE_FTASKS):
			return display.getUndoneFloatingTasks();
		case (ARG_DONE_FTASKS):
			return display.getDoneFloatingTasks();		
		case (ARG_EVENTS):
			return display.getEvents();
		case (ARG_TASKS):
			return display.getTasks();
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
