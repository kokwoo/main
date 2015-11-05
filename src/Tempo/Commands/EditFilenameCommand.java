package Tempo.Commands;

import Tempo.Logic.Calendar;

public class EditFilenameCommand implements Command{
	private static final String command = "change filename %1$s";
	
	private Calendar calendar;
	private String filename;
	
	public EditFilenameCommand(Calendar calendar, String filename){
		this.calendar = calendar;
		this.filename = filename;
	}
	
	public Result execute() {
		String originalFileName = calendar.getFilename();
		boolean success = calendar.setFilename(filename);
		String returnCommand = String.format(command, filename);
		
		if(success){
			return new Result(returnCommand,success,null);
	
		}else{
			calendar.setFilename(originalFileName);
			return new Result(returnCommand,success,null);
		}
	}

}
