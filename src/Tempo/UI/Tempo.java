package Tempo.UI;

import java.text.*;
import java.util.*;

import Tempo.CalendarObjects.CalendarObject;
import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;
import Tempo.CalendarObjects.Task;
import Tempo.Commands.Result;
import Tempo.Logic.RequestHandler;

public class Tempo {
	private RequestHandler requestHandler = RequestHandler.getInstance();

	private static Scanner sc;
	private static boolean run;

	private static final String WELCOME_MESSAGE = "Welcome to Tempo! Type manual for instructions";
	private static final String GOOD_MORNING = "Good Morning! ";
	private static final String GOOD_AFTERNOON = "Good Afternoon! ";
	private static final String GOOD_EVENING = "Good evening! ";

	private static final String SUCCESSFUL_MESSAGE = "Command: '%1$s' was performed successfully.";
	private static final String UNSUCESSFUL_MESSAGE = "Command: '%1$s' was not performed succesfully. Please refer to the help menu.";



	private static Tempo Tempo = new Tempo();

	private Tempo() {
	}

	private Tempo getInstance() {
		return Tempo;
	}

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Please enter a filename! :)");
			System.out.println("Usage: java Tempo <filename>");
			System.exit(0);
		}
		Tempo tempo = Tempo.getInstance();
		tempo.run(args[0]);
	}

	private void run(String fileName) {
		printWelcomeMsg();
		System.out.println(requestHandler.initialize(fileName));
		sc = new Scanner(System.in);

		run = true;

		while (run) {
			Result result = listenForInput();

			if (result.isSuccess()) {
				if (result.isDisplayResult()) {
					System.out.println(result.getCmdPerformed());
				} else {
					System.out.println(String.format(SUCCESSFUL_MESSAGE, result.getCmdPerformed()));

					if (result.hasWarning()) {
						System.out.println(result.getWarning());
					}
				}
			} else {
				System.out.println(String.format(UNSUCESSFUL_MESSAGE, result.getCmdPerformed()));
				if (result.hasWarning()) {
					System.out.println(result.getWarning());
				}
			}
		}
	}

	private void printWelcomeMsg() {
		printGreetings();
		printDate();
		System.out.println(WELCOME_MESSAGE);
	}

	private void printGreetings() {
		DateFormat df = new SimpleDateFormat("HH");

		String timeString = df.format(getTime());

		int hour = Integer.parseInt(timeString);

		if (hour > 4 && hour <= 12) {
			System.out.print(GOOD_MORNING);
		} else if (hour > 12 && hour <= 16) {
			System.out.print(GOOD_AFTERNOON);
		} else {
			System.out.print(GOOD_EVENING);
		}
	}

	private void printDate() {
		DateFormat df = new SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm");
		System.out.println("Today's date : " + df.format(getTime()));
	}

	private Date getTime() {
		return new Date();
	}

	private Result listenForInput() {
		String input = sc.nextLine();

		if (!input.equals("")) {
			return requestHandler.processCommand(input);

		} else {
			return listenForInput();
		}
	}
}
