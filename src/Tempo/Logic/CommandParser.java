package Tempo.Calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.joestelmach.natty.*;

import Tempo.Commands.AddCommand;
import Tempo.Commands.Command;
import Tempo.Commands.DisplayCommand;
import Tempo.Commands.DoneCommand;
import Tempo.Commands.ExitCommand;
import Tempo.Commands.RemoveCommand;
import Tempo.Commands.SearchCommand;
import Tempo.Commands.UndoCommand;
import Tempo.Commands.UpdateCommand;

public class CommandParser {
	private static CommandParser instance = new CommandParser();

	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_CREATE = "create";
	private static final String COMMAND_NEW = "new";

	private static final String COMMAND_REMOVE = "remove";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_CANCEL = "cancel";

	private static final String COMMAND_UPDATE = "update";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_CHANGE = "change";

	private static final String COMMAND_DONE = "done";
	private static final String COMMAND_COMPLETED = "completed";
	private static final String COMMAND_FINISHED = "finished";

	private static final String COMMAND_VIEW = "view";
	private static final String COMMAND_DISPLAY = "display";

	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_FIND = "find";

	private static final String COMMAND_UNDO = "undo";

	private static final String COMMAND_HELP = "help";

	private static final String COMMAND_EXIT = "exit";

	private Calendar calendar = Calendar.getInstance();
	private IndexStore indexStore = IndexStore.getInstance();
	private Display display = Display.getInstance();

	private CommandParser() {

	}

	public static CommandParser getInstance() {
		return instance;
	}

	// UPDATE: update <id> <field name>:<new value>
	// REMOVE: remove <id>

	public Command parse(String commandString) {
		// process command type
		String commandType = getCommandType(commandString);
		String arguments = getArguments(commandString);
		// process add, process update, process remove
		switch (commandType.toLowerCase()) {
			// Add Function
			case COMMAND_ADD :
			case COMMAND_CREATE :
			case COMMAND_NEW :
				return processAddCommand(arguments);

			// Remove Function
			case COMMAND_REMOVE :
			case COMMAND_DELETE :
			case COMMAND_CANCEL :
				return processRemoveCommand(arguments);

			// Update Function
			case COMMAND_UPDATE :
			case COMMAND_EDIT :
			case COMMAND_CHANGE :
				return processUpdateCommand(arguments);

			// Mark as Done Function
			case COMMAND_DONE :
			case COMMAND_FINISHED :
			case COMMAND_COMPLETED :
				return processDoneCommand(arguments);

			// Display Function
			case COMMAND_VIEW :
			case COMMAND_DISPLAY :
				return processDisplayCommand(arguments);

			// Search Function
			case COMMAND_SEARCH :
			case COMMAND_FIND :
				return processSearchCommand(arguments);

			// Undo Function
			case COMMAND_UNDO :
				return processUndoCommand();

			// Display help/manual
			case COMMAND_HELP :
				return null;

			// Exit command
			case COMMAND_EXIT :
				return processExitCommand();

			// Generate Error Command Message
			default :
				return null;
		}
	}

	private String getCommandType(String commandString) {
		return getFirstWord(commandString);
	}

	public String getArguments(String message) {
		return removeFirstWord(message);
	}

	// ADD EVENT: add event <name> from <start date> at <start time> to <end
	// date> at <end time> repeat:<frequency of occurrence>
	// ADD TASK: add task <name> due <due date>
	// ADD FLOATING: add task <name>
	private Command processAddCommand(String argumentString) {
		String addType = getFirstWord(argumentString);
		argumentString = removeFirstWord(argumentString);
		ArrayList<String> args = null;
		boolean isRecurring = false;
		ArrayList<String> recurringArgs = null;
		String recurringType = null;
		String recurringDate = null;

		Command command;
		
		if(argumentString.contains(" repeat ")){
			String[] split = argumentString.split(" repeat ");
			String recurringArgsString = split[split.length-1];
			argumentString = getArgumentString(argumentString);
			isRecurring = true;
			recurringArgs = processRecurringArgs(recurringArgsString);
			
			recurringType = recurringArgs.get(0);
			recurringDate = recurringArgs.get(1);
		}

		if (addType.equalsIgnoreCase("event")) {
			args = processAddEventCommand(argumentString);
		} else if (addType.equalsIgnoreCase("task")) {
			args = processAddTaskCommand(argumentString);
		} else {
			// TO-DO DISPLAY ERROR HERE
		}

		command = new AddCommand(calendar, args, isRecurring, recurringType, recurringDate);
		return command;
	}

