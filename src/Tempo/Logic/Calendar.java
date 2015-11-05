package Tempo.Logic;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import Tempo.CalendarObjects.CalendarObject;
import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;
import Tempo.CalendarObjects.Task;
import Tempo.Commands.Command;
import Tempo.Commands.Result;
import Tempo.Commands.UndoRemove;
import Tempo.Storage.CalendarExporter;
import Tempo.Storage.CalendarImporter;

public class Calendar {

	private static Calendar instance = new Calendar();
	private static IndexStore indexStore;
	private static CalendarImporter importer;
	private static CalendarExporter exporter;
	
	private static Stack<Command> history;

	private static final String CMD_ADD = "add";
	private static final String CMD_ADD_EVENT = "add event %1$s";
	private static final String CMD_ADD_RECURR_EVENT = "add recurring event %1$s";
	private static final String CMD_ADD_TASK = "add task %1$s";
	private static final String CMD_ADD_RECURR_TASK = "add recurring task %1$s";
	private static final String CMD_ADD_FLOATING = "add floating task %1$s";
	private static final String CMD_REMOVE = "remove";
	private static final String CMD_REMOVE_EVENT = "remove event %1$s";
	private static final String CMD_REMOVE_TASK = "remove task %1$s";
	private static final String CMD_REMOVE_FLOATING = "remove floating task %1$s";
	private static final String CMD_UPDATE = "update";
	private static final String CMD_UPDATE_EVENT = "update event %1$s";
	private static final String CMD_UPDATE_TASK = "update task %1$s";
	private static final String CMD_UPDATE_FLOATING = "update floating task %1$s";
	private static final String CMD_DONE = "done";
	private static final String CMD_DONE_TASK = "done task %1$s";
	private static final String CMD_DONE_FLOATING = "done floating task %1$s";
	private static final String CMD_UNDO = "undo ";
	private static final String CMD_INVALID_UNDO = "invalid undo";
	private static final String CMD_SEARCH = "search %1$s";

	private static final String KEY_EVENTS = "events";
	private static final String KEY_TASKS = "tasks";
	private static final String KEY_FLOATING = "floating tasks";
	private static final String KEY_DAILY = "daily";
	private static final String KEY_WEEKLY = "weekly";
	private static final String KEY_MONTHLY = "monthly";
	private static final String KEY_ANNUALLY = "annually";

	public static final long MILLISECONDS_A_DAY = 86400000;
	public static final long MILLISECONDS_A_WEEK = 604800000;
	public static final String DATE_DELIMETER = "/";

	private static final int INDEX_INVALID = -1;

	private String _fileName;

	private int prevModIndex = INDEX_INVALID;
	private Event prevModEvent = null;
	private Task prevModTask = null;
	private FloatingTask prevModFloatingTask = null;
	private String prevCommand = CMD_INVALID_UNDO;

	private ArrayList<CalendarObject> eventsList;
	private ArrayList<CalendarObject> tasksList;
	private ArrayList<CalendarObject> floatingTasksList;

	private Calendar() {
		eventsList = new ArrayList<CalendarObject>();
		tasksList = new ArrayList<CalendarObject>();
		floatingTasksList = new ArrayList<CalendarObject>();
		indexStore = IndexStore.getInstance();
		importer = CalendarImporter.getInstance();
		exporter = CalendarExporter.getInstance();
		history = new Stack<Command>();
	}

	public static Calendar getInstance() {
		return instance;
	}

	public void createFile(String fileName) {
		_fileName = fileName;
		File file = new File(_fileName);
		if (file.exists()) {
			importFromFile();
			indexStore.initialiseStore(eventsList, tasksList, floatingTasksList);
		}
	}

	/***** ADD COMMAND EXECUTION ******/

	public Result addEvent(String name, String start, String end) {
		int newEventIndex = indexStore.getNewId();
		int newSeriesIndex = indexStore.getNewSeriesId();
		Event newEvent = new Event(newEventIndex,newSeriesIndex, name, start, end);
		eventsList.add(newEvent);
		
		indexStore.addEvent(newEventIndex, newEvent);
		sortEvents();
		exportToFile();

		//savePrevCmd(newEventIndex, newEvent, null, null, CMD_ADD);

		String cmd = String.format(CMD_ADD_EVENT, name);

		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}
	

