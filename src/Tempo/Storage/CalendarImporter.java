//@@author A0125962B
package Tempo.Storage;

import java.io.*;
import java.util.*;

import Tempo.Data.CalendarObject;
import Tempo.Data.Event;
import Tempo.Data.FloatingTask;
import Tempo.Data.Task;

public class CalendarImporter {
	private static CalendarImporter instance = new CalendarImporter();
	
	private String _fileName; 
	
	private ArrayList<CalendarObject> events;
	private ArrayList<CalendarObject> tasks;
	private ArrayList<CalendarObject> floatingTasks;
	
	private BufferedReader in;
	
	private CalendarImporter(){
		events = new ArrayList<CalendarObject>();
		tasks = new ArrayList<CalendarObject>();
		floatingTasks = new ArrayList<CalendarObject>();
	}
	
	public static CalendarImporter getInstance(){
		return instance;
	}
	
	public boolean importFromFile(String fileName){
		_fileName = fileName;
		
		events = new ArrayList<CalendarObject>();
		tasks = new ArrayList<CalendarObject>();
		floatingTasks = new ArrayList<CalendarObject>();
		
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
				
				if(split.length == 5){
					if(isBoolean(split[3])){
						int taskIndex = Integer.parseInt(split[0]);
						int taskSeriesIndex = Integer.parseInt(split[1]);
						Task newTask = new Task(taskIndex, taskSeriesIndex, split[2], split[3], split[4]);
						tasks.add(newTask);
					}else{
						int eventIndex = Integer.parseInt(split[0]);
						int eventSeriesIndex = Integer.parseInt(split[1]);
						Event newEvent  = new Event(eventIndex, eventSeriesIndex, split[2], split[3], split[4]);
						events.add(newEvent);
					}
				}else if (split.length == 4){
					int floatingTaskIndex = Integer.parseInt(split[0]);
					int floatingTaskSeriesIndex = Integer.parseInt(split[1]);
					FloatingTask newFloatingTask =  new FloatingTask(floatingTaskIndex, floatingTaskSeriesIndex, split[2], split[3]);
					floatingTasks.add(newFloatingTask);
				}
			}
			in.close();
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
	
	public ArrayList<CalendarObject> getEventsList(){		
		return events;
	}
	
	public ArrayList<CalendarObject> getTasksList(){
		return tasks;
	}
	
	public ArrayList<CalendarObject>getFloatingTasksList(){
		return floatingTasks;
	}
}
