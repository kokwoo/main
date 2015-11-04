package Tempo.CalendarObjects;

public class CalendarObject implements Comparable<CalendarObject> {
	protected String name;
	
	private static final int ZERO = 0;
	
	public CalendarObject(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getIndex() {
		return ZERO;
	}
	
	public int getSeriesIndex() {
		return ZERO;
	}
	
	public int compareTo(CalendarObject obj) {
		return ZERO;
	}
	
}
