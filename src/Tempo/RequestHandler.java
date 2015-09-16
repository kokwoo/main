package Tempo;

import java.util.Scanner;

public class RequestHandler {
	private  final String MSG_CMD_NOT_VALID = "Why don't you try entering an actual command?";
	private  final String CMD_ADD_EVENT = "ADD EVENT";
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
		case "add event":
			return addEvent();
		case "exit":
			exit();
		default:
			return false;
		}
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
		String nextCommand = "";
		do {

			Scanner read = new Scanner(System.in);
			nextCommand = read.nextLine();


		} while(!isValidCommand(nextCommand));
		return nextCommand;

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
		boolean isValid = existsInArray(VALID_COMMANDS,command);
		if(!isValid) {
			System.out.println(MSG_CMD_NOT_VALID);
		}
		return isValid;
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