	private ArrayList<String> processAddEventCommand(String argumentString) {
		// Dinner with mum from 9am to 9pm
		// Dinner with mum today 9pm to 10pm
		// Dinner with mum today from 9am to 9pm
		// Dinner with mum
		String nameString = "";
		String startDateString = null;
		String startTimeString = null;
		String endTimeString = null;
		Date startDateTime = null;
		Date endDateTime = null;
		String startDateTimeString = null;
		String endDateTimeString = null;
		
		ArrayList<String> returnList = new ArrayList<String>();

		if (argumentString.contains(" from ")) {
			nameString = getEventNameFrom(argumentString);
			argumentString = argumentString.split(nameString)[1].trim();

			// ! Try parsing nameString on Natty: if returns a dateGroup, means there is date within the nameString
			DateGroup dateGroup = parseDateTimeString(nameString);
			if (dateGroup != null) {
				// We are in trouble... :(
				Date startDate = dateGroup.getDates().get(0);
				SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
				startDateString = formatDate.format(startDate);
			}
			
			if (argumentString.contains(" to ")) {
				// CASE 1: from and to
				String[] startEndTime = argumentString.split(" to ");

				endDateTimeString = startEndTime[1];
				startDateTimeString = removeFirstWord(startEndTime[0]);

				startDateTime = getDateTime(startDateTimeString);
				endDateTime = getDateTime(endDateTimeString);

			} else {
				// CASE 2: from, no to (start date/time only)
				endDateTime = null;
				startDateTimeString = removeFirstWord(argumentString).trim();
				startDateTime = getDateTime(startDateTimeString);
			}
		} else if (argumentString.contains(" to ")){ 
			//NO "FROM" BUT HAVE "TO"
			
			nameString = getEventNameTo(argumentString);
			argumentString = argumentString.split(nameString)[1].trim();
			
			DateGroup dateGroup = parseDateTimeString(nameString);
			
			if (dateGroup != null) {
				startDateTime = dateGroup.getDates().get(0);
				endDateTime = getDateTime(argumentString);

				String dateString = dateGroup.getText();
				nameString = nameString.split(dateString)[0].trim();
			} else {
				nameString = null;
				startDateTime = null;
				endDateTime = null;
			}
			
			
		}else {
			// Try parsing on natty
			DateGroup dateGroup = parseDateTimeString(argumentString);

			if (dateGroup != null) {
				startDateTime = dateGroup.getDates().get(0);
				endDateTime = null;

				String dateString = dateGroup.getText();
				nameString = argumentString.split(dateString)[0].trim();
			} else {
				nameString = argumentString;
			}
		}
		
		
		if(startDateString != null){
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
			if(startDateTime != null){
				startTimeString = formatTime.format(startDateTime);
				startDateTimeString = startDateString + "/" + startTimeString;
			}
			
			if(endDateTime != null){
				endTimeString = formatTime.format(endDateTime);
				endDateTimeString = startDateString + "/" + endTimeString;
			}
			
		}else{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy/HH:mm");

			if (startDateTime != null) {
				startDateTimeString = df.format(startDateTime);
			}

			if (endDateTime != null) {
				endDateTimeString = df.format(endDateTime);
			}
		}
		
		if(startDateTimeString != null && endDateTimeString!= null){
			endDateTimeString = adjustDates(startDateTimeString, endDateTimeString);
		}
		
		returnList.add(nameString);
		returnList.add(startDateTimeString);
		returnList.add(endDateTimeString);
		
//		System.out.println("Name: " + nameString);
//		System.out.println("Start: " + startDateTimeString);
//		System.out.println("End: " + endDateTimeString);


		return returnList;
	}

	private ArrayList<String> processAddTaskCommand(String argumentString) {
		String nameString = null;
		Date dueDate = null;
		String dueDateString = null;

		ArrayList<String> returnList = new ArrayList<String>();

		if (argumentString.toLowerCase().contains("due")) {
			nameString = getTaskName(argumentString);
			dueDate = getTaskDueDate(argumentString);
		} else {
			// Try parsing on natty
			DateGroup dateGroup = parseDateTimeString(argumentString);

			if (dateGroup != null) {
				dueDate = dateGroup.getDates().get(0);

				String dateString = dateGroup.getText();
				nameString = argumentString.split(dateString)[0].trim();
			} else {
				// Floating Task
				nameString = argumentString.trim();
				returnList.add(nameString);

				return returnList;
			}
		}

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy/HH:mm");

		if (dueDate != null) {
			dueDateString = df.format(dueDate);
		}

		returnList.add(nameString);
		returnList.add(dueDateString);

		return returnList;
	}

