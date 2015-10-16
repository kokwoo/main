package Tempo;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class Calendar {
	
	
	
	private String _fileName;

	private ArrayList<Event> eventsList;
	private ArrayList<Task> tasksList;
	private ArrayList<FloatingTask> floatingTasksList;
	private IndexStore indexStore;

	// display arguments
//	private final String ARG_EVENTS = "events";
//	private final String ARG_UPCOMING_EVENTS = "upcoming events";
//	private final String ARG_TASKS = "tasks";
//	private final String ARG_UNDONE_TASKS = "undone tasks";
//	private final String ARG_MISSED_TASKS = "missed tasks";
//	private final String ARG_TODAY = "today";
//	private final String ARGS_ALL = "all";

	// public Calendar() {
	// eventsList = new ArrayList<Event>();
	// tasksList = new ArrayList<Task>();
	// floatingTasksList = new ArrayList<FloatingTask>();
	// }

	public Calendar(String fileName) {
		_fileName = fileName;
		eventsList = new ArrayList<Event>();
		tasksList = new ArrayList<Task>();
		floatingTasksList = new ArrayList<FloatingTask>();

		File file = new File(_fileName);

		// if the file exists, import the existing data from file
		// else ignore
		if (file.exists()) {
			importFromFile();
		}

		indexStore = new IndexStore(eventsList, tasksList, floatingTasksList);
	}

	public void addEvent(String name, String startDate, String startTime, String endDate, String endTime) {
		int newEventIndex = indexStore.getNewId();
		Event newEvent = new Event(newEventIndex, name, startDate, startTime, endDate, endTime);
		eventsList.add(newEvent);
		indexStore.addEvent(newEventIndex, newEvent);
		Collections.sort(eventsList);
		exportToFile();
	}

	public void addTask(String name, String dueDate) {
		int newTaskIndex = indexStore.getNewId();
		Task newTask = new Task(newTaskIndex, name, dueDate);
		tasksList.add(newTask);
		indexStore.addTask(newTaskIndex, newTask);
		Collections.sort(tasksList);
		exportToFile();
	}

	public void addFloatingTask(String name) {
		int newTaskIndex = indexStore.getNewId();
		FloatingTask newFloatingTask = new FloatingTask(newTaskIndex, name);
		indexStore.addTask(newTaskIndex, newFloatingTask);
		floatingTasksList.add(newFloatingTask);
		exportToFile();
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
		assert indexStore.events.containsKey(idx);
		assert fields.size() == newValues.size();

		int arrayListIndex = getArrayListIndexOfEvent(idx);
		Event eventToUpdate = eventsList.get(arrayListIndex);
		for (int i = 0; i < fields.size(); i++) {
			eventToUpdate.update(fields.get(i), newValues.get(i));
		}
	}

	private void updateTask(int idx, ArrayList<String> fields, ArrayList<String> newValues) {
		assert indexStore.tasks.containsKey(idx);
		assert fields.size() == newValues.size();
		
		int arrayListIndex = getArrayListIndexOfTask(idx);
		Task taskToUpdate = tasksList.get(arrayListIndex);
		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}
	}

	private void updateFloatingTask(int idx, ArrayList<String> fields, ArrayList<String> newValues) {
		assert indexStore.tasks.containsKey(idx);
		assert fields.size() == newValues.size();

		int arrayListIndex = getArrayListIndexOfFloatingTask(idx);
		FloatingTask taskToUpdate = floatingTasksList.get(arrayListIndex);
		for (int i = 0; i < fields.size(); i++) {
			taskToUpdate.update(fields.get(i), newValues.get(i));
		}
	}
	
	public ArrayList<String> searchId(int id){
		ArrayList<String> idFoundLines = new ArrayList<String>();
		if(!indexStore.isEvent(id) && !indexStore.isFloatingTask(id)){
			idFoundLines.clear();
		}
		
		else if(indexStore.isEvent(id)){
			Event event = indexStore.getEventById(id);
			idFoundLines.add(event.toString());
		}
		
		else if(indexStore.isFloatingTask(id)){
			FloatingTask task = indexStore.getTaskById(id);
			idFoundLines.add(task.toString());
		}
		return idFoundLines;
		
	}
	
	public ArrayList<String> searchKeyWord(String keyword){
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
		
		return wordFoundLines;

	}
	
	private boolean containWord (String content, String keyword) {
		String[] splited = content.split("\\W");
		for (int i = 0; i < splited.length; i++) {
			if (splited[i].equalsIgnoreCase(keyword)) {
				return true;
			}
		}
		return false;
	}
	
	/*public void display(String displayType)  {
		Display display = new Display(getEventsList(), getTasksList(), getFloatingTasksList());
		
		
		switch (displayType) {
			case (ARG_EVENTS) :
				display.events();
			case (ARG_TASKS) :
				display.tasks();
			case (ARG_UPCOMING_EVENTS) :
			try {
				display.upcomingEvents();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			case (ARG_UNDONE_TASKS) :
				display.undoneTasks();
			case (ARG_MISSED_TASKS) :
				display.missedTasks();
			case (ARG_TODAY) :	
			try {
				display.today();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			case (ARGS_ALL) :
			try {
				display.all();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		
	}*/

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
				i = index;
			}
		}

		return index;
	}

	private int getArrayListIndexOfFloatingTask(int id) {
		int index = 0;

		for (int i = 0; i < floatingTasksList.size(); i++) {
			if (floatingTasksList.get(i).getIndex() == id) {
				i = index;
			}
		}

		return index;
	}

}
