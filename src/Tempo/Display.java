package Tempo;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Display {

	// Global variables
	private ArrayList<Event> events;
	private ArrayList<Event> eventsToday;
	private ArrayList<Event> upcomingEvents;
	private ArrayList<Event> pastEvents;
	private ArrayList<Task> tasks;
	private ArrayList<Task> tasksToday;
	private ArrayList<Task> upcomingTasks;
	private ArrayList<Task> missedTasks;
	private ArrayList<FloatingTask> floatingTasks;
	private CurrentDateAndTime date;

	// Messages
	private String MISSED_TASKS = "These are the tasks you missed";
	private String NO_MISSED_TASKS = "You have no missed task";
	private String UNDONE_TASKS = "These are the list of undone tasks";
	private String FLOATING_TASKS = "These are the list of tasks without deadline";
	private String NO_FLOATING_TASKS = "You have no more task without deadline";
	private String TODAY_TASKS = "Tasks due today" ;
	private String NO_TODAY_TASKS = "You have no task today";
	private String UPCOMING_TASKS = "These are the list of upcoming tasks today";
	private String NO_UPCOMING_TASKS = "You have no upcoming task";
	private String PAST_EVENTS = "These are the list of past events";
	private String NO_PAST_EVENTS = "You have no past event";
	private String TODAY_EVENTS = "These are your events for the day" ;
	private String NO_TODAY_EVENTS = "You have no event today";
	private String UPCOMING_EVENTS ="These are the list of upcoming events";
	private String NO_UPCOMING_EVENTS = "You have no up coming event";

	public Display(ArrayList<Event> _events, ArrayList<Task> _tasks, ArrayList<FloatingTask> _floatingTasks) {
		date = new CurrentDateAndTime();

		events = new ArrayList<Event>();
		tasks = new ArrayList<Task>();
		floatingTasks = new ArrayList<FloatingTask>();

		eventsToday = new ArrayList<Event>();
		upcomingEvents = new ArrayList<Event>();
		pastEvents = new ArrayList<Event>();

		tasksToday = new ArrayList<Task>();
		upcomingTasks = new ArrayList<Task>();
		missedTasks = new ArrayList<Task>();

		events = _events;
		tasks = _tasks;
		floatingTasks = _floatingTasks;

		// System.out.println("In display...");
		//
		// System.out.println("Printing all events");
		//
		// for(Event e : events){
		// System.out.println(e.getIndex());
		// System.out.println(e.getName());
		// }
		//
		// System.out.println("Printing all tasks");
		//
		// for(Task t : tasks){
		// System.out.println(t.getIndex());
		// System.out.println(t.getName());
		// }
		//
		// System.out.println("Printing all floating tasks");
		//
		// for(FloatingTask f : floatingTasks){
		// System.out.println(f.getIndex());
		// System.out.println(f.getName());
		// }
	}

	public static boolean manual() {
		// INTRO
		System.out.println("Hello! This is the cheatsheet for tempo!");
		printNewLine();
		System.out.println("Manage TEMPO with the following KEYWORDS and PARAMETERS");
		System.out.println("The following cheatsheet shows you the function and the keywords you can use");
		System.out.println("<KEY>: keywords you can use for each function");
		System.out.println("<id>: id is the index of the event as supplied by “List all events”");
		System.out.println("<start date>/<end date>: day month year");
		System.out.println("<start time>/<end time>: 24 hours format");
		printNewLine();

		// how to add
		System.out.println("HOW TO ADD EVENT");
		System.out.println("================");
		System.out.println("<KEY>: add/create/new");
		System.out.println("<KEY> event <name> from <start date> at <start time> to <end date> at <end time>");
		printNewLine();

		// how to edit
		System.out.println("HOW TO EDIT EVENT");
		System.out.println("=================");
		System.out.println("<KEY>: edit/update/change");
		System.out.println("<KEY> event <id> <name> from <start date> at <start time> to <end date> at <end time>");
		System.out.println("For editing the event name only: <KEY> event <id> name: <name>");
		System.out.println("For editing the start date only: <KEY> event <id> start date: <start date>");
		System.out.println("For editing the start time only: <KEY> event <id> start time: <start time>");
		System.out.println("For editing of end date only: <KEY> event <id> end date: <end date>");
		System.out.println("For editing of end time only: <KEY> event <id> end time: <end time>");
		printNewLine();

		// how to add recurring events
		System.out.println("HOW TO ADD RECURRING EVENT");
		System.out.println("==========================");
		System.out.println("<KEY>: repeat");
		System.out.println("For daily recurring events: <KEY> event <id> daily until <end date>");
		System.out.println("For weekly recurring events: <KEY> event <id> weekly until <end date>");
		System.out.println("For monthly recurring events: <KEY> event <id> monthly until <end date>");
		System.out.println("For annually recurring events: <KEY> event <id> yearly until <end date>");
		printNewLine();

		// how to delete existing events
		System.out.println("HOW TO DELETE EXISTING EVENT");
		System.out.println("============================");
		System.out.println("<KEY>: delete/cancel/remove");
		System.out.println("<KEY> event <id>");
		printNewLine();

		// how to search events
		System.out.println("HOW TO SEARCH EXISTING EVENT");
		System.out.println("============================");
		System.out.println("<KEY>: search/find");
		System.out.println("By name: <KEY> event <keywords>");
		System.out.println("By id: <KEY> event <id>");
		printNewLine();

		// how to list upcoming events
		System.out.println("HOW TO LIST UPCOMING EVENT/S");
		System.out.println("============================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> upcoming events");
		printNewLine();

		// how to list all events
		System.out.println("HOW TO LIST ALL EVENT/S");
		System.out.println("=======================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> events");
		printNewLine();

		// how to add tasks
		System.out.println("HOW TO ADD TASK");
		System.out.println("===============");
		System.out.println("<KEY>: add/create/new");
		System.out.println("For tasks with no deadlines: <KEY> task <name>");
		System.out.println("For tasks with deadlines: <KEY> task <name> due <date>");
		printNewLine();

		// how to edit existing tasks
		System.out.println("HOW TO EDIT EXISTING TASK");
		System.out.println("=========================");
		System.out.println("<KEY>: edit/update/change");
		System.out.println("<KEY> task <id> <name> due <date>");
		System.out.println("For changing of name only: <KEY> task <id> name: <name>");
		System.out.println("For changing of due date only: <KEY> task <id> due: <date>");
		printNewLine();

		// how to delete existing tasks
		System.out.println("HOW TO DELETE EXISTING TASK");
		System.out.println("===========================");
		System.out.println("<KEY>: delete/cancel/remove");
		System.out.println("<KEY> task <id>");
		printNewLine();

		// how to search tasks
		System.out.println("HOW TO SEARCH EXISTING TASK");
		System.out.println("===========================");
		System.out.println("<KEY>: search/find");
		System.out.println("<KEY> task <keywords>");
		printNewLine();

		// how to list undone tasks
		System.out.println("HOW TO LIST UNDONE TASK/S");
		System.out.println("=========================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> undone tasks");
		printNewLine();

		// how to list missed tasks
		System.out.println("HOW TO LIST MISSED TASK/S");
		System.out.println("=========================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> missed tasks");
		printNewLine();

		// how to list all tasks
		System.out.println("HOW TO LIST ALL TASK/S");
		System.out.println("======================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> tasks");
		printNewLine();

		// how to mark task as done
		System.out.println("HOW TO MARK TASK AS DONE");
		System.out.println("========================");
		System.out.println("<KEY>: mark/flag");
		System.out.println("<KEY> task done <id>");
		printNewLine();

		// how to undo previous operation
		System.out.println("HOW TO UNDO PREVIOUS OPERATION");
		System.out.println("==============================");
		System.out.println("<KEY>: undo");
		System.out.println("<KEY>");
		printNewLine();

		// how to view today’s events and tasks due
		System.out.println("HOW TO VIEW EVENTS AND TASKS DUE TODAY ");
		System.out.println("=======================================");
		System.out.println("<KEY>: view/display");
		System.out.println("<KEY> today");
		printNewLine();

		// how to view this week’s events and tasks due
		System.out.println("HOW TO VIEW EVENTS AND TASKS DUE THIS WEEK ");
		System.out.println("===========================================");
		System.out.println("<KEY>: view/display");
		System.out.println("<KEY> week");
		printNewLine();
		return false;

	}

	// view all the events
	public boolean events() {
		splitEvents(date.getDate());
		printAllEvents();
		return true;
	}

	private void printAllEvents() {
		printEventsToday();
		printUpcomingEvents();
		printPastEvents();
	}

	private void printUpcomingEvents() {
		if (upcomingEvents.isEmpty()) {
			System.out.println(NO_UPCOMING_EVENTS);
			printNewLine();
		}

		else {
			System.out.println(UPCOMING_EVENTS);
			printEvents(upcomingEvents.size(), upcomingEvents);
			printNewLine();
		}
	}

	private void printEventsToday() {
		if (eventsToday.isEmpty()) {
			System.out.println(NO_TODAY_EVENTS);
			printNewLine();
		} else {
			System.out.println(TODAY_EVENTS);
			printEvents(eventsToday.size(), eventsToday);
			printNewLine();
		}
	}

	private void printPastEvents() {
		if (pastEvents.isEmpty()) {
			System.out.println(NO_PAST_EVENTS);
			printNewLine();
		}

		else {
			System.out.println(PAST_EVENTS);
			printEvents(pastEvents.size(), pastEvents);
			printNewLine();
		}

	}

	private Date getDateInDateFormat(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date formatDate = null;
		try {
			formatDate = sdf.parse(date);
		} catch (ParseException e) {
			System.out.println("Error fomatting date");
		}
		return formatDate;
	}

	private void splitEvents(String currentDate) {
		Date dateCurr = getDateInDateFormat(currentDate);

		for (int i = 0; i < events.size(); i++) {
			Date dateCompare = getDateInDateFormat(events.get(i).getStartDate());
			if (dateCurr.compareTo(dateCompare) == 0) {
				addTodayEvents(i);
			} else if (dateCurr.compareTo(dateCompare) < 0) {
				addUpcomingEvents(i);
			} else if (dateCurr.compareTo(dateCompare) > 0) {
				addPastEvents(i);
			}
		}
	}

	private void addPastEvents(int i) {
		pastEvents.add(events.get(i));
	}

	private void addUpcomingEvents(int i) {
		upcomingEvents.add(events.get(i));
	}

	private void addTodayEvents(int i) {
		eventsToday.add(events.get(i));
	}

	private void printEvents(int numOfEvents, ArrayList<Event> events) {
		for (int i = 0; i < numOfEvents; i++) {
			Event currEvent = events.get(i);
			int num = i + 1;
			printEventsString(currEvent, num);
		}
	}

	private void printEventsString(Event currEvent, int num) {
		System.out.print(num + ") ");
		System.out.println(currEvent.getName() + " From: " + currEvent.getStartDateTime() + " To: "
				+ currEvent.getEndDateTime() + "\t[ID:" + currEvent.getIndex() + "] ");
	}

	// view all tasks
	public boolean tasks() {
		printFloatingTasks(floatingTasks);
		splitTasksByDates(date.getDate());
		printTasksToday();
		printUpcomingTasks();
		return true;
	}

	private void printUpcomingTasks() {

		if (upcomingTasks.isEmpty()) {
			System.out.println(NO_UPCOMING_TASKS);
			printNewLine();
		} else {
			System.out.println(UPCOMING_TASKS);
			printTasks(upcomingTasks);
			printNewLine();
		}
	}

	private void printTasksToday() {
		if (tasksToday.isEmpty()) {
			System.out.println(NO_TODAY_TASKS);
			printNewLine();
		}

		else {
			System.out.println(TODAY_TASKS);
			printTasks(tasksToday);
			printNewLine();
		}
	}

	private void splitTasksByDates(String currentDate) {
		Date dateCurr = getDateInDateFormat(currentDate);
		
		for (int i = 0; i < tasks.size(); i++) {
			Date dateCompare = getDateInDateFormat(tasks.get(i).getDueDateSimplified());
			if (dateCurr.compareTo(dateCompare) == 0) {
				addTodayTasks(i);
			} else if (dateCurr.compareTo(dateCompare) < 0) {
				addUpcomingTasks(i);
			} else if (dateCurr.compareTo(dateCompare) > 0) {
				addMissedTasks(i);
			}
		}
	}

	private void addTodayTasks(int i) {
		tasksToday.add(tasks.get(i));
	}

	private void addUpcomingTasks(int i) {
		upcomingTasks.add(tasks.get(i));
	}

	private void addMissedTasks(int i) {
		missedTasks.add(tasks.get(i));
	}

	private void printFloatingTasks(ArrayList<FloatingTask> floatingTasks) {

		if (floatingTasks.isEmpty()) {
			System.out.println(NO_FLOATING_TASKS);
		}

		else {
			System.out.println(FLOATING_TASKS);
			for (int i = 0; i < floatingTasks.size(); i++) {
				FloatingTask currFT = floatingTasks.get(i);
				int num = i + 1;
				printFloatingTasksString(currFT, num);
			}
			printNewLine();
		}
	}

	private void printFloatingTasksString(FloatingTask currFT, int num) {
		System.out.print(num + ") ");
		System.out.println(currFT.getName() + "\t[ID:" + currFT.getIndex() + "] ");
	}

	private void printTasks(ArrayList<Task> tasks) {
		for (int i = 0; i < tasks.size(); i++) {
			int num = i + 1;
			Task currTask = tasks.get(i);
			printTaskString(num, currTask);
		}
	}

	private void printTaskString(int num, Task currTask) {
		System.out.print(num + ") ");
		System.out
				.println(currTask.getName() + " Due: " + currTask.getDueDate() + "\t[ID:" + currTask.getIndex() + "] ");
	}

	// view all tasks and all events
	public void all() throws ParseException {
		// events
		splitEvents(date.getDate());
		printAllEvents();

		// tasks
		printFloatingTasks(floatingTasks);
		splitTasksByDates(date.getDate());
		printTasksToday();
		printUpcomingTasks();
		printMissedTasks();
	}

	// view upcoming events
	public boolean upcomingEvents() throws ParseException {
		splitEvents(date.getDate());
		printUpcomingEvents();
		return false;

	}

	// view undone tasks
	public boolean undoneTasks() {
		ArrayList<Task> undoneTasks = new ArrayList<Task>();
		ArrayList<FloatingTask> undoneFloatingTasks = new ArrayList<FloatingTask>();
		// System.out.println("These are the tasks that are still undone:");
		findUndoneTasks(undoneTasks);
		findUndoneFloatingTasks(undoneFloatingTasks);
		System.out.println(UNDONE_TASKS);
		printTasks(undoneTasks);
		printFloatingTasks(undoneFloatingTasks);

		return false;
	}

	private void findUndoneFloatingTasks(ArrayList<FloatingTask> undoneFloatingTasks) {
		for (int i = 0; i < floatingTasks.size(); i++) {
			if (!floatingTasks.get(i).getDone()) {
				addToUndoneFloatingTasks(undoneFloatingTasks, i);
			}
		}
	}

	private void findUndoneTasks(ArrayList<Task> undoneTasks) {
		for (int i = 0; i < tasks.size(); i++) {
			if (!tasks.get(i).getDone()) {
				addToUndoneTasks(undoneTasks, i);
			}
		}
	}

	private void addToUndoneFloatingTasks(ArrayList<FloatingTask> undoneFloatingTasks, int i) {
		undoneFloatingTasks.add(floatingTasks.get(i));
	}

	private void addToUndoneTasks(ArrayList<Task> undoneTasks, int i) {
		undoneTasks.add(tasks.get(i));
	}

	public void today() throws ParseException {
		splitEvents(date.getDate());
		splitTasksByDates(date.getDate());
		printEventsToday();
		printTasksToday();

	}

	public void missedTasks() {
		splitTasksByDates(date.getDate());
		printMissedTasks();
	}

	private void printMissedTasks() {
		if (missedTasks.isEmpty()) {
			System.out.println(NO_MISSED_TASKS);
			printNewLine();
		} else {
			System.out.println(MISSED_TASKS);
			printTasks(missedTasks);
			printNewLine();
		}

	}

	private static void printNewLine() {
		System.out.println("");
	}
}
