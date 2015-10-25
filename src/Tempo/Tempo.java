package Tempo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Tempo {
	private tempRequestHandler handler = tempRequestHandler.getInstance();

	private static Scanner sc;
	private static boolean isRunning;

	private static final String MESSAGE_WELCOME = "Welcome to Tempo! Type manual for instructions";
	private static final String MESSAGE_GOODBYE = "Thank you for using Tempo!";
	private static final String COMMAND_EXIT = "exit";
	private static final String MESSAGE_GOOD_MORNING = "Good Morning! ";
	private static final String MESSAGE_GOOD_AFTERNOON = "Good Afternoon! ";
	private static final String MESSAGE_GOOD_EVENING = "Good evening! ";
	private static final String FORMAT_DATE = "EEEE, dd/MM/yyyy HH:mm";

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
		System.out.println(handler.initialize(fileName));
		sc = new Scanner(System.in);

		isRunning = true;

		while (isRunning) {
			ArrayList<String> output = listenForInput();

			for (String line : output) {
				System.out.println(line);

				if (output.equals(COMMAND_EXIT)) {
					isRunning = false;
					System.out.println(MESSAGE_GOODBYE);
				}
			}
		}
	}

	private void printWelcomeMsg() {
		printGreetings();
		printDate();
		System.out.println(MESSAGE_WELCOME);
	}

	private void printGreetings() {
		DateFormat dateFormat = new SimpleDateFormat("HH");

		String timeString = dateFormat.format(getTime());

		int hour = Integer.parseInt(timeString);

		if (hour > 4 && hour <= 12) {
			System.out.print(MESSAGE_GOOD_MORNING);
		} else if (hour > 12 && hour <= 16) {
			System.out.print(MESSAGE_GOOD_AFTERNOON);
		} else {
			System.out.print(MESSAGE_GOOD_EVENING);
		}
	}

	private void printDate() {
		DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
		System.out.println("Today's date : " + dateFormat.format(getTime()));
	}

	private Date getTime() {
		return new Date();
	}

	private ArrayList<String> listenForInput() {
		return handler.processCommand(sc.nextLine());
	}
}
