package Tempo;

import java.text.*;
import java.util.*;

public class Event implements Comparable<Event>{
	protected String _name;
	protected Date _startDateTime;
	protected Date _endDateTime;
	protected int _index;
	
	private static final String DELIMETER = "!!";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy/HH:mm");

	
	public Event(int index, String name, String startDate, String startTime, String endDate, String endTime){
		_index = index;
		_name = name;		
		setStartDateTime(startDate, startTime);
		setEndDateTime(endDate, endTime);
	}
	
	public void update(String field, String newValue) {
		switch(field) {
			case "name": 
				setName(newValue);
				break;
			case "start date":
				setStartDate(newValue);
				break;
			case "start time":
				setStartTime(newValue);
				break;
			case "end date":
				setEndDate(newValue);
				break;
			case "end time":
				setEndTime(newValue);
				break;
		}
	}
	
	private void setName(String newName) {
		_name = newName;
	}
	
	private void setStartDateTime(String startDate, String startTime) {
		String startDateTimeString = startDate + "/" + startTime;
		
		try {
			_startDateTime = dateFormatter.parse(startDateTimeString);
		} catch (ParseException e) {
			System.out.println("Unable to format Start Date/Time!");
		}
	}
	
	private void setStartDate(String newStartDate) {
		String startTime = getStartTime();
		setStartDateTime(newStartDate, startTime);
	}
	
	private void setStartTime(String newStartTime) {
		String startDate = getStartDate();
		setStartDateTime(startDate, newStartTime);
	}
	
	private void setEndDateTime(String endDate, String endTime) {
		String endDateTimeString = endDate + "/" + endTime;
		
		try {
			_endDateTime = dateFormatter.parse(endDateTimeString);
		} catch (ParseException e) {
			System.out.println("Unable to format End Date/Time!");
		}
	}
	
	private void setEndDate(String newEndDate) {
		String endTime = getEndTime();
		setEndDateTime(newEndDate, endTime);
	}
	
	private void setEndTime(String newEndTime) {
		String endDate = getEndDate();
		setEndDateTime(endDate, newEndTime);
	}
	
	public int getIndex() {
		return _index;
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
	
	public String toString(){
		return getIndex() + DELIMETER + getName() + DELIMETER + getStartDate() + DELIMETER + getStartTime() + DELIMETER + getEndDate() + DELIMETER + getEndTime(); 
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
