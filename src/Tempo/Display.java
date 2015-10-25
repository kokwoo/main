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
	private ArrayList<Task> undoneTasks;
	private ArrayList<FloatingTask> undoneFloatingTasks;
	private ArrayList<Task> doneTasks;
	private ArrayList<FloatingTask> doneFloatingTasks;
	private CurrentDateAndTime date;

	// Messages
	private String MISSED_TASKS = "These are the tasks you missed";
	private String NO_MISSED_TASKS = "You have no missed task";
	private String UNDONE_TASKS = "These are the list of undone tasks";
	private String FLOATING_TASKS = "These are the list of tasks without deadline";
	private String NO_FLOATING_TASKS = "You have no task without deadline";
	private String TODAY_TASKS = "Tasks due today";
	private String NO_TODAY_TASKS = "You have no task today";
	private String UPCOMING_TASKS = "These are the list of upcoming tasks today";
	private String NO_UPCOMING_TASKS = "You have no upcoming task";
	private String PAST_EVENTS = "These are the list of past events";
	private String NO_PAST_EVENTS = "You have no past event";
	private String TODAY_EVENTS = "These are your events for the day";
	private String NO_TODAY_EVENTS = "You have no event today";
	private String UPCOMING_EVENTS = "These are the list of upcoming events";
	private String NO_UPCOMING_EVENTS = "You have no upcoming event";
	private String DONE_TASKS = "These are all the tasks that are done";
	
	public ArrayList<Event> getEventsToday(){
		return eventsToday;
	}
	
	public ArrayList<Event> getUpcomingEvents(){
		return upcomingEvents;
	}
	
	public ArrayList<Event> getPastEvents(){
		return pastEvents;
	}
	
	public ArrayList<Task> getTasksToday(){
		return tasksToday;
	}
	
	public ArrayList<Task> getUpcomingTasks(){
		return upcomingTasks;
	}
	
	public ArrayList<Task> getMissedTasks(){
		return missedTasks;
	}
	
	public ArrayList<Task> getUndoneTasks(){
		return undoneTasks;
	}
	
	public ArrayList<Task> getDoneTasks(){
		return doneTasks;
	}
	
	public ArrayList<FloatingTask> getUndoneFloatingTasks(){
		return undoneFloatingTasks;
	}
	
	public ArrayList<FloatingTask> getDoneFloatingTasks(){
		return doneFloatingTasks;
	}

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

		undoneTasks = new ArrayList<Task>();
		undoneFloatingTasks = new ArrayList<FloatingTask>();

		doneTasks = new ArrayList<Task>();
		doneFloatingTasks = new ArrayList<FloatingTask>();

		events = _events;
		tasks = _tasks;
		floatingTasks = _floatingTasks;

		splitsEvents();
		// splitting (floating) done and undone tasks
		splitFtasks();
		// splitting done and undone tasks
		splitTasks();
		// splitting the dated undone tasks
		splitDatedTask();
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

	private static void printNewLine() {
		System.out.println("");
	}

	
	public boolean events() {

		printUpcomingEvents();

		printTodayEvents();

		printPastEvents();

		return true;
	}

	public boolean upcomingEvents() {
		printUpcomingEvents();
		return true;
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

	private void printTodayEvents() {
		if (eventsToday.isEmpty()) {
			System.out.println(NO_TODAY_EVENTS);
			printNewLine();
		} else {
			System.out.println(TODAY_EVENTS);
			printEvents(eventsToday.size(), eventsToday);
			printNewLine();
		}
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

	private void splitsEvents() {
		String currentDate = date.getDate();
		Date dateCurr = getDateInDateFormat(currentDate);
		for (int i = 0; i < events.size(); i++) {
			Date dateCompare = getDateInDateFormat(events.get(i).getStartDate());
			if (dateCurr.compareTo(dateCompare) == 0) {
				eventsToday.add(events.get(i));
			} else if (dateCurr.compareTo(dateCompare) < 0) {
				upcomingEvents.add(events.get(i));
			} else if (dateCurr.compareTo(dateCompare) > 0) {
				pastEvents.add(events.get(i));
			}
		}
	}

	private void printEvents(int numOfEvents, ArrayList<Event> events) {
		for (int i = 0; i < numOfEvents; i++) {
			Event currEvent = events.get(i);
			int num = i + 1;
			System.out.print(num + ") ");
			System.out.println(currEvent.getName() + " From: " + currEvent.getStartDateTime() + " To: "
					+ currEvent.getEndDateTime() + "\t[ID:" + currEvent.getIndex() + "] ");
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

	// view all tasks
	// Finds all the undone tasks in the floating tasks and tasks
	// prints only the undone floating tasks
	// split the undone tasks into upcoming and today
	// print today undone tasks
	// print upcoming undone tasks
	// show tasks that are done only in "display all"

	public void tasks() {

		// print floating tasks (only undone)
		printFloatingTasks(undoneFloatingTasks);

		// print tasks today(only undone)
		printTodayTasks();

		// print upcoming tasks(only undone)
		printUpcomingTasks();
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

	private void printTodayTasks() {
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

	private void splitDatedTask() {
		String currentDate = date.getDate();
		Date dateCurr = getDateInDateFormat(currentDate);

		for (int i = 0; i < undoneTasks.size(); i++) {
			Date dateCompare = getDateInDateFormat(undoneTasks.get(i).getDueDateSimplified());
			if (dateCurr.compareTo(dateCompare) == 0) {
				tasksToday.add(undoneTasks.get(i));
			} else if (dateCurr.compareTo(dateCompare) < 0) {
				upcomingTasks.add(undoneTasks.get(i));
			} else if (dateCurr.compareTo(dateCompare) > 0) {
				missedTasks.add(undoneTasks.get(i));
			}
		}
	}

	private void printFloatingTasks(ArrayList<FloatingTask> _floatingTasks) {
		if ( _floatingTasks.isEmpty()) {
			System.out.println(NO_FLOATING_TASKS);
			printNewLine();
		}

		else {
			System.out.println(FLOATING_TASKS);
			for (int i = 0; i <  _floatingTasks.size(); i++) {
				FloatingTask currFT =  _floatingTasks.get(i);
				int num = i + 1;
				System.out.print(num + ") ");
				System.out.println(currFT.getName() + "\t[ID:" + currFT.getIndex() + "] ");
			}
			printNewLine();
		}
	}

	private void splitTasks() {
		for (int i = 0; i < tasks.size(); i++) {
			if (!tasks.get(i).getDone()) {
				undoneTasks.add(tasks.get(i));
			} else {
				doneTasks.add(tasks.get(i));
			}
		}
	}

	private void splitFtasks() {
		for (int i = 0; i < floatingTasks.size(); i++) {
			if (!floatingTasks.get(i).getDone()) {
				undoneFloatingTasks.add(floatingTasks.get(i));
			} else {
				doneFloatingTasks.add(floatingTasks.get(i));
			}
		}
	}

	private void printTasks(ArrayList<Task> tasks) {
		for (int i = 0; i < tasks.size(); i++) {
			int num = i + 1;
			Task currTask = tasks.get(i);
			System.out.print(num + ") ");
			System.out.println(
					currTask.getName() + " Due: " + currTask.getDueDate() + "\t[ID:" + currTask.getIndex() + "] ");
		}
	}

	public void missedTasks() {
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
	
	public void undoneTasks(){
		System.out.println(UNDONE_TASKS);
		printTasks(undoneTasks);
		printNewLine();
		printFloatingTasks(undoneFloatingTasks);
	}
	
	public void today() throws ParseException {
		printTodayEvents();
		printTodayTasks();
	}
	
	public void all() throws ParseException {
		// events
		events();

		// tasks
		tasks();
		
		System.out.println(DONE_TASKS);
		printTasks(doneTasks);
		printFloatingTasks(doneFloatingTasks);
	}

}
