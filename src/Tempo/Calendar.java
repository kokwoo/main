package Tempo;

import java.util.ArrayList;
import java.util.Collections;

public class Calendar {
	private String _fileName;
	
	private ArrayList<Event> eventsList;
	private ArrayList<Task> tasksList;
	private ArrayList<FloatingTask> floatingTasksList;

	public Calendar() {
		eventsList = new ArrayList<Event>();
		tasksList = new ArrayList<Task>();
		floatingTasksList = new ArrayList<FloatingTask>();
	}

	public Calendar(String fileName) {
		_fileName = fileName;
		eventsList = new ArrayList<Event>();
		tasksList = new ArrayList<Task>();
		floatingTasksList = new ArrayList<FloatingTask>();
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

	public void removeEvent() {

	}

	public void removeTask() {

	}

	public void removeFloatingTask() {

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
		CalendarExporter exporter = new CalendarExporter(_fileName, eventsList, tasksList, floatingTasksList);
		exporter.export();
	}
	
	public void importFromFile(){
		
	}
	
	
}
