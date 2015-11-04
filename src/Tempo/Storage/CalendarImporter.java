package Tempo.Storage;

import java.io.*;
import java.util.*;

import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;
import Tempo.CalendarObjects.Task;

public class CalendarImporter {
	private static CalendarImporter instance = new CalendarImporter();
	
	private String _fileName; 
	
	private ArrayList<Event> events;
	private ArrayList<Task> tasks;
	private ArrayList<FloatingTask> floatingTasks;
	
	private BufferedReader in;
	
	private CalendarImporter(){
		events = new ArrayList<Event>();
		tasks = new ArrayList<Task>();
		floatingTasks = new ArrayList<FloatingTask>();
	}
	
	public static CalendarImporter getInstance(){
		return instance;
	}
	
	public boolean importFromFile(String fileName){
		_fileName = fileName;
		
		try {
			in  = new BufferedReader(new FileReader(_fileName));
		} catch (FileNotFoundException e) {
			System.out.println("Unable to read from file!");
			return false;
		}
		
		String currLine;
		try {
			while((currLine = in.readLine()) != null){
				String[] split = currLine.trim().split("!!");
//				--EVENTS--
//				0!!dinner with mum!!30/10/2015/22:00!!30/10/2015/23:00
//				--TASKS--
//				4!!eat brkfast!!false!!20/01/2015
//				--FLOATING TASKS--
//				0!!Do homework!!false
				
				if(split.length == 4){
					if(isBoolean(split[2])){
						//is a task
						//tasks
						int taskIndex = Integer.parseInt(split[0]);
						int taskSeriesIndex = Integer.parseInt(split[1]);
						Task newTask = new Task(taskIndex, taskSeriesIndex, split[2], split[3], split[4]);
						tasks.add(newTask);
						Collections.sort(tasks);
					}else{
						//is a event
						//events
						int eventIndex = Integer.parseInt(split[0]);
						int eventSeriesIndex = Integer.parseInt(split[1]);
						Event newEvent  = new Event(eventIndex, eventSeriesIndex, split[2], split[3], split[4]);
						events.add(newEvent);
						Collections.sort(events);
					}
				}else if (split.length == 3){
					//floating tasks
					int floatingTaskIndex = Integer.parseInt(split[0]);
					int floatingTaskSeriesIndex = Integer.parseInt(split[1]);
					FloatingTask newFloatingTask =  new FloatingTask(floatingTaskIndex, floatingTaskSeriesIndex, split[2], split[3]);
					floatingTasks.add(newFloatingTask);
				}else{
					//unrecognized
				}
			}
		} catch (IOException e) {
			System.out.println("Error encountered when reading from file!");
			return false;
		}
		return true;
	}
	
	private boolean isBoolean(String booleanStr){
		if(booleanStr.equals("true") || booleanStr.equals("false")){
			return true;
		}else{
			return false;
		}
	}
	
	public ArrayList<Event> getEventsList(){
		return events;
	}
	
	public ArrayList<Task> getTasksList(){
		return tasks;
	}
	
	public ArrayList<FloatingTask> getFloatingTasksList(){
		return floatingTasks;
	}
}
