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
		initialiseStore(eventsList, tasksList, floatingTasksList);
	}
	
	private void initialiseStore(ArrayList<Event> eventsList, ArrayList<Task> tasksList, 
			  					ArrayList<FloatingTask> floatingTasksList) {
		initialiseEventsMap(eventsList);
		initialiseTasksMap(tasksList, floatingTasksList);
		updateRecycledId();
	}
	
	private void initialiseEventsMap(ArrayList<Event> eventsList) {
		for (int i = 0; i < eventsList.size(); i++) {
			Event currEvent = eventsList.get(i);
			addEvent(currEvent.getIndex(), currEvent);
			updateNextUnusedId(currEvent.getIndex());
		}
	}
	
	private void initialiseTasksMap(ArrayList<Task> tasksList, 
									ArrayList<FloatingTask> floatingTasksList) {
		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = tasksList.get(i);
			addTask(currTask.getIndex(), currTask);
			updateNextUnusedId(currTask.getIndex());
		}
		
		for (int i = 0; i < floatingTasksList.size(); i++) {
			FloatingTask currTask = floatingTasksList.get(i);
			addTask(currTask.getIndex(), currTask);
			updateNextUnusedId(currTask.getIndex());
		}
	}
	
	private void updateNextUnusedId(int idx) {
		if (idx >= nextUnusedId) {
			nextUnusedId = idx + 1;
		}
	}
	
	private void updateRecycledId() {
		for (int i = 0; i < nextUnusedId; i++) {
			if (!isUsedId(i)) {
				recycledId.add(i);
			}
		}
	}
	
	private boolean isUsedId(int idx) {
		return (events.containsKey(idx) || tasks.containsKey(idx));
	}
	
	public void addEvent(int index, Event newEvent) {
		events.put(index, newEvent);
	}
	
	public void addTask(int index, FloatingTask newTask) {
		tasks.put(index, newTask);
	}
	
	public void removeEvent(int index) {
		events.remove(index);
		recycledId.add(index);
	}
	
	public void removeTask(int index) {
		tasks.remove(index);
		recycledId.add(index);
	}
	
	public int getNewId() {
		int id;
		
		if (recycledId.isEmpty()) {
			id = nextUnusedId;
			updateNextUnusedId(id);
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
		FloatingTask task = getTaskById(id);
		if (task.isFloatingTask()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Event getEventById(int id) {
		return events.get(id);
	}
	
	public FloatingTask getTaskById(int id) {
		return tasks.get(id);
	}
}