	public Result addBackEvent(Event newEvent) {
		eventsList.add(newEvent);
		indexStore.addEvent(newEvent.getIndex(), newEvent);
		sortEvents();
		
		String name = newEvent.getName();
		String cmd = String.format(CMD_ADD_EVENT, name);

		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}

	public Result addRecurringEvent(String name, String start, String end, String recurringType, String recurringEnd) {
		int newEventIndex = indexStore.getNewId();
		int newSeriesIndex = indexStore.getNewSeriesId();
		Event newEvent = new Event(newEventIndex, newSeriesIndex, name, start, end);
		eventsList.add(newEvent);
		indexStore.addEvent(newEventIndex, newEvent);

		// Gets the list of recurring dates;
		ArrayList<String> recurringDates = processRecurringDates(start, recurringEnd, recurringType);
		
		for(String dates: recurringDates){
			newEventIndex = indexStore.getNewId();
			newEvent = new Event(newEventIndex, newSeriesIndex, name, dates, end);
			eventsList.add(newEvent);
			indexStore.addEvent(newEventIndex, newEvent);
		}

		sortEvents();
		exportToFile();

		String cmd = String.format(CMD_ADD_RECURR_EVENT, name);
		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}
	
	public Result addBackRecurrEvent(ArrayList<CalendarObject> events) {
		String name = new String();
		
		for (int i = 0; i < events.size(); i++) {
			Event newEvent = (Event) events.get(i);
			int index = newEvent.getIndex();
			eventsList.add(newEvent);
			indexStore.addEvent(index, newEvent);
			name = newEvent.getName();
		}

		sortEvents();
		exportToFile();

		String cmd = String.format(CMD_ADD_RECURR_EVENT, name);
		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}

	public Result addTask(String name, String dueDate) {
		int newTaskIndex = indexStore.getNewId();
		int newSeriesIndex = indexStore.getNewSeriesId();
		Task newTask = new Task(newTaskIndex, newSeriesIndex, name, dueDate);
		tasksList.add(newTask);
		indexStore.addTask(newTaskIndex, newTask);
		sortTasks();
		exportToFile();

		//savePrevCmd(newTaskIndex, null, newTask, null, CMD_ADD);

		String cmd = String.format(CMD_ADD_TASK, name);
		return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
	}
	
	public Result addBackTask(Task newTask) {
		tasksList.add(newTask);
		indexStore.addTask(newTask.getIndex(), newTask);
		sortTasks();

		String name = newTask.getName();
		String cmd = String.format(CMD_ADD_TASK, name);

		return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
	}
	
	public Result addRecurringTask(String name, String dueDate, String recurringType,String recurringEnd) {
		int newTaskIndex = indexStore.getNewId();
		int newSeriesIndex = indexStore.getNewSeriesId();
		Task newTask = new Task(newTaskIndex, newSeriesIndex, name, dueDate);
		tasksList.add(newTask);
		indexStore.addTask(newTaskIndex, newTask);

		// Gets the list of recurring dates;
		ArrayList<String> recurringDates = processRecurringDates(dueDate, recurringEnd, recurringType);
		
		for(String dates: recurringDates){
			newTaskIndex = indexStore.getNewId();
			newTask = new Task(newTaskIndex, newSeriesIndex, dates, dueDate);
			tasksList.add(newTask);
			indexStore.addTask(newTaskIndex, newTask);
		}

		sortTasks();
		exportToFile();
		
		String cmd = String.format(CMD_ADD_RECURR_TASK, name);
		return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
	}
	
	public Result addBackRecurrTask(ArrayList<CalendarObject> tasks) {
		String name = new String();
		
		for (int i = 0; i < tasks.size(); i++) {
			Task newTask = (Task) tasks.get(i);
			int index = newTask.getIndex();
			tasksList.add(newTask);
			indexStore.addTask(index, newTask);
			name = newTask.getName();
		}

		sortTasks();
		exportToFile();
		
		String cmd = String.format(CMD_ADD_RECURR_TASK, name);
		return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
	}

	public Result addFloatingTask(String name) {
		int newTaskIndex = indexStore.getNewId();
		int newSeriesIndex = indexStore.getNewSeriesId();
		FloatingTask newFloatingTask = new FloatingTask(newTaskIndex, newSeriesIndex, name);
		indexStore.addTask(newTaskIndex, newFloatingTask);
		floatingTasksList.add(newFloatingTask);
		exportToFile();

		//savePrevCmd(newTaskIndex, null, null, newFloatingTask, CMD_ADD);

		String cmd = String.format(CMD_ADD_FLOATING, name);
		return new Result(cmd, true, putInHashMap(KEY_FLOATING, floatingTasksList));
	}
	

