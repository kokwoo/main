package Tempo;

import java.util.*;

public class IndexStore {
	private static int nextUnusedId;
	private static LinkedList<Integer> recycledId;
	private static HashMap<Integer, Event> events;
	private static HashMap<Integer, FloatingTask> tasks;
	
	public IndexStore(ArrayList<Event> eventsList, ArrayList<Task> tasksList, 
					  ArrayList<FloatingTask> floatingTasksList) {
		nextUnusedId = 0;
		recycledId = new LinkedList<Integer>();
		events = new HashMap<Integer, Event>();
		tasks = new HashMap<Integer, FloatingTask>();
		initialiseMaps(eventsList, tasksList, floatingTasksList);
	}
	
	private void initialiseMaps(ArrayList<Event> eventsList, ArrayList<Task> tasksList, 
			  					ArrayList<FloatingTask> floatingTasksList) {
		initialiseEventsMap(eventsList);
		initialiseTasksMap(tasksList, floatingTasksList);
	}
	
	private void initialiseEventsMap(ArrayList<Event> eventsList) {
		for (int i = 0; i < eventsList.size(); i++) {
			Event currEvent = eventsList.get(i);
			addEvent(currEvent.getIndex(), currEvent);
		}
	}
	
	private void initialiseTasksMap(ArrayList<Task> tasksList, 
									ArrayList<FloatingTask> floatingTasksList) {
		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = tasksList.get(i);
			addTask(currTask.getIndex(), currTask);
		}
		
		for (int i = 0; i < floatingTasksList.size(); i++) {
			FloatingTask currTask = floatingTasksList.get(i);
			addTask(currTask.getIndex(), currTask);
		}
	}
		
	public void addEvent(int index, Event newEvent) {
		events.put(index, newEvent);
	}
	
	public void addTask(int index, FloatingTask newTask) {
		tasks.put(index, newTask);
	}
	
	public int getNewId() {
		int id;
		
		if (recycledId.isEmpty()) {
			id = nextUnusedId;
			nextUnusedId++;
		} else {
			id = recycledId.remove();
		}
		
		return id;
	}
	
	public boolean isEvent(int id) {
		if (events.containsKey(id)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFloatingTask(int id) {
		FloatingTask task = tasks.get(id);
		if (task.hasDueDate()) {
			return false;
		} else {
			return true;
		}
	}
	
	public Event getEventById(int id) {
		return events.get(id);
	}
	
	public FloatingTask getTaskById(int id) {
		return tasks.get(id);
	}
}
