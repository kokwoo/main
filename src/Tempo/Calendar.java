package Tempo;

import java.io.*;
import java.util.*;

public class Calendar {
	private String _fileName;
	
	private ArrayList<Event> eventsList;
	private ArrayList<Task> tasksList;
	private ArrayList<FloatingTask> floatingTasksList;

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
	}

	public void addEvent(String name, String startDate, String startTime, String endDate, String endTime) {
		Event newEvent = new Event(name, startDate, startTime, endDate, endTime);
		eventsList.add(newEvent);
		Collections.sort(eventsList);
	}

	public void addTask(String name, String dueDate) {
		Task newTask = new Task(name, dueDate);
		tasksList.add(newTask);
		Collections.sort(tasksList);
	}

	public void addFloatingTask(String name) {
		FloatingTask newFloatingTask = new FloatingTask(name);
		floatingTasksList.add(newFloatingTask);
	}

	public void removeEvent(int idx) {
		eventsList.remove(idx);
	}

	public void removeTask(int idx) {
		tasksList.remove(idx);
	}

	public void removeFloatingTask(int idx) {
		floatingTasksList.remove(idx);
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
