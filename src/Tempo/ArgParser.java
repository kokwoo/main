/*
 * Class: ArgParser
 *  Utility Class to Parse user input into <action> , <arguments>
 *  Example use :
 *  				String action = <ArgParser>.parseAction(userinput);
 *  				String arguments = <ArgParser>.parseArguments(userinput);
 */
package Tempo;

import java.util.ArrayList;
import java.util.HashMap;

public class ArgParser {
	private HashMap<ArrayList<String>, String> keywords;

	public ArgParser() {
		keywords = new HashMap<ArrayList<String>, String>();
		initialiseKeywords(keywords);
	}
	
	public void initialiseKeywords() {
		addAddKeywords();
		addUpdateKeywords();
		addDisplayKeywords();
		addRemoveKeywords();
	}
	
	private void addAddKeywords() {
		ArrayList<String> add = new ArrayList<String>();
		add.add("add");
		add.add("create");
		add.add("new");
		keywords.put(add, "add");
	}
	
	private void addUpdateKeywords() {
		ArrayList<String> update = new ArrayList<String>();
		update.add("update");
		update.add("edit");
		update.add("change");
		keywords.put(update, "update");
	}
	
	private void addDisplayKeywords() {
		ArrayList<String> display = new ArrayList<String>();
		display.add("display");
		display.add("all");
		display.add("view");
		display.add("list");
		keywords.put(display, "display");
	}
	
	private void addRemoveKeywords() {
		ArrayList<String> remove = new ArrayList<String>();
		remove.add("remove");
		remove.add("cancel");
		remove.add("delete");
		keywords.put(remove, "remove");
	}

	public String getAction(String message) throws IllegalArgumentException {
		if (!isValidInput(message)) {
			throw new IllegalArgumentException();
		}
		String[] tokenizedInput = message.split(" ");
		String action = tokenizedInput[0];
		String keyword = getValidCommand(action);

		return keyword;
	}
	
	public String[] getArguments(String message) throws IllegalArgumentException {
		if (!isValidInput(message)) {
			throw new IllegalArgumentException();
		}
		String[] tokenizedInput = message.split(" ");
		String[] arguments = removeFirstWord(tokenizedInput, 1, tokenizedInput.length);
		return arguments;
	}

	private String getValidCommand(String action) {
		for (ArrayList<String> currActionKey : keywords.keySet()) {
			{
				if (isInArray(currActionKey, action)) {
					return currActionKey.get(0);
				}
			}
		}
		return null;
	}

	private boolean isInArray(ArrayList<String> actionKey, String action) {
		for (int i = 0; i < actionKey.size(); i++) {
			if (actionKey.get(i).equals(action)) {
				return true;
			}
		}
		return false;
	}

	private boolean isEmpty(String message) {
		return message.length() == 0;
	}
	
	private boolean isEmpty(String[] tokenizedInput) {
		return tokenizedInput.length == 0;
	}

	private boolean isNull(String message) {
		return message == null;
	}
	
	private boolean isNull(String[] tokenizedInput) {
		return tokenizedInput == null;
	}
	
	private boolean isValidInput(String[] tokenizedInput) {
		if (isEmpty(tokenizedInput) || isNull(tokenizedInput)) {
			return false;
		}
		return true;
	}

	private boolean isValidInput(String message) {
		if (isNull(message) || isEmpty(message)) {
			return false;
		}
		return true;
	}
	
	private String[] removeFirstWord(String[] tokenizedInput, int start, int end) throws IllegalArgumentException {
		if (!isValidInput(tokenizedInput)) {
			throw new IllegalArgumentException();
		}
		String[] subArray = new String[end - start];
		System.arraycopy(tokenizedInput, 1, subArray, 0, end - start);
		return subArray;
	}
}
