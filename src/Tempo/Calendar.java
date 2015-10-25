package Tempo;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class Calendar {
	
	private static Calendar instance = new Calendar();
	private static IndexStore indexStore;
	
	private static final String MSG_ADDED_EVENT = "Event %1$s has been added.";
	private static final String MSG_ADDED_TASK = "Task %1$s has been added.";
	private static final String MSG_REMOVED_EVENT = "Event %1$s has been removed.";
	private static final String MSG_REMOVED_TASK = "Task %1$s has been removed.";
	private static final String MSG_UPDATED_EVENT = "Your event has been updated.";
	private static final String MSG_UPDATED_TASK = "Your task has been updated.";


	private String _fileName;

	private int prevModIndex = -1;
	private Event prevModEvent = null;
	private Task prevModTask = null;
	private FloatingTask prevModFloatingTask = null;
	private String prevCommand = "disabled";

	private ArrayList<Event> eventsList;
	private ArrayList<Task> tasksList;
	private ArrayList<FloatingTask> floatingTasksList;

	private Calendar() {
		eventsList = new ArrayList<Event>();
		tasksList = new ArrayList<Task>();
		floatingTasksList = new ArrayList<FloatingTask>();
		indexStore = IndexStore.getInstance();
		indexStore.initialiseStore(eventsList, tasksList, floatingTasksList);
	}
	
	public static Calendar getInstance() {
		return instance;
	}
	
	public void createFile(String fileName) {
		_fileName = fileName;
		File file = new File(_fileName);
		if (file.exists()) {
			importFromFile();
		}
	}
	
	/***** ADD COMMAND EXECUTION ******/
	
	public ArrayList<String> addEvent(String name, String start, String end) {
		int newEventIndex = indexStore.getNewId();
		Event newEvent = new Event(newEventIndex, name, start, end);
		eventsList.add(newEvent);
		indexStore.addEvent(newEventIndex, newEvent);
		Collections.sort(eventsList);
		exportToFile();

		savePrevCmd(newEventIndex, newEvent, null, null, "add");
		
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(String.format(MSG_ADDED_EVENT, name));
		
		return feedback;
	}
	
	public ArrayList<String> addTask(String name, String dueDate) {
		int newTaskIndex = indexStore.getNewId();
		Task newTask = new Task(newTaskIndex, name, dueDate);
		tasksList.add(newTask);
		indexStore.addTask(newTaskIndex, newTask);
		Collections.sort(tasksList);
		exportToFile();

		savePrevCmd(newTaskIndex, null, newTask, null, "add");
		
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(String.format(MSG_ADDED_TASK, name));
		
		return feedback;	
	}

	public ArrayList<String> addFloatingTask(String name) {
		int newTaskIndex = indexStore.getNewId();
		FloatingTask newFloatingTask = new FloatingTask(newTaskIndex, name);
		indexStore.addTask(newTaskIndex, newFloatingTask);
		floatingTasksList.add(newFloatingTask);
		exportToFile();

		savePrevCmd(newTaskIndex, null, null, newFloatingTask, "add");
		
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(String.format(MSG_ADDED_TASK, name));
		
		return feedback;	
	}
	
	/***** REMOVE COMMAND EXECUTION ******/
	
	public ArrayList<String> removeEvent(int idx) {
		String eventName = new String();
		
		for (int i = 0; i < eventsList.size(); i++) {
			if (eventsList.get(i).getIndex() == idx) {
				savePrevCmd(idx, eventsList.get(i), null, null, "remove");
				name = eventsList.get(i).getName();
				indexStore.removeEvent(eventsList.get(i).getIndex());
				eventsList.remove(i);
				break;
			}
		}
		
		exportToFile();
		
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(String.format(MSG_REMOVED_EVENT, eventName));
		
		return feedback;
	}

	public ArrayList<String> removeTask(int idx) {
		String taskName = new String();
		for (int i = 0; i < tasksList.size(); i++) {
			if (tasksList.get(i).getIndex() == idx) {
				savePrevCmd(idx, null, tasksList.get(i), null, "remove");
				taskName = tasksList.get(i).getName();
				indexStore.removeTask(tasksList.get(i).getIndex());
				tasksList.remove(i);
				break;
			}
		}
		
		exportToFile();
		
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(String.format(MSG_REMOVED_TASK, taskName));
		
		return feedback;
	}

	public ArrayList<String> removeFloatingTask(int idx) {
		String taskName = new String();
		for (int i = 0; i < floatingTasksList.size(); i++) {
			if (floatingTasksList.get(i).getIndex() == idx) {
				savePrevCmd(idx, null, null, floatingTasksList.get(i), "remove");
				taskName = tasksList.get(i).getName();
				indexStore.removeTask(floatingTasksList.get(i).getIndex());
				floatingTasksList.remove(i);
				break;
			}
		}
		
		exportToFile();
		
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(String.format(MSG_REMOVED_TASK, taskName));
		
		return feedback;
	}
	
	/***** UPDATE COMMAND EXECUTION ******/
	
	public ArrayList<String> updateEvent(int idx, ArrayList<String> fields, ArrayList<String> newValues) {
		int arrayListIndex = getArrayListIndexOfEvent(idx);
		Event eventToUpdate = eventsList.get(arrayListIndex);

		savePrevCmd(idx, eventToUpdate, null, null, "update");

		for (int i = 0; i < fields.size(); i++) {
			eventToUpdate.update(fields.get(i), newValues.get(i));
		}
		
		exportToFile();
		
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(MSG_UPDATED_EVENT);
		
		return feedback;
	}

	public ArrayList<String> updateTask(int idx, ArrayList<String> fields, ArrayList<String> newValues) {
		int arrayListIndex = getArrayListIndexOfTask(idx);
		Task taskToUpdate = tasksList.get(arrayListIndex);

		savePrevCmd(idx, null, taskToUpdate, null, "update");

		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}
		exportToFile();
		
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(MSG_UPDATED_TASK);
		
		return feedback;

	}

	public ArrayList<String> updateFloatingTask(int idx, ArrayList<String> fields, ArrayList<String> newValues) {

		int arrayListIndex = getArrayListIndexOfFloatingTask(idx);
		FloatingTask taskToUpdate = floatingTasksList.get(arrayListIndex);

		savePrevCmd(idx, null, null, taskToUpdate, "update");

		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}
		exportToFile();
		
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(MSG_UPDATED_TASK);
		
		return feedback;
	}

	/***** UNDO COMMAND EXECUTION ******/

	public ArrayList<String> undo() {
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
