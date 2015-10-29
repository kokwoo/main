package Tempo;

public class testDisplay {

	public static void main(String[] args) {
		Event newEvent = new Event(1, "Wedding", "07/10/2015/14:00", "10/10/2015/16:00");		
	  //  Event newEvent1 = new Event(1, "Meeting", "7/10/2015", "14:00", "12/06/2015", "16:00");		
	///	Event newEvent2 = new Event(1, "birthday party", "10/06/2015", "14:00", "12/06/2015", "16:00");		
	//	Event newEvent3 = new Event(1, "clubbing", "10/06/2015", "14:00", "12/06/2015", "16:00");
		
		CurrentDateAndTime date = new CurrentDateAndTime();
		
		System.out.println(date.getDate());
		
		System.out.println(newEvent.getStartDate());

		System.out.println(newEvent.getStartTime());
		
		System.out.println(newEvent.getIndex());
		
		
	}

}
