package Tempo;

import java.util.ArrayList;

public class CalendarTest {
	public static void main(String args[]){
//		Event e1 = new Event("Event 1!", "26/08/2015", "10:00", "26/08/2015", "13:00");
//		Event e2 = new Event("Event 2!", "26/08/2015", "13:00", "26/08/2015", "15:00");
//		Task t = new Task("Task 1!", "17/08/2014");
//		
//		System.out.println(e1.getName());
//		System.out.println(e1.getStartDateTime());
//		System.out.println(e1.getEndDateTime());
//		System.out.println(e1.getStartTime());
//		System.out.println(e1.getEndTime());
//		System.out.println(e1.getStartTimeInMilli());
//		System.out.println(e1.getEndTimeInMilli());
//		
//		System.out.println(e2.getName());
//		System.out.println(e2.getStartDateTime());
//		System.out.println(e2.getEndDateTime());
//		System.out.println(e2.getStartTime());
//		System.out.println(e2.getEndTime());
//		System.out.println(e2.getStartTimeInMilli());
//		System.out.println(e2.getEndTimeInMilli());
//		
//		System.out.println(t.getName());
//		System.out.println(t.getDueDate());
//		System.out.println(t.getDueDateTimeInMilli());
		
		Calendar cal = new Calendar();
		
		cal.addEvent("Event A", "26/08/2015", "10:00", "26/08/2015", "12:00");
		cal.addEvent("Event B", "26/08/2015", "11:00", "26/08/2015", "14:00");
		cal.addEvent("Event C", "26/08/2015", "12:00", "26/08/2015", "14:00");
		cal.addEvent("Event D", "26/08/2015", "09:00", "26/08/2015", "12:00");
		
		cal.addTask("I'm not urgent at all", "26/08/2020");
		cal.addTask("I'm very urgent", "27/08/2015");
		cal.addTask("I'm super urgent", "26/08/2015");
		cal.addTask("I'm not so urgent", "28/08/2015");
		
		cal.addFloatingTask("Task H");
		cal.addFloatingTask("Task E");
		cal.addFloatingTask("Task L");
		cal.addFloatingTask("Task L");
		cal.addFloatingTask("Task O");
		
		ArrayList<Event> events = cal.getEventsList();
		ArrayList<Task> tasks = cal.getTasksList();
		ArrayList<FloatingTask> floatingTasks = cal.getFloatingTasksList();
		
		for(Event e : events){
			System.out.println(e.getName());
		}
		
		for(Task t : tasks){
			System.out.println(t.getName());
		}
		
		for(FloatingTask f : floatingTasks){
			System.out.println(f.getName());
		}
		
	}
}
