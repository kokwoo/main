package Tempo;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Display {
	private ArrayList<Event> events;
	private ArrayList<Event> eventsToday;
	private ArrayList<Event> upcomingEvents;
	private ArrayList<Event> pastEvents;
	private ArrayList<Task> tasks;
	private ArrayList<Task> tasksToday;
	private ArrayList<Task> restOfTasks;
	private ArrayList<FloatingTask> floatingTasks;
	private CurrentDateAndTime date;

	public Display(ArrayList<Event> _events, ArrayList<Task> _tasks, ArrayList<FloatingTask> _floatingTasks) {
		date = new CurrentDateAndTime();
		
		events = new ArrayList<Event>();
		tasks = new ArrayList<Task>();
		floatingTasks = new ArrayList<FloatingTask>();
		
		eventsToday = new ArrayList<Event>();
		upcomingEvents = new ArrayList<Event>();
		pastEvents = new ArrayList<Event>();
		
		tasksToday = new ArrayList<Task>();
		restOfTasks = new ArrayList<Task>();
		
		events = _events;
		tasks = _tasks;
		floatingTasks = _floatingTasks;
		
//		System.out.println("In display...");
//		
//		System.out.println("Printing all events");
//		
//		for(Event e : events){
//			System.out.println(e.getIndex());
//			System.out.println(e.getName());
//		}
//		
//		System.out.println("Printing all tasks");
//		
//		for(Task t : tasks){
//			System.out.println(t.getIndex());
//			System.out.println(t.getName());
//		}
//		
//		System.out.println("Printing all floating tasks");
//		
//		for(FloatingTask f : floatingTasks){
//			System.out.println(f.getIndex());
//			System.out.println(f.getName());
//		}
	}

	public static boolean manual() {
		// INTRO
		System.out.println("Hello! This is the cheatsheet for tempo!");
		System.out.println("");
		System.out.println("Manage TEMPO with the following KEYWORDS and PARAMETERS");
		System.out.println("The following cheatsheet shows you the function and the keywords you can use");
		System.out.println("<KEY>: keywords you can use for each function");
		System.out.println("<id>: id is the index of the event as supplied by “List all events”");
		System.out.println("<start date>/<end date>: day month year");
		System.out.println("<start time>/<end time>: 24 hours format");
		System.out.println("");

		// how to add
		System.out.println("HOW TO ADD EVENT");
		System.out.println("================");
		System.out.println("<KEY>: add/create/new");
		System.out.println("<KEY> event <name> from <start date> at <start time> to <end date> at <end time>");
		System.out.println("");

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
		System.out.println("");

		// how to add recurring events
		System.out.println("HOW TO ADD RECURRING EVENT");
		System.out.println("==========================");
		System.out.println("<KEY>: repeat");
		System.out.println("For daily recurring events: <KEY> event <id> daily until <end date>");
		System.out.println("For weekly recurring events: <KEY> event <id> weekly until <end date>");
		System.out.println("For monthly recurring events: <KEY> event <id> monthly until <end date>");
		System.out.println("For annually recurring events: <KEY> event <id> yearly until <end date>");
		System.out.println("");

		// how to delete existing events
		System.out.println("HOW TO DELETE EXISTING EVENT");
		System.out.println("============================");
		System.out.println("<KEY>: delete/cancel/remove");
		System.out.println("<KEY> event <id>");
		System.out.println("");

		// how to search events
		System.out.println("HOW TO SEARCH EXISTING EVENT");
		System.out.println("============================");
		System.out.println("<KEY>: search/find");
		System.out.println("By name: <KEY> event <keywords>");
		System.out.println("By id: <KEY> event <id>");
		System.out.println("");

		// how to list upcoming events
		System.out.println("HOW TO LIST UPCOMING EVENT/S");
		System.out.println("============================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> upcoming events");
		System.out.println("");

		// how to list all events
		System.out.println("HOW TO LIST ALL EVENT/S");
		System.out.println("=======================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> events");
		System.out.println("");

		// how to add tasks
		System.out.println("HOW TO ADD TASK");
		System.out.println("===============");
		System.out.println("<KEY>: add/create/new");
		System.out.println("For tasks with no deadlines: <KEY> task <name>");
		System.out.println("For tasks with deadlines: <KEY> task <name> due <date>");
		System.out.println("");

		// how to edit existing tasks
		System.out.println("HOW TO EDIT EXISTING TASK");
		System.out.println("=========================");
		System.out.println("<KEY>: edit/update/change");
		System.out.println("<KEY> task <id> <name> due <date>");
		System.out.println("For changing of name only: <KEY> task <id> name: <name>");
		System.out.println("For changing of due date only: <KEY> task <id> due: <date>");
		System.out.println("");

		// how to delete existing tasks
		System.out.println("HOW TO DELETE EXISTING TASK");
		System.out.println("===========================");
		System.out.println("<KEY>: delete/cancel/remove");
		System.out.println("<KEY> task <id>");
		System.out.println("");

		// how to search tasks
		System.out.println("HOW TO SEARCH EXISTING TASK");
		System.out.println("===========================");
		System.out.println("<KEY>: search/find");
		System.out.println("<KEY> task <keywords>");
		System.out.println("");

		// how to list undone tasks
		System.out.println("HOW TO LIST UNDONE TASK/S");
		System.out.println("=========================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> undone tasks");
		System.out.println("");

		// how to list missed tasks
		System.out.println("HOW TO LIST MISSED TASK/S");
		System.out.println("=========================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> missed tasks");
		System.out.println("");

		// how to list all tasks
		System.out.println("HOW TO LIST ALL TASK/S");
		System.out.println("======================");
		System.out.println("<KEY>: list/all/view/display");
		System.out.println("<KEY> tasks");
		System.out.println("");

		// how to mark task as done
		System.out.println("HOW TO MARK TASK AS DONE");
		System.out.println("========================");
		System.out.println("<KEY>: mark/flag");
		System.out.println("<KEY> task done <id>");
		System.out.println("");

		// how to undo previous operation
		System.out.println("HOW TO UNDO PREVIOUS OPERATION");
		System.out.println("==============================");
		System.out.println("<KEY>: undo");
		System.out.println("<KEY>");
		System.out.println("");

		// how to view today’s events and tasks due
		System.out.println("HOW TO VIEW EVENTS AND TASKS DUE TODAY ");
		System.out.println("=======================================");
		System.out.println("<KEY>: view/display");
		System.out.println("<KEY> today");
		System.out.println("");

		// how to view this week’s events and tasks due
		System.out.println("HOW TO VIEW EVENTS AND TASKS DUE THIS WEEK ");
		System.out.println("===========================================");
		System.out.println("<KEY>: view/display");
		System.out.println("<KEY> week");
		System.out.println("");
		return false;

	}

	// view all the events
	public boolean events() throws ParseException {
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
			System.out.println("There are no upcoming events");
			System.out.println("");
		}

		else {
			System.out.println("These are your upcoming events!");
			printEvents(upcomingEvents.size(), upcomingEvents);
			System.out.println("");
		}
	}

	private void printEventsToday() {
		if (eventsToday.isEmpty()) {
			System.out.println("There are no events today!");
			System.out.println("");
		} else {
			System.out.println("These are your events for the day!");
			printEvents(eventsToday.size(), eventsToday);
			System.out.println("");
		}
	}
	
	private void printPastEvents() {
		if(pastEvents.isEmpty()){
			System.out.println("There are no past events!");
			System.out.println("");
		}
		
		else{
			System.out.println("These are your past events!");
			printEvents(pastEvents.size(), pastEvents);
			System.out.println("");
		}
		
	}
	
	
	private void splitEvents(String currentDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	Date dateCurr = sdf.parse(currentDate);
		for (int i = 0; i < events.size(); i++) {
			Date dateCompare = sdf.parse(events.get(i).getStartDate());
			if (dateCurr.compareTo(dateCompare)==0) {
				eventsToday.add(events.get(i));
			} 
			else if(dateCurr.compareTo(dateCompare)<0){
				upcomingEvents.add(events.get(i));
			}
			else if(dateCurr.compareTo(dateCompare)>0){
				pastEvents.add(events.get(i));
			}
		}
	}

	private void printEvents(int numOfEvents, ArrayList<Event> events) {
		for (int i = 0; i < numOfEvents; i++) {
			Event currEvent = events.get(i);
			int num = i + 1;
			System.out.print(num + ") ");
			//System.out.println(events.get(i).toString());
			System.out.println(currEvent.getName() +" From: "+ currEvent.getStartDateTime() + " To: " + currEvent.getEndDateTime() + "\t[ID:" + currEvent.getIndex() + "] ");
		}
	}

	// view all tasks
	public boolean tasks() {
		printFloatingTasks(floatingTasks);
		splitTasksByDates(date.getDate());
		printTasksToday();
		printRestOfTasks();
		return true;
	}

	private void printRestOfTasks() {

		if (restOfTasks.isEmpty()) {
			System.out.println("You have no more tasks with deadline");
			System.out.println("");
		} else {
			System.out.println("These are the rest of your tasks!");
			printTasks(restOfTasks);
			System.out.println("");
		}
	}

	private void printTasksToday() {
		if (tasksToday.isEmpty()) {
			System.out.println("You have no tasks today");
			System.out.println("");
		}

		else {
			System.out.println("Tasks due today!");
			printTasks(tasksToday);
			System.out.println("");
		}
	}

	private void splitTasksByDates(String currentDate) {
		for (int i = 0; i < tasks.size(); i++) {
			if (currentDate.equalsIgnoreCase(tasks.get(i).getDueDateSimplified())) {
				tasksToday.add(tasks.get(i));
			} else {
				restOfTasks.add(tasks.get(i));
			}
		}
	}

	private void printFloatingTasks(ArrayList<FloatingTask> floatingTasks) {

		if (floatingTasks.isEmpty()) {
			System.out.println("You have no more task without deadline");
		}

		else {
			System.out.println("Tasks without deadline!");
			for (int i = 0; i < floatingTasks.size(); i++) {
				FloatingTask currFT = floatingTasks.get(i);
				int num = i + 1;
				System.out.print(num + ") ");
				//System.out.println(floatingTasks.get(i).toString());
				System.out.println(currFT.getName() + "\t[ID:" + currFT.getIndex() + "] ");
			}
			System.out.println("");
		}
	}

	private void printTasks(ArrayList<Task> tasks) {
		for (int i = 0; i < tasks.size(); i++) {
			int num = i + 1;
			Task currTask = tasks.get(i);
			System.out.print(num + ") ");
			System.out.println(currTask.getName() + " Due: " + currTask.getDueDate() + "\t[ID:" + currTask.getIndex() + "] ");
		}
	}

	// view all tasks and all events
	public void all() throws ParseException {
		splitEvents(date.getDate());
		printAllEvents();

		printFloatingTasks(floatingTasks);
		splitTasksByDates(date.getDate());
		printTasksToday();
		printRestOfTasks();
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
		for (int i = 0; i < tasks.size(); i++) {
			if (!tasks.get(i).getDone()) {
				undoneTasks.add(tasks.get(i));
			}
		}

		for (int i = 0; i < floatingTasks.size(); i++) {
			if (!floatingTasks.get(i).getDone()) {
				undoneFloatingTasks.add(floatingTasks.get(i));
			}
		}

		System.out.println("These are the list of undone tasks");
		printTasks(undoneTasks);
		printFloatingTasks(undoneFloatingTasks);

		return false;
	}

	
	public void today() throws ParseException {
		splitEvents(date.getDate());
		splitTasksByDates(date.getDate());
		printEventsToday();
		printTasksToday();

	}

	public void missedTasks() {
		// TODO Auto-generated method stub
		
	}

}
