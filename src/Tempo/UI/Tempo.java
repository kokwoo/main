package Tempo.UI;

import java.text.*;
import java.util.*;

import Tempo.CalendarObjects.CalendarObject;
import Tempo.CalendarObjects.Task;
import Tempo.Commands.Result;
import Tempo.Logic.RequestHandler;

public class Tempo {
	private RequestHandler requestHandler = RequestHandler.getInstance();

	private static Scanner sc;
	private static boolean run;

	private static final String WELCOME_MESSAGE = "Welcome to Tempo! Type manual for instructions";
	private static final String GOOD_MORNING = "Good Morning! ";
	private static final String GOOD_AFTERNOON = "Good Afternoon! ";
	private static final String GOOD_EVENING = "Good evening! ";
	
	private static final String SUCCESSFUL_MESSAGE = "Command: '%1$s' was performed successfully.";
	private static final String UNSUCESSFUL_MESSAGE = "Command: '%1$s' was not performed succesfully. Please refer to the help menu.";
	
	private static final String KEY_TASKS_TODAY = "tasksToday";
	private static final String KEY_UPCOMING_TASKS = "upcomingTasks";
	private static final String KEY_MISSED_TASKS = "missedTasks";
	private static final String KEY_DONE_TASKS = "doneTasks";
	//UNDONE TASKS?
	private static final String KEY_EVENTS_TODAY = "eventsToday";
	private static final String KEY_UPCOMING_EVENTS = "upcomingEvents";
	private static final String KEY_PAST_EVENTS = "pastEvents";
	private static final String KEY_UNDONE_FLOATING = "undoneFloatingTasks";
	private static final String KEY_DONE_FLOATING = "doneFloatingTasks";
	
	private ArrayList<CalendarObject> tasksToday;
	private ArrayList<CalendarObject> upcomingTasks;
	private ArrayList<CalendarObject> missedTasks;
	private ArrayList<CalendarObject> doneTasks;
	private ArrayList<CalendarObject> eventsToday;
	private ArrayList<CalendarObject> upcomingEvents;
	private ArrayList<CalendarObject> pastEvents;
	private ArrayList<CalendarObject> undoneFloatingTasks;
	private ArrayList<CalendarObject> doneFloatingTasks;

	private static Tempo Tempo = new Tempo();

	private Tempo() {
		tasksToday = new ArrayList<CalendarObject>();
		upcomingTasks = new ArrayList<CalendarObject>();
		missedTasks = new ArrayList<CalendarObject>();
		doneTasks = new ArrayList<CalendarObject>();
		eventsToday = new ArrayList<CalendarObject>();
		upcomingEvents = new ArrayList<CalendarObject>();
		pastEvents = new ArrayList<CalendarObject>();
		undoneFloatingTasks = new ArrayList<CalendarObject>();
		doneFloatingTasks = new ArrayList<CalendarObject>();
	}

	private Tempo getInstance() {
		return Tempo;
	}

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Please enter a filename! :)");
			System.out.println("Usage: java Tempo <filename>");
			System.exit(0);
		}
		Tempo tempo = Tempo.getInstance();
		tempo.run(args[0]);
	}

	private void run(String fileName) {
		printWelcomeMsg();
		System.out.println(requestHandler.initialize(fileName));
		sc = new Scanner(System.in);

		run = true;

		while (run) {
			Result result = listenForInput();
			
			if(result.getIsSuccess()){
				clearAllLists();
				System.out.println(String.format(SUCCESSFUL_MESSAGE, result.getCommandPerformed()));
				HashMap<String, ArrayList<CalendarObject>> results = result.getResults();
				
				tasksToday = results.get(KEY_TASKS_TODAY);
				upcomingTasks = results.get(KEY_UPCOMING_TASKS);
				missedTasks = results.get(KEY_MISSED_TASKS);
				doneTasks = results.get(KEY_DONE_TASKS);
				eventsToday = results.get(KEY_EVENTS_TODAY);
				upcomingEvents = results.get(KEY_UPCOMING_EVENTS);
				pastEvents = results.get(KEY_PAST_EVENTS);
				undoneFloatingTasks = results.get(KEY_UNDONE_FLOATING);
				doneFloatingTasks = results.get(KEY_DONE_FLOATING);
				
				
			}else{
				System.out.println(String.format(UNSUCESSFUL_MESSAGE, result.getCommandPerformed()));
			}
		}
	}

	private void printWelcomeMsg() {
		printGreetings();
		printDate();
		System.out.println(WELCOME_MESSAGE);
	}

	private void printGreetings() {
		DateFormat df = new SimpleDateFormat("HH");

		String timeString = df.format(getTime());

		int hour = Integer.parseInt(timeString);

		if (hour > 4 && hour <= 12) {
			System.out.print(GOOD_MORNING);
		} else if (hour > 12 && hour <= 16) {
			System.out.print(GOOD_AFTERNOON);
		} else {
			System.out.print(GOOD_EVENING);
		}
	}

	private void printDate() {
		DateFormat df = new SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm");
		System.out.println("Today's date : " + df.format(getTime()));
	}

	private Date getTime() {
		return new Date();
	}

	private Result listenForInput() {
		String input = sc.nextLine();
		
		if(!input.equals("")){
			return requestHandler.processCommand(input);
	
		}else{
			return listenForInput();
		}
	}
	
	private void clearAllLists(){
		tasksToday = null;
		upcomingTasks = null;
		missedTasks = null;
		doneTasks = null;
		eventsToday = null;
		upcomingEvents = null;
		pastEvents = null;
		undoneFloatingTasks = null;
		doneFloatingTasks = null;
	}
	
}
