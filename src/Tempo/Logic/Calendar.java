package Tempo.Logic;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import Tempo.CalendarObjects.*;
import Tempo.Commands.*;
import Tempo.Storage.*;

public class Calendar {

	private static Calendar instance = new Calendar();
	private static IndexStore indexStore;
	private static CalendarImporter importer;
	private static CalendarExporter exporter;

	private static Stack<Command> undoHistory;
	private static Stack<Command> redoHistory;
	private static Stack<Command> cmdHistory;

	private static final String MSG_WARNING_CLASH = "Warning: this event clashes with another event.\nEnter 'undo' if you would like to revoke the previous operation.";

	private static final String CMD_ADD_EVENT = "add event %1$s";
	private static final String CMD_ADD_RECURR_EVENT = "add recurring event %1$s";
	private static final String CMD_ADD_TASK = "add task %1$s";
	private static final String CMD_ADD_RECURR_TASK = "add recurring task %1$s";
	private static final String CMD_ADD_FLOATING = "add floating task %1$s";

	private static final String CMD_REMOVE_EVENT = "remove event %1$s";
	private static final String CMD_REMOVE_TASK = "remove task %1$s";
	private static final String CMD_REMOVE_FLOATING = "remove floating task %1$s";

	private static final String CMD_UPDATE_EVENT = "update event %1$s";
	private static final String CMD_UPDATE_TASK = "update task %1$s";
	private static final String CMD_UPDATE_FLOATING = "update floating task %1$s";

	private static final String CMD_DONE_TASK = "done task %1$s";
	private static final String CMD_DONE_FLOATING = "done floating task %1$s";

	private static final String CMD_UNDONE_TASK = "undone task %1$s";
	private static final String CMD_UNDONE_FLOATING = "undone floating task %1$s";

	private static final String CMD_UNDO = "undo";
	private static final String CMD_REDO = "redo";

	private static final String CMD_SEARCH = "search %1$s";

	private static final String KEY_EVENTS = "events";
	private static final String KEY_TASKS = "tasks";
	private static final String KEY_FLOATING = "floating tasks";
	private static final String KEY_DAILY = "daily";
	private static final String KEY_WEEKLY = "weekly";
	private static final String KEY_MONTHLY = "monthly";
	private static final String KEY_ANNUALLY = "annually";

	private static final String FIELD_START_DATE = "start date";
	private static final String FIELD_START_TIME = "start time";
	private static final String FIELD_END_DATE = "end date";
	private static final String FIELD_END_TIME = "end time";

	public static final long MILLISECONDS_A_DAY = 86400000;
	public static final long MILLISECONDS_A_WEEK = 604800000;
	public static final String DATE_DELIMETER = "/";

	private static final int INDEX_INVALID = -1;

	private String _fileName;

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
		undoHistory = new Stack<Command>();
		redoHistory = new Stack<Command>();
		cmdHistory = new Stack<Command>();
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
	
	public boolean setFilename(String fileName){
		_fileName = fileName;
		if(exporter.setFileName(_fileName)){
			exporter.export();
			return true;
		}else{
			return false;
		}
	}
	
	public String getFilename(){
		return _fileName;
	}

	/***** ADD COMMAND EXECUTION ******/

