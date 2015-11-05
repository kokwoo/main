package Tempo.Commands;

public class ClearCommand implements Command{
	private static final String clearString = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
	
	public ClearCommand(){
	}
	
	public Result execute() {
		return new Result(clearString, true, true, null);
	}

}
