package Tempo.UnitTesting;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Test;

import Tempo.CalendarObjects.CalendarObject;
import Tempo.CalendarObjects.Event;
import Tempo.Commands.Result;
import Tempo.Logic.Display;
import Tempo.Logic.RequestHandler;

public class TestLogic {

	private static final String CMD_ADD_EVENT = "add event %1$s";
	private static final String CMD_ADD_RECURR_EVENT = "add recurring event %1$s";
	private static final String CMD_ADD_TASK = "add task %1$s";
	private static final String CMD_ADD_RECURR_TASK = "add recurring task %1$s";
	private static final String CMD_ADD_FLOATING = "add floating task %1$s";

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testClearFile() {
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		clearFile(tempRH);

		String returnString = String.format("%1$s has been cleared.", fileName);
		Result tempResult = new Result(returnString, true, true, null);
		assertEquals(tempResult.getCmdPerformed(), tempRH.processCommand("clear file").getCmdPerformed());
		assertEquals(tempResult.isSuccess(), tempRH.processCommand("clear file").isSuccess());
		assertEquals(tempResult.isDisplayResult(), tempRH.processCommand("clear file").isDisplayResult());
	}

	private void clearFile(RequestHandler tempRH) {
		tempRH.processCommand("clear file");
	}

	@Test
	public final void testAddEvent() {
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		Event checkEvent = new Event(0, 0, "Dinner with mum", "22/11/2015/19:00", "24/11/2015/19:00");
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		checkArray.add(checkEvent);
		tempRH.initialize(fileName);
		tempRH.processCommand(event);
		String cmd = String.format(CMD_ADD_EVENT, "Dinner with mum");
		Result tempResult = new Result(cmd, true, putHashMap("events",checkArray ));
		assertEquals(tempResult.getCmdPerformed(),tempRH.processCommand(event).getCmdPerformed() );
		assertEquals(tempResult.isSuccess(),tempRH.processCommand(event).isSuccess() );
		assertEquals(tempResult.isDisplayResult(),tempRH.processCommand(event).isDisplayResult() );	
		
		clearFile(tempRH);
		
	}
		
	private HashMap<String, ArrayList<CalendarObject>> putHashMap(String key, ArrayList<CalendarObject> value) {
		HashMap<String, ArrayList<CalendarObject>> map;
		map = new HashMap<String, ArrayList<CalendarObject>>();
		map.put(key, value);
		return map;
	}
	
	@Test
	public final void testDisplay() {
		 String fileName = "testfile.txt";
		 RequestHandler tempRH = RequestHandler.getInstance();
		 tempRH.initialize(fileName);
	//	 String checkStr = "You have no event today\n" + "You have no upcoming event\n" + "These are the list of past events\n" + "\n" 
	//	 + "1) do something From: Saturday, 10/10/2015 10:00 To: Tuesday, 10/11/2015 11:00	[ID:0] ";
		 
		 assertTrue(tempRH.processCommand("display events").isDisplayResult());
		 assertTrue(tempRH.processCommand("display events").isSuccess());
	//	 HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String, ArrayList<CalendarObject>>();
		 
		 
		 
	}
	

}
