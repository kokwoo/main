package Tempo.Commands;

import Tempo.Logic.Display;

//@@author A0125962B
public class HelpCommand implements Command{
	private static String cmd = "help";
	
	public Result execute() {
		return new Result(cmd,Display.getManual(),true,null);
	}
}
