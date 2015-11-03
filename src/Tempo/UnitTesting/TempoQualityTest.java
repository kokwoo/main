package Tempo.UnitTesting;
/**
 * UNIT TESTS FOR TEMPO.
 * POST ISSUES OR QUESTIONS TO HOWHOW71 @ GITHUB
 */


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import Tempo.Logic.RequestHandler;
import junit.framework.TestCase;

public class TempoQualityTest extends TestCase {
	private final static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final static ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	public static void test() {
		//setUpStreams();
		String filename = "testfile.txt";
		boolean fileExists = clear(filename);
		assertEquals(fileExists,false);
		Queue cmds = setupCommands();
		Queue digests = setupSha1Checksums();
		RequestHandler tempRH = RequestHandler.getInstance();
		canOpenFile(filename,tempRH);
		canRunCommands(filename,tempRH,cmds,digests);
		checkDisplay(tempRH);
	}
	// CURRENT ISSUES WITH PARSER - LIST SHOULD BE RECOGNIZED AS EQUIVALENT OF DISPLAY. IT IS NOT.
	// <KEY> TODAY SHOULD DISPLAY EVENTS OF TODAY. IT DOES NOT.
	private static void checkDisplay(RequestHandler tempRH) {
		
		Queue<Integer> digests = new LinkedList<Integer>();
		Queue<String> cmds = new LinkedList<String>();
		Integer testOne = 2093606962;
		Integer testTwo = 1087741371;
		digests.add(testOne);
		digests.add(testTwo);
		
		String displayAll = "display all";
		String displayEvents = "display events";
		//String viewToday = "view today";
		cmds.add(displayAll);
		cmds.add(displayEvents);
		//cmds.add(viewToday);
		while(!cmds.isEmpty()) {
			ArrayList <String> output  = tempRH.processCommand(cmds.poll());
			System.out.println(output);
			String result = "";
			Integer hash = getHash(output);
			assertEquals(hash, digests.poll());
			//System.out.println(hash);
		}
		
		
		// TODO Auto-generated method stub
		
	}

	private static int  getHash(ArrayList<String> output) {
		// TODO Auto-generated method stub
		String result = "";
		for (int i = 0; i < output.size(); i++) {
			result = result + output.get(i);
		}
		System.out.println("HASH : " + result.hashCode());
		return result.hashCode();
		
	}

	public static void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	private static boolean clear(String filename) {
		// TODO Auto-generated method stub
		File f = new File(filename);
		f.delete();
		return f.exists();
	}

	private static Queue setupSha1Checksums() {
		// TODO Auto-generated method stub
		Queue<String> digests = new LinkedList<String>();
		String testOne = "d1865679ed6e91f7fe180d0f84945d555dbd5b10";
		String testTwo = "c9a545239fc462b72ea59dcb148d874c7e2d6c94";
		String testThree = "14f4a5ca09801b260534ef4240d5a7682d859d1d";
		String testFour = "4d9a48c3958debdf64055aabde8bb1900b85213d";
		String testFive = "4e11f3c4154b8c4496099755c3931772980294b8";
		String testSix = "7c69806c9260e7e777a85b67d1c938b01a8fad14";
		String testSeven = "06267383e225a15c13378e928abe9ffe1e6be28f";
		String testEight = "30f8f984c79659d6d455715765fb483b03c73b13";
		String testNine = "bc44be1b2952eed06a060dc55466dbfd3758683f";
		String testTen = "bc44be1b2952eed06a060dc55466dbfd3758683f";
		
		digests.add(testOne);
		digests.add(testTwo);
		digests.add(testThree);
		digests.add(testFour);
		digests.add(testFive);
		digests.add(testSix);
		digests.add(testSeven);
		digests.add(testEight);
		digests.add(testNine);
		digests.add(testTen);
		return digests;
	}

	public static void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	private static Queue setupCommands() {
		Queue<String> commands = new LinkedList<String>();
		commands.add("add event do something from 10/10/2015 at 10:00 to 10/11/2015 at 11:00");
		commands.add("add event clean room from 100/10/2015 at 10:00 to 10/11/2015 at 11:00");
		commands.add("add task drink beeerr");
		commands.add("add task study for exam");
		commands.add("add event eat something from 10/10/2015 at 10:00 to 10/11/2015 at 25:00");
		//commands.add(null);
		commands.add("remove 0");
		commands.add("add task turn up from 12/10/2015 at 10:00 to 12/11/2015 at 25:00");
		commands.add("add task do something from 12/10/2015 at 10:00 to 10/11/2015 at 25:00");
		commands.add("add event do something from 10/10/2015 at 10:00 to 10/11/2015 at 25:00");
		//commands.add("remove 100");
		commands.add("   ");
		return commands;
	}

	private static void canRunCommands(String filename, RequestHandler tempRH,Queue<String> cmds,Queue<String> digests) {
		File file = new File(filename);
		while(!cmds.isEmpty()) {
			tempRH.processCommand(cmds.poll());
			String calculatedDigest = calculateSha1CheckSum(file);
			System.out.println("CORRECT DIGEST : " + calculatedDigest);
			String correctDigest = digests.poll();
			assertEquals(calculatedDigest,correctDigest);
		}

		return;
	}

	/**
	Optimal implementation of SHA-1 in Java.
	Used the tutorial from mkyong.com for help with implementation
	in Java, and w3schools.com for conceptual understanding

	@param file		calculate SHA-1 of this file
	@return	the hash value of the input file in Hex.
	 */
	private static String calculateSha1CheckSum(File file)  {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			FileInputStream fis = new FileInputStream(file);
			byte[] block = new byte[1024];
			int nread = 0;
			while((nread  = fis.read(block)) != -1) {
				md.update(block,0,nread);

			}
			byte[] mdbytes = md.digest();

			StringBuffer sb = new StringBuffer("");
			for(int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100,16).substring(1));
			}
			fis.close();
			return sb.toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return "";
	}


	private static void canOpenFile(String filename,RequestHandler tempRH) {
		assertEquals(tempRH.initialize(filename),"testfile.txt is ready to use.");
	}

}
