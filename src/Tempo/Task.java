package Tempo;

import java.util.*;
import java.text.*;

public class Task extends FloatingTask implements Comparable<Task>{
	protected Date _dueDate;
	
	public Task(String name, String done, String dueDateString){
		super(name, done);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			_dueDate = formatter.parse(dueDateString);
		} catch (ParseException e) {
			System.out.println("Unable to format date");
		}
	}
	
	public Task(String name, String dueDateString){
		super(name);
		//SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss");
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			_dueDate = formatter.parse(dueDateString);
		} catch (ParseException e) {
			System.out.println("Unable to format date");
		}
	}
	
	public String getDueDate(){
		//SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy");
		return formatter.format(_dueDate);
	}
	
	private String getDueDateSimplified(){
		//For toString() only
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(_dueDate);
	}
	
	public long getDueDateTimeInMilli(){
		return _dueDate.getTime();
	}
	
	public int compareTo(Task t) {
		if(this.getDueDateTimeInMilli() < t.getDueDateTimeInMilli()){
			return -1;
		}else if(this.getDueDateTimeInMilli() == t.getDueDateTimeInMilli()){
			return 0;
		}else if (this.getDueDateTimeInMilli() > t.getDueDateTimeInMilli()){
			return 1;
		}
		return 0;
	}
	
	public String toString(){
		String delimeter = "!!";
		return super.toString() + delimeter + getDueDateSimplified();
	}
}
