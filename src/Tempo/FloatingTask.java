package Tempo;

import java.util.*;

public class FloatingTask{
	protected String _name;
	protected boolean _done;
	protected int _index;
	public static int nextHigherIndex = 0;
	public static LinkedList<Integer> recycledIndex = new LinkedList<Integer>();
	
	public FloatingTask(int index, String name){
		_name = name;
		_done = false;
	}
	
	public FloatingTask(int index, String name, String done) {
		_name = name;
		_done = Boolean.parseBoolean(done);
	}
	
	public int getIndexForNewEvent() {
		int index;
		
		if (recycledIndex.isEmpty()) {
			index = nextHigherIndex;
			nextHigherIndex++;
		} else {
			index = recycledIndex.remove();
		}
		
		return index;
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
	
	public String toString(){
		String delimeter = "!!";
		return getName() + delimeter + getDone();
	}
}
