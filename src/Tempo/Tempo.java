package Tempo;

import java.text.*;
import java.util.*;

public class Tempo {
	private tempRequestHandler tempRH = tempRequestHandler.getInstance();

	private static Scanner sc;
	private static boolean run;

	private static final String WELCOME_MESSAGE = "lcome to Tempo! Type manual for instructions";
	private static final String GOODBYE_MESSAGE = "Thank you for using Tempo!";
	private static final String EXIT_CMD = "exit";
	private static final String GOOD_MORNING = "Good Morning! ";
	private static final String GOOD_AFTERNOON = "Good Afternoon! ";
	private static final String GOOD_EVENING = "Good evening! ";

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
		System.out.println(tempRH.initialize(fileName));
		sc = new Scanner(System.in);

		run = true;

		while (run) {
			ArrayList<String> output = listenForInput();

			for (String line : output) {
				System.out.println(line);

				if (output.equals(EXIT_CMD)) {
					run = false;
					System.out.println(GOODBYE_MESSAGE);
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

	private ArrayList<String> listenForInput() {
		String input = sc.nextLine();
		
		if(!input.equals("")){
			return tempRH.processCommand(input);
	
		}else{
			return listenForInput();
		}
	}
}
