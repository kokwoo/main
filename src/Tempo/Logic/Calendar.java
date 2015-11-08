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
	private static Display display;

	private static Stack<Command> undoHistory;
	private static Stack<Command> redoHistory;
	private static Stack<Command> cmdHistory;

	private static final String MSG_WARNING_CLASH = "Warning: this event clashes with another event.\nEnter 'undo' if you would like to revoke the previous operation.";
	private static final String MSG_WARNING_RECUR_WITHOUT_DUE = "Warning: Unable to recur floating task!";
	private static final String MSG_ERROR_INVALID_FIELD = "Error: You have input an invalid field.\nPleae refer to the 'help' menu for reference.";

	private static final String CMD_ADD_EVENT = "add event %1$s";
	private static final String CMD_ADD_RECURR_EVENT = "add recurring event %1$s";
	private static final String CMD_ADD_TASK = "add task %1$s";
	private static final String CMD_ADD_RECURR_TASK = "add recurring task %1$s";
	private static final String CMD_ADD_FLOATING = "add floating task %1$s";

	private static final String CMD_REMOVE_EVENT = "remove event %1$s";
	private static final String CMD_REMOVE_TASK = "remove task %1$s";
	private static final String CMD_REMOVE_FLOATING = "remove floating task %1$s";

	private static final String CMD_EDIT_FILENAME = "renamed file as <%1$s>.";

	private static final String CMD_UPDATE_EVENT = "update event %1$s";
	private static final String CMD_UPDATE_TASK = "update task %1$s";
	private static final String CMD_UPDATE_FLOATING = "update floating task %1$s";

	private static final String CMD_DONE_TASK = "done task %1$s";
	private static final String CMD_DONE_FLOATING = "done floating task %1$s";

	private static final String CMD_UNDONE_TASK = "undone task %1$s";
	private static final String CMD_UNDONE_FLOATING = "undone floating task %1$s";

	private static final String CMD_UNDO = "undo";
	private static final String CMD_UNDO_CLEAR = "clear %1$s";

	private static final String CMD_REDO = "redo";

	private static final String CMD_SEARCH = "search %1$s";

	private static final String CMD_CLEAR = "%1$s has been cleared.";

	private static final String KEY_EVENTS = "events";
	private static final String KEY_TASKS = "tasks";
	private static final String KEY_FLOATING = "floating tasks";
	private static final String KEY_DAILY = "daily";
	private static final String KEY_WEEKLY = "weekly";
	private static final String KEY_MONTHLY = "monthly";
	private static final String KEY_ANNUALLY = "annually";
	
	private static final String KEY_EVENTS_BEST_MATCHES = "eventsBestMatches";
	private static final String KEY_EVENTS_ALTERNATIVE_MATCHES = "eventsAlternativeMatches";
	private static final String KEY_TASKS_BEST_MATCHES = "tasksBestMatches";
	private static final String KEY_TASKS_ALTERNATIVE_MATCHES = "tasksAlternativeMatches";
	private static final String KEY_FLOATING_TASKS_BEST_MATCHES = "floatingTasksBestMatches";
	private static final String KEY_FLOATING_TASKS_ALTERNATIVE_MATCHES = "floatingTasksAlternativeMatches";


	private static final String PARAM_NAME = "name";
	private static final String PARAM_START_DATE = "start date";
	private static final String PARAM_START_TIME = "start time";
	private static final String PARAM_END_DATE = "end date";
	private static final String PARAM_END_TIME = "end time";
	private static final String PARAM_DUE = "due";
	private static final String PARAM_REPEAT = "repeat";
	private static final String PARAM_RECURRING = "recurring";

	private static final String PARAM_DUMMY_DATE = "01/01/1990";

	private static final String KEY_BEST_MATCHES = "bestMatches";
	private static final String KEY_ALTERNATIVE_MATCHES = "alternativeMatches";

	private static final String FIELD_START_DATE = "start date";
	private static final String FIELD_START_TIME = "start time";
	private static final String FIELD_END_DATE = "end date";
	private static final String FIELD_END_TIME = "end time";

	public static final long MILLISECONDS_A_DAY = 86400000;
	public static final long MILLISECONDS_A_WEEK = 604800000;
	public static final String DATE_DELIMETER = "/";

	private boolean isUndoCmd = false;
	private boolean isUpdateRecurringCmd = false;

	private String fileName;

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
		display = Display.getInstance();
		undoHistory = new Stack<Command>();
		redoHistory = new Stack<Command>();
		cmdHistory = new Stack<Command>();
	}

	public static Calendar getInstance() {
		return instance;
	}

	public void createFile(String fileName) {
		this.fileName = fileName;
		File file = new File(fileName);
		if (file.exists()) {
			importFromFile();
			indexStore.initialiseStore(eventsList, tasksList, floatingTasksList);
		}
	}

	public Result editFilename(String fileName) {
		if (!isUndoCmd) {
			clearRedoHistory();
		}

		if (setFilename(fileName)) {
			String cmd = String.format(CMD_EDIT_FILENAME, fileName);
			return new Result(cmd, true, null);
		} else {
			return new Result(null, false, null);
		}
	}

	public boolean setFilename(String fileName) {
		if (!isUndoCmd) {
			clearRedoHistory();
		}

		boolean isSuccess = false;
		this.fileName = fileName;
		if (exporter.setFileName(fileName)) {
			exporter.export();
			isSuccess = true;
		}
		return isSuccess;
	}

	public String getFilename() {
		return fileName;
	}

	public Result clearFile() {
		Command newUndo = (Command) new UndoClear(eventsList, tasksList, floatingTasksList);
		undoHistory.add(newUndo);

		clearCalendar();

		exportToFile();
		String returnString = String.format(CMD_CLEAR, fileName);

		if (!isUndoCmd) {
			clearRedoHistory();
		}

		return new Result(returnString, true, true, null);
	}

	public void clearCalendar() {
		eventsList = new ArrayList<CalendarObject>();
		tasksList = new ArrayList<CalendarObject>();
		floatingTasksList = new ArrayList<CalendarObject>();

		indexStore.resetStore();

		System.out.println(eventsList);
		System.out.println(tasksList);
		System.out.println(floatingTasksList);
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

		if (!isUndoCmd) {
			clearRedoHistory();
		}

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

		if (!isUpdateRecurringCmd) {
			Command newUndo = (Command) new UndoAdd(newEventIndex, true, false, true);
			undoHistory.add(newUndo);
		}

		if (!isUndoCmd) {
			clearRedoHistory();
		}

		String cmd = String.format(CMD_ADD_RECURR_EVENT, name);
		return new Result(cmd, true, putInHashMap(KEY_EVENTS, eventsList));
	}

	public Result addBackAll(ArrayList<CalendarObject> events, ArrayList<CalendarObject> tasks,
			ArrayList<CalendarObject> floatingTasks) {
		eventsList = events;
		tasksList = tasks;
		floatingTasksList = floatingTasks;
		indexStore.initialiseStore(events, tasks, floatingTasks);

		HashMap<String, ArrayList<CalendarObject>> listsMap;
		listsMap = new HashMap<String, ArrayList<CalendarObject>>();
		listsMap.put(KEY_EVENTS, events);
		listsMap.put(KEY_TASKS, tasks);
		listsMap.put(KEY_FLOATING, floatingTasks);

		String cmd = String.format(CMD_UNDO_CLEAR, fileName);
		return new Result(cmd, true, listsMap);
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

		if (!isUndoCmd) {
			clearRedoHistory();
		}

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

		if (!isUpdateRecurringCmd) {
			Command newUndo = (Command) new UndoAdd(newTaskIndex, false, true, true);
			undoHistory.add(newUndo);
		}

		if (!isUndoCmd) {
			clearRedoHistory();
		}

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

		if (!isUndoCmd) {
			clearRedoHistory();
		}

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

		if (!isUpdateRecurringCmd) {
			Command newUndo;
		
			if (isSeries) {
				newUndo = (Command) new UndoRemove(eventsToRemove, true);
			} else {
				Event event = (Event) eventsToRemove.get(0);
				newUndo = (Command) new UndoRemove(event);
			}
		
			undoHistory.add(newUndo);
		}

		if (!isUndoCmd) {
			clearRedoHistory();
		}

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
					indexStore.removeTask(currTask.getIndex());
					tasksList.remove(i);
					i--;
				}
			}
		}

		exportToFile();

		if (!isUpdateRecurringCmd) {
			Command newUndo;
	
			if (isSeries) {
				newUndo = (Command) new UndoRemove(tasksToRemove, true);
			} else {
				Task task = (Task) tasksToRemove.get(0);
				newUndo = (Command) new UndoRemove(task);
			}
	
			undoHistory.add(newUndo);
		}

		if (!isUndoCmd) {
			clearRedoHistory();
		}

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

		if (!isUndoCmd) {
			clearRedoHistory();
		}

		String cmd = String.format(CMD_REMOVE_FLOATING, taskName);
		return new Result(cmd, true, putInHashMap(KEY_FLOATING, tasksList));
	}

	/***** UPDATE COMMAND EXECUTION ******/

	public Result updateEvent(int idx, ArrayList<String> fields, ArrayList<String> newValues, boolean isSeries) {
		boolean hasClash = false;
		ArrayList<CalendarObject> oldEvents = new ArrayList<CalendarObject>();

		int arrayListIndex = getArrayListIndexOfEvent(idx);
		Event eventToUpdate = (Event) eventsList.get(arrayListIndex);
		int seriesIndex = eventToUpdate.getSeriesIndex();
		Event oldEvent = copyEvent(eventToUpdate);
		int recurringFieldIdx = -1;

		String name = eventToUpdate.getName();
		String cmd = String.format(CMD_UPDATE_EVENT, name);

		boolean isUpdateRecurring = false;

		if (hasInvalidFields(fields, KEY_EVENTS)) {
			return new Result(cmd, MSG_ERROR_INVALID_FIELD, false, null);
		}

		isUpdateRecurring = hasUpdateRecurring(fields);

		for (int i = 0; i < fields.size(); i++) {
			if (!(fields.get(i).equals(PARAM_RECURRING) || fields.get(i).equals(PARAM_REPEAT))) {
				eventToUpdate.update(fields.get(i), newValues.get(i));
			} else {
				recurringFieldIdx = i;
			}
			if (!hasClash && hasChangedTime(fields.get(i)) && hasClash(eventToUpdate)) {
				hasClash = true;
			}
		}

		if (isSeries) {
			for (int i = 0; i < eventsList.size(); i++) {
				Event currEvent = (Event) eventsList.get(i);
				if (currEvent.getSeriesIndex() == seriesIndex) {
					oldEvents.add(copyEvent(currEvent));
					for (int j = 0; j < fields.size(); j++) {
						if ((fields.get(j).equals(PARAM_RECURRING) || fields.get(j).equals(PARAM_REPEAT))) {
							continue;
						}
						currEvent.update(fields.get(j), newValues.get(j));
						if (!hasClash && hasChangedTime(fields.get(j)) && hasClash(currEvent)) {
							hasClash = true;
						}
					}

				}
			}
		}

		if (isUpdateRecurring) {
			if (recurringFieldIdx != -1) {
				try {
					if (hasOnlyOneField(fields) && !isSeries) {
						for (int i = 0; i < eventsList.size(); i++) {
							Event currEvent = (Event) eventsList.get(i);
							if (currEvent.getSeriesIndex() == seriesIndex) {
								oldEvents.add(copyEvent(currEvent));
							}
						}
					}
					ArrayList<String> recurringParams = CommandParser
							.processRecurringArgs(newValues.get(recurringFieldIdx));

					Event tempEvent = copyEvent(eventToUpdate);
					isUpdateRecurringCmd = true;
					removeEvent(eventToUpdate.getIndex(), true);
					addRecurringEvent(tempEvent.getName(), tempEvent.getStartDateTimeSimplified(),
							tempEvent.getEndDateTimeSimplified(), recurringParams.get(0), recurringParams.get(1));
					isUpdateRecurringCmd = false;
				} catch (Exception e) {
					return new Result(cmd, MSG_ERROR_INVALID_FIELD, false, null);
				}
			}
		}

		exportToFile();

		Command newUndo;

		if (isSeries || isUpdateRecurring) {
			newUndo = (Command) new UndoUpdate(oldEvents, true);
		} else {
			newUndo = (Command) new UndoUpdate(oldEvent);
		}

		undoHistory.add(newUndo);

		if (!isUndoCmd) {
			clearRedoHistory();
		}

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
		ArrayList<CalendarObject> oldTasks = new ArrayList<CalendarObject>();

		int arrayListIndex = getArrayListIndexOfTask(idx);
		Task taskToUpdate = (Task) tasksList.get(arrayListIndex);
		int seriesIndex = taskToUpdate.getSeriesIndex();
		Task oldTask = copyTask(taskToUpdate);

		String name = taskToUpdate.getName();
		String cmd = String.format(CMD_UPDATE_TASK, name);

		boolean isUpdateRecurring = false;
		int recurringFieldIdx = -1;

		if (hasInvalidFields(fields, KEY_TASKS)) {
			return new Result(cmd, MSG_ERROR_INVALID_FIELD, false, null);
		}

		isUpdateRecurring = hasUpdateRecurring(fields);

		for (int i = 0; i < fields.size(); i++) {
			if (!(fields.get(i).equals(PARAM_RECURRING) || fields.get(i).equals(PARAM_REPEAT))) {
				taskToUpdate.update(fields.get(i), newValues.get(i));
			} else {
				recurringFieldIdx = i;
			}
		}

		if (isSeries) {
			for (int i = 0; i < tasksList.size(); i++) {
				Task currTask = (Task) tasksList.get(i);
				if (currTask.getSeriesIndex() == seriesIndex) {
					oldTasks.add(copyTask(currTask));
					for (int j = 0; j < fields.size(); j++) {
						if ((fields.get(j).equals(PARAM_RECURRING) || fields.get(j).equals(PARAM_REPEAT))) {
							continue;
						}
						currTask.update(fields.get(j), newValues.get(j));
					}
				}
			}
		}

		if (isUpdateRecurring) {
			if (recurringFieldIdx != -1) {
				try {
					if (hasOnlyOneField(fields) && !isSeries) {
						for(int i = 0; i < tasksList.size(); i++) {
							Task currTask = (Task) tasksList.get(i);
							if (currTask.getSeriesIndex() == seriesIndex) {
								oldTasks.add(copyTask(currTask));
							}
						}
					}
					ArrayList<String> recurringParams = CommandParser
							.processRecurringArgs(newValues.get(recurringFieldIdx));

					Task tempTask = copyTask(taskToUpdate);
					isUpdateRecurringCmd = true;
					removeTask(taskToUpdate.getIndex(), true);
					addRecurringTask(tempTask.getName(), tempTask.getDueDateSimplified(), recurringParams.get(0),
							recurringParams.get(1));
					isUpdateRecurringCmd = false;
				} catch (Exception e) {
					return new Result(cmd, MSG_ERROR_INVALID_FIELD, false, null);
				}
			} 
		}

		exportToFile();

		Command newUndo;

		if (isSeries || isUpdateRecurring) {
			newUndo = (Command) new UndoUpdate(oldTasks, false);
		} else {
			newUndo = (Command) new UndoUpdate(oldTask);
		}

		undoHistory.add(newUndo);

		if (!isUndoCmd) {
			clearRedoHistory();
		}

		return new Result(cmd, true, putInHashMap(KEY_TASKS, tasksList));
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

		String name = taskToUpdate.getName();
		String cmd = String.format(CMD_UPDATE_FLOATING, name);

		boolean updateDue = false;
		int dueFieldIdx = -1;

		if (hasInvalidFields(fields, KEY_FLOATING)) {
			return new Result(cmd, MSG_ERROR_INVALID_FIELD, false, null);
		}

		updateDue = hasUpdateDue(fields);

		// updateRecurring = hasUpdateRecurring(fields);

		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).equals(PARAM_DUE)) {
				dueFieldIdx = i;
			}
		}

		for (int i = 0; i < fields.size(); i++) {
			if(!fields.get(i).equals(PARAM_DUE)){
				taskToUpdate.update(fields.get(i), newValues.get(i));
			}else{
				dueFieldIdx = i;
			}
		}
		
		if(updateDue){
			FloatingTask tempTask = copyFloatingTask(taskToUpdate);
			removeFloatingTask(idx, true);
			addTask(tempTask.getName(), newValues.get(dueFieldIdx));
		}

		exportToFile();

		Command newUndo = (Command) new UndoUpdate(oldTask);
		undoHistory.add(newUndo);

		if (!isUndoCmd) {
			clearRedoHistory();
		}

		return new Result(cmd, true, putInHashMap(KEY_FLOATING, floatingTasksList));
	}

	private FloatingTask copyFloatingTask(FloatingTask task) {
		int idx = task.getIndex();
		int seriesIdx = task.getSeriesIndex();
		String taskName = task.getName();
		String taskDoneStatus = String.valueOf(task.isDone());
		return new FloatingTask(idx, seriesIdx, taskName, taskDoneStatus);
	}

	private boolean hasUpdateRecurring(ArrayList<String> fields) {
		for (String field : fields) {
			if (field.equals(PARAM_RECURRING) || field.equals(PARAM_REPEAT)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasUpdateDue(ArrayList<String> fields) {
		for (String field : fields) {
			if (field.equals(PARAM_DUE)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasInvalidFields(ArrayList<String> fields, String key) {
		if (key.equals(KEY_EVENTS)) {
			for (String field : fields) {
				if (isValidEventField(field)) {
					// DO NOTHING
				} else {
					return true;
				}
			}
		} else if (key.equals(KEY_TASKS)) {
			for (String field : fields) {
				if (isValidTaskField(field)) {
					// DO NOTHING
				} else {
					return true;
				}
			}
		} else if (key.equals(KEY_FLOATING)) {
			for (String field : fields) {
				if (isValidFloatingTaskField(field)) {
					// DO NOTHING
				} else {
					return true;
				}
			}
		} else {
			return true;
		}

		return false;
	}

	private boolean isValidEventField(String field) {
		switch (field) {
			case PARAM_NAME:
			case PARAM_START_DATE:
			case PARAM_START_TIME:
			case PARAM_END_DATE:
			case PARAM_END_TIME:
			case PARAM_RECURRING:
			case PARAM_REPEAT:
				return true;
			default:
				return false;
		}
	}

	private boolean isValidTaskField(String field) {
		switch (field) {
			case PARAM_NAME:
			case PARAM_DUE:
			case PARAM_RECURRING:
			case PARAM_REPEAT:
				return true;
			default:
				return false;
		}
	}

	private boolean isValidFloatingTaskField(String field) {
		switch (field) {
			case PARAM_NAME:
			case PARAM_DUE:
				return true;
			default:
				return false;
		}
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
			if (!isUndoCmd) {
				clearRedoHistory();
			}
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
			if (!isUndoCmd) {
				clearRedoHistory();
			}
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
			if (!isUndoCmd) {
				clearRedoHistory();
			}
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
			if (!isUndoCmd) {
				clearRedoHistory();
			}
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
		isUndoCmd = true;
		Result result = undoHistory.pop().execute();
		if (!cmdHistory.isEmpty()) {
			redoHistory.add(cmdHistory.pop());
		}
		exportToFile();
		isUndoCmd = false;
		return result;
	}

	public void removeLastUndo() {
		undoHistory.pop();
	}

	public void addToUndoHistory(Command cmd) {
		undoHistory.add(cmd);
	}

	/***** REDO COMMAND EXECUTION ******/
	public Result redo() {
		if (redoHistory.isEmpty()) {
			return new Result(CMD_REDO, false, null);
		}
		Result result = redoHistory.pop().execute();
		exportToFile();

		return result;
	}

	public void saveCmd(Command cmd) {
		cmdHistory.add(cmd);
	}

	private void clearRedoHistory() {
		redoHistory.clear();
	}

	/***** SEARCH COMMAND EXECUTION ******/

	public Result search(String arguments) {
		String bestMatchRegex = generateBestMatchRegex(arguments);
		String alternativeMatchRegex = generateAlternativeMatchRegex(arguments);

		ArrayList<CalendarObject> eventsToSearch = copyEventsList();
		ArrayList<CalendarObject> tasksToSearch = copyTasksList();
		ArrayList<CalendarObject> floatingTasksToSearch = copyFloatingTasksList();
		
		HashMap<String, ArrayList<CalendarObject>> results = new HashMap<String, ArrayList<CalendarObject>>();

		HashMap<String, ArrayList<CalendarObject>> eventSearchResult = getMatches(eventsToSearch, bestMatchRegex,
				alternativeMatchRegex);
		HashMap<String, ArrayList<CalendarObject>> taskSearchResult = getMatches(tasksToSearch, bestMatchRegex,
				alternativeMatchRegex);
		HashMap<String, ArrayList<CalendarObject>> floatingTaskSearchResult = getMatches(floatingTasksToSearch,
				bestMatchRegex, alternativeMatchRegex);

		ArrayList<CalendarObject> eventsBestMatch = eventSearchResult.get(KEY_BEST_MATCHES);
		ArrayList<CalendarObject> eventsAlternativeMatch = eventSearchResult.get(KEY_ALTERNATIVE_MATCHES);

		ArrayList<CalendarObject> tasksBestMatch = taskSearchResult.get(KEY_BEST_MATCHES);
		ArrayList<CalendarObject> tasksAlternativeMatch = taskSearchResult.get(KEY_ALTERNATIVE_MATCHES);

		ArrayList<CalendarObject> floatingTaskBestMatch = floatingTaskSearchResult.get(KEY_BEST_MATCHES);
		ArrayList<CalendarObject> floatingTasksAlternativeMatch = floatingTaskSearchResult.get(KEY_ALTERNATIVE_MATCHES);
		
		results.put(KEY_EVENTS_BEST_MATCHES, eventsBestMatch);
		results.put(KEY_EVENTS_ALTERNATIVE_MATCHES, eventsAlternativeMatch);
		results.put(KEY_TASKS_BEST_MATCHES, tasksBestMatch);
		results.put(KEY_TASKS_ALTERNATIVE_MATCHES, tasksAlternativeMatch);
		results.put(KEY_FLOATING_TASKS_BEST_MATCHES, floatingTaskBestMatch);
		results.put(KEY_FLOATING_TASKS_ALTERNATIVE_MATCHES, floatingTasksAlternativeMatch);
		
		String cmd = String.format(CMD_SEARCH, arguments);

		String displayString = display.formatSearchResults(eventsBestMatch, eventsAlternativeMatch, tasksBestMatch, tasksAlternativeMatch, floatingTaskBestMatch, floatingTasksAlternativeMatch);
		
		Result result = new Result(cmd, displayString ,true,results);
		return result;
	}

	private String generateBestMatchRegex(String args) {
		String[] splittedArgs = args.split("\\s+");

		String regex = "(?i:.*";

		for (String s : splittedArgs) {
			regex += s + ".*";
		}

		regex += ")";
		return regex;
	}

	private String generateAlternativeMatchRegex(String args) {
		String[] splittedArgs = args.split("\\s+");

		String regex = "(?i:.*";

		for (int i = 0; i < splittedArgs.length - 1; i++) {
			regex += splittedArgs[i] + ".*|.*";
		}

		if (!(splittedArgs.length - 1 < 0)) {
			regex += splittedArgs[splittedArgs.length - 1] + ".*";
		}

		regex += ")";
		return regex;
	}

	private ArrayList<CalendarObject> copyEventsList() {
		ArrayList<CalendarObject> returnArray = new ArrayList<CalendarObject>();

		for (CalendarObject obj : eventsList) {
			returnArray.add(obj);
		}
		return returnArray;
	}

	private ArrayList<CalendarObject> copyTasksList() {
		ArrayList<CalendarObject> returnArray = new ArrayList<CalendarObject>();

		for (CalendarObject obj : tasksList) {
			returnArray.add(obj);
		}
		return returnArray;
	}

	private ArrayList<CalendarObject> copyFloatingTasksList() {
		ArrayList<CalendarObject> returnArray = new ArrayList<CalendarObject>();

		for (CalendarObject obj : floatingTasksList) {
			returnArray.add(obj);
		}
		return returnArray;
	}

	private HashMap<String, ArrayList<CalendarObject>> getMatches(ArrayList<CalendarObject> objectsToMatch,
			String bestMatchRegex, String alternativeMatchRegex) {
		HashMap<String, ArrayList<CalendarObject>> returnHashMap = new HashMap<String, ArrayList<CalendarObject>>();
		ArrayList<CalendarObject> bestMatches = new ArrayList<CalendarObject>();
		ArrayList<CalendarObject> alternativeMatches = new ArrayList<CalendarObject>();

		while (objectsToMatch.size() > 0) {
			CalendarObject currObject = objectsToMatch.get(0);

			if (currObject.toString().matches(bestMatchRegex)) {
				bestMatches.add(currObject);
				objectsToMatch.remove(0);
			} else if (currObject.toString().matches(alternativeMatchRegex)) {
				alternativeMatches.add(currObject);
				objectsToMatch.remove(0);
			} else {
				objectsToMatch.remove(0);
			}
		}

		returnHashMap.put(KEY_BEST_MATCHES, bestMatches);
		returnHashMap.put(KEY_ALTERNATIVE_MATCHES, alternativeMatches);

		return returnHashMap;
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
		exporter.setFileName(fileName);
		exporter.export();
		// System.out.println("Export Successful!");
	}

	public void importFromFile() {
		System.out.println("Importing: " + fileName);
		if (importer.importFromFile(fileName)) {
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
				index = i;
			}
		}

		return index;
	}

	private int getArrayListIndexOfTask(int id) {
		int index = 0;

		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = (Task) tasksList.get(i);
			if (currTask.getIndex() == id) {
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
	
	private boolean hasOnlyOneField(ArrayList<String> fields) {
		return fields.size() == 1;
	}

	/******
	 * HELPER METHODS FOR RECURRING EVENTS NOTE: THE ARRAYLIST THAT IS RETURNED
	 * DOES NOT RETURN THE ORIGINAL START DATE FOR EXAMPLE: IF THE START DATE IS
	 * ON 12/03/15, THE FIRST DATE TO BE ADDED WILL BE ON 13/03/15
	 ******/

	private ArrayList<String> processRecurringDates(String start, String recurringEnd, String recurringType) {
		switch (recurringType.toLowerCase()) {
		case KEY_DAILY:
			return getDailyRecurringDates(start, recurringEnd);

		case KEY_WEEKLY:
			return getWeeklyRecurringDates(start, recurringEnd);

		case KEY_MONTHLY:
			return getMonthlyRecurringDates(start, recurringEnd);

		case KEY_ANNUALLY:
			return getAnnualRecurringDates(start, recurringEnd);

		default:
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
