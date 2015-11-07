package Tempo.CalendarObjects;

import java.text.*;
import java.util.*;

public class Event implements CalendarObject, Comparable<Event>{
	protected Date startDateTime;
	protected Date endDateTime;
	protected int index;
	protected int seriesIndex;
	protected String name;
	
	private static final String DELIMETER = "!!";
	private static final String DATE_DELIMETER = "/";
	private static final String PARAM_NAME = "name";
	private static final String PARAM_START_DATE = "start date";
	private static final String PARAM_START_TIME = "start time";
	private static final String PARAM_END_DATE = "end date";
	private static final String PARAM_END_TIME = "end time";
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
	
	public Event(int index, int seriesIndex, String name, String start, String end){
		this.name = name;
		this.index = index;
		this.seriesIndex = seriesIndex;		
		setStartDateTime(start.trim());
		setEndDateTime(end.trim());
		}
	
	public void update(String field, String newValue) {
		switch(field) {
			case PARAM_NAME: 
				setName(newValue);
				break;
			case PARAM_START_DATE:
				setStartDate(newValue);
				break;
			case PARAM_START_TIME:
				setStartTime(newValue);
				break;
			case PARAM_END_DATE:
				setEndDate(newValue);
				break;
			case PARAM_END_TIME:
				setEndTime(newValue);
				break;
		}
	}
	
	private void setName(String newName) {
		this.name = newName;
	}
	
	private void setStartDateTime(String start){
		try {
			startDateTime = dateFormatter.parse(start);
		} catch (ParseException e) {
			System.out.println("Unable to format Start Date/Time!");
		}
	}
	
	
	private void setStartDate(String newStartDate) {
		String startTime = getStartTime();
		String newStartDateTime = newStartDate + DATE_DELIMETER + startTime;
		setStartDateTime(newStartDateTime);
	}
	
	private void setStartTime(String newStartTime) {
		String startDate = getStartDate();
		String newStartDateTime = startDate + DATE_DELIMETER + newStartTime;
		setStartDateTime(newStartDateTime);
	}
	
	private void setEndDateTime(String end) {
		try {
			endDateTime = dateFormatter.parse(end);
		} catch (ParseException e) {
			System.out.println("Unable to format End Date/Time!");
		}
	}
	
	private void setEndDate(String newEndDate) {
		String endTime = getEndTime();
		String newEndDateTime = newEndDate + DATE_DELIMETER + endTime;
		setEndDateTime(newEndDateTime);
	}
	
	private void setEndTime(String newEndTime) {
		String endDate = getEndDate();
		String newEndDateTime = endDate + DATE_DELIMETER + newEndTime;
		setEndDateTime(newEndDateTime);
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getSeriesIndex(){
		return seriesIndex;
	}
	
	public String getName(){
		return name;
	}
	
	public String getStartDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(startDateTime);
	}
	
	public String getStartTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(startDateTime);
	}
	
	public String getStartDateTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm");
		return formatter.format(startDateTime);
	}
	
	public String getStartDateTimeSimplified(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
		return formatter.format(startDateTime);
	}
	
	public String getEndDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(endDateTime);
	}
	
	public String getEndTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(endDateTime);
	}
	
	public String getEndDateTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm");
		return formatter.format(endDateTime);
	}
	
	public String getEndDateTimeSimplified(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
		return formatter.format(endDateTime);
	}

	
	public long getStartTimeInMilli(){
		return startDateTime.getTime();
	}
	
	public long getEndTimeInMilli(){
		return endDateTime.getTime();
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
		return getIndex() + DELIMETER + getSeriesIndex()+ DELIMETER + getName() + DELIMETER + getStartDate() + DATE_DELIMETER + getStartTime() + DELIMETER + getEndDate()+ DATE_DELIMETER +getEndTime(); 
	}
	
	public boolean clashesWith(Event e){
		if(isBefore(e) || isAfter(e)){
			return false;
		}else{
			return true;
		}
	}
	
	private boolean isBefore(Event e) {
		return this.getEndTimeInMilli() <= e.getStartTimeInMilli();
	}
	
	private boolean isAfter(Event e) {
		return this.getStartTimeInMilli() >= e.getEndTimeInMilli();
	}
}