	public Result addEvent(String name, String start, String end) {
		boolean hasClash = false;
		int newEventIndex = indexStore.getNewId();
		int newSeriesIndex = indexStore.getNewSeriesId();
		Event newEvent = new Event(newEventIndex, newSeriesIndex, name, start, end);

		if (hasClash(newEvent)) {
			hasClash = true;
		}

		eventsList.add(newEvent);

		indexStore.addEvent(newEventIndex, newEvent);
		sortEvents();
		exportToFile();

		Command newUndo = (Command) new UndoAdd(newEventIndex, true, false, false);
		undoHistory.add(newUndo);

		String cmd = String.format(CMD_ADD_EVENT, name);

		if (hasClash) {
			return new Result(cmd, MSG_WARNING_CLASH, true, putInHashMap(KEY_EVENTS, eventsList));
		}
		
		clearRedoHistory();

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

		String split[] = start.split(DATE_DELIMETER);
		String timeString = split[split.length - 1];

		long startEndDiff = getStartEndDiff(start, end);

		// Gets the list of recurring dates;
		ArrayList<String> recurringDates = processRecurringDates(start, recurringEnd, recurringType);

		for (String dates : recurringDates) {
			newEventIndex = indexStore.getNewId();

			String newStart = dates + DATE_DELIMETER + timeString;
			String newEnd = null;

			if (startEndDiff != -1) {
				newEnd = getNewEndString(newStart, startEndDiff);
			}

			newEvent = new Event(newEventIndex, newSeriesIndex, name, newStart, newEnd);
			eventsList.add(newEvent);
			indexStore.addEvent(newEventIndex, newEvent);
		}

		sortEvents();
		exportToFile();
		
		Command newUndo = (Command) new UndoAdd(newEventIndex, true, false, true);
		undoHistory.add(newUndo);
		
		clearRedoHistory();

		String cmd = String.format(CMD_ADD_RECURR_EVENT, name);
		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}

