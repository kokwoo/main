package Tempo.Commands;

public class ExitCommand implements Command{
	private static final String MSG_BYE = "Thank you for using Tempo!";
	
	public Result execute() {
		System.out.println(MSG_BYE);
		System.exit(0);
		return null;
	}
	
}
