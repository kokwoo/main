package Tempo;

import java.util.*;

public class RequestHandler {

	//global variable
	ArgParser parser;
	
	// private final String MSG_CMD_NOT_VALID = "Why don't you try entering an actual command?";
	private final String MSG_ARG_NOT_VALID = "Why don't you try entering an actual argument?";
	private final String CMD_ADD = "add";
	private final String CMD_REMOVE = "remove";
	private final String CMD_UPDATE = "update";
	private final String CMD_DISPLAY = "display";
	private final String CMD_EXIT = "EXIT";
	private final String CMD_HELP = "help";
	private final String CMD_MANUAL = "manual";

	private final String[] VALID_COMMANDS = { CMD_ADD, CMD_REMOVE,CMD_EXIT,CMD_UPDATE};


	// display args
	private final String ARG_MANUAL = "manual";
	private final String ARG_EVENTS = "events";
	private final String ARG_UPCOMING_EVENTS = "upcoming events";
	private final String ARG_TASKS = "tasks";
	private final String ARG_UNDONE_TASKS = "undone tasks";
	private final String ARG_MISSED_TASKS = "missed tasks";

	// display based on days
	private final String ARG_TODAY = "today";
	private final String ARG_WEEK = "week";

	// such args list out all the events and tasks
	private final String ARGS_ALL = "all";
	
	public RequestHandler() {
		parser = new ArgParser();
	}
	
	public String readNextCommand() {
		String cmd;
		do {
			Scanner sc = new Scanner(System.in);
			String nextCommand = sc.nextLine();
			
			cmd = parser.getCommand(nextCommand);
			String args = parser.getArguments(nextCommand);
			
			execute(cmd, args);

			if(nextCommand.equals("exit")) {
				sc.close();
			}


		} while (isValidCommand(cmd));
		return cmd;	
	}

	public void execute(String command, String arguments) throws IllegalArgumentException {
		if (!isValidInput(command)) {
			// TODO: invalid input action
		}
		switch (command) {
			case CMD_ADD:
				add(arguments);
			case CMD_REMOVE:
				remove(arguments);
			case CMD_UPDATE:
				update(arguments);
			case CMD_DISPLAY:
				//display(arguments); lol idk what to call here
			case CMD_EXIT:
				exit();
			default:
				// TODO: Tell user it is an invalid command
				exit();
		}
	}

	private void add(String arguments) {
		if (parser.isEvent(arguments)) {
			String name = parser.getName(arguments);
			String startDate = parser.getEventStartDate(arguments);
			String startTime = parser.getEventStartTime(arguments);
			String endDate = parser.getEventEndDate(arguments);
			String endTime = parser.getEventEndTime(arguments);
			// TODO: Call addEvent in Calendar 
		} else if (parser.isFloatingTask(arguments)) {
			String name = parser.getName(arguments);
			// TODO: Call addFloatingTask in Calendar
		} else {
			String name = parser.getName(arguments);
			String dueDate = parser.getTaskDueDate(arguments);
			// TODO: Call addTask in Calendar
		}
	}
	
	private void remove(String arguments) {
		int id = parser.getId(arguments);
		// TODO: Call remove in Calendar
	}

	private void update(String arguments) {	
		int id = parser.getId(arguments);
		ArrayList<String> fields = parser.getFieldsList(arguments);
		ArrayList<String> newValues = parser.getNewValuesList(arguments);
		// TODO: Call update in Calendar
	}

	private boolean display(String command, String[] arguments) {
		String args = arguments.toString().toLowerCase();
		
		if (command.equalsIgnoreCase(CMD_HELP) | command.equalsIgnoreCase(CMD_MANUAL)) {
			Display.manual();
		}

		switch (args) {
			case (ARG_MANUAL) :
				Display.manual();
				break;
			case (ARG_EVENTS) :
				
			case (ARG_TASKS) :
				
			case (ARG_UPCOMING_EVENTS) :
				
			case (ARG_UNDONE_TASKS) :
				
			case (ARG_MISSED_TASKS) :
				
			case (ARG_TODAY) :
				
			case (ARGS_ALL) :
				calendar.display(args);
				break;
			default :
				System.out.println(MSG_ARG_NOT_VALID);
				return false;
		}
		
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
