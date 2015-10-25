package Tempo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.joestelmach.natty.*;

public class CommandParser {
	private static CommandParser instance = new CommandParser();
	
	private Calendar calendar = Calendar.getInstance();
	private IndexStore indexStore = IndexStore.getInstance();
	private Display display = Display.getInstance();
	
	private CommandParser(){
		
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
			case "add" :
			case "create" :
			case "new" :
				return processAddCommand(arguments);

			// Remove Function
			case "delete" :
			case "remove" :
			case "cancel" :
				 return processRemoveCommand(arguments); 

			// Update Function
			case "update" :
			case "edit" :
			case "change" :
				return null;

			// Mark as Done Function
			case "done" :
			case "finished" :
			case "completed" :
				return null;

			// Display Function
			case "view" :
			case "display" :
				return null;

			// Search Function
			case "search" :
			case "find" :
				return null;
			
			// Undo Function
			case "undo":
				return processUndoCommand();
			
			// Display help/manual
			case "help":
				return null;
				
			// Exit command	
			case "exit":
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

	// ADD EVENT: add event <name> from <start date> at <start time> to <end date> at <end time> repeat:<frequency of occurrence>
	// ADD TASK: add task <name> due <due date>
	// ADD FLOATING: add task <name>
	private Command processAddCommand(String argumentString) {
		String addType = getFirstWord(argumentString);
		argumentString = removeFirstWord(argumentString);
		
		ArrayList<String> args = null;
		
		Command command;
		
		if(addType.equalsIgnoreCase("event")){
			args = processAddEventCommand(argumentString);
		}else if (addType.equalsIgnoreCase("task")){
			args = processAddTaskCommand(argumentString);
		}else{
			//TO-DO DISPLAY ERROR HERE
		}
		
		command = new AddCommand(calendar, args);
		return command;
	}
	
	private ArrayList<String> processAddEventCommand(String argumentString){
		String nameString = "";
		Date startDateTime = null;
		Date endDateTime = null;
		
		ArrayList<String> returnList = new ArrayList<String>();
		
		if (argumentString.contains("from")) {
			nameString = getEventName(argumentString);
			argumentString = argumentString.split(nameString)[1].trim();
			if (argumentString.contains(" to ")) {
				// CASE 1: from and to
				String[] startEndTime = argumentString.split(" to ");

				String endDateTimeString = startEndTime[1];
				String startDateTimeString = removeFirstWord(startEndTime[0]);

				startDateTime = getDateTime(startDateTimeString);
				endDateTime = getDateTime(endDateTimeString);

			} else {
				// CASE 2: from, no to (start date/time only)
				endDateTime = null;
				String startDateTimeString = removeFirstWord(argumentString).trim();
				startDateTime = getDateTime(startDateTimeString);
			}
		} else {
			// Try parsing on natty
			DateGroup dateGroup = parseDateTimeString(argumentString);

			if (dateGroup != null) {
				startDateTime = dateGroup.getDates().get(0);
				endDateTime = null;

				String dateString = dateGroup.getText();
				nameString = argumentString.split(dateString)[0].trim();
			}
		}
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
		
		String startTimeString = "null";
		String endTimeString = "null";
		
		if(startDateTime != null){
			startTimeString = df.format(startDateTime);
		}
		
		if(endDateTime != null){
			endTimeString = df.format(endDateTime);
		}
		
		returnList.add(nameString);
		returnList.add(startTimeString);
		returnList.add(endTimeString);
		
		return returnList;
	}
	
	private ArrayList<String> processAddTaskCommand(String argumentString){
		String nameString = null;
		Date dueDate = null;
		String dueDateString = null;
		
		ArrayList<String> returnList =  new ArrayList<String>();
		
		if(argumentString.toLowerCase().contains("due")){
			nameString = getTaskName(argumentString);
			dueDate = getTaskDueDate(argumentString);
		}else{
			// Try parsing on natty
			DateGroup dateGroup = parseDateTimeString(argumentString);

			if (dateGroup != null) {
				dueDate = dateGroup.getDates().get(0);

				String dateString = dateGroup.getText();
				nameString = argumentString.split(dateString)[0].trim();
			}else{
				//Floating Task
				nameString = argumentString.trim();
				returnList.add(nameString);
				
				return returnList;
			}
		}
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy/HH:mm");
		
		if(dueDate != null){
			dueDateString = df.format(dueDate);
		}
		
		returnList.add(nameString);
		returnList.add(dueDateString);
		
		return returnList;
	}
	
	private Command processRemoveCommand(String argumentString){
		int idx;
		Command command;
		
		idx = getId(argumentString);
		
		if(idx != -1){
			command = new RemoveCommand(calendar,indexStore, idx);
			return command;	
		}else{
			//DISPLAY ERROR MESSAGE (TO-DO)
			return null;
		}
	}
	
	private Command processUndoCommand(){
		return new UndoCommand(calendar);
	}
	
	private Command processExitCommand(){
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
				
				//splits date according to 
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
				
				//splits date according to 
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
		try{
			int id = Integer.parseInt(arguments);
			return id;
		}catch(NumberFormatException e){
			return -1;
		}
		
	}

	// FOR ADD EVENTS FUNCTION
	private String getEventName(String arguments) {
		String[] parameters1 = arguments.split("from");
		return parameters1[0].trim();
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
			// String s = dateGroup.getText();
			// System.out.println("dateGroup.getText(): " + s);
			// List<Date> dates = dateGroup.getDates();

			return dateGroup;
		} else {
			return null;
		}
	}
}