package Tempo.Commands;

import Tempo.Logic.Calendar;

public class UndoEditFileName implements Command {
	private static Calendar cal = Calendar.getInstance();
	private static final String CMD_UNDO = "undo <rename file as %1$s>";
	private String fileName;
	private String originalFileName;
	
	public UndoEditFileName(String fileName) {
		this.fileName = fileName;
		originalFileName = cal.getFilename();
	}

	public Result execute() {
		Result result = executeUndoEditFileName();
		String cmd = String.format(CMD_UNDO, originalFileName);
		result.setCommand(cmd);
		return result;
	}
	
	private Result executeUndoEditFileName() {
		return cal.editFilename(fileName);
	}

}
