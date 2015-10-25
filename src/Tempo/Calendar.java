package Tempo;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class Calendar {
	
	private static Calendar instance = new Calendar();

	private String _fileName;

	private int prevModIndex = -1;
	private Event prevModEvent = null;
	private Task prevModTask = null;
	private FloatingTask prevModFloatingTask = null;
	private String prevCommand = "disabled";

	private ArrayList<Event> eventsList;
	private ArrayList<Task> tasksList;
	private ArrayList<FloatingTask> floatingTasksList;

	private IndexStore indexStore;

	// display arguments
	private final String ARG_EVENTS = "events";
	private final String ARG_UPCOMING_EVENTS = "upcoming events";
	private final String ARG_TASKS = "tasks";
	private final String ARG_UNDONE_TASKS = "undone tasks";
	private final String ARG_MISSED_TASKS = "missed tasks";
	private final String ARG_TODAY = "today";
	private final String ARGS_ALL = "all";

	// public Calendar() {
	// eventsList = new ArrayList<Event>();
	// tasksList = new ArrayList<Task>();
	// floatingTasksList = new ArrayList<FloatingTask>();
	// }

	private Calendar() {
		eventsList = new ArrayList<Event>();
		tasksList = new ArrayList<Task>();
		floatingTasksList = new ArrayList<FloatingTask>();
		indexStore = new IndexStore(eventsList, tasksList, floatingTasksList);
	}
	
	public static Calendar getInstance() {
		return instance;
	}
	
	public void createFile(String fileName) {
		_fileName = fileName;
		File file = new File(_fileName);
		// if the file exists, import the existing data from file
		// else ignore
		if (file.exists()) {
			importFromFile();
		}
	}

	public void undo() {
		switch (prevCommand) {
		case "add":
			undoAdd();
			break;
		case "remove":
			undoRemove();
			break;
		case "update":
			undoUpdate();
			break;
		default:
			System.out.println("Undo cannot be used here.");
			// should not display here
		}

		disableUndo();
	}

	public void disableUndo() {
		prevModIndex = -1;
		prevCommand = "disabled";
		prevModEvent = null;
		prevModTask = null;
		prevModFloatingTask = null;
	}

	public void savePrevCmd(int index, Event event, Task task, FloatingTask floatingTask, String command) {
		prevModIndex = index;
		prevModEvent = event;
		prevModTask = task;
		prevModFloatingTask = floatingTask;
		prevCommand = command;
	}

	public void undoAdd() {
		remove(prevModIndex);
		exportToFile();
	}

	public void undoRemove() {
		if (prevModEvent != null) {
			addBackEvent();
		} else {
			addBackTask();
		}
		exportToFile();
	}

	public void undoUpdate() {
		if (prevModEvent != null) {
			undoUpdateEvent();
		} else if (prevModFloatingTask != null) {
			undoUpdateFloatingTask();
		} else {
			undoUpdateTask();
		}
		exportToFile();
	}

	public void undoUpdateEvent() {
		for (int i = 0; i < eventsList.size(); i++) {
			if (eventsList.get(i).getIndex() == prevModIndex) {
				eventsList.set(i, prevModEvent);
				Collections.sort(eventsList);
				indexStore.replaceEvent(prevModIndex, prevModEvent);
			}
		}
	}

	public void undoUpdateFloatingTask() {
		for (int i = 0; i < floatingTasksList.size(); i++) {
			if (floatingTasksList.get(i).getIndex() == prevModIndex) {
				floatingTasksList.set(i, prevModFloatingTask);
				indexStore.replaceTask(prevModIndex, prevModFloatingTask);
			}
		}
	}

	public void undoUpdateTask() {
		for (int i = 0; i < tasksList.size(); i++) {
			if (tasksList.get(i).getIndex() == prevModIndex) {
				tasksList.set(i, prevModTask);
				Collections.sort(tasksList);
				indexStore.replaceTask(prevModIndex, prevModTask);
			}
		}
	}

	public void addBackEvent() {
		eventsList.add(prevModEvent);
		indexStore.addEvent(prevModIndex, prevModEvent);
		Collections.sort(eventsList);
		exportToFile();
	}

	public void addBackTask() {
		if (prevModFloatingTask != null) {
			floatingTasksList.add(prevModFloatingTask);
		} else {
			tasksList.add(prevModTask);
			Collections.sort(tasksList);
		}

		indexStore.addTask(prevModIndex, prevModTask);
		exportToFile();
	}

	public void addEvent(String name, String start, String end) {
		int newEventIndex = indexStore.getNewId();
		Event newEvent = new Event(newEventIndex, name, start, end);
		eventsList.add(newEvent);
		indexStore.addEvent(newEventIndex, newEvent);
		Collections.sort(eventsList);
		exportToFile();

		savePrevCmd(newEventIndex, newEvent, null, null, "add");
	}

	public void addTask(String name, String dueDate) {
		int newTaskIndex = indexStore.getNewId();
		Task newTask = new Task(newTaskIndex, name, dueDate);
		tasksList.add(newTask);
		indexStore.addTask(newTaskIndex, newTask);
		Collections.sort(tasksList);
		exportToFile();

		savePrevCmd(newTaskIndex, null, newTask, null, "add");
	}

	public void addFloatingTask(String name) {
		int newTaskIndex = indexStore.getNewId();
		FloatingTask newFloatingTask = new FloatingTask(newTaskIndex, name);
		indexStore.addTask(newTaskIndex, newFloatingTask);
		floatingTasksList.add(newFloatingTask);
		exportToFile();

		savePrevCmd(newTaskIndex, null, null, newFloatingTask, "add");
	}

	public void remove(int idx) {
		if (indexStore.isEvent(idx)) {
			removeEvent(idx);
		} else if (indexStore.isFloatingTask(idx)) {
			removeFloatingTask(idx);
		} else {
			removeTask(idx);
		}
		exportToFile();
	}

	private void removeEvent(int idx) {
		assert indexStore.events.containsKey(idx);
		for (int i = 0; i < eventsList.size(); i++) {
			if (eventsList.get(i).getIndex() == idx) {
				savePrevCmd(idx, eventsList.get(i), null, null, "remove");
				indexStore.removeEvent(eventsList.get(i).getIndex());
				eventsList.remove(i);
				break;
			}
		}
	}

	private void removeTask(int idx) {
		assert indexStore.tasks.containsKey(idx);
		for (int i = 0; i < tasksList.size(); i++) {
			if (tasksList.get(i).getIndex() == idx) {
				savePrevCmd(idx, null, tasksList.get(i), null, "remove");
				indexStore.removeTask(tasksList.get(i).getIndex());
				tasksList.remove(i);
				break;
			}
		}
	}

	private void removeFloatingTask(int idx) {
		assert indexStore.tasks.containsKey(idx);
		for (int i = 0; i < floatingTasksList.size(); i++) {
			if (floatingTasksList.get(i).getIndex() == idx) {
				savePrevCmd(idx, null, null, floatingTasksList.get(i), "remove");
				indexStore.removeTask(floatingTasksList.get(i).getIndex());
				floatingTasksList.remove(i);
				break;
			}
		}
	}

	public void update(int idx, ArrayList<String> fields, ArrayList<String> newValues) {
		if (indexStore.isEvent(idx)) {
			updateEvent(idx, fields, newValues);
		} else if (indexStore.isFloatingTask(idx)) {
			updateFloatingTask(idx, fields, newValues);
		} else {
			updateTask(idx, fields, newValues);
		}
		exportToFile();

	}

	private void updateEvent(int idx, ArrayList<String> fields, ArrayList<String> newValues) {

		int arrayListIndex = getArrayListIndexOfEvent(idx);
		Event eventToUpdate = eventsList.get(arrayListIndex);

		savePrevCmd(idx, eventToUpdate, null, null, "update");

		for (int i = 0; i < fields.size(); i++) {
			eventToUpdate.update(fields.get(i), newValues.get(i));
		}

	}

	private void updateTask(int idx, ArrayList<String> fields, ArrayList<String> newValues) {

		int arrayListIndex = getArrayListIndexOfTask(idx);
		Task taskToUpdate = tasksList.get(arrayListIndex);

		savePrevCmd(idx, null, taskToUpdate, null, "update");

		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}

	}

	private void updateFloatingTask(int idx, ArrayList<String> fields, ArrayList<String> newValues) {

		int arrayListIndex = getArrayListIndexOfFloatingTask(idx);
		FloatingTask taskToUpdate = floatingTasksList.get(arrayListIndex);

		savePrevCmd(idx, null, null, taskToUpdate, "update");

		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}
	}

	public ArrayList<String> searchId(int id) {
		ArrayList<String> idFoundLines = new ArrayList<String>();
		if (!indexStore.isEvent(id) && !indexStore.isFloatingTask(id)) {
			idFoundLines.clear();
		}

		else if (indexStore.isEvent(id)) {
			Event event = indexStore.getEventById(id);
			idFoundLines.add(event.toString());
		}

		else if (indexStore.isFloatingTask(id)) {
			FloatingTask task = indexStore.getTaskById(id);
			idFoundLines.add(task.toString());
		}
		disableUndo();
		return idFoundLines;

	}

	public ArrayList<String> searchKeyWord(String keyword) {
		ArrayList<String> wordFoundLines = new ArrayList<String>();
		for (int i = 0; i < eventsList.size(); i++) {
			if (containWord(eventsList.get(i).toString(), keyword)) {
				wordFoundLines.add(eventsList.get(i).toString());
			}
		}

		for (int i = 0; i < tasksList.size(); i++) {
			if (containWord(tasksList.get(i).toString(), keyword)) {
				wordFoundLines.add(tasksList.get(i).toString());
			}
		}

		for (int i = 0; i < floatingTasksList.size(); i++) {
			if (containWord(floatingTasksList.get(i).toString(), keyword)) {
				wordFoundLines.add(floatingTasksList.get(i).toString());
			}
		}
		disableUndo();
		return wordFoundLines;

	}

	private boolean containWord(String content, String keyword) {
		String[] splited = content.split("\\W");
		for (int i = 0; i < splited.length; i++) {
			if (splited[i].equalsIgnoreCase(keyword)) {
				return true;
			}
		}
		return false;
	}

	public void display(String displayType) {
		Display display = new Display(getEventsList(), getTasksList(), getFloatingTasksList());

		switch (displayType) {
		case (ARG_EVENTS):
			display.events();
		case (ARG_TASKS):
			display.tasks();
		case (ARG_UPCOMING_EVENTS):
			display.upcomingEvents();
		case (ARG_UNDONE_TASKS):
			display.undoneTasks();
		case (ARG_MISSED_TASKS):
			display.missedTasks();
		case (ARG_TODAY):
			try {
				display.today();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		case (ARGS_ALL):
			try {
				display.all();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public ArrayList<Event> getEventsList() {
		return eventsList;
	}

	public ArrayList<Task> getTasksList() {
		return tasksList;
	}

	public ArrayList<FloatingTask> getFloatingTasksList() {
		return floatingTasksList;
	}

	public void exportToFile() {
		// System.out.println("Exporting: " + _fileName);
		CalendarExporter exporter = new CalendarExporter(_fileName, eventsList, tasksList, floatingTasksList);
		exporter.export();
		// System.out.println("Export Successful!");
	}

	public void importFromFile() {
		System.out.println("Importing: " + _fileName);
		CalendarImporter importer = new CalendarImporter(_fileName);
		eventsList = importer.getEventsList();
		tasksList = importer.getTasksList();
		floatingTasksList = importer.getFloatingTasksList();
		System.out.println("Import Sucessful!");
	}

	private int getArrayListIndexOfEvent(int id) {
		int index = 0;

		for (int i = 0; i < eventsList.size(); i++) {
			if (eventsList.get(i).getIndex() == id) {
				i = index;
			}
		}

		return index;
	}

	private int getArrayListIndexOfTask(int id) {
		int index = 0;

		for (int i = 0; i < tasksList.size(); i++) {
			if (tasksList.get(i).getIndex() == id) {
				//i = index;
				index = i;
			}
		}

		return index;
	}

	private int getArrayListIndexOfFloatingTask(int id) {
		int index = 0;

		for (int i = 0; i < floatingTasksList.size(); i++) {
			if (floatingTasksList.get(i).getIndex() == id) {
				//i = index;
				index = i;
			}
		}

		return index;
	}

}
