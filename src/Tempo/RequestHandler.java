package Tempo;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RequestHandler {

	// global variable
	ArgParser parser;
	Calendar calendar;

	// private final String MSG_CMD_NOT_VALID = "Why don't you try entering an
	// actual command?";
	private final String MSG_ARG_NOT_VALID = "Invalid command! Please refer to help";
	private final String CMD_ADD = "add";
	private final String CMD_REMOVE = "remove";
	private final String CMD_UPDATE = "update";
	private final String CMD_DISPLAY = "display";
	private final String CMD_EXIT = "EXIT";
	private final String CMD_HELP = "help";
	private final String CMD_MANUAL = "manual";
	private final String CMD_SEARCH = "search";
	private final String CMD_UNDO = "undo";

	private final String[] VALID_COMMANDS = { CMD_ADD, CMD_REMOVE, CMD_EXIT, CMD_UPDATE };

	// display args
	private final String ARG_MANUAL = "manual";
	private final String ARG_EVENTS = "events";
	private final String ARG_UPCOMING_EVENTS = "upcoming events";
	private final String ARG_TASKS = "tasks";
	private final String ARG_UNDONE_TASKS = "undone tasks";
	private final String ARG_MISSED_TASKS = "missed tasks";
	// private final String ARG_SEARCH = "search";

	// display based on days
	private final String ARG_TODAY = "today";
	// private final String ARG_WEEK = "week";

	// such args list out all the events and tasks
	private final String ARGS_ALL = "all";

	private final String MSG_SEARCH_RESULTS = "These are your search results";
	private final String MSG_NO_SEARCH_RESULTS = "We are unable to match any of your search";

	//Logger logger;
	//FileHandler fh;

	public RequestHandler(String fileName) {
		parser = new ArgParser();
		calendar = Calendar.getInstance();
		calendar.createFile(fileName);


	/*	assert parser != null;
		assert calendar != null;

		// This block configure the logger with handler and formatter
		try {
			logger = Logger.getLogger("TempoLog");

			fh = new FileHandler("TempoLog.log", true);
			logger.addHandler(fh);

			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public String readNextCommand() {
		String cmd;
		do {
			Scanner sc = new Scanner(System.in);
			String nextCommand = sc.nextLine();

			assert !nextCommand.equals("");
			//logger.info("User Entered: " + nextCommand);

			if (nextCommand.equals("exit")) {
				sc.close();
				System.out.println("Thanks for using Tempo! :)");
				System.exit(0);
			}

			cmd = parser.getCommand(nextCommand);
			String args = parser.getArguments(nextCommand);

			execute(cmd, args);

		} while (isValidCommand(cmd));
		return cmd;
	}

	public void execute(String command, String arguments) throws IllegalArgumentException {
		// I think if the command is invalid can go straight down to the default
		// switch case
		// aka display error message :/
		// if (!isValidInput(command)) {
		// // TODO: invalid input action
		// }
		switch (command) {
		case CMD_ADD:
			add(arguments);
			break;
		case CMD_REMOVE:
			remove(arguments);
			break;
		case CMD_UPDATE:
			update(arguments);
			break;
		case CMD_DISPLAY:
			display(command, arguments);
			break;
		case CMD_SEARCH:
			search(arguments);
			break;
		case CMD_UNDO:
			undo(arguments);
			break;
		case CMD_EXIT:
			exit();
			break;
		default:
			display(command, arguments);
			break;
		// exit();
		}
	}

	private void search(String argument) {
		ArrayList<String> wordFoundLines = new ArrayList<String>();
		ArrayList<String> idFoundLines = new ArrayList<String>();
		if(parser.containsId(argument)){
			int id = parser.getIdToSearch(argument);
			idFoundLines = calendar.searchId(id);
			printSearchResults(idFoundLines);
			return;
		}
		
		else{
		String keyword = argument;
		wordFoundLines = calendar.searchKeyWord(keyword);
		printSearchResults(wordFoundLines);
		}
	}

	private void printSearchResults(ArrayList<String> foundLines) {

		if (foundLines.isEmpty()) {
			System.out.println(MSG_NO_SEARCH_RESULTS);
		}

		else {
			System.out.println(MSG_SEARCH_RESULTS);
			for (int i = 0; i < foundLines.size(); i++) {
				System.out.println(foundLines.get(i));
			}
		}
	}

	private void undo(String arguments) {
		if (!arguments.equals("")) {
			// error handling TODO:
		}
		
		calendar.undo();
	}
	
	// WORKING
	private void add(String arguments) {
		if (parser.isEvent(arguments)) {
			String name = parser.getName(arguments);
			String startDate = parser.getEventStartDate(arguments);
			String startTime = parser.getEventStartTime(arguments);
			String endDate = parser.getEventEndDate(arguments);
			String endTime = parser.getEventEndTime(arguments);

			// System.out.println("Passing into addEvent: " + name + " " +
			// startDate + " " + startTime + " " + endDate + " " + endTime);

			calendar.addEvent(name, startDate, startTime, endDate, endTime);
		} else if (parser.isFloatingTask(arguments)) {
			String name = parser.getName(arguments);

			calendar.addFloatingTask(name);
		} else {
			String name = parser.getName(arguments);
			String dueDate = parser.getTaskDueDate(arguments);

			calendar.addTask(name, dueDate);
		}
	}

	// WORKING
	private void remove(String arguments) {
		int id = parser.getId(arguments);

		calendar.remove(id);
	}

	private void update(String arguments) {
		int id = parser.getId(arguments);
		ArrayList<String> fields = parser.getFieldsList(arguments);
		ArrayList<String> newValues = parser.getNewValuesList(arguments);

		calendar.update(id, fields, newValues);
	}

	private void display(String command, String displayType) {

		Display display = new Display(calendar.getEventsList(), calendar.getTasksList(),
				calendar.getFloatingTasksList());

		if (command.equalsIgnoreCase(CMD_HELP) | command.equalsIgnoreCase(CMD_MANUAL)) {
			Display.manual();
		}

		switch (displayType) {
		case (ARG_EVENTS):
			display.events();
			break;
		case (ARG_TASKS):
			display.tasks();
			break;
		case (ARG_UPCOMING_EVENTS):
			display.upcomingEvents();
			break;
		case (ARG_UNDONE_TASKS):
			display.undoneTasks();
			break;
		case (ARG_MISSED_TASKS):
			display.missedTasks();
			break;
		// case (ARG_SEARCH):
		// display.search();
		// break;
		case (ARG_TODAY):
			try {
				display.today();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case (ARGS_ALL):
			try {
				display.all();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			System.out.println(MSG_ARG_NOT_VALID);
			break;
		}
	}

	///////////////////////////////////////////////////////
	/*
	 * String args = arguments.toString().toLowerCase();
	 * 
	 * if (command.equalsIgnoreCase(CMD_HELP) |
	 * command.equalsIgnoreCase(CMD_MANUAL)) { Display.manual(); }
	 * 
	 * // this is not the best but it'll do for now ba.... switch (args) { case
	 * (ARG_MANUAL) : Display.manual(); break; case (ARG_EVENTS) :
	 * 
	 * case (ARG_TASKS) :
	 * 
	 * case (ARG_UPCOMING_EVENTS) :
	 * 
	 * case (ARG_UNDONE_TASKS) :
	 * 
	 * case (ARG_MISSED_TASKS) :
	 * 
	 * case (ARG_TODAY) :
	 * 
	 * case (ARGS_ALL) : calendar.display(args); break; default :
	 * System.out.println(MSG_ARG_NOT_VALID); break; }
	 */

	private void exit() {
		System.exit(0);
	}

	private boolean isValidInput(String command) {
		System.out.println(command);
		System.out.println(Arrays.toString(VALID_COMMANDS));
		return isInArray(VALID_COMMANDS, command);
	}

	private boolean isValidCommand(String command) {
		return command != null;
	}

	private boolean isInArray(String[] array, String text) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equalsIgnoreCase((text))) {
				return true;
			}
		}
		return false;
	}
}
