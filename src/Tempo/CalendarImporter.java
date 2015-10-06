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
				
				if(split.length == 5){
					//events
					Event newEvent  = new Event(split[0], split[1], split[2], split[3], split[4]);
					events.add(newEvent);
					Collections.sort(events);
				}else if(split.length == 3){
					//tasks
					Task newTask = new Task(split[0], split[1], split[2]);
					tasks.add(newTask);
					Collections.sort(tasks);
				}else if (split.length == 2){
					//floating tasks
					FloatingTask newFloatingTask =  new FloatingTask(split[0], split[1]);
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
