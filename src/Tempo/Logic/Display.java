package Tempo.Logic;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import Tempo.Commands.Result;
import Tempo.Data.CalendarObject;
import Tempo.Data.Event;
import Tempo.Data.FloatingTask;
import Tempo.Data.Task;
//@@author A0125303X
public class Display {
	private Calendar cal = Calendar.getInstance();

	// Global variables
	private ArrayList<CalendarObject> events;
	private ArrayList<CalendarObject> eventsToday;
	private ArrayList<CalendarObject> upcomingEvents;
	private ArrayList<CalendarObject> pastEvents;
	private ArrayList<CalendarObject> tasks;
	private ArrayList<CalendarObject> tasksToday;
	private ArrayList<CalendarObject> upcomingTasks;
	private ArrayList<CalendarObject> missedTasks;
	private ArrayList<CalendarObject> floatingTasks;
	private ArrayList<CalendarObject> undoneTasks;
	private ArrayList<CalendarObject> undoneFloatingTasks;
	private ArrayList<CalendarObject> doneTasks;
	private ArrayList<CalendarObject> doneFloatingTasks;
	// private ArrayList<String> wordFoundLines;
	private CurrentTime date;

	// Messages
	private final String UPCOMING_EVENTS = "These are the list of upcoming events";
	private final String NO_UPCOMING_EVENTS = "You have no upcoming event";
	private final String TODAY_EVENTS = "These are your events for the day";
	private final String NO_TODAY_EVENTS = "You have no event today";
	private final String PAST_EVENTS = "These are the list of past events";
	private final String NO_PAST_EVENTS = "You have no past event";
	private final String TODAY_TASKS = "Tasks due today";
	private final String NO_TODAY_TASKS = "You have no task today";
	private final String UPCOMING_TASKS = "These are the list of upcoming tasks";
	private final String NO_UPCOMING_TASKS = "You have no upcoming task";
	private final String MISSED_TASKS = "These are the tasks you missed";
	private final String NO_MISSED_TASKS = "You have no missed task";
	private final String UNDONE_TASKS = "These are the list of undone tasks";
	private final String NO_UNDONE_TASKS = "You have no undone tasks";
	private final String DONE_TASKS = "These are all the tasks that are done";
	private final String NO_DONE_TASKS = "You have no done tasks";
	private final String UNDONE_FLOATING_TASKS = "These are the list of tasks without deadline";
	private final String NO_UNDONE_FLOATING_TASKS = "You have no task without deadline";
	private final String DONE_FLOATING_TASKS = "These are the list of tasks without deadline that are done";
	private final String NO_DONE_FLOATING_TASKS = "You have no task without deadline that are done";
	private final String EMPTY_STRING = "";
	
	private static final String NO_BEST_MATCH = "There are no best matches found!";
	private static final String BEST_MATCH_STRING = "These are the best matches found:";
	private static final String NO_ALTERNATIVE_MATCH = "There are no alternative matches found!";
	private static final String NO_MATCHES = "There are no matches found!";
	private static final String ALTERNATIVE_MATCH_STRING = "Other Results:";
	
	private static final String KEY_EVENTS = "Events";
	private static final String KEY_TASKS = "Tasks";
	private static final String KEY_FLOATING_TASKS = "Floating Tasks";

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
		cal = Calendar.getInstance();
		
		events = cal.getEventsList();
		tasks = cal.getTasksList();
		floatingTasks = cal.getFloatingTasksList();
		date = new CurrentTime();

		eventsToday = new ArrayList<CalendarObject>();
		upcomingEvents = new ArrayList<CalendarObject>();
		pastEvents = new ArrayList<CalendarObject>();

		tasksToday = new ArrayList<CalendarObject>();
		upcomingTasks = new ArrayList<CalendarObject>();
		missedTasks = new ArrayList<CalendarObject>();

		undoneTasks = new ArrayList<CalendarObject>();
		undoneFloatingTasks = new ArrayList<CalendarObject>();

		doneTasks = new ArrayList<CalendarObject>();
		doneFloatingTasks = new ArrayList<CalendarObject>();

