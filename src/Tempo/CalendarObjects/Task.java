package Tempo.CalendarObjects;

import java.util.*;
import java.text.*;

public class Task extends FloatingTask implements Comparable<Task> {
	protected Date _dueDate;
	private static final String DELIMETER = "!!";
	
	public Task(int index, int seriesIndex, String name, String done, String dueDateString){
		super(index, seriesIndex, name, done);
		setDueDate(dueDateString);
	}
	
	public Task(int index, int seriesIndex, String name, String dueDateString) {
		super(index, seriesIndex, name);
		setDueDate(dueDateString);
	}
	
	private void setDueDate(String dueDateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			_dueDate = formatter.parse(dueDateString);
		} catch (ParseException e) {
			System.out.println("Unable to format date");
		}
	}
	
	@Override
	public void update(String field, String newValue) {
		switch(field) {
			case "name": 
				super.setName(newValue);
				break;
			case "due":
				setDueDate(newValue);
				break;
		}
	}
	
	@Override
	public boolean isFloatingTask() {
		return false;
	}
		
	public String getDueDate(){
		//SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy");
		return formatter.format(_dueDate);
	}
	
	public String getDueDateSimplified(){
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
		return super.toString() + DELIMETER + getDueDateSimplified();
	}
}
