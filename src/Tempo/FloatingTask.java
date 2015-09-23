package Tempo;

public class FloatingTask{
	protected String _name;
	protected boolean _done;

	public FloatingTask(String name) {
		_name = name;
		_done = false;
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
}
