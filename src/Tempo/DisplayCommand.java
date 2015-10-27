//package Tempo;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//
//public class DisplayCommand implements Command{
//	private Display display;
//	private String displayType;
//	
//	// display arguments
//	private final String ARG_EVENTS = "events";
//	private final String ARG_UPCOMING_EVENTS = "upcoming events";
//	private final String ARG_TASKS = "tasks";
//	private final String ARG_UNDONE_TASKS = "undone tasks";
//	private final String ARG_MISSED_TASKS = "missed tasks";
//	private final String ARG_TODAY = "today";
//	private final String ARGS_ALL = "all";
//	
//	private final String DISPLAY_ERROR = "Error: The command entered is invalid!";
//	
//	public DisplayCommand(Display display, String displayType){
//		this.display = display;
//		this.displayType = displayType;
//	}
//	
//	public ArrayList<String> execute() {
//		switch (displayType) {
//		case (ARG_EVENTS):
//			return display.events();
//		break;
//		case (ARG_TASKS):
//			return display.tasks();
//		break;
//		case (ARG_UPCOMING_EVENTS):
//			return display.upcomingEvents();
//		break;
//		case (ARG_UNDONE_TASKS):
//			return display.undoneTasks();
//		break;
//		case (ARG_MISSED_TASKS):
//			return  display.missedTasks();
//		break;
//		case (ARG_TODAY):
//			return display.today();
//		break;
//		case (ARGS_ALL):
//			return display.all();
//		break;
//		default:
//			ArrayList<String> returnList = new ArrayList<String>();
//			returnList.add(DISPLAY_ERROR);
//			return returnList;
//			
//		}
//	}
//}
