package Tempo;

import java.util.HashMap;

public class AddEvent implements UserObject{
	HashMap <String,String>eventInformation;
	private String eventName;
	private String[] arguments;
	public HashMap<String, String> getEventInformation() {
		return eventInformation;
	}


	public void setEventInformation(HashMap<String, String> eventInformation) {
		this.eventInformation = eventInformation;
	}


	public String getEventName() {
		return eventName;
	}


	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	
	public AddEvent(String arr[]) {
	//   0    1     2     3       4       5     6         7        8   9     10
	//add event <name> from <start date> at <start time> to <end date> at <end time>
		//
		 //  0  1    2       3   4      5  6  7     8     9 10  
		//add event MYEVENT from TODAY at 12 to TOMORROW at 1
	eventName = arr[1];
	startDate = arr[3];
	startTime = arr[5];
	endDate = arr[7];
	endTime = arr[9];
	
/*	System.out.println("event name " + eventName);
	System.out.println("start date " + startDate);
	System.out.println("start time " + startTime);
	System.out.println("end date" + endDate);
	System.out.println("end time " + endTime);	*/
	arguments = arr;
	
	}


	@Override
	public String[] getArguments() {
		return arguments;
	}


	@Override
	public String getType() {
		return "add event";
	}


	
}
