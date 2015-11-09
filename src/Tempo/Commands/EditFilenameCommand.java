package Tempo.Commands;

import Tempo.Logic.Calendar;

//@@author A0125962B
public class EditFileNameCommand implements Command{
	private static final String command = "rename file as %1$s";
	
	private Calendar cal;
	private String fileName;
	
	public EditFileNameCommand(Calendar cal, String fileName){
		this.cal = cal;
		this.fileName = fileName;
	}
	
	public Result execute() {
		saveCommand();
		String originalFileName = cal.getFilename();
		boolean isSuccess = cal.setFilename(fileName);
		String returnCommand = String.format(command, fileName);
		
		if(isSuccess){
			cal.addToUndoHistory((Command) new UndoEditFileName(originalFileName));
			return new Result(returnCommand,isSuccess,null);
	
		}else{
			cal.setFilename(originalFileName);
			return new Result(returnCommand,isSuccess,null);
		}
	}
	
	private void saveCommand() {
		cal.saveCmd((Command) new EditFileNameCommand(cal, fileName));
	}

}