	public Result addBackFloating(FloatingTask newTask) {
		floatingTasksList.add(newTask);
		indexStore.addTask(newTask.getIndex(), newTask);

		String name = newTask.getName();
		String cmd = String.format(CMD_ADD_FLOATING, name);

		return new Result(cmd, true, putInHashMap(KEY_FLOATING, floatingTasksList));
	}

	/***** REMOVE COMMAND EXECUTION ******/

	public Result removeEvent(int idx, boolean isSeries) {
		ArrayList<CalendarObject> eventsToRemove = new ArrayList<CalendarObject>();
		String eventName = new String();
		int seriesIndex = -1;
		
		for (int i = 0; i < eventsList.size(); i++) {
			Event currEvent = (Event)eventsList.get(i);
			if (currEvent.getIndex() == idx) {
				eventsToRemove.add(currEvent);
				//savePrevCmd(idx, eventsList.get(i), null, null, CMD_REMOVE);
				seriesIndex = currEvent.getSeriesIndex();
				eventName = currEvent.getName();
				indexStore.removeEvent(currEvent.getIndex());
				eventsList.remove(i);
				break;
			}
		}
		
		System.out.println("isSeries = " + isSeries); // debug
		
		if(isSeries){
			for(int i = 0; i < eventsList.size(); i++){
				Event currEvent = (Event)eventsList.get(i);
				if(currEvent.getSeriesIndex() == seriesIndex){
					eventsToRemove.add(currEvent);
					indexStore.removeEvent(currEvent.getIndex());
					eventsList.remove(i);
				}
			}
		}
		exportToFile();
		
		Command newUndo;
		
		if (isSeries) {
			newUndo = (Command) new UndoRemove(eventsToRemove, true);
		} else {
			Event event = (Event) eventsToRemove.get(0);
			newUndo = (Command) new UndoRemove(event);
		}
		
		history.add(newUndo);

		String cmd = String.format(CMD_REMOVE_EVENT, eventName);
		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}

	public Result removeTask(int idx, boolean removeSeries) {
		String taskName = new String();
		int seriesIndex = -1;
		
		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = (Task)tasksList.get(i);
			if (currTask.getIndex() == idx) {
				//savePrevCmd(idx, null, tasksList.get(i), null, CMD_REMOVE);
				seriesIndex = currTask.getSeriesIndex();
				taskName = currTask.getName();
				indexStore.removeTask(currTask.getIndex());
				tasksList.remove(i);
				break;
			}
		}
		
		if(removeSeries){
			for(int i = 0; i < tasksList.size(); i++){
				Task currTask = (Task)tasksList.get(i);
				if(currTask.getSeriesIndex() == seriesIndex){
					indexStore.removeEvent(currTask.getIndex());
					tasksList.remove(i);
				}
			}
		}
		
		exportToFile();

