package Tempo.Commands;

import java.util.ArrayList;
import java.util.HashMap;

import Tempo.Data.CalendarObject;
import Tempo.Data.Event;
import Tempo.Data.FloatingTask;

//@@author A0125962B
public class Result {
	private String cmdPeformed;
	private String warning;
	private boolean isSuccess;
	private boolean isDisplayResult;
	private HashMap<String, ArrayList<CalendarObject>> results;
	
	public Result(String commandPerformed, boolean isSuccess, 
				HashMap<String, ArrayList<CalendarObject>> result) {
		this.cmdPeformed = commandPerformed;
		this.warning = null;
		this.isDisplayResult = false;
		this.isSuccess = isSuccess;
		this.results = result;
	}
	
	public Result(String commandPerformed, String warning, 
				boolean isSuccess, 
				HashMap<String, ArrayList<CalendarObject>> result) {
		this.cmdPeformed = commandPerformed;
		this.warning = warning;
		this.isDisplayResult = false;
		this.isSuccess = isSuccess;
		this.results = result;
	}
	
	public Result(String cmdPerformed, boolean isDisplayResult, 
				boolean isSuccess, 
				HashMap<String, ArrayList<CalendarObject>> result){
		this.cmdPeformed = cmdPerformed;
		this.warning = null;
		this.isDisplayResult = isDisplayResult;
		this.isSuccess = isSuccess;
		this.results = result;
	}
	
	public String getCmdPerformed(){
		return cmdPeformed;
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
	
	public boolean isDisplayResult(){
		return isDisplayResult;
	}
	
	public boolean isSuccess(){
		return isSuccess;
	}
	
	public HashMap<String, ArrayList<CalendarObject>> getResults(){
		return results;
	}
	
	public void setCommand(String newCommand) {
		cmdPeformed = newCommand;
	}
	
}
