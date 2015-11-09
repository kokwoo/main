package Tempo.UnitTesting;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Test;

import Tempo.Commands.Result;
import Tempo.Data.CalendarObject;
import Tempo.Data.Event;
import Tempo.Data.FloatingTask;
import Tempo.Data.Task;
import Tempo.Logic.Display;
import Tempo.Logic.RequestHandler;

public class TestLogic {
	//@@author A0125303X
	private static final String CMD_ADD_EVENT = "add event %1$s";
	private static final String CMD_ADD_TASK = "add task %1$s";
	private static final String CMD_ADD_FLOATING = "add floating task %1$s";
	private static final String CMD_ADD_RECURR_EVENT = "add recurring event %1$s";
	private static final String CMD_ADD_RECURR_TASK = "add recurring task %1$s";

	private static final String CMD_REMOVE_EVENT = "remove event %1$s";
	private static final String CMD_REMOVE_TASK = "remove task %1$s";
	private static final String CMD_REMOVE_FLOATING = "remove floating task %1$s";

	private static final String CMD_UPDATE_EVENT = "update event %1$s";
	private static final String CMD_UPDATE_TASK = "update task %1$s";
	private static final String CMD_UPDATE_FLOATING = "update floating task %1$s";

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testClearFile() {
		String fileName = "testfile.txt";
		RequestHandler tempRH = initTempRH();

		String returnString = String.format("%1$s has been cleared.", fileName);
		Result tempResult = new Result(returnString, true, true, null);

		assertBooleanAndStrCmd(tempRH, tempResult, "clear file");
		assertEquals(tempResult.getResults(), tempRH.processCommand("clear file").getResults());
	}

	@Test
	public final void testAddEvent() {
		int pass = 0;
		// To test
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		RequestHandler tempRH = initTempRH();
		tempRH.processCommand(event);


		// equivalence testing
		// invalid date
		String event1 = "add event Dinner with mum from 45/12/2015 at 19:00 to 24/12/2015 at 19:00";
		String event2 = "add event Dinner with mum from 22/22/2015 at 19:00 to 24/12/2015 at 19:00";
		String event3 = "add event Dinner with mum from 22/12/9999 at 19:00 to 24/12/2015 at 19:00";
		// invalid time
		String event4 = "add event Dinner with mum from 22/12/2015 at 55:55 to 24/12/2015 at 19:00";
		// end date before start date
		String event5 = "add event Dinner with mum from 30/12/2015 at 19:00 to 24/12/2015 at 19:00";

		// all test cases should pass
		tryCatchAdd(pass, tempRH, event1);
		tryCatchAdd(pass, tempRH, event2);
		tryCatchAdd(pass, tempRH, event3);
		tryCatchAdd(pass, tempRH, event4);
		tryCatchAdd(pass, tempRH, event5);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event checkEvent = new Event(0, 0, "Dinner with mum", "22/11/2015/19:00", "24/11/2015/19:00");
		checkArray.add(checkEvent);
		String cmd = String.format(CMD_ADD_EVENT, "Dinner with mum");
		Result tempResult = new Result(cmd, true, putHashMap("events", checkArray));

		// Compare results object
		assertBooleanAndStrCmd(tempRH, tempResult, event);
		clearFile(tempRH);
		
		
		ArrayList<CalendarObject> checkArray1 = tempResult.getResults().get("events");
		ArrayList<CalendarObject> checkArray2 = tempRH.processCommand(event).getResults().get("events");
		assertEquals(checkArray1.toString(), checkArray2.toString());
		clearFile(tempRH);

	}

