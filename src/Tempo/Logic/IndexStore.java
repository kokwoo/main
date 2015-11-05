package Tempo.Logic;

import java.util.*;

import Tempo.CalendarObjects.CalendarObject;
import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;
import Tempo.CalendarObjects.Task;

public class IndexStore {
	private static IndexStore instance = new IndexStore();
	
	private static int nextUnusedPriId;
	private static int nextUnusedSecId;
	private static LinkedList<Integer> recycledPriId;
	private static LinkedList<Integer> recycledSecId;
	public static HashMap<Integer, CalendarObject> events;
	public static HashMap<Integer, CalendarObject> tasks;
	
	private IndexStore() {
		nextUnusedPriId = 0;
		nextUnusedSecId = 0;
		recycledPriId = new LinkedList<Integer>();
		recycledSecId = new LinkedList<Integer>();
		events = new HashMap<Integer, CalendarObject>();
		tasks = new HashMap<Integer, CalendarObject>();
	}
	
	public static IndexStore getInstance() {
		return instance;
	}
		
	public void initialiseStore(ArrayList<CalendarObject> eventsList, ArrayList<CalendarObject> tasksList, 
			  					ArrayList<CalendarObject> floatingTasksList) {
		initialiseEventsMap(eventsList);
		initialiseTasksMap(tasksList, floatingTasksList);
		updateRecycledId();
	}
	
	private void initialiseEventsMap(ArrayList<CalendarObject> eventsList) {
		for (int i = 0; i < eventsList.size(); i++) {
			Event currEvent = (Event) eventsList.get(i);
			addEvent(currEvent.getIndex(), currEvent);
			updateNextUnusedId(currEvent.getIndex());
		}
	}
	
	private void initialiseTasksMap(ArrayList<CalendarObject> tasksList, 
									ArrayList<CalendarObject> floatingTasksList) {
		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = (Task) tasksList.get(i);
			addTask(currTask.getIndex(), currTask);
			updateNextUnusedId(currTask.getIndex());
		}
		
		for (int i = 0; i < floatingTasksList.size(); i++) {
			FloatingTask currTask = (FloatingTask) floatingTasksList.get(i);
			addTask(currTask.getIndex(), currTask);
			updateNextUnusedId(currTask.getIndex());
		}
	}
	
	private void updateNextUnusedId(int idx) {
		if (idx >= nextUnusedPriId) {
			nextUnusedPriId = idx + 1;
		}
	}
	
	private void updateNextUnusedSecId(int idx) {
		if (idx >= nextUnusedSecId) {
			nextUnusedSecId = idx + 1;
		}
	}
	
	private void updateRecycledId() {
		for (int i = 0; i < nextUnusedPriId; i++) {
			if (!isUsedId(i)) {
				recycledPriId.add(i);
			}
		}
	}
	
	private void updateRecycledSecId() {
		boolean[] usedId = getUsedSecId();
		for (int i = 0; i < usedId.length; i++) {
			if (!usedId[i]) {
				recycledSecId.add(i);
			}
		}
	}
	
	private boolean isUsedId(int idx) {
		return (events.containsKey(idx) || tasks.containsKey(idx));
	}
	
	private boolean[] getUsedSecId() {
		boolean[] usedId = new boolean[nextUnusedSecId];
		for (Integer key : events.keySet()) {
			Event currEvent = (Event) events.get(key);
			int currId = currEvent.getSeriesIndex();
			if (!usedId[currId]) {
				usedId[currId] = true;
			}
		}
		
		for (Integer key : tasks.keySet()) {
			FloatingTask currTask = (FloatingTask) tasks.get(key);
			int currId = currTask.getSeriesIndex();
			if (!usedId[currId]) {
				usedId[currId] = true;
			}
		}
		
		return usedId;
	}
	
	public void addEvent(int index, Event newEvent) {
		events.put(index, newEvent);
	}
	
	public void addTask(int index, FloatingTask newTask) {
		tasks.put(index, newTask);
	}
	
	public void removeEvent(int index) {
		events.remove(index);
		recycledPriId.add(index);
	}
	
	public void removeTask(int index) {
		tasks.remove(index);
		recycledPriId.add(index);
	}
	
	public void replaceEvent(int index, Event event) {
		removeEvent(index);
		addEvent(index, event);
		removeRecycledId(index);
	}
	
	public void replaceTask(int index, FloatingTask task) {
		removeTask(index);
		addTask(index, task);
		removeRecycledId(index);
	}
	
	public int getNewId() {
		int id;
		if (recycledPriId.isEmpty()) {
			id = nextUnusedPriId;
			updateNextUnusedId(id);
		} else {
			id = recycledPriId.remove();
		}
		
		return id;
	}
	
	public int getNewSeriesId() {
		int id;
		if (recycledSecId.isEmpty()) {
			id = nextUnusedSecId;
			updateNextUnusedSecId(id);
		} else {
			id = recycledSecId.remove();
		}
		
		return id;
	}
	
	public void removeRecycledId(int index) {
		for (int i = 0; i < recycledPriId.size(); i++) {
			if (recycledPriId.get(i) == index) {
				recycledPriId.remove(i);
				break;
			}
		}
	}
	
	public boolean isEvent(int id) {
		return events.containsKey(id);
	}
	
	public boolean isFloatingTask(int id) {
		FloatingTask task = (FloatingTask) getTaskById(id);
		return task.isFloatingTask();
	}
	
	public boolean isTask(int id) {
		return (!isEvent(id) && !isFloatingTask(id));
	}
	
	public CalendarObject getEventById(int id) {
		return events.get(id);
	}
	
	public CalendarObject getTaskById(int id) {
		return tasks.get(id);
	}
}
