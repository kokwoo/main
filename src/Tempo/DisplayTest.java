package Tempo;


import java.text.ParseException;
import java.util.ArrayList;


public class DisplayTest {
	
	public static void main(String args[]) throws ParseException {
		ArrayList<Event> events = new ArrayList<Event>();
		ArrayList<Task> tasks = new ArrayList<Task>();
		ArrayList<FloatingTask> floatingTasks = new ArrayList<FloatingTask>();

		Display dp = new Display(events, tasks, floatingTasks);
		
		Event newEvent = new Event(1, "Wedding", "07/10/2015", "14:00", "10/10/2015", "16:00");
		Event newEvent1 = new Event(1, "Meeting", "7/10/2015", "14:00", "12/06/2015", "16:00");
		Event newEvent2 = new Event(1, "birthday party", "10/06/2015", "14:00", "12/06/2015", "16:00");
		Event newEvent3 = new Event(1, "clubbing", "10/06/2015", "14:00", "12/06/2015", "16:00");
		
		events.add(newEvent);
		events.add(newEvent1);
		events.add(newEvent2);
		events.add(newEvent3);
		
		Task newTask = new Task(1, "Cs2103 display", "07/10/2015");
	//	Task newTask1 = new Task("Cs2103 display", "10/06/2015");
		Task newTask2 = new Task(1, "Cs2103 display", "10/11/2015");
	//	Task newTask3 = new Task("Cs2103 display", "10/06/2015");
	//	Task newTask4 = new Task("Cs2103 display", "10/06/2015");
		tasks.add(newTask);
	//	tasks.add(newTask1);
		tasks.add(newTask2);
	//	tasks.add(newTask3);
	//	tasks.add(newTask4);
		
		FloatingTask newFT = new FloatingTask(1, "DO DISPLAY");
	//	FloatingTask newFT1 = new FloatingTask("DO DISPLAY");
	//	FloatingTask newFT2 = new FloatingTask("DO DISPLAY");
		floatingTasks.add(newFT);
	//	floatingTasks.add(newFT1);
	//	floatingTasks.add(newFT2);
		
		
	/*	
	 * CurrentDateAndTime date = new CurrentDateAndTime();
		System.out.println(date.getDay());
		System.out.println(date.getMonth());
		System.out.println(date.getYear());
		System.out.println(date.getHour());
		System.out.println(date.getMinute());
		System.out.println(date.getSecond());
		*/
		
		//dp.missedTasks();
		//dp.all();
		//dp.events();
		//dp.tasks();
		//dp.undoneTasks();
		//dp.today();
		//dp.upcomingEvents();		
		
	//	CurrentDateAndTime date = new CurrentDateAndTime();
	//	date.Date();
	}
	

}
