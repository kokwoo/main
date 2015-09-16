/*
 * Class: ArgParser
 *  Utility Class to Parse user input into <action> , <arguments>
 *  Example use :
 *  				String action = <ArgParser>.parseAction(userinput);
 *  				String arguments = <ArgParser>.parseArguments(userinput);
 */
package Tempo;
public class ArgParser {


	public ArgParser() {

	}
	/**
	 * 	@param userInput		
	 *  @return arguments					**/
	public String[] parseArguments(String userInput) {
		return getArguments(userInput);
	}
	/**
	 * 	@param userInput		
	 *  @return action					**/	
	public String parseAction(String userInput) {
		return getAction(userInput);
	}

	/**
	 * 	@param userInput		
	 *  @return arguments
	 *  @throws IllegalArugmentException					**/
	private String[] getArguments  (String message) throws IllegalArgumentException {
		if(!verifyInput(message)) {
			throw new IllegalArgumentException();
		}
		String[] tokenizedInput = message.split(" ");
		String[] argument = extractSubArray(tokenizedInput,1,tokenizedInput.length);
		return argument;
	}


	/** @param full array		
	 *  @param lowerbound      
	 *  @param upper bound
	 *  @throws IllegalArgumentException		
	 *  @return subarray 					**/
	private String[] extractSubArray(String[] tokenizedInput, int start, int end) throws IllegalArgumentException {
		if(!verifyInput(tokenizedInput)) {
			throw new IllegalArgumentException();
		}
		String[] subArray = new String[end - start];
		System.arraycopy(tokenizedInput, 1, subArray, 0, end-start);
		return subArray;
	}

	private boolean verifyInput(String[] tokenizedInput) {
		// TODO Auto-generated method stub
		if(isEmpty(tokenizedInput) || isNull(tokenizedInput)) {
			return false;
		}
		return true;
	}

	private boolean isNull(String[] tokenizedInput) {
		return tokenizedInput == null;
	}

	private boolean isEmpty(String[] tokenizedInput) {
		return tokenizedInput.length == 0;

	}

	private boolean verifyInput(String message) {
		if(isNull(message) || isEmpty(message)) {
			return false;
		}
		return true;
	}
	private String getAction(String message) throws IllegalArgumentException {
		if(!verifyInput(message)) {
			throw new IllegalArgumentException();
		}
		String[] tokenizedInput = message.split(" ");
		String action = tokenizedInput[0];
		return action;
	}

	private boolean isEmpty(String message) {
		return message.length() == 0;
	}

	private boolean isNull(String message) {
		return message == null;
	}
}
