package Tempo;

//import java.util.Arrays;

public class Tempo {
	private static final String WELCOME_MESSAGE = "Welcome to Tempo! Type manual for instructions";
	private static final String EXIT_CMD = "exit";

	public static void main(String args[]) {
		if(args.length != 1){
			System.out.println("Please enter a filename! :)");
			System.out.println("Usage: java Tempo <filename>");
			System.exit(0);
		}
		
		RequestHandler run = new RequestHandler(args[0]);
		ArgParser parser = new ArgParser();
		System.out.println(WELCOME_MESSAGE);
		listenForInput(run, parser);
	}

	public static void listenForInput(RequestHandler run, ArgParser parser) {
		String userInput = "";
		while (!userInput.equals(EXIT_CMD)) {
			userInput = run.readNextCommand();
			//System.out.println("user input " + userInput);
		}
	}
}