		String cmd = String.format(CMD_REMOVE_TASK, taskName);
		return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
	}

	public Result removeFloatingTask(int idx, boolean removeSeries) {
		String taskName = new String();
		for (int i = 0; i < floatingTasksList.size(); i++) {
			FloatingTask currFloating = (FloatingTask)floatingTasksList.get(i);
			if (currFloating.getIndex() == idx) {
				//savePrevCmd(idx, null, null, floatingTasksList.get(i), CMD_REMOVE);
				taskName = currFloating.getName();
				indexStore.removeTask(currFloating.getIndex());
				floatingTasksList.remove(i);
				break;
			}
		}
		exportToFile();

		String cmd = String.format(CMD_REMOVE_FLOATING, taskName);
		return new Result(cmd, true, putInHashMap(KEY_FLOATING, tasksList));
	}

	/***** UPDATE COMMAND EXECUTION ******/

	public Result updateEvent(int idx, ArrayList<String> fields, ArrayList<String> newValues) {
		int arrayListIndex = getArrayListIndexOfEvent(idx);
		Event eventToUpdate = (Event) eventsList.get(arrayListIndex);
		Event originalEvent = copyEvent(eventToUpdate);

		//savePrevCmd(idx, originalEvent, null, null, CMD_UPDATE);

		for (int i = 0; i < fields.size(); i++) {
			eventToUpdate.update(fields.get(i), newValues.get(i));
		}

		exportToFile();

		String name = eventToUpdate.getName();
		String cmd = String.format(CMD_UPDATE_EVENT, name);

		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}

	private Event copyEvent(Event event) {
		int idx = event.getIndex();
		int seriesId = event.getSeriesIndex();
		String eventName = event.getName();
		String startDateTime = event.getStartDateTimeSimplified();
		String endDateTime = event.getEndDateTimeSimplified();
		return new Event(idx, seriesId, eventName, startDateTime, endDateTime);
	}

	public Result updateTask(int idx, ArrayList<String> fields, ArrayList<String> newValues) {
		int arrayListIndex = getArrayListIndexOfTask(idx);
		Task taskToUpdate = (Task) tasksList.get(arrayListIndex);
		Task originalTask = copyTask(taskToUpdate);

		//savePrevCmd(idx, null, originalTask, null, CMD_UPDATE);

		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}
		exportToFile();

		String name = taskToUpdate.getName();
		String cmd = String.format(CMD_UPDATE_TASK, name);

		return new Result(cmd, true, putInHashMap(KEY_TASKS, eventsList));
	}

	private Task copyTask(Task task) {
		int idx = task.getIndex();
		int seriesIdx = task.getSeriesIndex();
		String taskName = task.getName();
		String taskDoneStatus = String.valueOf(task.isDone());
		String dueDate = task.getDueDateSimplified();
		return new Task(idx, seriesIdx, taskName, taskDoneStatus, dueDate);
	}

	public Result updateFloatingTask(int idx, ArrayList<String> fields, ArrayList<String> newValues) {

		int arrayListIndex = getArrayListIndexOfFloatingTask(idx);
		FloatingTask taskToUpdate = (FloatingTask) floatingTasksList.get(arrayListIndex);
		FloatingTask originalTask = copyFloatingTask(taskToUpdate);

		//savePrevCmd(idx, null, null, originalTask, CMD_UPDATE);

		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}
		exportToFile();

		String name = taskToUpdate.getName();
		String cmd = String.format(CMD_UPDATE_FLOATING, name);

		return new Result(cmd, true, putInHashMap(KEY_FLOATING, eventsList));
	}

	private FloatingTask copyFloatingTask(FloatingTask task) {
		int idx = task.getIndex();
		int seriesIdx = task.getSeriesIndex();
		String taskName = task.getName();
		String taskDoneStatus = String.valueOf(task.isDone());
		return new FloatingTask(idx, seriesIdx, taskName, taskDoneStatus);
	}

	/***** DONE COMMAND EXECUTION ******/

	public Result markTaskAsDone(int idx) {
		if (isFloatingTask(idx)) {
			return markFloatingTaskAsDone(idx);
		}

		int arrayListIndex = getArrayListIndexOfTask(idx);
		Task taskToMark = (Task) tasksList.get(arrayListIndex);
		Task originalTask = taskToMark;

		String taskName = taskToMark.getName();

		if (taskToMark.isDone()) {
			//disableUndo();
			return new Result(CMD_DONE_TASK, false, null);
		} else {
			//savePrevCmd(taskToMark.getIndex(), null, originalTask, null, CMD_DONE);
			taskToMark.markAsDone();
			exportToFile();
			String cmd = String.format(CMD_DONE_TASK, taskName);
			return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
		}
	}

	public Result markFloatingTaskAsDone(int idx) {
		int arrayListIndex = getArrayListIndexOfFloatingTask(idx);
		FloatingTask taskToMark = (FloatingTask) floatingTasksList.get(arrayListIndex);
		FloatingTask originalTask = taskToMark;

		String taskName = taskToMark.getName();

		if (taskToMark.isDone()) {
			//disableUndo();
			return new Result(CMD_DONE_FLOATING, false, null);
		} else {
			//savePrevCmd(taskToMark.getIndex(), null, null, originalTask, CMD_DONE);
			taskToMark.markAsDone();
			exportToFile();
			String cmd = String.format(CMD_DONE_FLOATING, taskName);
			return new Result(cmd, true, putInHashMap(KEY_FLOATING, floatingTasksList));
		}
	}
	
	public Result markTaskAsUndone(int idx) {
		if (isFloatingTask(idx)) {
			return markFloatingTaskAsDone(idx);
		}

		int arrayListIndex = getArrayListIndexOfTask(idx);
		Task taskToMark = (Task) tasksList.get(arrayListIndex);
		Task originalTask = taskToMark;

		String taskName = taskToMark.getName();

		if (!taskToMark.isDone()) {
			//disableUndo();
			return new Result(CMD_DONE_TASK, false, null);
		} else {
			//savePrevCmd(taskToMark.getIndex(), null, originalTask, null, CMD_DONE);
			taskToMark.markAsUndone();
			exportToFile();
			String cmd = String.format(CMD_DONE_TASK, taskName);
			return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
		}
	}

	public Result markFloatingTaskAsUndone(int idx) {
		int arrayListIndex = getArrayListIndexOfFloatingTask(idx);
		FloatingTask taskToMark = (FloatingTask) floatingTasksList.get(arrayListIndex);
		FloatingTask originalTask = taskToMark;

		String taskName = taskToMark.getName();

		if (!taskToMark.isDone()) {
			//disableUndo();
			return new Result(CMD_DONE_FLOATING, false, null);
		} else {
			//savePrevCmd(taskToMark.getIndex(), null, null, originalTask, CMD_DONE);
			taskToMark.markAsUndone();
			exportToFile();
			String cmd = String.format(CMD_DONE_FLOATING, taskName);
			return new Result(cmd, true, putInHashMap(KEY_FLOATING, floatingTasksList));
		}
	}

	/***** UNDO COMMAND EXECUTION ******/

	public Result undo() {
		Result result = history.pop().execute();
		exportToFile();
		return result;
	}
	
	/*
	public Result undo() {
		Result result = executeUndo();
		disableUndo();
		exportToFile();

		return result;
	}
	*/

	/*
	private Result executeUndo() {
		switch (prevCommand) {
			case CMD_ADD :
				return undoAdd();
			case CMD_REMOVE :
				return undoRemove();
			case CMD_UPDATE :
				return undoUpdate();
			case CMD_DONE :
				return undoMarkTaskAsDone();
			default :
				return handleInvalidUndo();
		}
	}

	private void disableUndo() {
		prevModIndex = INDEX_INVALID;
		prevCommand = CMD_INVALID_UNDO;
		prevModEvent = null;
		prevModTask = null;
		prevModFloatingTask = null;
	}

	private void savePrevCmd(int index, Event event, Task task, FloatingTask floatingTask, String command) {
		prevModIndex = index;
		prevModEvent = event;
		prevModTask = task;
		prevModFloatingTask = floatingTask;
		prevCommand = command;
	}

	private Result undoAdd() {
		if (isEvent(prevModIndex)) {
			return removeEvent(prevModIndex);
		} else if (isFloatingTask(prevModIndex)) {
			return removeFloatingTask(prevModIndex);
		} else {
			return removeTask(prevModIndex);
		}
	}

	private Result undoRemove() {
		indexStore.removeRecycledId(prevModIndex);
		if (prevModEvent != null) {
			return addEvent(prevModEvent);
		} else if (prevModFloatingTask != null) {
			return addFloatingTask(prevModFloatingTask);
		} else {
			return addTask(prevModTask);
		}
	}

	private Result undoUpdate() {
		if (isEvent(prevModIndex)) {
			return undoUpdateEvent();
		} else if (isFloatingTask(prevModIndex)) {
			return undoUpdateFloatingTask();
		} else {
			return undoUpdateTask();
		}
	}

	private Result undoUpdateEvent() {
		String name = new String();
		
		for (int i = 0; i < eventsList.size(); i++) {
			Event currEvent = (Event)eventsList.get(i);
			if (currEvent.getIndex() == prevModIndex) {
				eventsList.remove(i);
				eventsList.add(i, prevModEvent);
				name = currEvent.getName();
				sortEvents();
				indexStore.replaceEvent(prevModIndex, prevModEvent);
				break;
			}
		}
		
		String cmd = CMD_UNDO + String.format(CMD_UPDATE_EVENT, name);
		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}

	private Result undoUpdateFloatingTask() {
		String name = new String();
		
		for (int i = 0; i < floatingTasksList.size(); i++) {
			FloatingTask currFloatingTask = (FloatingTask) floatingTasksList.get(i);
			if (currFloatingTask.getIndex() == prevModIndex) {
				floatingTasksList.remove(i);
				floatingTasksList.add(i, prevModFloatingTask);
				name =currFloatingTask.getName();
				indexStore.replaceTask(prevModIndex, prevModFloatingTask);
				break;
			}
		}
		
		String cmd = CMD_UNDO + String.format(CMD_UPDATE_FLOATING, name);
		return new Result(cmd, true, putInHashMap(KEY_FLOATING, floatingTasksList));
	}

	private Result undoUpdateTask() {
		String name = new String();
		
		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = (Task)tasksList.get(i);
			if (currTask.getIndex() == prevModIndex) {
				tasksList.remove(i);
				tasksList.add(i, prevModTask);
				name = currTask.getName();
				sortTasks();
				indexStore.replaceTask(prevModIndex, prevModTask);
				break;
			}
		}
		
		String cmd = CMD_UNDO + String.format(CMD_UPDATE_TASK, name);
		return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
	}

	private Result undoMarkTaskAsDone() {
		if (prevModTask != null) {
			int arrayListIndex = getArrayListIndexOfTask(prevModIndex);
			Task taskToUnmark = (Task) tasksList.get(arrayListIndex);
			if (taskToUnmark.isDone()) {
				taskToUnmark.markAsUndone();
			}
			String name = taskToUnmark.getName();
			String cmd = CMD_UNDO + String.format(CMD_DONE_TASK, name);
			return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
		} else {
			int arrayListIndex = getArrayListIndexOfFloatingTask(prevModIndex);
			FloatingTask taskToUnmark = (FloatingTask) floatingTasksList.get(arrayListIndex);
			taskToUnmark.markAsUndone();
			String name = taskToUnmark.getName();
			String cmd = CMD_UNDO + String.format(CMD_DONE_FLOATING, name);
			return new Result(cmd, true, putInHashMap(KEY_FLOATING, floatingTasksList));

		}
	}

	private Result handleInvalidUndo() {
		return new Result(CMD_UNDO, false, null);
	}
	
	*/

	/***** SEARCH COMMAND EXECUTION ******/

	public Result search(String arguments) {
		String command = String.format(CMD_SEARCH, arguments);
		
		ArrayList<CalendarObject> eventsFound = new ArrayList<CalendarObject>();
		ArrayList<CalendarObject> tasksFound = new ArrayList<CalendarObject>();
		ArrayList<CalendarObject> floatingTasksFound = new ArrayList<CalendarObject>();
		
		String regex = generateRegex(arguments);
		
		for(CalendarObject event: eventsList){
			if(event.toString().matches(regex)){
				eventsFound.add(event);
			}
		}
		
		for(CalendarObject task: tasksList){
			if(tasksFound.toString().matches(regex)){
				tasksFound.add(task);
			}
		}
		
		for(CalendarObject floatingTask: floatingTasksList){
			if(floatingTask.toString().matches(regex)){
				floatingTasksFound.add(floatingTask);
			}
		}
		
		HashMap<String, ArrayList<CalendarObject>> results = new HashMap<String, ArrayList<CalendarObject>>();
		
		results.put(KEY_EVENTS, eventsFound);
		results.put(KEY_TASKS, tasksFound);
		results.put(KEY_FLOATING, floatingTasksFound);
		
		return new Result(command, true, results);
	}
	
	private String generateRegex(String args){
		String[] splittedArgs = args.split("\\s+");
		
		String regex = "(?i:.*";
		
		for(String s: splittedArgs){
			regex += s + ".*";
		}
		
		regex += ")";
		return regex;
	}

