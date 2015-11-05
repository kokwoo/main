package Tempo.Commands;

public class UndoUpdate implements Command {
	
	protected int prevModIndex;
	protected int prevModSeriesIndex;
	
	public UndoUpdate() {
		
	}
	
	public Result execute() {
		return new Result("", false, null);
	}
}
