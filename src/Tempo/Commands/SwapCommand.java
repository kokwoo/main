package Tempo.Commands;

import Tempo.Logic.Calendar;

public class SwapCommand implements Command {
	private static final String command = "switch working file to %1$s";

	private Calendar cal;
	private String fileName;

	public SwapCommand(Calendar cal, String fileName){
		this.cal = cal;
		this.fileName = fileName;
	}

	public Result execute() {
		saveCommand();
		String originalFileName = cal.getFilename();
		boolean isSuccess = cal.swapFile(fileName);
		String returnCommand = String.format(command, fileName);

		if (isSuccess) {
			//cal.addToUndoHistory((Command) new UndoEditFileName(originalFileName));
			return new Result(returnCommand, isSuccess, null);

		} else {
			cal.swapFile(originalFileName);
			return new Result(returnCommand, isSuccess, null);
		}
	}

	private void saveCommand() {
		cal.saveCmd((Command) new EditFileNameCommand(cal, fileName));
	}

}
