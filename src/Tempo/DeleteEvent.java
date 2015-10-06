package Tempo;

public class DeleteEvent implements UserObject {
	int eventId;
	
	public DeleteEvent(String[] arguments) {
		int id =  Integer.parseInt(arguments[1]);
		this.eventId = id;
	}
	
	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
    public int getEventId() {
    	return this.eventId;
    }

}