	@Test
	public final void testAddTask() {
		int pass = 0;
		// To test
		String task = "add task Lunch with mum due 12/12/2015";
		RequestHandler tempRH = initTempRH();
		tempRH.processCommand(task);

		// equivalence testing
		// invalid date
//		String task1 = "add task Dinner with mum due 45/12/2015";
//		String task2 = "add task Dinner with mum due 22/22/2015";
//		String task3 = "add task Dinner with mum due 22/12/9999";
//		
///		tempRH.processCommand(task1);
//		// all test cases should pass
//		tryCatchAdd(pass, tempRH, task1);
//		tryCatchAdd(pass, tempRH, task2);
//		tryCatchAdd(pass, tempRH, task3);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Task checkTask = new Task(0, 0, "Lunch with mum", "12/12/2015");
		checkArray.add(checkTask);
		String cmd = String.format(CMD_ADD_TASK, "Lunch with mum");
		Result tempResult = new Result(cmd, true, putHashMap("tasks", checkArray));

		// Compare results object
		assertBooleanAndStrCmd(tempRH, tempResult, task);
		clearFile(tempRH);
		
		
		ArrayList<CalendarObject> checkArray1 = tempResult.getResults().get("tasks");
		ArrayList<CalendarObject> checkArray2 = tempRH.processCommand(task).getResults().get("tasks");
		assertEquals(checkArray1.toString(), checkArray2.toString());
		clearFile(tempRH);

	}

