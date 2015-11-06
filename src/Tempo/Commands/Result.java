package Tempo.Commands;

import java.util.ArrayList;
import java.util.HashMap;

import Tempo.CalendarObjects.CalendarObject;
import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;

public class Result {
	private String commandPerformed;
	private String warning;
	private boolean isSuccess;
	private boolean isDisplay;
	private HashMap<String, ArrayList<CalendarObject>> results;
	
	public Result(String commandPerformed, boolean isSuccess, 
				HashMap<String, ArrayList<CalendarObject>> result) {
		this.commandPerformed = commandPerformed;
		this.warning = null;
		this.isDisplay = false;
		this.isSuccess = isSuccess;
		this.results = result;
	}
	
	public Result(String commandPerformed, String warning, 
				boolean isSuccess, 
				HashMap<String, ArrayList<CalendarObject>> result) {
		this.commandPerformed = commandPerformed;
		this.warning = warning;
		this.isDisplay = false;
		this.isSuccess = isSuccess;
		this.results = result;
	}
	
	public Result(String commandPerformed, boolean isDisplayResult, 
				boolean isSuccess, 
				HashMap<String, ArrayList<CalendarObject>> result){
		this.commandPerformed = commandPerformed;
		this.warning = null;
		this.isDisplay = isDisplayResult;
		this.isSuccess = isSuccess;
		this.results = result;
	}
	
	public String getCommandPerformed(){
		return commandPerformed;
	}
	
	public boolean hasWarning(){
		if(warning != null){
			return true;
		}else{
			return false;
		}
	}
	
	public String getWarning(){
		return warning;
	}
	
	public boolean getIsDisplay(){
		return isDisplay;
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
