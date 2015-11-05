package Tempo.Commands;

import java.util.ArrayList;
import java.util.HashMap;

import Tempo.CalendarObjects.CalendarObject;
import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;

public class Result {
	private String commandPerformed;
	private boolean isSuccess;
	private HashMap<String, ArrayList<CalendarObject>> results;
	
	public Result(String commandPerformed, boolean isSuccess, HashMap<String, ArrayList<CalendarObject>> result){
		this.commandPerformed = commandPerformed;
		this.isSuccess = isSuccess;
		this.results = result;
	}
	
	public String getCommandPerformed(){
		return commandPerformed;
	}
	
	public boolean getIsSuccess(){
		return isSuccess;
	}
	
	public HashMap<String, ArrayList<CalendarObject>> getResults(){
		return results;
	}
	
	public void setCommand(String newCommand) {
		commandPerformed = newCommand;
	}
	
}
