package Tempo;

public class FloatingTask{
	protected String _name;
	protected boolean _done;
	protected int _index;
	
	public FloatingTask(int index, String name){
		_name = name;
		_done = false;
	}
	
	public FloatingTask(int index, String name, String done) {
		_name = name;
		_done = Boolean.parseBoolean(done);
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
	
	public boolean hasDueDate() {
		return false;
	}
	
	public String toString(){
		String delimeter = "!!";
		return getName() + delimeter + getDone();
	}
}