	private Command processRemoveCommand(String argumentString) {
		int idx;
		Command command;

		idx = getId(argumentString);

		if (idx != -1) {
			command = new RemoveCommand(calendar, indexStore, idx);
			return command;
		} else {
			// DISPLAY ERROR MESSAGE (TO-DO)
			return null;
		}
	}

	private Command processUpdateCommand(String arguments) {
		int idx = getId(getFirstWord(arguments));

		ArrayList<String> fields = getFieldsList(arguments);
		ArrayList<String> newValues = getNewValuesList(arguments);

		if (idx != -1) {
			Command command = new UpdateCommand(calendar, indexStore, idx, fields, newValues);
			return command;
		} else {
			// DISPLAY ERROR MESSAGE
			return null;
		}

	}

	private Command processDoneCommand(String argumentString) {
		int idx;
		Command command;

		idx = getId(argumentString);

		if (idx != -1) {
			command = new DoneCommand(calendar, indexStore, idx);
			return command;
		} else {
			// DISPLAY ERROR MESSAGE (TO-DO)
			return null;
		}
	}

	private Command processSearchCommand(String arguments) {
		Command command;
		command = new SearchCommand(calendar, arguments);
		return command;
	}

	private Command processDisplayCommand(String arguments) {
		Command command;
		// System.out.println("got it!");
		// System.out.println(arguments);
		command = new DisplayCommand(display, arguments);
		if (checkArguments(arguments)) {
			return command;
		} else {
			// DISPLAY ERROR MSG
			return null;
		}

	}

	private boolean checkArguments(String arguments) {
		switch (arguments) {
			case ("events") :
			case ("tasks") :
			case ("upcoming events") :
			case ("undone tasks") :
			case ("missed tasks") :
			case ("today") :
			case ("all") :
				return true;
			default :
				return false;
		}
	}

	private Command processUndoCommand() {
		return new UndoCommand(calendar);
	}

	private Command processExitCommand() {
		return new ExitCommand();
	}

	private Date getDateTime(String dateTimeString) {
		try {
			if (dateTimeString.contains("at")) {
				String dateString = dateTimeString.split("at")[0].trim();
				String timeString = dateTimeString.split("at")[1].trim();

				SimpleDateFormat dateFormat;
				DateGroup dateGroup = null;
				Date date = null;

				// splits date according to
				if (dateString.contains("/")) {
					dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					date = dateFormat.parse(dateString);
				} else if (dateString.contains("-")) {
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					date = dateFormat.parse(dateString);
				} else if (dateString.contains(".")) {
					dateFormat = new SimpleDateFormat("dd.MM.yyyy");
					date = dateFormat.parse(dateString);
				} else {
					dateGroup = parseDateTimeString(dateString);

					if (dateGroup != null) {
						date = dateGroup.getDates().get(0);
						dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						dateString = dateFormat.format(date);
					} else {
						date = null;
					}
				}
				dateGroup = parseDateTimeString(timeString);
				Date time = null;

				if (dateGroup == null) {
					SimpleDateFormat tempFormat = new SimpleDateFormat("HH:mm");
					time = tempFormat.parse("00:00");
				} else {
					time = dateGroup.getDates().get(0);
				}

				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				timeString = timeFormat.format(time);

				String combinedDateTimeString = dateString + "/" + timeString;
				SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy/HH:mm");

				Date parsedDate = dateTimeFormat.parse(combinedDateTimeString);
				return parsedDate;
			} else {
				SimpleDateFormat dateFormat;
				DateGroup dateGroup = null;
				Date date = null;

				// splits date according to
				if (dateTimeString.contains("/")) {
					dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					date = dateFormat.parse(dateTimeString);
				} else if (dateTimeString.contains("-")) {
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					date = dateFormat.parse(dateTimeString);
				} else if (dateTimeString.contains(".")) {
					dateFormat = new SimpleDateFormat("dd.MM.yyyy");
					date = dateFormat.parse(dateTimeString);
				} else {
					dateGroup = parseDateTimeString(dateTimeString);

					if (dateGroup != null) {
						date = dateGroup.getDates().get(0);
					} else {
						date = null;
					}
				}

				return date;
			}
		} catch (Exception e) {
			return null;
		}

	}

	// FOR REMOVE/MARK AS DONE FUNCTION
	private int getId(String arguments) {
		try {
			int id = Integer.parseInt(arguments);
			return id;
		} catch (NumberFormatException e) {
			return -1;
		}

	}

