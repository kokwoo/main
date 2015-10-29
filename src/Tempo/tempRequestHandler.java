package Tempo;

import java.util.*;

public class tempRequestHandler {
	private static tempRequestHandler instance = new tempRequestHandler();
	
	private static final String MSG_INVALID_COMMAND = "Sorry, we are unable to process your command. Please try again.";
	
	private CommandParser parser;
	private Calendar calendar;
	
	private tempRequestHandler(){
		parser = CommandParser.getInstance();
		calendar = Calendar.getInstance();
	}
	
	public static tempRequestHandler getInstance(){
		return instance;
	}
	
	public ArrayList<String> processCommand(String commandString){
		Command command = parser.parse(commandString);
		if(command != null){
			return command.execute();
		}else{
		}return handleInvalidCommand();
		
	}
	
	private ArrayList<String> handleInvalidCommand() {
		ArrayList<String> feedback = new ArrayList<String>();
		feedback.add(MSG_INVALID_COMMAND);
		return feedback;
	}
	
	public String initialize(String filename){
		calendar.createFile(filename);
		return filename + " is ready to use.";
	}
}