	private long getStartEndDiff(String start, String end) {
		if (end != null) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
				Date startDate = df.parse(start);
				Date endDate = df.parse(end);

				long startDateMilli = startDate.getTime();
				long endDateMilli = endDate.getTime();

				long startEndDiff = endDateMilli - startDateMilli;
				return startEndDiff;
			} catch (Exception e) {
				return -1;
			}
		}
		return -1;
	}

	private String getNewEndString(String newStart, long startEndDiff) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
		try {
			long newStartDateLong = df.parse(newStart).getTime();
			long newEndDateLong = newStartDateLong + startEndDiff;
			String newEnd = df.format(newEndDateLong);
			return newEnd;
		} catch (Exception e) {
			return null;
		}
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

		Command newUndo = (Command) new UndoAdd(newTaskIndex, false, true, false);
		undoHistory.add(newUndo);
		
		clearRedoHistory();

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

	public Result addRecurringTask(String name, String dueDate, String recurringType, String recurringEnd) {
		int newTaskIndex = indexStore.getNewId();
		int newSeriesIndex = indexStore.getNewSeriesId();
		Task newTask = new Task(newTaskIndex, newSeriesIndex, name, dueDate);
		tasksList.add(newTask);
		indexStore.addTask(newTaskIndex, newTask);

		// Gets the list of recurring dates;
		ArrayList<String> recurringDates = processRecurringDates(dueDate, recurringEnd, recurringType);

		for (String dates : recurringDates) {
			newTaskIndex = indexStore.getNewId();
			newTask = new Task(newTaskIndex, newSeriesIndex, name, dates);
			tasksList.add(newTask);
			indexStore.addTask(newTaskIndex, newTask);
		}

		sortTasks();
		exportToFile();
		
		Command newUndo = (Command) new UndoAdd(newTaskIndex, false, true, true);
		undoHistory.add(newUndo);
		
		clearRedoHistory();

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

		Command newUndo = (Command) new UndoAdd(newTaskIndex, false, false, false);
		undoHistory.add(newUndo);
		
		clearRedoHistory();

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
			Event currEvent = (Event) eventsList.get(i);
			if (currEvent.getIndex() == idx) {
				eventsToRemove.add(currEvent);
				seriesIndex = currEvent.getSeriesIndex();
				eventName = currEvent.getName();
				indexStore.removeEvent(currEvent.getIndex());
				eventsList.remove(i);
				break;
			}
		}
		
		if (isSeries) {
			for (int i = 0; i < eventsList.size(); i++) {
				Event currEvent = (Event) eventsList.get(i);
				if (currEvent.getSeriesIndex() == seriesIndex) {
					eventsToRemove.add(currEvent);
					indexStore.removeEvent(currEvent.getIndex());
					eventsList.remove(i);
					i--;
					continue;
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

		undoHistory.add(newUndo);
		
		clearRedoHistory();

		String cmd = String.format(CMD_REMOVE_EVENT, eventName);
		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}

	public Result removeTask(int idx, boolean isSeries) {
		ArrayList<CalendarObject> tasksToRemove = new ArrayList<CalendarObject>();
		String taskName = new String();
		int seriesIndex = -1;

		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = (Task) tasksList.get(i);
			if (currTask.getIndex() == idx) {
				tasksToRemove.add(currTask);
				seriesIndex = currTask.getSeriesIndex();
				taskName = currTask.getName();
				indexStore.removeTask(currTask.getIndex());
				tasksList.remove(i);
				break;
			}
		}

		if (isSeries) {
			for (int i = 0; i < tasksList.size(); i++) {
				Task currTask = (Task) tasksList.get(i);
				if (currTask.getSeriesIndex() == seriesIndex) {
					tasksToRemove.add(currTask);
					indexStore.removeEvent(currTask.getIndex());
					tasksList.remove(i);
				}
			}
		}

		exportToFile();

		Command newUndo;

		if (isSeries) {
			newUndo = (Command) new UndoRemove(tasksToRemove, true);
		} else {
			Task task = (Task) tasksToRemove.get(0);
			newUndo = (Command) new UndoRemove(task);
		}

		undoHistory.add(newUndo);
		
		clearRedoHistory();

		String cmd = String.format(CMD_REMOVE_TASK, taskName);
		return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
	}

	public Result removeFloatingTask(int idx, boolean isSeries) {
		String taskName = new String();
		FloatingTask taskToRemove = null;

		for (int i = 0; i < floatingTasksList.size(); i++) {
			FloatingTask currFloating = (FloatingTask) floatingTasksList.get(i);
			if (currFloating.getIndex() == idx) {
				taskToRemove = currFloating;
				taskName = currFloating.getName();
				indexStore.removeTask(currFloating.getIndex());
				floatingTasksList.remove(i);
				break;
			}
		}
		exportToFile();

		Command newUndo = (Command) new UndoRemove(taskToRemove);
		undoHistory.add(newUndo);
		
		clearRedoHistory();

		String cmd = String.format(CMD_REMOVE_FLOATING, taskName);
		return new Result(cmd, true, putInHashMap(KEY_FLOATING, tasksList));
	}

	/***** UPDATE COMMAND EXECUTION ******/

	public Result updateEvent(int idx, ArrayList<String> fields, ArrayList<String> newValues, boolean isSeries) {
		boolean hasClash = false;
		ArrayList<CalendarObject> eventsToUpdate = new ArrayList<CalendarObject>();

		int arrayListIndex = getArrayListIndexOfEvent(idx);
		Event eventToUpdate = (Event) eventsList.get(arrayListIndex);
		int seriesIndex = eventToUpdate.getSeriesIndex();
		Event oldEvent = copyEvent(eventToUpdate);

		for (int i = 0; i < fields.size(); i++) {
			eventToUpdate.update(fields.get(i), newValues.get(i));
			if (!hasClash && hasChangedTime(fields.get(i)) && hasClash(eventToUpdate)) {
				hasClash = true;
			}
		}

		if (isSeries) {
			eventsToUpdate.add(oldEvent);

			for (int i = 0; i < eventsList.size(); i++) {
				Event currEvent = (Event) eventsList.get(i);
				if (currEvent.getSeriesIndex() == seriesIndex) {
					eventsToUpdate.add(copyEvent(currEvent));
					for (int j = 0; j < fields.size(); j++) {
						currEvent.update(fields.get(j), newValues.get(j));
						if (!hasClash && hasChangedTime(fields.get(i)) && hasClash(currEvent)) {
							hasClash = true;
						}
					}

				}
			}
		}

		exportToFile();

		Command newUndo;

		if (isSeries) {
			newUndo = (Command) new UndoUpdate(eventsToUpdate, true);
		} else {
			newUndo = (Command) new UndoUpdate(oldEvent);
		}

		undoHistory.add(newUndo);
		
		clearRedoHistory();

		String name = eventToUpdate.getName();
		String cmd = String.format(CMD_UPDATE_EVENT, name);

		if (hasClash) {
			return new Result(cmd, MSG_WARNING_CLASH, true, putInHashMap(KEY_EVENTS, eventsList));
		}

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

	public Result updateTask(int idx, ArrayList<String> fields, ArrayList<String> newValues, boolean isSeries) {
		ArrayList<CalendarObject> tasksToUpdate = new ArrayList<CalendarObject>();

		int arrayListIndex = getArrayListIndexOfTask(idx);
		Task taskToUpdate = (Task) tasksList.get(arrayListIndex);
		int seriesIndex = taskToUpdate.getSeriesIndex();
		Task oldTask = copyTask(taskToUpdate);

		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}

		if (isSeries) {
			tasksToUpdate.add(oldTask);

			for (int i = 0; i < tasksList.size(); i++) {
				Task currTask = (Task) tasksList.get(i);
				if (currTask.getSeriesIndex() == seriesIndex) {
					tasksToUpdate.add(copyTask(currTask));
					for (int j = 0; j < fields.size(); j++) {
						currTask.update(fields.get(j), newValues.get(j));
					}

				}
			}
		}

		exportToFile();

		Command newUndo;

		if (isSeries) {
			newUndo = (Command) new UndoUpdate(tasksToUpdate, false);
		} else {
			newUndo = (Command) new UndoUpdate(oldTask);
		}

		undoHistory.add(newUndo);
		
		clearRedoHistory();

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

	public Result updateFloatingTask(int idx, ArrayList<String> fields, ArrayList<String> newValues, boolean isSeries) {

		int arrayListIndex = getArrayListIndexOfFloatingTask(idx);
		FloatingTask taskToUpdate = (FloatingTask) floatingTasksList.get(arrayListIndex);
		FloatingTask oldTask = copyFloatingTask(taskToUpdate);

		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}
		exportToFile();

		Command newUndo = (Command) new UndoUpdate(oldTask);
		undoHistory.add(newUndo);
		
		clearRedoHistory();

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

		String taskName = taskToMark.getName();

		if (taskToMark.isDone()) {
			return new Result(CMD_DONE_TASK, false, null);
		} else {
			taskToMark.markAsDone();
			Command newUndo = (Command) new UndoDone(idx, false, true);
			undoHistory.add(newUndo);
			clearRedoHistory();
			exportToFile();
			String cmd = String.format(CMD_DONE_TASK, taskName);
			return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
		}
	}

	public Result markFloatingTaskAsDone(int idx) {
		int arrayListIndex = getArrayListIndexOfFloatingTask(idx);
		FloatingTask taskToMark = (FloatingTask) floatingTasksList.get(arrayListIndex);

		String taskName = taskToMark.getName();

		if (taskToMark.isDone()) {
			return new Result(CMD_DONE_FLOATING, false, null);
		} else {
			taskToMark.markAsDone();
			Command newUndo = (Command) new UndoDone(idx, true, true);
			undoHistory.add(newUndo);
			clearRedoHistory();
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

		String taskName = taskToMark.getName();

		if (!taskToMark.isDone()) {
			return new Result(CMD_UNDONE_TASK, false, null);
		} else {
			taskToMark.markAsUndone();
			Command newUndo = (Command) new UndoDone(idx, false, false);
			undoHistory.add(newUndo);
			clearRedoHistory();
			exportToFile();
			String cmd = String.format(CMD_UNDONE_TASK, taskName);
			return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
		}
	}

	public Result markFloatingTaskAsUndone(int idx) {
		int arrayListIndex = getArrayListIndexOfFloatingTask(idx);
		FloatingTask taskToMark = (FloatingTask) floatingTasksList.get(arrayListIndex);

		String taskName = taskToMark.getName();

		if (!taskToMark.isDone()) {
			return new Result(CMD_UNDONE_FLOATING, false, null);
		} else {
			taskToMark.markAsUndone();
			Command newUndo = (Command) new UndoDone(idx, true, false);
			undoHistory.add(newUndo);
			clearRedoHistory();
			exportToFile();
			String cmd = String.format(CMD_UNDONE_FLOATING, taskName);
			return new Result(cmd, true, putInHashMap(KEY_FLOATING, floatingTasksList));
		}
	}

	/***** UNDO COMMAND EXECUTION ******/

	public Result undo() {
		if (undoHistory.isEmpty()) {
			return new Result(CMD_UNDO, false, null);
		}
		Result result = undoHistory.pop().execute();
		exportToFile();
		return result;
	}

	public void removeLastUndo() {
		undoHistory.pop();
	}
	
	/***** REDO COMMAND EXECUTION ******/
	public Result redo() {
		if (redoHistory.isEmpty()) {
			return new Result(CMD_REDO, false, null);
		}
		Result result = redoHistory.pop().execute();
		exportToFile();
		
		redoHistory.add(cmdHistory.pop());
		return result;
	}
	
	public void saveCmd(Command cmd) {
		cmdHistory.add(cmd);
	}
	
	private void clearRedoHistory() {
		redoHistory.clear();
		cmdHistory.clear();
	}
	

	/***** SEARCH COMMAND EXECUTION ******/

	public Result search(String arguments) {
		String command = String.format(CMD_SEARCH, arguments);

		ArrayList<CalendarObject> eventsFound = new ArrayList<CalendarObject>();
		ArrayList<CalendarObject> tasksFound = new ArrayList<CalendarObject>();
		ArrayList<CalendarObject> floatingTasksFound = new ArrayList<CalendarObject>();

		String regex = generateRegex(arguments);

		for (CalendarObject event : eventsList) {
			if (event.toString().matches(regex)) {
				eventsFound.add(event);
			}
		}

		for (CalendarObject task : tasksList) {
			if (tasksFound.toString().matches(regex)) {
				tasksFound.add(task);
			}
		}

		for (CalendarObject floatingTask : floatingTasksList) {
			if (floatingTask.toString().matches(regex)) {
				floatingTasksFound.add(floatingTask);
			}
		}

		HashMap<String, ArrayList<CalendarObject>> results = new HashMap<String, ArrayList<CalendarObject>>();

		results.put(KEY_EVENTS, eventsFound);
		results.put(KEY_TASKS, tasksFound);
		results.put(KEY_FLOATING, floatingTasksFound);

		return new Result(command, true, results);
	}

	private String generateRegex(String args) {
		String[] splittedArgs = args.split("\\s+");

		String regex = "(?i:.*";

		for (String s : splittedArgs) {
			regex += s + ".*";
		}

		regex += ")";
		return regex;
	}
	
	/***** OTHER METHODS ******/

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
			Event currEvent = (Event) eventsList.get(i);
			if (currEvent.getIndex() == id) {
				i = index;
			}
		}

		return index;
	}

	private int getArrayListIndexOfTask(int id) {
		int index = 0;

		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = (Task) tasksList.get(i);
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
			FloatingTask currFloatingTask = (FloatingTask) floatingTasksList.get(i);
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
		long startMilli = -1;
		long endMilli = -1;

		ArrayList<String> returnArray = new ArrayList<String>();

		try {
			startDate = parseDate(start);

			if (end != null) {
				endDate = parseDate(end);
			}
		} catch (Exception e) {
			return null;
		}

		startMilli = dateToMilli(startDate);

		if (endDate != null) {
			endMilli = dateToMilli(endDate);
		}

		if (endDate != null) {
			while ((endMilli - startMilli) > 0) {
				startMilli += MILLISECONDS_A_DAY;
				String currDate = formatDateMilli(startMilli);
				returnArray.add(currDate);
			}
		} else {
			for (int i = 0; i < 20; i++) {
				startMilli += MILLISECONDS_A_DAY;
				String currDate = formatDateMilli(startMilli);
				returnArray.add(currDate);
			}
		}
		return returnArray;
	}

	private ArrayList<String> getWeeklyRecurringDates(String start, String end) {
		Date startDate = null;
		Date endDate = null;
		long startMilli = -1;
		long endMilli = -1;

		ArrayList<String> returnArray = new ArrayList<String>();

		try {
			startDate = parseDate(start);

			if (end != null) {
				endDate = parseDate(end);
			}
		} catch (Exception e) {
			return null;
		}

		startMilli = dateToMilli(startDate);

		if (endDate != null) {
			endMilli = dateToMilli(endDate);
		}

		if (endDate != null) {
			while ((endMilli - startMilli) > MILLISECONDS_A_WEEK) {
				startMilli += MILLISECONDS_A_WEEK;
				String currDate = formatDateMilli(startMilli);
				returnArray.add(currDate);
			}
		} else {
			for (int i = 0; i < 20; i++) {
				startMilli += MILLISECONDS_A_WEEK;
				String currDate = formatDateMilli(startMilli);
				returnArray.add(currDate);
			}
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

		long startMilli = -1;
		long endMilli = -1;

		try {
			startDate = parseDate(start);

			if (end != null) {
				endDate = parseDate(end);
			}
		} catch (Exception e) {
			return null;
		}

		startMilli = dateToMilli(startDate);
		if (endDate != null) {
			endMilli = dateToMilli(endDate);
		}

		if (endDate != null) {
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
		} else {
			for (int i = 0; i < 20; i++) {
				startMonth++;
				if (startMonth == 13) {
					startMonth = 1;
					startYear++;
				}

				String currDate = formatCurrDateString(startDay, String.valueOf(startMonth), String.valueOf(startYear),startTime);

				try {
					startMilli = dateToMilli(parseDate(currDate));
				} catch (ParseException e) {
					continue;
				}
				if (isValidDate(startMilli, endMilli)) {
					returnArray.add(formatDateMilli(startMilli));
				}
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

		long startMilli = -1;
		long endMilli = -1;

		try {
			startDate = parseDate(start);

			if (end != null) {
				endDate = parseDate(end);
			}
		} catch (Exception e) {
			return null;
		}

		startMilli = dateToMilli(startDate);

		if (endDate != null) {
			endMilli = endDate.getTime();
		}

		if (endDate != null) {
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
		} else {
			for (int i = 0; i < 20; i++) {
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

	public void sortEvents() {
		ArrayList<Event> events = new ArrayList<Event>();

		for (CalendarObject o : eventsList) {
			Event currEvent = (Event) o;
			events.add(currEvent);
		}

		Collections.sort(events);
		eventsList.clear();

		for (Event e : events) {
			eventsList.add(e);
		}
	}

	public void sortTasks() {
		ArrayList<Task> tasks = new ArrayList<Task>();

		for (CalendarObject o : tasksList) {
			Task currTask = (Task) o;
			tasks.add(currTask);
		}

		Collections.sort(tasks);
		tasksList.clear();

		for (Task t : tasks) {
			tasksList.add(t);
		}
	}

	private boolean hasClash(Event event) {
		for (int i = 0; i < eventsList.size(); i++) {
			Event currEvent = (Event) eventsList.get(i);
			if (!event.equals(currEvent) && event.clashesWith(currEvent)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasChangedTime(String field) {
		field = field.trim();
		return (field.equals(FIELD_START_DATE) || field.equals(FIELD_START_TIME) || field.equals(FIELD_END_DATE)
				|| field.equals(FIELD_END_TIME));
	}
}
