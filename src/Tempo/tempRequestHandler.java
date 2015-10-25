package Tempo;

import java.util.*;

public class tempRequestHandler {
	private static tempRequestHandler instance = new tempRequestHandler();
	
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
		return command.execute();
	}
	
	public String initialize(String filename){
		calendar.createFile(filename);
		return filename + " is ready to use.";
	}
}
