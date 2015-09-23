package Tempo;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Task extends FloatingTask implements Comparable<Task>{
	protected Date _dueDate;
	
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
}
