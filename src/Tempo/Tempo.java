package Tempo;

//import java.util.Arrays;

public class Tempo {
	private static final String WELCOME_MESSAGE = "Welcome to Tempo! Type manual for instructions";
	private static final String EXIT_CMD = "exit";

	public static void main(String args[]) {
		RequestHandler run = new RequestHandler();
		ArgParser parser = new ArgParser();
		System.out.println(WELCOME_MESSAGE);
		listenForInput(run, parser);
	}

	public static void listenForInput(RequestHandler run, ArgParser parser) {
		String userInput = "";
		while (!userInput.equals(EXIT_CMD)) {
			userInput = run.readNextCommand();
		}
	}
}
