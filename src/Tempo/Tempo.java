package Tempo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Tempo {

	private static DateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
	private static Calendar cal = Calendar.getInstance();
	private static final String WELCOME_MESSAGE = "Welcome to Tempo! Type manual for instructions";
	private static final String EXIT_CMD = "exit";
	private static final String GOOD_MORNING = "Good Morning! ";
	private static final String GOOD_AFTERNOON = "Good Afternoon! ";
	private static final String GOOD_EVENING = "Good evening! ";

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Please enter a filename! :)");
			System.out.println("Usage: java Tempo <filename>");
			System.exit(0);
		}
	
		RequestHandler run = new RequestHandler(args[0]);
		ArgParser parser = new ArgParser();
		printWelcomeMsg();		
		listenForInput(run, parser);
	}

	private static void printWelcomeMsg() {
		printGreetings();
		printDate();
		System.out.println(WELCOME_MESSAGE);
	}

	private static void printGreetings() {
		if (getTime() > 4 && getTime() <= 12) {
			System.out.print(GOOD_MORNING);
		}
		else if (getTime() > 12 && getTime() <= 16) {
			System.out.print(GOOD_AFTERNOON);
		} else {
			System.out.print(GOOD_EVENING);
		}
	}

	private static void printDate() {
		System.out.println("Today's date : " + df.format(cal.getTime()));
	}

	private static int getTime() {
		int time = cal.get(Calendar.HOUR_OF_DAY);
		return time;
	}

	public static void listenForInput(RequestHandler run, ArgParser parser) {
		String userInput = "";
		while (!userInput.equals(EXIT_CMD)) {
			userInput = run.readNextCommand();
			// System.out.println("user input " + userInput);
		}
	}
}
