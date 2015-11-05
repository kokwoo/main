package Tempo.UI;

import java.util.Date;

public class TableEntry {
	protected String name;
	protected String done;
	protected String startDate;
	protected String startTime;
	protected String endDate;
	protected String endTime;
	protected int index;
	
	public TableEntry() {
		
	}

	public TableEntry(int index, String name, String done,String startDate, String startTime, String endDate, String endTime) {
		this.index = index;
		this.name = name;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
		System.out.println("d; " + done);
		if(done.equalsIgnoreCase("true")) {
			this.done = "complete.";
		}
		else if(done.equalsIgnoreCase("false")) {
			System.out.println("FALSE");
			this.done = "undone";
		}
		else {
			this.done = " ";
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setDone(boolean done) {
		if(done) {
			this.done = "Yes";
		}
		else {
			this.done = "No";
		}
	}
  public String getDone() {
	  return this.done;
  }
}
