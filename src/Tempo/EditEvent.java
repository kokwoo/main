package Tempo;

import java.util.Arrays;

public class EditEvent {
	int eventid;
	public int getEventid() {
		return eventid;
	}

	public void setEventid(int eventid) {
		this.eventid = eventid;
	}

	public String getEventname() {
		return eventname;
	}

	public void setEventname(String eventname) {
		this.eventname = eventname;
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

	String eventname;
	String startDate;
	String startTime;
	String endDate;
	String endTime;
	public EditEvent(String args[]) {
		
		

		
		String[] subArray = new String[4];
		System.arraycopy(args, 7, subArray, 0, 4);
			
		
		
		//edit event 1 start from sdate at stime to edate at etime
		
		int indexName = getIndexOf(args,"name") + 1;
		this.eventname = args[indexName];
		//<KEY> event <id> <name> from <start date> at <start time> to <end date> at <end time>
		int indexStartDate = getIndexOf(args,"from") + 1;
		this.startDate = args[indexStartDate];
		
		int indexStartTime = getIndexOf(args,"at") + 1;
		this.startTime = args[indexStartTime];
		
		int indexEndDate = getIndexOf(subArray,"to") + 1;
		this.endDate = subArray[indexEndDate];
		
		int indexEndTime = getIndexOf(subArray,"at") + 1;
		this.endTime = subArray[indexEndTime];
		
		//System.out.println("indexEndDate : " + this.endDate);
		
		//int indexEndTime = getIndexOf(args,"to")
/*
 * <KEY> event <id> <name> from <start date> at <start time> to <end date> at <end time>	
 *  edit event 1 start from sdate at stime to edate at etime
 */
		
		
	}
	
	public int getIndexOf(String args[],String key) {
		int value = -1;
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals(key)) {
				value = i;
				break;
			}
		}
	return value;
	}
	
}
