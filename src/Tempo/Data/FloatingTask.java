//@@author A0125962B
package Tempo.Data;

public class FloatingTask implements CalendarObject{
	protected String name;
	protected boolean done;
	protected int index;
	protected int seriesIndex;
	
	private static final String DELIMETER = "!!";
	
	public FloatingTask(int index, String name, String done){
		this.name = name;
		this.index = index;
		this.name = name;
		this.done = false;
	}
	
	public FloatingTask(int index, int seriesIndex, String name){
		this.name = name;
		this.index = index;
		this.name = name;
		this.done = false;
	}
	
	public FloatingTask(int index, int seriesIndex, String name, String done) {
		this.name = name;
		this.index = index;
		this.seriesIndex = seriesIndex;
		this.done = Boolean.parseBoolean(done);
	}
	
	public void update(String field, String newValue) {
		if (field.equals("name")) {
			setName(newValue);
		}
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getSeriesIndex(){
		return seriesIndex;
	}

	public String getName() {
		return name;
	}

	public boolean isDone() {
		return done;
	}

	public void markAsDone() {
		done = true;
	}
	
	public void markAsUndone() {
		done = false;
	}
	
	public boolean isFloatingTask() {
		return true;
	}
	
	public String toString(){
		return getIndex() + DELIMETER + getSeriesIndex() + DELIMETER + getName() + DELIMETER + isDone();
	}
}
