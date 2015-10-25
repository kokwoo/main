package Tempo;

import java.util.ArrayList;

public class ExitCommand implements Command{
	
	public ArrayList<String> execute() {
		ArrayList<String> exit = new ArrayList<String>();
		exit.add("exit");
		return exit;
	}
	
}
