package Tempo.Logic;

import java.util.*;

import Tempo.Commands.Command;
import Tempo.Commands.Result;

public class RequestHandler {
	private static RequestHandler instance = new RequestHandler();

	private static final String MSG_INVALID_COMMAND = "Sorry, we are unable to process your command. Please try again.";

	private CommandParser parser;
	private Calendar calendar;

	private RequestHandler() {
		parser = CommandParser.getInstance();
		calendar = Calendar.getInstance();
	}

	public static RequestHandler getInstance() {
		return instance;
	}

	public Result processCommand(String commandString) {
		Command command = null;
//		try {
			command = parser.parse(commandString);
			if (command != null) {
				return command.execute();
			} else {
			}
			return handleInvalidCommand(commandString);
//		} catch (Exception e) {
//			return handleInvalidCommand(commandString);
//		}

	}

	private Result handleInvalidCommand(String commandString) {
		Result result = new Result(commandString, false, null);
		return result;
	}

	public String initialize(String filename) {
		calendar.createFile(filename);
		return filename + " is ready to use.";
	}

	public Calendar getCalendar() {
		return this.calendar;
	}
}
