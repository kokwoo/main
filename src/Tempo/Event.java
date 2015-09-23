package Tempo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event implements Comparable<Event>{
	protected String _name;
	protected Date _startDateTime;
	protected Date _endDateTime;
	
	
	public Event(String name, String startDate, String startTime, String endDate, String endTime){
		_name = name;
		
		String startDateTimeString = startDate + "/" + startTime;
		String endDateTimeString = endDate + "/" + endTime;
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
		
		try {
			_startDateTime = formatter.parse(startDateTimeString);
		} catch (ParseException e) {
			System.out.println("Unable to format Start Date/Time!");
		}
		
		try {
			_endDateTime = formatter.parse(endDateTimeString);
		} catch (ParseException e) {
			System.out.println("Unable to format End Date/Time!");
		}
	}
	
	public String getName(){
		return _name;
	}
	
	public String getStartDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(_startDateTime);
	}
	
	public String getStartTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(_startDateTime);
	}
	
	public String getStartDateTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm");
		return formatter.format(_startDateTime);
	}
	
	public String getEndDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(_endDateTime);
	}
	
	public String getEndTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(_endDateTime);
	}
	
	public String getEndDateTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm");
		return formatter.format(_endDateTime);
	}
	
	public long getStartTimeInMilli(){
		return _startDateTime.getTime();
	}
	
	public long getEndTimeInMilli(){
		return _endDateTime.getTime();
	}
	
	public int compareTo(Event e){
		if(this.getStartTimeInMilli() < e.getStartTimeInMilli()){
			return -1;
		}else if(this.getStartTimeInMilli() == e.getStartTimeInMilli()){
			return 0;
		}else if(this.getStartTimeInMilli() > e.getStartTimeInMilli()){
			return 1;
		}
		return 0;
	}
	
//	public boolean clashesWith(Event e){
//		if(this.getStartTimeInMilli() < e.getEndTimeInMilli()){
//			return true;
//		}else if(this.getEndTimeInMilli() > e.getStartTimeInMilli()){
//			return true;
//		}else{
//			return false;
//		}
//	}
}
