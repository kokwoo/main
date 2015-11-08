package Tempo.Commands;

import Tempo.Logic.Display;

public class HelpCommand implements Command{
	private static String cmd = "help";
	
	public Result execute() {
		return new Result(cmd,Display.getManual(),true,null);
	}
}
