package Tempo;

import java.util.*;

public class RequestHandler {
	//global variable
	
	//private final String MSG_CMD_NOT_VALID = "Why don't you try entering an actual command?";
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
	
	//display based on days
	private final String ARG_TODAY = "today";
	private final String ARG_WEEK = "week";
	
	// such args list out all the events and tasks
	private final String ARGS_ALL = "all";
	
	public String readNextCommand() {
		String cmd;
		do {
			Scanner sc = new Scanner(System.in);
			String nextCommand = sc.nextLine();
			
			ArgParser parser = new ArgParser();
			cmd = parser.getAction(nextCommand);
			String args[] = parser.getArguments(nextCommand);
			
			execute(cmd, args);

			if(nextCommand.equals("exit")) {
				sc.close();
			}


		} while (isValidCommand(cmd));
		return cmd;	
	}

	/**
	 * Takes in command, and executes an event bsaed on given arguments
	 * 
	 * @param command
	 * @param arguments
	 * @return success
	 */
	public boolean execute(String command, String[] arguments) throws IllegalArgumentException {
		if (!isValidInput(command)) {
			return false;
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

	/**
	 * FOR ALL THESE METHODS: YOU GET PASSED : <COMMAND, ARGUMENTS>. THE
	 * ARUGMENTS ARE ESSENTIALLY WHATEVER THE USER INPUTS AFTER ADD,DELETE,
	 * WHATEVER KEYWORD. REFER TO THE MANUAL. ADD , EVENTID,DATE,ETC,DESCRIPTION
	 * DELETE, EVENTID,DAT
	 * 
	 * @param command
	 * @param arguments
	 */
	private void add(String[] arguments) {
	}
	
	private void remove(String[] arguments) {

	}

	private void update(String[] arguments) {
		
	}

	private boolean display(String command, String arguments) {
		String fileName = null;
		Display display = new Display(fileName);
		if(command.equalsIgnoreCase(CMD_HELP) | command.equalsIgnoreCase(CMD_MANUAL)){
			Display.manual();
		}
		
		switch(arguments.toLowerCase()){
		case(ARG_MANUAL):
			Display.manual();
		case(ARG_EVENTS):
			display.events();
		case(ARG_TASKS):
			display.tasks();
		case(ARG_UPCOMING_EVENTS):
			display.upcomingEvents();
		case(ARG_UNDONE_TASKS):
			display.undoneTasks();
		case(ARG_MISSED_TASKS):
			display.missedTasks();
		case(ARG_TODAY):
			display.today();
		case(ARG_WEEK):
			display.week();
		case(ARGS_ALL):
			display.all();
		default:
			System.out.println(MSG_ARG_NOT_VALID);
			return false;
			
			
			
		}
	}
		
	

	/*
	 * Command to add event to calender
	 */
	//private boolean addEvent() {
	//	return false;
	//}

	/*
	 * Exists application.
	 */
	private void exit() {
		System.exit(0);
	}

	/**
	 * Checks to make sure computer input is correct
	 * 
	 * @param command
	 * @return true if command is valid
	 */
	private boolean isValidInput(String command) {
		System.out.println(command);
		System.out.println(Arrays.toString(VALID_COMMANDS));
		return isInArray(VALID_COMMANDS, command);
	}

	/**
	 * Checks to make sure user input is correct. If not, displays message to
	 * console.
	 * 
	 * @param command
	 * @return true if command is valid
	 */
	private boolean isValidCommand(String command) {
		return command != null;
	}

	/**
	 * @param array
	 * @param text
	 * @return true if text exists in array
	 */
	private boolean isInArray(String[] array, String text) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equalsIgnoreCase((text))) {
				return true;
			}
		}
		return false;
	}
}
