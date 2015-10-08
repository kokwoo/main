package Tempo;

import java.io.*;
import java.util.*;

public class CalendarImporter {
	private String _fileName; 
	
	private ArrayList<Event> events;
	private ArrayList<Task> tasks;
	private ArrayList<FloatingTask> floatingTasks;
	
	private BufferedReader in;
	
	public CalendarImporter(String fileName){
		this._fileName =  fileName;
		
		events = new ArrayList<Event>();
		tasks = new ArrayList<Task>();
		floatingTasks = new ArrayList<FloatingTask>();
		
		try {
			in  = new BufferedReader(new FileReader(_fileName));
		} catch (FileNotFoundException e) {
			System.out.println("Unable to read from file!");
		}
		
		importFromFile();
	}
	
	private void importFromFile(){
		String currLine;
		try {
			while((currLine = in.readLine()) != null){
				String[] split = currLine.split("!!");
				
				if(split.length == 6){
					//events
					int eventIndex = Integer.parseInt(split[0]);
					Event newEvent  = new Event(eventIndex, split[1], split[2], 
												split[3], split[4], split[5]);
					events.add(newEvent);
					Collections.sort(events);
				}else if(split.length == 4){
					//tasks
					int taskIndex = Integer.parseInt(split[0]);
					Task newTask = new Task(taskIndex, split[1], split[2], 
											split[3]);
					tasks.add(newTask);
					Collections.sort(tasks);
				}else if (split.length == 3){
					//floating tasks
					int floatingTaskIndex = Integer.parseInt(split[0]);
					FloatingTask newFloatingTask =  new FloatingTask(floatingTaskIndex, 
																	split[1], split[2]);
					floatingTasks.add(newFloatingTask);
				}else{
					//unrecognized
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
