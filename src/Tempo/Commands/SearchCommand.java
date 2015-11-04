package Tempo.Commands;

import java.util.*;

import Tempo.Logic.Calendar;

public class SearchCommand implements Command {
	private Calendar cal;
	private String arguments;


	public SearchCommand(Calendar cal, String arguments) {
		this.cal = cal;
		this.arguments = arguments;
	}

	@Override
	public Result execute() {
			return cal.search(arguments);
	}
}