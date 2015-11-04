package Tempo.Commands;

import java.util.ArrayList;

public class ExitCommand implements Command{
	private static final String GOODBYE_MESSAGE = "Thank you for using Tempo!";
	public Result execute() {
		System.out.println(GOODBYE_MESSAGE);
		System.exit(0);
		return null;
	}
	
}
