package Tempo.Commands;

import Tempo.Logic.Calendar;

public class EditFileNameCommand implements Command{
	private static final String command = "change filename %1$s";
	
	private Calendar cal;
	private String fileName;
	
	public EditFileNameCommand(Calendar cal, String fileName){
		this.cal = cal;
		this.fileName = fileName;
	}
	
	public Result execute() {
		saveCommand();
		String originalFileName = cal.getFilename();
		boolean success = cal.setFilename(fileName);
		String returnCommand = String.format(command, fileName);
		
		if(success){
			return new Result(returnCommand,success,null);
	
		}else{
			cal.setFilename(originalFileName);
			return new Result(returnCommand,success,null);
		}
	}
	
	private void saveCommand() {
		cal.saveCmd((Command) new EditFileNameCommand(cal, fileName));
	}

}
