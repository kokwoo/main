package Tempo.Storage;

import java.io.*;
import java.util.ArrayList;

import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;
import Tempo.CalendarObjects.Task;
import Tempo.Logic.Calendar;

public class CalendarExporter {
	private static CalendarExporter instance = new CalendarExporter();
	
	private String _fileName; 
	private Calendar calendar;
	
	private ArrayList<Event> events;
	private ArrayList<Task> tasks;
	private ArrayList<FloatingTask> floatingTasks;
	
	private static final String HEADER_EVENTS = "--EVENTS--";
	private static final String HEADER_TASKS = "--TASKS--";
	private static final String HEADER_FLOATING_TASKS = "--FLOATING TASKS--";
	private BufferedWriter out;
	
	private CalendarExporter(){
		_fileName = "";
		events = new ArrayList<Event>();
		tasks = new ArrayList<Task>();
		floatingTasks = new ArrayList<FloatingTask>();
		
		calendar = Calendar.getInstance();
						
	}
	
	public CalendarExporter getInstance(){
		return instance;
	}
	
	public void setFileName(String filename){
		_fileName = filename.trim();
		try{
			out = new BufferedWriter(new FileWriter(_fileName));
		}catch (Exception e){
			//UNABLE TO OPEN FILENAME
		}
	}
	
	public void export(){
		events = calendar.getEventsList();
		tasks = calendar.getTasksList();
		floatingTasks = calendar.getFloatingTasksList();
		
		try {
			out.write(HEADER_EVENTS + "\n");
			for(int i = 0; i < events.size(); i++){
				out.write(events.get(i).toString() + "\n");
			}
			out.write(HEADER_TASKS + "\n");
			for(int i = 0; i < tasks.size(); i++){
				out.write(tasks.get(i).toString() + "\n");
			}
			out.write(HEADER_FLOATING_TASKS + "\n");
			for(int i = 0; i < floatingTasks.size(); i++){
				out.write(floatingTasks.get(i).toString() + "\n");
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
