package Tempo;

public class FloatingTask{
	protected String _name;
	protected boolean _done;
	protected int _index;
	
	private static final String DELIMETER = "!!";
	
	public FloatingTask(int index, String name){
		_index = index;
		_name = name;
		_done = false;
	}
	
	public FloatingTask(int index, String name, String done) {
		_index = index;
		_name = name;
		_done = Boolean.parseBoolean(done);
	}
	
	public void update(String field, String newValue) {
		if (field.equals("name")) {
			setName(newValue);
		}
	}
	
	public void setName(String newName) {
		_name = newName;
	}
	
	public int getIndex() {
		return _index;
	}

	public String getName() {
		return _name;
	}

	public boolean getDone() {
		return _done;
	}

	public void markAsDone() {
		_done = true;
	}
	
	public boolean isFloatingTask() {
		return true;
	}
	
	public String toString(){
		return getIndex() + DELIMETER + getName() + DELIMETER + getDone();
	}
}