//	private ArrayList<String> toDisplayFTasks(ArrayList<String> wordFoundLines, FloatingTask floatingTask, int num) {
//
//		wordFoundLines.add(num + ") " + floatingTask.getName() + "\t[ID:" + floatingTask.getIndex() + "] ");
//		return wordFoundLines;
//	}
//
//	private ArrayList<String> toDisplayTasks(ArrayList<String> wordFoundLines, Task task, int num) {
//		wordFoundLines.add("Tasks");
//		wordFoundLines
//				.add(num + ") " + task.getName() + " Due: " + task.getDueDate() + "\t[ID:" + task.getIndex() + "] ");
//
//		return wordFoundLines;
//	}
//
//	private ArrayList<String> toDisplayEvent(ArrayList<String> wordFoundLines, Event event, int num) {
//		wordFoundLines.add("Events");
//		wordFoundLines.add(num + ") " + event.getName() + " From: " + event.getStartDateTime() + " To: "
//				+ event.getEndDateTime() + "\t[ID:" + event.getIndex() + "] ");
//		return wordFoundLines;
//	}
//
//	private boolean containsWord(String content, String keyword) {
//		String[] splited = content.split("\\W");
//		for (int i = 0; i < splited.length; i++) {
//			if (splited[i].equalsIgnoreCase(keyword)) {
//				return true;
//			}
//		}
//		return false;
//	}

	public ArrayList<CalendarObject> getEventsList() {
		return eventsList;
	}

	public ArrayList<CalendarObject> getTasksList() {
		return tasksList;
	}

	public ArrayList<CalendarObject> getFloatingTasksList() {
		return floatingTasksList;
	}

	public void exportToFile() {
		// System.out.println("Exporting: " + _fileName);
		exporter.setFileName(_fileName);
		exporter.export();
		// System.out.println("Export Successful!");
	}

	public void importFromFile() {
		System.out.println("Importing: " + _fileName);
		if (importer.importFromFile(_fileName)) {
			eventsList = importer.getEventsList();
			tasksList = importer.getTasksList();
			floatingTasksList = importer.getFloatingTasksList();
			
			sortEvents();
			sortTasks();
			
			System.out.println("Import Sucessful!");
		} else {
			System.out.println("Import failed!");
		}
	}

	private int getArrayListIndexOfEvent(int id) {
		int index = 0;

		for (int i = 0; i < eventsList.size(); i++) {
			Event currEvent = (Event)eventsList.get(i);
			if (currEvent.getIndex() == id) {
				i = index;
			}
		}

		return index;
	}

	private int getArrayListIndexOfTask(int id) {
		int index = 0;

		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = (Task)tasksList.get(i);
			if (currTask.getIndex() == id) {
				// i = index;
				index = i;
			}
		}

		return index;
	}

	private int getArrayListIndexOfFloatingTask(int id) {
		int index = 0;

		for (int i = 0; i < floatingTasksList.size(); i++) {
			FloatingTask currFloatingTask = (FloatingTask)floatingTasksList.get(i);
			if (currFloatingTask.getIndex() == id) {
				// i = index;
				index = i;
			}
		}

		return index;
	}

	private boolean isEvent(int id) {
		return indexStore.isEvent(id);
	}

	private boolean isFloatingTask(int id) {
		return indexStore.isFloatingTask(id);
	}

	private HashMap<String, ArrayList<CalendarObject>> putInHashMap(String key, ArrayList<CalendarObject> value) {
		HashMap<String, ArrayList<CalendarObject>> map;
		map = new HashMap<String, ArrayList<CalendarObject>>();
		map.put(key, value);
		return map;
	}

	/******
	 * HELPER METHODS FOR RECURRING EVENTS NOTE: THE ARRAYLIST THAT IS RETURNED DOES NOT RETURN THE ORIGINAL START DATE FOR EXAMPLE: IF THE START DATE IS ON 12/03/15, THE FIRST DATE TO BE ADDED WILL
	 * BE ON 13/03/15
	 ******/

	private ArrayList<String> processRecurringDates(String start, String recurringEnd, String recurringType) {
		switch (recurringType.toLowerCase()) {
			case KEY_DAILY :
				return getDailyRecurringDates(start, recurringEnd);

			case KEY_WEEKLY :
				return getWeeklyRecurringDates(start, recurringEnd);

			case KEY_MONTHLY :
				return getMonthlyRecurringDates(start, recurringEnd);

			case KEY_ANNUALLY :
				return getAnnualRecurringDates(start, recurringEnd);

			default :
				return null;
		}
	}

	private ArrayList<String> getDailyRecurringDates(String start, String end) {
		Date startDate = null;
		Date endDate = null;

		ArrayList<String> returnArray = new ArrayList<String>();

		try {
			startDate = parseDate(start);
			endDate = parseDate(end);
		} catch (Exception e) {
			return null;
		}

		long startMilli = dateToMilli(startDate);
		long endMilli = dateToMilli(endDate);

		while ((endMilli - startMilli) > 0) {
			startMilli += MILLISECONDS_A_DAY;
			String currDate = formatDateMilli(startMilli);
			returnArray.add(currDate);
		}

		return returnArray;
	}

	private ArrayList<String> getWeeklyRecurringDates(String start, String end) {
		Date startDate = null;
		Date endDate = null;

		ArrayList<String> returnArray = new ArrayList<String>();

		try {
			startDate = parseDate(start);
			endDate = parseDate(end);
		} catch (Exception e) {
			return null;
		}

		long startMilli = dateToMilli(startDate);
		long endMilli = dateToMilli(endDate);

		while ((endMilli - startMilli) > MILLISECONDS_A_WEEK) {
			startMilli += MILLISECONDS_A_WEEK;
			String currDate = formatDateMilli(startMilli);
			returnArray.add(currDate);
		}
		return returnArray;
	}

	private ArrayList<String> getMonthlyRecurringDates(String start, String end) {
		ArrayList<String> returnArray = new ArrayList<String>();

		String[] splitStart = start.split(DATE_DELIMETER);
		String startDay = splitStart[0];
		int startMonth = Integer.parseInt(splitStart[1]);
		int startYear = Integer.parseInt(splitStart[2]);
		String startTime = splitStart[3];

		Date startDate = null;
		Date endDate = null;

		try {
			startDate = parseDate(start);
			endDate = parseDate(end);
		} catch (Exception e) {
			return null;
		}

		long startMilli = dateToMilli(startDate);
		long endMilli = dateToMilli(endDate);

		while ((endMilli - startMilli) > 0) {
			startMonth++;

			if (startMonth == 13) {
				startMonth = 1;
				startYear++;
			}

			String currDate = formatCurrDateString(startDay, String.valueOf(startMonth), String.valueOf(startYear),
					startTime);

			try {
				startMilli = dateToMilli(parseDate(currDate));
			} catch (ParseException e) {
				continue;
			}

			if (isValidDate(startMilli, endMilli)) {
				returnArray.add(formatDateMilli(startMilli));
			}
		}
		return returnArray;
	}

	private ArrayList<String> getAnnualRecurringDates(String start, String end) {
		ArrayList<String> returnArray = new ArrayList<String>();

		String[] splitStart = start.split(DATE_DELIMETER);
		String startDay = splitStart[0];
		String startMonth = splitStart[1];
		int startYear = Integer.parseInt(splitStart[2]);
		String startTime = splitStart[3];

		Date startDate = null;
		Date endDate = null;

		try {
			startDate = parseDate(start);
			endDate = parseDate(end);
		} catch (Exception e) {
			return null;
		}

		long startMilli = dateToMilli(startDate);
		long endMilli = endDate.getTime();

		while ((endMilli - startMilli) > 0) {
			startYear++;

			String currDate = formatCurrDateString(startDay, startMonth, String.valueOf(startYear), startTime);

			try {
				startMilli = dateToMilli(parseDate(currDate));
			} catch (ParseException e) {
				continue;
			}

			if (isValidDate(startMilli, endMilli)) {
				returnArray.add(formatDateMilli(startMilli));
			}
		}
		return returnArray;
	}

	/****** HELPER METHODS FOR RECURRING EVENTS ******/
	private Date parseDate(String dateString) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		return df.parse(dateString);
	}

	private String formatDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		return df.format(date);
	}

	private String formatDateMilli(long date) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		return df.format(date);
	}

	private long dateToMilli(Date date) {
		return date.getTime();
	}

	private String formatCurrDateString(String startDay, String startMonth, String startYear, String startTime) {
		return startDay + DATE_DELIMETER + startMonth + DATE_DELIMETER + startYear + DATE_DELIMETER + startTime;
	}

	private boolean isValidDate(long currDate, long endDate) {
		if (currDate > endDate) {
			return false;
		} else {
			return true;
		}
	}
	
	public void sortEvents(){
		ArrayList<Event> events = new ArrayList<Event>();
		
		for(CalendarObject o: eventsList){
			Event currEvent = (Event)o;
			events.add(currEvent);
		}
		
		Collections.sort(events);
		eventsList.clear();
		
		for(Event e: events){
			eventsList.add(e);
		}
	}
	
	public void sortTasks(){
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		for(CalendarObject o: tasksList){
			Task currTask = (Task)o;
			tasks.add(currTask);
		}
		
		Collections.sort(tasks);
		tasksList.clear();
		
		for(Task t: tasks){
			tasksList.add(t);
		}
	}
}
