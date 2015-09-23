package Tempo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CalendarExporter {
	private String _fileName; 
	
	private ArrayList<Event> events;
	private ArrayList<Task> tasks;
	private ArrayList<FloatingTask> floatingTasks;
	
	private BufferedWriter out;
	
	public CalendarExporter(String fileName, ArrayList<Event> events, ArrayList<Task> tasks, ArrayList<FloatingTask> floatingTasks){
		this._fileName = fileName;
		this.events = events;
		this.tasks = tasks;
		this.floatingTasks = floatingTasks;
		
		try {
			out = new BufferedWriter(new FileWriter(_fileName, true));
		} catch (IOException e) {
			System.out.println("Error while exporting calendar");
		}					
						
	}
	
	public void export(){
		
	}
	
	public void writeToFile(String s){
		try {
			out.write(s + System.getProperty("line.separator"));
			out.flush();
		} catch (Exception e) {
			System.out.println("Error while exporting calendar");
		}
	}
	
	
}