	@Test
	public final void testAddFTask() {
		// To test
		String Ftask = "add task Dinner with mum";
		RequestHandler tempRH = initTempRH();

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		FloatingTask checkTask = new FloatingTask(0, 0, "Dinner with mum");
		checkArray.add(checkTask);
		String cmd = String.format(CMD_ADD_FLOATING, "Dinner with mum");
		Result tempResult = new Result(cmd, true, putHashMap("floating tasks", checkArray));

		// Compare results object
		assertBooleanAndStrCmd(tempRH, tempResult, Ftask);
		clearFile(tempRH);

		ArrayList<CalendarObject> expectedArray = tempResult.getResults().get("floating tasks");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand(Ftask).getResults().get("floating tasks");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);

	}

	@Test
	public final void testRemoveEvent() {

		String event = "add event Dinner with mum from today at 9pm to tomorrow at 10pm";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		tempRH.processCommand(event);
		tempRH.processCommand(event);
		tempRH.processCommand(event);

		// checker
		String cmd = String.format(CMD_REMOVE_EVENT, "Dinner with mum");
		Result tempResult = new Result(cmd, true, putHashMap("events", null));

		// Compare results object
		assertEquals(tempResult.getCmdPerformed(), tempRH.processCommand("remove 0").getCmdPerformed());
		assertEquals(tempResult.isSuccess(), tempRH.processCommand("remove 1").isSuccess());
		assertEquals(tempResult.isDisplayResult(), tempRH.processCommand("remove 2").isDisplayResult());
		clearFile(tempRH);
	}

	@Test
	public final void testRemoveTask() {
		// To test
		String task = "add task Lunch with mum due 12/12/2015";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		tempRH.processCommand(task);
		tempRH.processCommand(task);
		tempRH.processCommand(task);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		String cmd = String.format(CMD_REMOVE_TASK, "Lunch with mum");
		Result tempResult = new Result(cmd, true, putHashMap("tasks", checkArray));

		// Compare results object
		assertEquals(tempResult.getCmdPerformed(), tempRH.processCommand("remove 0").getCmdPerformed());
		assertEquals(tempResult.isSuccess(), tempRH.processCommand("remove 1").isSuccess());
		assertEquals(tempResult.isDisplayResult(), tempRH.processCommand("remove 2").isDisplayResult());

	}

	@Test
	public final void testRemoveFTask() {
		// To test
		String Ftask = "add task Lunch with mum";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		tempRH.processCommand(Ftask);
		tempRH.processCommand(Ftask);
		tempRH.processCommand(Ftask);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		String cmd = String.format(CMD_REMOVE_FLOATING, "Lunch with mum");
		Result tempResult = new Result(cmd, true, putHashMap("floating tasks", checkArray));

		// Compare results object
		assertEquals(tempResult.getCmdPerformed(), tempRH.processCommand("remove 0").getCmdPerformed());
		assertEquals(tempResult.isSuccess(), tempRH.processCommand("remove 1").isSuccess());
		assertEquals(tempResult.isDisplayResult(), tempRH.processCommand("remove 2").isDisplayResult());

		// equivalence testing
		try {
			assertEquals(tempResult.isDisplayResult(), tempRH.processCommand("remove 3").isDisplayResult());
			assertEquals(tempResult.isDisplayResult(), tempRH.processCommand("remove -1").isDisplayResult());
		} catch (NullPointerException ex) {

		}
		clearFile(tempRH);

	}

	@Test
	public final void testUpdateEventEndTime() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event checkNewName = new Event(0, 0, "Dinner with mum", "22/11/2015/19:00", "24/11/2015/23:00");
		checkArray.add(checkNewName);

		String cmdName = String.format(CMD_UPDATE_EVENT, "Dinner with mum");
		Result tempResult = new Result(cmdName, true, putHashMap("events", checkArray));

		tempRH.processCommand(event);
		ArrayList<CalendarObject> expectedArray = tempResult.getResults().get("events");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand("update 0 end time:23:00").getResults()
				.get("events");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);
	}

	@Test
	public final void testUpdateEventEndDate() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event checkNewName = new Event(0, 0, "Dinner with mum", "22/11/2015/19:00", "25/11/2015/19:00");
		checkArray.add(checkNewName);

		String cmdName = String.format(CMD_UPDATE_EVENT, "Dinner with mum");
		Result tempResult = new Result(cmdName, true, putHashMap("events", checkArray));

		tempRH.processCommand(event);
		ArrayList<CalendarObject> expectedArray = tempResult.getResults().get("events");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand("update 0 end date:25/11/2015").getResults()
				.get("events");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);
	}

	@Test
	public final void testUpdateEventStartTime() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event checkNewName = new Event(0, 0, "Dinner with mum", "22/11/2015/23:00", "24/11/2015/19:00");
		checkArray.add(checkNewName);

		String cmdName = String.format(CMD_UPDATE_EVENT, "Dinner with mum");
		Result tempResult = new Result(cmdName, true, putHashMap("events", checkArray));

		tempRH.processCommand(event);
		ArrayList<CalendarObject> expectedArray = tempResult.getResults().get("events");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand("update 0 start time:23:00").getResults()
				.get("events");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);
	}

	@Test
	public final void testUpdateEventStartDate() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);

		// checker for updating start date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event checkNewName = new Event(0, 0, "Dinner with mum", "21/11/2015/19:00", "24/11/2015/19:00");
		checkArray.add(checkNewName);

		String cmdName = String.format(CMD_UPDATE_EVENT, "Dinner with mum");
		Result tempResult = new Result(cmdName, true, putHashMap("events", checkArray));

		tempRH.processCommand(event);
		ArrayList<CalendarObject> expectedArray = tempResult.getResults().get("events");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand("update 0 start date:21/11/2015").getResults()
				.get("events");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);
	}

	@Test
	public final void testUpdateEventName() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);

		// checker for both updating name and due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event checkNewName = new Event(0, 0, "changed", "22/11/2015/19:00", "24/11/2015/19:00");
		checkArray.add(checkNewName);

		String cmdName = String.format(CMD_UPDATE_EVENT, "changed");
		Result tempResult = new Result(cmdName, true, putHashMap("events", checkArray));

		tempRH.processCommand(event);
		ArrayList<CalendarObject> expectedArray = tempResult.getResults().get("events");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand("update 0 name:changed").getResults()
				.get("events");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);
	}

	@Test
	public final void testUpdateTaskName() {
		// To test - initialize test.txt
		String task = "add task Gymming due 12/12/2015";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);

		// checker for updating name
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Task checkNewName = new Task(0, 0, "okok", "12/12/2015");
		checkArray.add(checkNewName);

		String cmdName = String.format(CMD_UPDATE_TASK, "okok");
		Result tempResult = new Result(cmdName, true, putHashMap("tasks", checkArray));

		tempRH.processCommand(task);
		ArrayList<CalendarObject> expectedArray = tempResult.getResults().get("tasks");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand("update 0 name:okok").getResults().get("tasks");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);
	}

	@Test
	public final void testUpdateTaskDue() {
		// To test - initialize test.txt
		String task = "add task Gymming due 12/12/2015";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);

		// checker for due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Task checkNewDueDate = new Task(0, 0, "Gymming", "23/11/2015");
		checkArray.add(checkNewDueDate);

		String cmdDueDate = String.format(CMD_UPDATE_TASK, "Gymming");
		Result tempResult1 = new Result(cmdDueDate, true, putHashMap("tasks", checkArray));

		// Compare results object
		tempRH.processCommand(task);
		ArrayList<CalendarObject> expectedArrayName = tempResult1.getResults().get("tasks");
		ArrayList<CalendarObject> actualArrayName = tempRH.processCommand("update 0 due:23/11/2015").getResults()
				.get("tasks");
		assertEquals(actualArrayName.toString(), expectedArrayName.toString());
		clearFile(tempRH);

	}

	@Test
	public final void testUpdateFTaskName() {
		// To test - initialize test.txt
		String task = "add task Gymming";
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);

		// checker for due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		FloatingTask checkNewDueDate = new FloatingTask(0, 0, "changed");
		checkArray.add(checkNewDueDate);

		String cmdDueDate = String.format(CMD_UPDATE_FLOATING, "changed");
		Result tempResult1 = new Result(cmdDueDate, true, putHashMap("floating tasks", checkArray));

		// Compare results object
		tempRH.processCommand(task);
		ArrayList<CalendarObject> expectedArrayName = tempResult1.getResults().get("floating tasks");
		ArrayList<CalendarObject> actualArrayName = tempRH.processCommand("update 0 name:changed").getResults()
				.get("floating tasks");
		assertEquals(actualArrayName.toString(), expectedArrayName.toString());
		clearFile(tempRH);

	}

	@Test
	public final void testDisplayAll() {
		RequestHandler tempRH = initTempRH();

		assertTrue(tempRH.processCommand("display all").isDisplayResult());
		assertTrue(tempRH.processCommand("display all").isSuccess());
		clearFile(tempRH);
		// checker
		ArrayList<String> checkerArray = new ArrayList<String>();
		checkerArray.add("You have no event today");
		checkerArray.add("");
		checkerArray.add("You have no upcoming event");
		checkerArray.add("");
		checkerArray.add("You have no past event");
		checkerArray.add("");
		checkerArray.add("You have no task without deadline");
		checkerArray.add("");
		checkerArray.add("You have no task without deadline that are done");
		checkerArray.add("");
		checkerArray.add("You have no done tasks");
		checkerArray.add("");
		checkerArray.add("You have no task today");
		checkerArray.add("");
		checkerArray.add("You have no upcoming task");
		checkerArray.add("");
		checkerArray.add("You have no missed task");
		checkerArray.add("");

		String checkerString = strArrayToString(checkerArray);

		// compare results
		assertEquals(checkerString, tempRH.processCommand("display all").getCmdPerformed());
		clearFile(tempRH);
	}

	@Test
	// equivalence testing
	public final void testDisplayAll1() {
		RequestHandler tempRH = initTempRH();
		// upcoming event
		tempRH.processCommand("add event date with gf from 23/12/2015 at 17:00 to 26/12/2015 at 19:00");
		// past event
		tempRH.processCommand("add event gymming from 06/11/2015 at 19:00 to 07/11/2015 at 19:00");
		// event today
		tempRH.processCommand("add event Dinner with mum from 08/11/2015 at 21:00 to 09/11/2015 at 21:00");
		// past task
		tempRH.processCommand("add task Dinner with bf due 06/11/2015");
		// today task
		tempRH.processCommand("add task Dinner with gf due 08/11/2015");
		// upcoming task
		tempRH.processCommand("add task Dinner with mum due 12/12/2015");
		// task done
		tempRH.processCommand("add task eat due 23/12/2015");
		tempRH.processCommand("done 6");
		// floating task
		tempRH.processCommand("add task Hello");
		// floating task done
		tempRH.processCommand("add task Hello2");
		tempRH.processCommand("done 8");

		assertTrue(tempRH.processCommand("display all").isDisplayResult());
		assertTrue(tempRH.processCommand("display all").isSuccess());

		// checker
		ArrayList<String> checkerArray = new ArrayList<String>();
		checkerArray.add("You have no event today");
		checkerArray.add("");
		checkerArray.add("These are the list of upcoming events");
		checkerArray.add("");
		checkerArray.add("1) date with gf From: Wednesday, 23/12/2015 17:00 To: Saturday, 26/12/2015 19:00	[ID:0] ");
		checkerArray.add("");
		checkerArray.add("These are the list of past events");
		checkerArray.add("");
		checkerArray.add("1) gymming From: Friday, 06/11/2015 19:00 To: Saturday, 07/11/2015 19:00	[ID:1] ");
		checkerArray.add("2) Dinner with mum From: Sunday, 08/11/2015 21:00 To: Monday, 09/11/2015 21:00	[ID:2] ");
		checkerArray.add("");
		checkerArray.add("These are the list of tasks without deadline");
		checkerArray.add("");
		checkerArray.add("1) Hello	[ID:7] ");
		checkerArray.add("");
		checkerArray.add("These are the list of tasks without deadline that are done");
		checkerArray.add("");
		checkerArray.add("1) Hello2	[ID:8] ");
		checkerArray.add("");
		checkerArray.add("These are all the tasks that are done");
		checkerArray.add("");
		checkerArray.add("1) eat Due: Wednesday, 23/12/2015	[ID:6] ");
		checkerArray.add("");
		checkerArray.add("You have no task today");
		checkerArray.add("");
		checkerArray.add("These are the list of upcoming tasks");
		checkerArray.add("");
		checkerArray.add("1) Dinner with mum Due: Saturday, 12/12/2015	[ID:5] ");
		checkerArray.add("");
		checkerArray.add("These are the tasks you missed");
		checkerArray.add("");
		checkerArray.add("1) Dinner with bf Due: Friday, 06/11/2015	[ID:3] ");
		checkerArray.add("2) Dinner with gf Due: Sunday, 08/11/2015	[ID:4] ");
		checkerArray.add("");

		String checkerString = strArrayToString(checkerArray);

		// compare results
		assertEquals(checkerString, tempRH.processCommand("display all").getCmdPerformed());
		clearFile(tempRH);
	}

	public String strArrayToString(ArrayList<String> in) {
		String s = "";

		for (String str : in) {
			s += str + "\n";
		}

		return s;
	}

	@Test
	public final void testSearchEvents() {
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		tempRH.processCommand("add event Dinner with mum from 21/12/2015 at 19:00 to 23/12/2015 at 19:00");

		// checker for due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event event = new Event(0, 0, "Dinner with mum", "21/12/2015/19:00", "23/12/2015/19:00");
		checkArray.add(event);

		String cmdDueDate = String.format(CMD_ADD_EVENT, "Dinner with mum");
		Result tempResult1 = new Result(cmdDueDate, true, putHashMap("events", checkArray));

		// Compare results object
		ArrayList<CalendarObject> expectedArrayName = tempResult1.getResults().get("events");
		ArrayList<CalendarObject> actualArrayName = tempRH.processCommand("search dinner").getResults()
				.get("eventsBestMatches");
		assertEquals(actualArrayName.toString(), expectedArrayName.toString());
		clearFile(tempRH);

	}
	
	@Test
	public final void testAddRecurringEventsDialy() {
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		
		// checker for due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event event = new Event(0, 0, "Dinner with mum", "21/11/2015/19:00", "22/11/2015/19:00");
		Event event1 = new Event(1, 0, "Dinner with mum", "22/11/2015/19:00", "23/11/2015/19:00");
		Event event2 = new Event(2, 0, "Dinner with mum", "23/11/2015/19:00", "24/11/2015/19:00");
		Event event3 = new Event(3, 0, "Dinner with mum", "24/11/2015/19:00", "25/11/2015/19:00");
		checkArray.add(event);
		checkArray.add(event1);
		checkArray.add(event2);
		checkArray.add(event3);

		String cmdDueDate = String.format(CMD_ADD_RECURR_EVENT, "Dinner with mum");
		Result tempResult1 = new Result(cmdDueDate, true, putHashMap("events", checkArray));

		// Compare results object
		ArrayList<CalendarObject> expectedArrayName = tempResult1.getResults().get("events");
		ArrayList<CalendarObject> actualArrayName = tempRH.processCommand("add event Dinner with mum from 21/11/2015 at 19:00 to 22/11/2015 at 19:00 repeat daily till 24/11/2015 ").getResults().get("events");
		assertEquals(actualArrayName.toString(), expectedArrayName.toString());
		clearFile(tempRH);

	}
	
	@Test
	public final void testAddRecurringEventsWeekly() {
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		
		// checker for due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event event = new Event(0, 0, "Dinner with mum", "21/11/2015/19:00", "22/11/2015/19:00");
		Event event1 = new Event(1, 0, "Dinner with mum", "28/11/2015/19:00", "29/11/2015/19:00");
		Event event2 = new Event(2, 0, "Dinner with mum", "05/12/2015/19:00", "06/12/2015/19:00");
		Event event3 = new Event(3, 0, "Dinner with mum", "12/12/2015/19:00", "13/12/2015/19:00");
		Event event4 = new Event(4, 0, "Dinner with mum", "19/12/2015/19:00", "20/12/2015/19:00");
		Event event5 = new Event(5, 0, "Dinner with mum", "26/12/2015/19:00", "27/12/2015/19:00");
		checkArray.add(event);
		checkArray.add(event1);
		checkArray.add(event2);
		checkArray.add(event3);
		checkArray.add(event4);
		checkArray.add(event5);

		String cmdDueDate = String.format(CMD_ADD_RECURR_EVENT, "Dinner with mum");
		Result tempResult1 = new Result(cmdDueDate, true, putHashMap("events", checkArray));

		// Compare results object
		ArrayList<CalendarObject> expectedArrayName = tempResult1.getResults().get("events");
		ArrayList<CalendarObject> actualArrayName = tempRH.processCommand("add event Dinner with mum from 21/11/2015 at 19:00 to 22/11/2015 at 19:00 repeat weekly till 01/01/2016").getResults().get("events");
		assertEquals(expectedArrayName.toString(), actualArrayName.toString());
		clearFile(tempRH);

	}
	
	@Test
	public final void testAddRecurringEventsMonthly() {
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		
		// checker for due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event event = new Event(0, 0, "Dinner with mum", "21/11/2015/19:00", "22/11/2015/19:00");
		Event event1 = new Event(1, 0, "Dinner with mum", "21/12/2015/19:00", "22/12/2015/19:00");
		Event event2 = new Event(2, 0, "Dinner with mum", "21/01/2016/19:00", "22/01/2016/19:00");

		checkArray.add(event);
		checkArray.add(event1);
		checkArray.add(event2);
		

		String cmdDueDate = String.format(CMD_ADD_RECURR_EVENT, "Dinner with mum");
		Result tempResult1 = new Result(cmdDueDate, true, putHashMap("events", checkArray));

		// Compare results object
		ArrayList<CalendarObject> expectedArrayName = tempResult1.getResults().get("events");
		ArrayList<CalendarObject> actualArrayName = tempRH.processCommand("add event Dinner with mum from 21/11/2015 at 19:00 to 22/11/2015 at 19:00 repeat monthly till 01/02/2016").getResults().get("events");
		assertEquals(expectedArrayName.toString(), actualArrayName.toString());
		clearFile(tempRH);

	}
	
	@Test
	public final void testAddRecurringTasksDaily() {
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		
		// checker for due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Task task = new Task(0, 0, "Dinner with mum", "21/11/2015");
		Task task1 = new Task(1, 0, "Dinner with mum", "22/11/2015");
		Task  task2 = new Task(2, 0, "Dinner with mum", "23/11/2015");
		Task task3 = new Task(3, 0, "Dinner with mum", "24/11/2015");
		checkArray.add(task);
		checkArray.add(task1);
		checkArray.add(task2);
		checkArray.add(task3);

		String cmdDueDate = String.format(CMD_ADD_RECURR_TASK, "Dinner with mum");
		Result tempResult1 = new Result(cmdDueDate, true, putHashMap("tasks", checkArray));

		// Compare results object
		ArrayList<CalendarObject> expectedArray = tempResult1.getResults().get("tasks");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand("add task Dinner with mum due 21/11/2015 repeat daily till 24/11/2015").getResults().get("tasks");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);

	}
	
	@Test
	public final void testAddRecurringTasksWeekly() {
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		
		// checker for due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Task task = new Task(0, 0, "Dinner with mum", "21/11/2015");
		Task task1 = new Task(1, 0, "Dinner with mum", "28/11/2015");
		Task  task2 = new Task(2, 0, "Dinner with mum", "05/12/2015");

		checkArray.add(task);
		checkArray.add(task1);
		checkArray.add(task2);

		String cmdDueDate = String.format(CMD_ADD_RECURR_TASK, "Dinner with mum");
		Result tempResult1 = new Result(cmdDueDate, true, putHashMap("tasks", checkArray));

		// Compare results object
		ArrayList<CalendarObject> expectedArray = tempResult1.getResults().get("tasks");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand("add task Dinner with mum due 21/11/2015 repeat weekly till 12/12/2015").getResults().get("tasks");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);

	}
	
	@Test
	public final void testAddRecurringTasksMonthly() {
		RequestHandler tempRH = initTempRH();
		clearFile(tempRH);
		
		// checker for due date
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Task task = new Task(0, 0, "Dinner with mum", "21/11/2015");
		Task task1 = new Task(1, 0, "Dinner with mum", "21/12/2015");
		Task  task2 = new Task(2, 0, "Dinner with mum", "21/01/2016");

		checkArray.add(task);
		checkArray.add(task1);
		checkArray.add(task2);

		String cmdDueDate = String.format(CMD_ADD_RECURR_TASK, "Dinner with mum");
		Result tempResult1 = new Result(cmdDueDate, true, putHashMap("tasks", checkArray));

		// Compare results object
		ArrayList<CalendarObject> expectedArray = tempResult1.getResults().get("tasks");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand("add task Dinner with mum due 21/11/2015 repeat monthly till 01/02/2016").getResults().get("tasks");
		assertEquals(actualArray.toString(), expectedArray.toString());
		clearFile(tempRH);

	}
	

	// *****************OTHER METHODS********************//
	private HashMap<String, ArrayList<CalendarObject>> putHashMap(String key, ArrayList<CalendarObject> value) {
		HashMap<String, ArrayList<CalendarObject>> map;
		map = new HashMap<String, ArrayList<CalendarObject>>();
		map.put(key, value);
		return map;
	}

	private RequestHandler initTempRH() {
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		return tempRH;
	}

	private void assertBooleanAndStrCmd(RequestHandler tempRH, Result tempResult, String str) {
		assertEquals(tempResult.getCmdPerformed(), tempRH.processCommand(str).getCmdPerformed());
		assertEquals(tempResult.isSuccess(), tempRH.processCommand(str).isSuccess());
		assertEquals(tempResult.isDisplayResult(), tempRH.processCommand(str).isDisplayResult());
	}

	private void clearFile(RequestHandler tempRH) {
		tempRH.processCommand("clear file");
	}

	private void tryCatchAdd(int pass, RequestHandler tempRH, String str) {
		try {
			tempRH.processCommand(str);
		} catch (NullPointerException ex) {
			pass = 1;
		}
		if (!(pass == 1)) {
			assertTrue(true);
		} else {
			assertTrue(false);
		}
		clearFile(tempRH);
	}
}
