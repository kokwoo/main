package Tempo;

import java.util.*;

public class FloatingTask{
	protected String _name;
	protected boolean _done;
	
	public FloatingTask(String name){
		_name = name;
		_done = false;
	}
	
	public FloatingTask(String name, String done) {
		_name = name;
		_done = Boolean.parseBoolean(done);
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
	
	public String toString(){
		String delimeter = "!!";
		return getName() + delimeter + getDone();
	}
}
