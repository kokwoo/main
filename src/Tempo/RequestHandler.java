package Tempo;

import java.util.Scanner;

public class RequestHandler {
	private  final String MSG_CMD_NOT_VALID = "Why don't you try entering an actual command?";
	private  final String CMD_ADD_EVENT = "add";
	private  final String CMD_DELETE_EVENT = "delete";
	private final String CMD_EDIT_EVENT = "edit";
	private final String CMD_DISPLAY_EVENT = "display";
	private  final String CMD_EXIT = "EXIT";
	private  final String[] VALID_COMMANDS = {CMD_ADD_EVENT,CMD_EXIT};
	public RequestHandler() {

	}
	/**
	 * Takes in command, and executes an event bsaed on given arguments
	 * @param command
	 * @param arguments
	 * @return success
	 */
	public boolean execute(String command, String arguments) throws IllegalArgumentException {
		if(!validInput(command)) {
			throw new IllegalArgumentException();
		}
		switch(command) {
		case CMD_ADD_EVENT:
			return add(command,arguments);
		case CMD_DELETE_EVENT:
			delete(command,arguments);
		case CMD_EDIT_EVENT:
			return edit(command,arguments);
		case CMD_DISPLAY_EVENT:
			return display(command,arguments);
		case CMD_EXIT:
			exit();
		default:
			return false;
		}
	}
	
	/**
	 * FOR ALL THESE METHODS: 
	 * YOU GET PASSED : <COMMAND, ARGUMENTS>. 
	 * 					THE ARUGMENTS ARE ESSENTIALLY WHATEVER THE USER INPUTS AFTER 
	 * 					ADD,DELETE, WHATEVER KEYWORD. REFER TO THE MANUAL.
	 * 					ADD , EVENTID,DATE,ETC,DESCRIPTION
	 * 					DELETE, EVENTID,DAT
	 * @param command
	 * @param arguments
	 */
	private void delete(String command, String arguments) {
		// TODO Auto-generated method stub
		
	}
	private boolean add(String command, String arguments) {
		// TODO Auto-generated method stub
		return false;
	}
	private boolean edit(String command, String arguments) {
		// TODO Auto-generated method stub
		return false;
	}
	private boolean display(String command, String arguments) {
		// TODO Auto-generated method stub
		return false;
	}
	/*
	 * Command to add event to calender 
	 */
	private boolean addEvent() {
		return false;
	}
	
	/*
	 * Exists application.
	 */
	private void exit() {
		System.exit(0);
	}
	
	/**
	 * Read next valid command.
	 * @return valid Command
	 */
	public String readNextCommand() {
		String cmd;
		do {
			String nextCommand = "";
			Scanner read = new Scanner(System.in);
			nextCommand = read.nextLine();
			ArgParser parser = new ArgParser();
		    cmd=	parser.parseAction(nextCommand);
			System.out.println("cmd : " + cmd);
			

		} while(!isValidCommand(cmd));
		return cmd;

	}
	/**
	 * Checks to make sure computer input is correct
	 * @param command
	 * @return true if command is valid
	 */
	private boolean validInput(String command) {
		return existsInArray(VALID_COMMANDS,command);
	}
	/**
	 * Checks to make sure user input is correct.
	 * If not, displays message to console.
	 * @param command
	 * @return true if command is valid
	 */
	private boolean isValidCommand(String command) {
			return	command != null;
	}

	/**
	 * @param array
	 * @param text
	 * @return true if text exists in array
	 */
	private boolean existsInArray(String[] array,String text) {
		for(int i = 0; i < array.length; i++) {
			if(array[i].equalsIgnoreCase((text))) {
				return true;
			}
		}
		return false;
	}
}
