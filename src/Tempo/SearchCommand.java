package Tempo;

import java.util.*;

public class SearchCommand implements Command {
	private Calendar cal;
	private String arguments;


	public SearchCommand(Calendar cal, String arguments) {
		this.cal = cal;
		this.arguments = arguments;
	}

	@Override
	public ArrayList<String> execute() {
			return cal.search(arguments);
	}
}