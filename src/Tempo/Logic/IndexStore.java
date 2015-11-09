//@@author A0127047J
package Tempo.Logic;

import java.util.*;

import Tempo.Data.CalendarObject;
import Tempo.Data.Event;
import Tempo.Data.FloatingTask;
import Tempo.Data.Task;

public class IndexStore {
	private static IndexStore instance = new IndexStore();
	
	private static int nextUnusedPriId;
	private static int nextUnusedSecId;
	private static LinkedList<Integer> recycledPriId;
	private static LinkedList<Integer> recycledSeriesId;
	public static HashMap<Integer, CalendarObject> events;
	public static HashMap<Integer, CalendarObject> tasks;
	
	private IndexStore() {
		resetStore();	
	}
	
	public static IndexStore getInstance() {
		return instance;
	}
	
	public void resetStore(){
		nextUnusedPriId = 0;
		nextUnusedSecId = 0;
		recycledPriId = new LinkedList<Integer>();
		recycledSeriesId = new LinkedList<Integer>();
		events = new HashMap<Integer, CalendarObject>();
		tasks = new HashMap<Integer, CalendarObject>();
	}
		
	public void initialiseStore(ArrayList<CalendarObject> eventsList, 
								ArrayList<CalendarObject> tasksList, 
			  					ArrayList<CalendarObject> floatingTasksList) {
		initialiseEventsMap(eventsList);
		initialiseTasksMap(tasksList, floatingTasksList);
		updateRecycledId();
		updateRecycledSecId();
	}
	
	private void initialiseEventsMap(ArrayList<CalendarObject> eventsList) {
		for (int i = 0; i < eventsList.size(); i++) {
			Event currEvent = (Event) eventsList.get(i);
			addEvent(currEvent.getIndex(), currEvent);
			updateNextUnusedId(currEvent.getIndex());
			updateNextUnusedSecId(currEvent.getSeriesIndex());
		}
	}
	
	private void initialiseTasksMap(ArrayList<CalendarObject> tasksList, 
									ArrayList<CalendarObject> floatingTasksList) {
		for (int i = 0; i < tasksList.size(); i++) {
			Task currTask = (Task) tasksList.get(i);
			addTask(currTask.getIndex(), currTask);
			updateNextUnusedId(currTask.getIndex());
			updateNextUnusedSecId(currTask.getSeriesIndex());
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
		for (Integer key : events.keySet()) {
			Event currEvent = (Event) events.get(key);
			int currId = currEvent.getSeriesIndex();
			if (!recycledSeriesId.contains(currId)) {
				recycledSeriesId.add(currId);
			}
		}
		
		for (Integer key : tasks.keySet()) {
			FloatingTask currTask = (FloatingTask) tasks.get(key);
			int currId = currTask.getSeriesIndex();
			if (!recycledSeriesId.contains(currId)) {
				recycledSeriesId.add(currId);
			}
		}
	}
	
	private boolean isUsedId(int idx) {
		return (events.containsKey(idx) || tasks.containsKey(idx));
	}
	
	public void addEvent(int index, Event newEvent) {
		if (recycledPriId.contains(index)) {
			recycledPriId.remove(index);
		}
		if (recycledSeriesId.contains(newEvent.getSeriesIndex())) {
			recycledSeriesId.remove(newEvent.getSeriesIndex());
		}
		events.put(index, newEvent);
	}
	
	public void addTask(int index, FloatingTask newTask) {
		if (recycledPriId.contains(index)) {
			recycledPriId.remove(index);
		}
		if (recycledSeriesId.contains(newTask.getSeriesIndex())) {
			recycledSeriesId.remove(index);
		}
		tasks.put(index, newTask);
	}
	
	public void removeEvent(int index) {
		Event eventToRemove = (Event) events.get(index);
		int seriesId = eventToRemove.getSeriesIndex();
		recycledSeriesId.add(seriesId);
		events.remove(index);
		recycledPriId.add(index);
	}
	
	public void removeTask(int index) {
		FloatingTask taskToRemove = (FloatingTask) tasks.get(index);
		int seriesId = taskToRemove.getSeriesIndex();
		recycledSeriesId.add(seriesId);
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
		if (recycledSeriesId.isEmpty()) {
			id = nextUnusedSecId;
			updateNextUnusedSecId(id);
		} else {
			id = recycledSeriesId.remove();
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