		// System.out.println(events.size());
		// System.out.println(cal.getEventsList().get(0).getStartDate());

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
		// System.out.println(currentDate);
		for (int i = 0; i < events.size(); i++) {
			Date dateCompare = getDateInDateFormat(((Event) events.get(i)).getStartDate());
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
			if (!((FloatingTask) floatingTasks.get(i)).isDone()) {
				undoneFloatingTasks.add(floatingTasks.get(i));
			} else {
				doneFloatingTasks.add(floatingTasks.get(i));
			}
		}
	}

	private void splitTasks() {
		for (int i = 0; i < tasks.size(); i++) {
			if (!((FloatingTask) tasks.get(i)).isDone()) {
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
			Date dateCompare = getDateInDateFormat(((Task) undoneTasks.get(i)).getDueDateSimplified());
			if (dateCurr.compareTo(dateCompare) == 0) {
				tasksToday.add(undoneTasks.get(i));
			} else if (dateCurr.compareTo(dateCompare) < 0) {
				upcomingTasks.add(undoneTasks.get(i));
			} else if (dateCurr.compareTo(dateCompare) > 0) {
				missedTasks.add(undoneTasks.get(i));
			}
		}
	}

	public Result getUpcomingEvents() {
		refresh();

		ArrayList<String> upcomingEventsStr = getUpcomingEventsString();
		String returnString = strArrayToString(upcomingEventsStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("upcomingEvents", upcomingEvents);
		Result upcomingEvents = new Result(returnString, true, true, hm);
		return upcomingEvents;
	}
	
	public ArrayList<String> getUpcomingEventsString() {
		refresh();

		ArrayList<String> upcomingEventsStr = new ArrayList<String>();
		if (upcomingEvents.isEmpty()) {
			upcomingEventsStr.add(NO_UPCOMING_EVENTS);
		} else {
			upcomingEventsStr.add(UPCOMING_EVENTS);
			upcomingEventsStr.add(EMPTY_STRING);
			upcomingEventsStr = addStrEventToArray(upcomingEventsStr, upcomingEvents);
		}
		
		return upcomingEventsStr;
	}

	public Result getEventsToday() {
		refresh();

		ArrayList<String> eventsTodayStr = getEventsTodayString();
		String returnString = strArrayToString(eventsTodayStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("Events", eventsToday);
		Result eventsToday = new Result(returnString, true, true, hm);

		return eventsToday;
	}
	
	public ArrayList<String> getEventsTodayString() {
		refresh();

		ArrayList<String> eventsTodayStr = new ArrayList<String>();
		if (eventsToday.isEmpty()) {
			eventsTodayStr.add(NO_TODAY_EVENTS);
		} else {
			eventsTodayStr.add(TODAY_EVENTS);
			eventsTodayStr.add(EMPTY_STRING);
			eventsTodayStr = addStrEventToArray(eventsTodayStr, eventsToday);
		}

		return eventsTodayStr;
	}

	public Result getPastEvents() {
		refresh();

		ArrayList<String> pastEventsStr = getPastEventsString();
		String returnString = strArrayToString(pastEventsStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("Events", pastEvents);
		Result pastEvents = new Result(returnString, true, true, hm);

		return pastEvents;
	}
	
	public ArrayList<String> getPastEventsString() {
		refresh();

		ArrayList<String> pastEventsStr = new ArrayList<String>();
		if (pastEvents.isEmpty()) {
			pastEventsStr.add(NO_PAST_EVENTS);
		} else {
			pastEventsStr.add(PAST_EVENTS);
			pastEventsStr.add(EMPTY_STRING);
			pastEventsStr = addStrEventToArray(pastEventsStr, pastEvents);
		}

		return pastEventsStr;
	}

	private ArrayList<String> addStrEventToArray(ArrayList<String> eventsStr, ArrayList<CalendarObject> events) {
		for (int i = 0; i < events.size(); i++) {
			Event currEvent = (Event)events.get(i);
			int num = i + 1;
			eventsStr.add(num + ") " + currEvent.getName() + " From: " + currEvent.getStartDateTime() + " To: "
					+ currEvent.getEndDateTime() + "\t[ID:" + currEvent.getIndex() + "] ");
		}
		return eventsStr;
	}
	
	/******TASKS ******/
	public Result getTasksToday() {
		refresh();
		ArrayList<String> tasksTodayStr = getTasksTodayString();
		String returnString = strArrayToString(tasksTodayStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("Tasks", tasksToday);
		Result tasksToday = new Result(returnString, true, true, hm);

		return tasksToday;
	}
	
	public ArrayList<String> getTasksTodayString() {
		refresh();
		ArrayList<String> tasksTodayStr = new ArrayList<String>();

		if (tasksToday.isEmpty()) {
			tasksTodayStr.add(NO_TODAY_TASKS);
		} else {
			tasksTodayStr.add(TODAY_TASKS);
			tasksTodayStr.add(EMPTY_STRING);
			tasksTodayStr = addStrTasksToArray(tasksTodayStr, tasksToday);
		}

		return tasksTodayStr;
	}

	public Result getUpcomingTasks() {
		refresh();
		ArrayList<String> upcomingTasksStr = getUpcomingTasksString();
		String returnString = strArrayToString(upcomingTasksStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("Tasks", upcomingTasks);
		Result upcomingTasks = new Result(returnString, true, true, hm);

		return upcomingTasks;
	}
	
	public ArrayList<String> getUpcomingTasksString() {
		refresh();
		ArrayList<String> upcomingTasksStr = new ArrayList<String>();

		if (upcomingTasks.isEmpty()) {
			upcomingTasksStr.add(NO_UPCOMING_TASKS);
		} else {
			upcomingTasksStr.add(UPCOMING_TASKS);
			upcomingTasksStr.add(EMPTY_STRING);
			upcomingTasksStr = addStrTasksToArray(upcomingTasksStr, upcomingTasks);
		}
		
		return upcomingTasksStr;
	}

	public Result getMissedTasks() {
		refresh();
		ArrayList<String> missedTasksStr = getMissedTasksString();
		String returnString = strArrayToString(missedTasksStr);


		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("Tasks", missedTasks);
		Result missedTasks = new Result(returnString, true, true, hm);

		return missedTasks;
	}
	
	public ArrayList<String> getMissedTasksString() {
		refresh();
		ArrayList<String> missedTasksStr = new ArrayList<String>();
		if (missedTasks.isEmpty()) {
			missedTasksStr.add(NO_MISSED_TASKS);
		} else {
			missedTasksStr.add(MISSED_TASKS);
			missedTasksStr.add(EMPTY_STRING);
			missedTasksStr = addStrTasksToArray(missedTasksStr, missedTasks);
		}

		return missedTasksStr;
	}

	public Result getUndoneTasks() {
		refresh();
		ArrayList<String> undoneTasksStr = getUndoneTasksString();
		String returnString = strArrayToString(undoneTasksStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("Tasks", undoneTasks);
		Result undoneTasks = new Result(returnString, true, true, hm);
		return undoneTasks;
	}
	
	public ArrayList<String> getUndoneTasksString(){
		refresh();
		ArrayList<String> undoneTasksStr = new ArrayList<String>();

		if (undoneTasks.isEmpty()) {
			undoneTasksStr.add(NO_UNDONE_TASKS);
		} else {
			undoneTasksStr.add(UNDONE_TASKS);
			undoneTasksStr.add(EMPTY_STRING);
			undoneTasksStr = addStrTasksToArray(undoneTasksStr, undoneTasks);
		}
		
		return undoneTasksStr;
	}

	public Result getDoneTasks() {
		refresh();
		ArrayList<String> doneTasksStr = getDoneTasksString();
		String returnString = strArrayToString(doneTasksStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("Tasks", doneTasks);
		Result doneTasks = new Result(returnString, true, true, hm);

		return doneTasks;
	}
	
	public ArrayList<String> getDoneTasksString() {
		refresh();
		ArrayList<String> doneTasksStr = new ArrayList<String>();
		if (doneTasks.isEmpty()) {
			doneTasksStr.add(NO_DONE_TASKS);
		} else {
			doneTasksStr.add(DONE_TASKS);
			doneTasksStr.add(EMPTY_STRING);
			doneTasksStr = addStrTasksToArray(doneTasksStr, doneTasks);
		}
		return doneTasksStr;
	}

	private ArrayList<String> addStrTasksToArray(ArrayList<String> tasksStr, ArrayList<CalendarObject> tasks) {
		for (int i = 0; i < tasks.size(); i++) {
			Task currTask = (Task)tasks.get(i);
			int num = i + 1;
			tasksStr.add(num + ") " + currTask.getName() + " Due: " + currTask.getDueDate() + "\t[ID:"
					+ currTask.getIndex() + "] ");
		}
		return tasksStr;
	}

	/*****FLOATING TASKS**********/
	public Result getUndoneFloatingTasks() {
		refresh();
		ArrayList<String> undoneFTasksStr = getUndoneFloatingTasksString();
		String returnString = strArrayToString(undoneFTasksStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("Tasks", undoneFloatingTasks);
		Result undoneFTasks = new Result(returnString, true, true, hm);

		return undoneFTasks;
	}
	
	public ArrayList<String> getUndoneFloatingTasksString() {
		refresh();
		ArrayList<String> undoneFTasksStr = new ArrayList<String>();

		if (undoneFloatingTasks.isEmpty()) {
			undoneFTasksStr.add(NO_UNDONE_FLOATING_TASKS);
		} else {
			undoneFTasksStr.add(UNDONE_FLOATING_TASKS);
			undoneFTasksStr.add(EMPTY_STRING);
			undoneFTasksStr = addStrFTasksToArray(undoneFTasksStr, undoneFloatingTasks);
		}
		return undoneFTasksStr;
	}

	public Result getDoneFloatingTasks() {
		refresh();
		ArrayList<String> doneFTasksStr =getDoneFloatingTasksString();
		String returnString = strArrayToString(doneFTasksStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("Tasks", doneFloatingTasks);
		Result doneFTasks = new Result(returnString, true, true, hm);

		return doneFTasks;
	}
	
	public ArrayList<String> getDoneFloatingTasksString() {
		refresh();
		ArrayList<String> doneFTasksStr = new ArrayList<String>();
		if (doneFloatingTasks.isEmpty()) {
			doneFTasksStr.add(NO_DONE_FLOATING_TASKS);
		} else {
			doneFTasksStr.add(DONE_FLOATING_TASKS);
			doneFTasksStr.add(EMPTY_STRING);
			doneFTasksStr = addStrFTasksToArray(doneFTasksStr, doneFloatingTasks);
		}
		return doneFTasksStr;
	}

	private ArrayList<String> addStrFTasksToArray(ArrayList<String> fTasksStr, ArrayList<CalendarObject> fTasks) {
		for (int i = 0; i < fTasks.size(); i++) {
			FloatingTask currFT = (FloatingTask)fTasks.get(i);
			int num = i + 1;
			fTasksStr.add(num + ") " + currFT.getName() + "\t[ID:" + currFT.getIndex() + "] ");
		}
		return fTasksStr;
	}

	public Result getEvents() {
		refresh();
		 ArrayList<String> eventsStr = new ArrayList<String>();
		 eventsStr.addAll(getEventsTodayString());
		 eventsStr.addAll(getUpcomingEventsString());
		 eventsStr.addAll(getPastEventsString());
		 
		 String returnString = strArrayToString(eventsStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("eventsToday", eventsToday);
		hm.put("upcomingEvents", upcomingEvents);
		hm.put("pastEvents", pastEvents);
		Result events = new Result(returnString, true, true, hm);

		return events;

	}
	
	public ArrayList<String> getEventsString() {
		refresh();
		
		 ArrayList<String> eventsStr = new ArrayList<String>();
		 eventsStr.addAll(getEventsTodayString());
		 eventsStr.add(EMPTY_STRING);
		 eventsStr.addAll(getUpcomingEventsString());
		 eventsStr.add(EMPTY_STRING);
		 eventsStr.addAll(getPastEventsString());
		 eventsStr.add(EMPTY_STRING);

		return eventsStr;

	}

	public Result getTasks() {
		refresh();
		
		 ArrayList<String> tasksStr = getTasksString();
		 
		 String returnString = strArrayToString(tasksStr);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("tasksToday", tasksToday);
		hm.put("upcomingTasks", upcomingTasks);
		hm.put("missedTasks", missedTasks);
		hm.put("doneTasks", doneTasks);
		Result tasks = new Result(returnString, true, true, hm);

		return tasks;
	}
	
	public ArrayList<String> getTasksString() {
		refresh();
		
		 ArrayList<String> tasksStr = new ArrayList<String>();
		 tasksStr.addAll(getUndoneFloatingTasksString());
		 tasksStr.add(EMPTY_STRING);
		 tasksStr.addAll(getDoneFloatingTasksString());
		 tasksStr.add(EMPTY_STRING);
		 tasksStr.addAll(getDoneTasksString());
		 tasksStr.add(EMPTY_STRING);
		 tasksStr.addAll(getTasksTodayString());
		 tasksStr.add(EMPTY_STRING);
		 tasksStr.addAll(getUpcomingTasksString());
		 tasksStr.add(EMPTY_STRING);
		 tasksStr.addAll(getMissedTasksString());
		 tasksStr.add(EMPTY_STRING);

		return tasksStr;
	}

	public Result getAll() {
		refresh();

		 ArrayList<String> all = new ArrayList<String>();
		 all.addAll(getEventsString());
		 all.addAll(getTasksString());
		
		 String returnString = strArrayToString(all);

		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("tasksToday", tasksToday);
		hm.put("upcomingTasks", upcomingTasks);
		hm.put("missedTasks", missedTasks);
		hm.put("doneTasks", doneTasks);
		hm.put("eventsToday", eventsToday);
		hm.put("upcomingEvents", upcomingEvents);
		hm.put("pastEvents", pastEvents);
		hm.put("undoneFloatingTasks", undoneFloatingTasks);
		hm.put("doneFloatingTasks", doneFloatingTasks);
		
		Result result = new Result(returnString, true, true, hm);

		return result;
	}

	public Result getToday() {
		ArrayList<String> todayString = getTodayString();
		String returnString = strArrayToString(todayString);
		
		HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		hm.put("tasksToday", tasksToday);
		hm.put("eventsToday", eventsToday);
		// hm.put("FloatingTasks", floatingTasks);
		Result today = new Result(returnString, true, true, hm);

		return today;
	}
	
	public ArrayList<String> getTodayString() {
		refresh();
		ArrayList<String> todayString = new ArrayList<String>();

		if (eventsToday.isEmpty()) {
			todayString.add(NO_TODAY_EVENTS);
		} else {
			todayString.add(TODAY_EVENTS);
			todayString = addStrEventToArray(todayString, eventsToday);
		}
		
		if (tasksToday.isEmpty()) {
			todayString.add(NO_TODAY_TASKS);
		} else {
			todayString.add(TODAY_TASKS);
			todayString = addStrEventToArray(todayString, tasksToday);
		}
		return todayString;
	}
	
	public String formatSearchResults(ArrayList<CalendarObject> eventsBestMatch, ArrayList<CalendarObject> eventsAlternativeMatch, ArrayList<CalendarObject>tasksBestMatch, ArrayList<CalendarObject>tasksAlternativeMatch, ArrayList<CalendarObject>floatingTaskBestMatch, ArrayList<CalendarObject>floatingTasksAlternativeMatch){
		ArrayList<String> searchResults = new ArrayList<String>();
		
		boolean hasBestMatches = true;
		boolean hasAlternativeMatches = false;
		
		if(eventsBestMatch.isEmpty() && tasksBestMatch.isEmpty() && floatingTaskBestMatch.isEmpty()){
			hasBestMatches = false;
			searchResults.add(NO_BEST_MATCH);
		}else{
			searchResults.add(BEST_MATCH_STRING);
			searchResults.add(EMPTY_STRING);
			
			if(!eventsBestMatch.isEmpty()){
				searchResults.add(KEY_EVENTS);
				searchResults = addStrEventToArray(searchResults, eventsBestMatch);
			}
			
			if(!tasksBestMatch.isEmpty()){
				searchResults.add(EMPTY_STRING);
				searchResults.add(KEY_TASKS);
				searchResults = addStrTasksToArray(searchResults, tasksBestMatch);
			}
			
			if(!floatingTaskBestMatch.isEmpty()){
				searchResults.add(EMPTY_STRING);
				searchResults.add(KEY_FLOATING_TASKS);
				searchResults = addStrFTasksToArray(searchResults, floatingTaskBestMatch);
			}
		}
		
		searchResults.add(EMPTY_STRING);
		
		if(eventsAlternativeMatch.isEmpty() && tasksAlternativeMatch.isEmpty() && floatingTasksAlternativeMatch.isEmpty()){
			hasAlternativeMatches = false;
			searchResults.add(NO_ALTERNATIVE_MATCH);
		}else{
			searchResults.add(ALTERNATIVE_MATCH_STRING);
			searchResults.add(EMPTY_STRING);
			
			if(!eventsAlternativeMatch.isEmpty()){
				searchResults.add(KEY_EVENTS);
				searchResults = addStrEventToArray(searchResults, eventsAlternativeMatch);
			}
			
			if(!tasksAlternativeMatch.isEmpty()){
				searchResults.add(EMPTY_STRING);
				searchResults.add(KEY_TASKS);
				searchResults = addStrTasksToArray(searchResults, tasksAlternativeMatch);
			}
			
			if(!floatingTasksAlternativeMatch.isEmpty()){
				searchResults.add(EMPTY_STRING);
				searchResults.add(KEY_FLOATING_TASKS);
				searchResults = addStrFTasksToArray(searchResults, floatingTasksAlternativeMatch);
			}
		}
		
		if(!hasBestMatches && !hasAlternativeMatches){
			searchResults = new ArrayList<String>();
			searchResults.add(NO_MATCHES);
		}
		
		String returnString = strArrayToString(searchResults);
		
		return returnString;
	}
	
	private String strArrayToString(ArrayList<String> in){
		String s = "";
		
		for(String str : in){
			s += str+"\n";
		}
		
		return s;
	}
	
	public static String getManual(){
		String manual = "";
		// INTRO
		manual += "Hello! This is the cheatsheet for tempo!\n";
		
		manual += "Manage TEMPO with the following KEYWORDS and PARAMETERS\n";
		manual += "The following cheatsheet shows you the function and the keywords you can use\n";
		manual += "<KEY>: keywords you can use for each function\n";
		manual += "<id>: id is the index of the event as supplied by “List all events”\n";
		manual += "<start date>/<end date>: day month year\n";
		manual += "<start time>/<end time>: 24 hours format\n";
		

		// how to add
		manual += "HOW TO ADD EVENT\n";
		manual += "================\n";
		manual += "<KEY>: add/create/new\n";
		manual += "<KEY> event <name> from <start date> at <start time> to <end date> at <end time>\n";
		

		// how to edit
		manual += "HOW TO EDIT EVENT\n";
		manual += "=================\n";
		manual += "<KEY>: edit/update/change\n";
		manual += "<KEY> event <id> <name> from <start date> at <start time> to <end date> at <end time>\n";
		manual += "For editing the event name only: <KEY> event <id> name: <name>\n";
		manual += "For editing the start date only: <KEY> event <id> start date: <start date>\n";
		manual += "For editing the start time only: <KEY> event <id> start time: <start time>\n";
		manual += "For editing of end date only: <KEY> event <id> end date: <end date>\n";
		manual += "For editing of end time only: <KEY> event <id> end time: <end time>\n";
		

		// how to add recurring events
		manual += "HOW TO ADD RECURRING EVENT\n";
		manual += "==========================\n";
		manual += "<KEY>: repeat\n";
		manual += "For daily recurring events: <KEY> event <id> daily until <end date>\n";
		manual += "For weekly recurring events: <KEY> event <id> weekly until <end date>\n";
		manual += "For monthly recurring events: <KEY> event <id> monthly until <end date>\n";
		manual += "For annually recurring events: <KEY> event <id> yearly until <end date>\n";
		

		// how to delete existing events
		manual += "HOW TO DELETE EXISTING EVENT\n";
		manual += "============================\n";
		manual += "<KEY>: delete/cancel/remove\n";
		manual += "<KEY> event <id>\n";
		

		// how to search events
		manual += "HOW TO SEARCH EXISTING EVENT\n";
		manual += "============================\n";
		manual += "<KEY>: search/find\n";
		manual += "By name: <KEY> event <keywords>\n";
		manual += "By id: <KEY> event <id>\n";
		

		// how to list upcoming events
		manual += "HOW TO LIST UPCOMING EVENT/S\n";
		manual += "============================\n";
		manual += "<KEY>: list/all/view/display\n";
		manual += "<KEY> upcoming events\n";
		

		// how to list all events
		manual += "HOW TO LIST ALL EVENT/S\n";
		manual += "=======================\n";
		manual += "<KEY>: list/all/view/display\n";
		manual += "<KEY> events\n";
		

		// how to add tasks
		manual += "HOW TO ADD TASK\n";
		manual += "===============\n";
		manual += "<KEY>: add/create/new\n";
		manual += "For tasks with no deadlines: <KEY> task <name>\n";
		manual += "For tasks with deadlines: <KEY> task <name> due <date>\n";
		

		// how to edit existing tasks
		manual += "HOW TO EDIT EXISTING TASK\n";
		manual += "=========================\n";
		manual += "<KEY>: edit/update/change\n";
		manual += "<KEY> task <id> <name> due <date>\n";
		manual += "For changing of name only: <KEY> task <id> name: <name>\n";
		manual += "For changing of due date only: <KEY> task <id> due: <date>\n";
		

		// how to delete existing tasks
		manual += "HOW TO DELETE EXISTING TASK\n";
		manual += "===========================\n";
		manual += "<KEY>: delete/cancel/remove\n";
		manual += "<KEY> task <id>\n";
		

		// how to search tasks
		manual += "HOW TO SEARCH EXISTING TASK\n";
		manual += "===========================\n";
		manual += "<KEY>: search/find\n";
		manual += "<KEY> task <keywords>\n";
		

		// how to list undone tasks
		manual += "HOW TO LIST UNDONE TASK/S\n";
		manual += "=========================\n";
		manual += "<KEY>: list/all/view/display\n";
		manual += "<KEY> undone tasks\n";
		

		// how to list missed tasks
		manual += "HOW TO LIST MISSED TASK/S\n";
		manual += "=========================\n";
		manual += "<KEY>: list/all/view/display\n";
		manual += "<KEY> missed tasks\n";
		

		// how to list all tasks
		manual += "HOW TO LIST ALL TASK/S\n";
		manual += "======================\n";
		manual += "<KEY>: list/all/view/display\n";
		manual += "<KEY> tasks\n";
		

		// how to mark task as done
		manual += "HOW TO MARK TASK AS DONE\n";
		manual += "========================\n";
		manual += "<KEY>: mark/flag\n";
		manual += "<KEY> task done <id>\n";
		

		// how to undo previous operation
		manual += "HOW TO UNDO PREVIOUS OPERATION\n";
		manual += "==============================\n";
		manual += "<KEY>: undo\n";
		manual += "<KEY>\n";
		

		// how to view today’s events and tasks due
		manual += "HOW TO VIEW EVENTS AND TASKS DUE TODAY \n";
		manual += "=======================================\n";
		manual += "<KEY>: view/display\n";
		manual += "<KEY> today\n";
		

		// how to view this week’s events and tasks due
		manual += "HOW TO VIEW EVENTS AND TASKS DUE THIS WEEK \n";
		manual += "===========================================\n";
		manual += "<KEY>: view/display\n";
		manual += "<KEY> week\n";
		
		return manual;
	}

}