	// FOR ADD EVENTS FUNCTION
	private String getEventNameFrom(String arguments) {
		String[] parameters = arguments.split(" from ");
		String returnString = parameters[0];

		for (int i = 1; i < parameters.length - 1; i++) {
			returnString += " from " + parameters[i];
		}
		return returnString;
	}
	
	private String getEventNameTo(String arguments) {
		String[] parameters = arguments.split(" to ");
		String returnString = parameters[0];

		for (int i = 1; i < parameters.length - 1; i++) {
			returnString += " to " + parameters[i];
		}
		return returnString;
	}
	
	private String getArgumentString(String arguments){
		String[] parameters = arguments.split(" repeat ");
		String returnString = parameters[0];

		for (int i = 1; i < parameters.length - 1; i++) {
			returnString += " repeat " + parameters[i];
		}
		return returnString;
	}
	
	private ArrayList<String> processRecurringArgs(String recurringArgs){
		String recurringType = null;
		Date recurringDate = null;
		String recurringDateStr = null;
		
		ArrayList<String> args = new ArrayList<String>();
		
		if(recurringArgs.contains(" till ")){
			String[] split = recurringArgs.split(" till ");
			recurringType = split[0];
			recurringDate = getDateTime(split[1]);
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			try{
				recurringDateStr = df.format(recurringDate);
			}catch(Exception e){
				recurringDateStr = null;
			}
		}else{
			recurringType = recurringArgs;
		}
		
		args.add(recurringType);
		args.add(recurringDateStr);
		
		return args;
	}

	// FOR ADD TASK FUNCTION
	private String getTaskName(String arguments) {
		String[] parameters1 = arguments.split("due");
		return parameters1[0].trim();
	}

	private Date getTaskDueDate(String arguments) {
		String[] parameters1 = arguments.split("due");
		String dueDateString = parameters1[1].trim();
		Date dueDate = getDateTime(dueDateString);
		return dueDate;
	}
	
	private String adjustDates(String start, String end){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
		Date startDate = null;
		Date endDate = null;
		
		try{
			startDate = df.parse(start);
			endDate = df.parse(end);
		}catch(Exception e){
			//EXCEPTION HANDLING???
		}
		
		long startDateMilli = startDate.getTime();
		long endDateMilli = endDate.getTime();
		
		if(endDateMilli < startDateMilli){
			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
	
			String startDateString = formatDate.format(startDate);
			String endTimeString = formatTime.format(endDate);
			
			end = startDateString + "/" + endTimeString;	
		}	
		return end;
	}

	// FOR UPDATE FUNCTION
	private ArrayList<String> getFieldsList(String arguments) {
		ArrayList<String> fields = new ArrayList<String>();
		arguments = removeFirstWord(arguments);
		if (arguments.contains(";")) {
			// more than one field to be updated
			String[] split = arguments.split("; ");
			for (int i = 0; i < split.length; i++) {
				String[] params = split[i].trim().split(":");
				fields.add(params[0].trim());
			}
		} else {
			String[] split = arguments.split(":");
			fields.add(split[0].trim());
		}
		return fields;
	}

	private ArrayList<String> getNewValuesList(String arguments) {
		ArrayList<String> newValues = new ArrayList<String>();
		arguments = removeFirstWord(arguments);
		if (arguments.contains(";")) {
			// more than one field to be updated
			String[] split = arguments.split("; ");
			for (int i = 0; i < split.length; i++) {
				String[] params = split[i].split(":");
				if (params.length == 3) {
					String timeString = params[1] + ":" + params[2];
					newValues.add(timeString);
				} else {
					newValues.add(params[1]);
				}
			}
		} else {
			String[] split = arguments.split(":");

			if (split.length == 3) {
				String timeString = split[1] + ":" + split[2];
				newValues.add(timeString);
			} else {
				newValues.add(split[1]);
			}
		}
		return newValues; // DONE
	}

	private String getFirstWord(String message) {
		String commandTypeString = message.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private String removeFirstWord(String message) {
		String[] split = message.split(" ");
		String returnMessage = "";
		for (int i = 1; i < split.length; i++) {
			returnMessage += split[i] + " ";
		}
		returnMessage = returnMessage.trim();
		return returnMessage;
	}

	private DateGroup parseDateTimeString(String dateTimeString) {
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(dateTimeString);
		if (!groups.isEmpty()) {
			DateGroup dateGroup = groups.get(0);
			return dateGroup;
		} else {
			return null;
		}
	}
}