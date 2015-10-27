package Tempo;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
getSearchResults
getAll
getEvents
getTasks*/

/*All my "get" methods will first refresh where it updates all the new changes it will then 
 * create a new ArrayList<String> to store the new changes and return this ArrayList<String>
*/

public class Display {

	private Calendar cal = Calendar.getInstance();

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
	// private ArrayList<String> wordFoundLines;
	private CurrentDateAndTime date;

	// Messages
	private String UPCOMING_EVENTS = "These are the list of upcoming events";
	private String NO_UPCOMING_EVENTS = "You have no upcoming event";
	private String TODAY_EVENTS = "These are your events for the day";
	private String NO_TODAY_EVENTS = "You have no event today";
	private String PAST_EVENTS = "These are the list of past events";
	private String NO_PAST_EVENTS = "You have no past event";

	
	//for testing only!!!!!!!!!!!!!!!!!
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

	// create an object of SingleObject
	private static Display instance = new Display();

	// make the constructor private so that this class cannot be
	// instantiated
	private Display() {
	}

	// Get the only object available
	public static Display getInstance() {
		return instance;
	}

	private void refresh() {
		events = cal.getEventsList();
		tasks = cal.getTasksList();
		floatingTasks = cal.getFloatingTasksList();
		date = new CurrentDateAndTime();
		splitsEvents();
		// splitting (floating) done and undone tasks
		splitFtasks();
		// splitting done and undone tasks
		splitTasks();
		// splitting the dated undone tasks
		splitDatedTask();
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

	private void splitFtasks() {
		for (int i = 0; i < floatingTasks.size(); i++) {
			if (!floatingTasks.get(i).isDone()) {
				undoneFloatingTasks.add(floatingTasks.get(i));
			} else {
				doneFloatingTasks.add(floatingTasks.get(i));
			}
		}
	}

	private void splitTasks() {
		for (int i = 0; i < tasks.size(); i++) {
			if (!tasks.get(i).isDone()) {
				undoneTasks.add(tasks.get(i));
			} else {
				doneTasks.add(tasks.get(i));
			}
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

	public ArrayList<String> getUpcomingEvents() {
		refresh();
		ArrayList<String> upcomingEventsStr = new ArrayList<String>();
		if (upcomingEvents.isEmpty()) {
			upcomingEventsStr.add(NO_UPCOMING_EVENTS);
		} else {
			upcomingEventsStr.add(UPCOMING_EVENTS);
			upcomingEventsStr = addEventToString(upcomingEventsStr, eventsToday);
		}
		return upcomingEventsStr;
	}

	public ArrayList<String> getEventsToday() {
		refresh();
		ArrayList<String> eventsTodayStr = new ArrayList<String>();
		if (eventsToday.isEmpty()) {
			eventsTodayStr.add(NO_TODAY_EVENTS);
		} else {
			eventsTodayStr.add(TODAY_EVENTS);
			eventsTodayStr = addEventToString(eventsTodayStr, eventsToday);
		}
		return eventsTodayStr;
	}

	public ArrayList<String> getPastEvents() {
		refresh();
		ArrayList<String> pastEventsStr = new ArrayList<String>();
		if (pastEvents.isEmpty()) {
			pastEventsStr.add(NO_PAST_EVENTS);
		}

		else {
			pastEventsStr.add(PAST_EVENTS);
			pastEventsStr = addEventToString(pastEventsStr, pastEvents);
		}
		return pastEventsStr;
	}

	private ArrayList<String> addEventToString(ArrayList<String> eventsStr, ArrayList<Event> events) {
		for (int i = 0; i < events.size(); i++) {
			Event currEvent = events.get(i);
			int num = i + 1;
			eventsStr.add(num + ") " + currEvent.getName() + " From: " + currEvent.getStartDateTime() + " To: "
					+ currEvent.getEndDateTime() + "\t[ID:" + currEvent.getIndex() + "] ");
		}
		return eventsStr;
	}

}