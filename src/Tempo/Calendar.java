package Tempo;

import java.io.*;
import java.util.*;

public class Calendar {
	private String _fileName;
	
	private ArrayList<Event> eventsList;
	private ArrayList<Task> tasksList;
	private ArrayList<FloatingTask> floatingTasksList;
	private IndexStore indexStore;

//	public Calendar() {
//		eventsList = new ArrayList<Event>();
//		tasksList = new ArrayList<Task>();
//		floatingTasksList = new ArrayList<FloatingTask>();
//	}

	public Calendar(String fileName) {
		_fileName = fileName;
		eventsList = new ArrayList<Event>();
		tasksList = new ArrayList<Task>();
		floatingTasksList = new ArrayList<FloatingTask>();
		
		File file = new File(_fileName);
		
		// if the file exists, import the existing data from file
		// else ignore
		if(file.exists()){
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
	}

	public void addTask(String name, String dueDate) {
		int newTaskIndex = indexStore.getNewId();
		Task newTask = new Task(newTaskIndex, name, dueDate);
		tasksList.add(newTask);
		indexStore.addTask(newTaskIndex, newTask);
		Collections.sort(tasksList);
	}

	public void addFloatingTask(String name) {
		int newTaskIndex = indexStore.getNewId();
		FloatingTask newFloatingTask = new FloatingTask(newTaskIndex, name);
		indexStore.addTask(newTaskIndex, newFloatingTask);
		floatingTasksList.add(newFloatingTask);
	}
	
	public void remove(int idx) {
		if (indexStore.isEvent(idx)) {
			removeEvent(idx);
		} else if(indexStore.isFloatingTask(idx)){
			removeFloatingTask(idx);
		} else {
			removeTask(idx);
		}
	}
	

	public void removeEvent(int idx) {
		for (int i = 0; i < eventsList.size(); i++) {
			if (eventsList.get(i).getIndex() == idx) {
				indexStore.removeEvent(eventsList.get(i).getIndex());
				eventsList.remove(i);
				break;
			}
		}
	}

	public void removeTask(int idx) {
		for (int i = 0; i < tasksList.size(); i++) {
			if (tasksList.get(i).getIndex() == idx) {
				indexStore.removeTask(tasksList.get(i).getIndex());
				tasksList.remove(i);
				break;
			}
		}
	}
	
	public void removeFloatingTask(int idx) {
		for (int i = 0; i < floatingTasksList.size(); i++) {
			if (floatingTasksList.get(i).getIndex() == idx) {
				indexStore.removeTask(floatingTasksList.get(i).getIndex());
				floatingTasksList.remove(i);
				break;
			}
		}
	}
	
	public void update(int idx, ArrayList<String> fields, 
					   ArrayList<String> newValues) {
		if (indexStore.isEvent(idx)) {
			updateEvent(idx, fields, newValues);
		} else if (indexStore.isFloatingTask(idx)) {
			updateFloatingTask(idx, fields, newValues);
		} else {
			updateTask(idx, fields, newValues);
		}
	}
	
	public void updateEvent(int idx, ArrayList<String> fields, 
							ArrayList<String> newValues) {
	}
	
	public void updateTask(int idx, ArrayList<String> fields, 
						   ArrayList<String> newValues) {
		// TODO:
	}
	
	public void updateFloatingTask(int idx, ArrayList<String> fields, 
								   ArrayList<String> newValues) {
		// TODO:
	}

	public void display() {
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
		System.out.println("Exporting: " + _fileName);
		CalendarExporter exporter = new CalendarExporter(_fileName, eventsList, tasksList, floatingTasksList);
		exporter.export();
		System.out.println("Export Successful!");
	}
	
	public void importFromFile(){
		System.out.println("Importing: " + _fileName);
		CalendarImporter importer = new CalendarImporter(_fileName);
		eventsList = importer.getEventsList();
		tasksList = importer.getTasksList();
		floatingTasksList = importer.getFloatingTasksList();
		System.out.println("Import Sucessful!");
	}
	
	
}
