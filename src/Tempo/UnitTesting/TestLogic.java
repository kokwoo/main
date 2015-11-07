package Tempo.UnitTesting;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Test;

import Tempo.CalendarObjects.CalendarObject;
import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;
import Tempo.CalendarObjects.Task;
import Tempo.Commands.Result;
import Tempo.Logic.Display;
import Tempo.Logic.RequestHandler;

public class TestLogic {

	private static final String CMD_ADD_EVENT = "add event %1$s";
	private static final String CMD_ADD_RECURR_EVENT = "add recurring event %1$s";
	private static final String CMD_ADD_TASK = "add task %1$s";
	private static final String CMD_ADD_RECURR_TASK = "add recurring task %1$s";
	private static final String CMD_ADD_FLOATING = "add floating task %1$s";

	private static final String CMD_REMOVE_EVENT = "remove event %1$s";
	private static final String CMD_REMOVE_TASK = "remove task %1$s";
	private static final String CMD_REMOVE_FLOATING = "remove floating task %1$s";

	private static final String CMD_UPDATE_EVENT = "update event %1$s";
	private static final String CMD_UPDATE_TASK = "update task %1$s";
	private static final String CMD_UPDATE_FLOATING = "update floating task %1$s";

	@After
	public void tearDown() throws Exception {
	}

	private HashMap<String, ArrayList<CalendarObject>> putHashMap(String key, ArrayList<CalendarObject> value) {
		HashMap<String, ArrayList<CalendarObject>> map;
		map = new HashMap<String, ArrayList<CalendarObject>>();
		map.put(key, value);
		return map;
	}

	@Test
	public final void testClearFile() {
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		// tempRH.processCommand("clear file");

		String returnString = String.format("%1$s has been cleared.", fileName);
		Result tempResult = new Result(returnString, true, true, null);

		assertEquals(tempResult.getCmdPerformed(), tempRH.processCommand("clear file").getCmdPerformed());
		assertEquals(tempResult.isSuccess(), tempRH.processCommand("clear file").isSuccess());
		assertEquals(tempResult.isDisplayResult(), tempRH.processCommand("clear file").isDisplayResult());
		assertEquals(tempResult.getResults(), tempRH.processCommand("clear file").getResults());
	}

	@Test
	public final void testAddEvent() {
		// To test
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		tempRH.processCommand(event);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Event checkEvent = new Event(0, 0, "Dinner with mum", "22/11/2015/19:00", "24/11/2015/19:00");
		checkArray.add(checkEvent);
		String cmd = String.format(CMD_ADD_EVENT, "Dinner with mum");
		Result tempResult = new Result(cmd, true, putHashMap("events", checkArray));

		// Compare results object
		assertEquals(tempResult.getCmdPerformed(), tempRH.processCommand(event).getCmdPerformed());
		assertEquals(tempResult.isSuccess(), tempRH.processCommand(event).isSuccess());
		assertEquals(tempResult.isDisplayResult(), tempRH.processCommand(event).isDisplayResult());
		tempRH.processCommand("clear file");

		ArrayList<CalendarObject> checkArray1 = tempResult.getResults().get("events");
		ArrayList<CalendarObject> checkArray2 = tempRH.processCommand(event).getResults().get("events");
		assertEquals(checkArray1.toString(), checkArray2.toString());
		tempRH.processCommand("clear file");

	}

	@Test
	public final void testAddTask() {
		// To test
		String task = "add task Lunch with mum due 12/12/2015";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		tempRH.processCommand(task);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		Task checkTask = new Task(0, 0, "Lunch with mum", "12/12/2015");
		checkArray.add(checkTask);
		String cmd = String.format(CMD_ADD_TASK, "Lunch with mum");
		Result tempResult = new Result(cmd, true, putHashMap("tasks", checkArray));

		// Compare results object
		assertEquals(tempResult.getCmdPerformed(), tempRH.processCommand(task).getCmdPerformed());
		assertEquals(tempResult.isSuccess(), tempRH.processCommand(task).isSuccess());
		assertEquals(tempResult.isDisplayResult(), tempRH.processCommand(task).isDisplayResult());
		tempRH.processCommand("clear file");

		ArrayList<CalendarObject> checkArray1 = tempResult.getResults().get("tasks");
		ArrayList<CalendarObject> checkArray2 = tempRH.processCommand(task).getResults().get("tasks");
		assertEquals(checkArray1.toString(), checkArray2.toString());
		tempRH.processCommand("clear file");

	}

	@Test
	public final void testAddFTask() {
		// To test
		String Ftask = "add task Dinner with mum";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);

		// checker
		ArrayList<CalendarObject> checkArray = new ArrayList<CalendarObject>();
		FloatingTask checkTask = new FloatingTask(0, 0, "Dinner with mum");
		checkArray.add(checkTask);
		String cmd = String.format(CMD_ADD_FLOATING, "Dinner with mum");
		Result tempResult = new Result(cmd, true, putHashMap("floating tasks", checkArray));

		// Compare results object
		assertEquals(tempResult.getCmdPerformed(), tempRH.processCommand(Ftask).getCmdPerformed());
		assertEquals(tempResult.isSuccess(), tempRH.processCommand(Ftask).isSuccess());
		assertEquals(tempResult.isDisplayResult(), tempRH.processCommand(Ftask).isDisplayResult());
		tempRH.processCommand("clear file");

		ArrayList<CalendarObject> expectedArray = tempResult.getResults().get("floating tasks");
		ArrayList<CalendarObject> actualArray = tempRH.processCommand(Ftask).getResults().get("floating tasks");
		assertEquals(actualArray.toString(), expectedArray.toString());
		tempRH.processCommand("clear file");

	}

	@Test
	public final void testRemoveEvent() {

		String event = "add event Dinner with mum from today at 9pm to tomorrow at 10pm";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		tempRH.processCommand("clear file");
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
		tempRH.processCommand("clear file");
	}

	@Test
	public final void testRemoveTask() {
		// To test
		String task = "add task Lunch with mum due 12/12/2015";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		tempRH.processCommand("clear file");
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
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		tempRH.processCommand("clear file");
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

	}

	@Test
	public final void testUpdateEventEndTime() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		// clear up the file
		tempRH.processCommand("clear file");

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
		tempRH.processCommand("clear file");
	}

	@Test
	public final void testUpdateEventEndDate() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		// clear up the file
		tempRH.processCommand("clear file");

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
		tempRH.processCommand("clear file");
	}

	@Test
	public final void testUpdateEventStartTime() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		// clear up the file
		tempRH.processCommand("clear file");

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
		tempRH.processCommand("clear file");
	}

	@Test
	public final void testUpdateEventStartDate() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		// clear up the file
		tempRH.processCommand("clear file");

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
		tempRH.processCommand("clear file");
	}

	@Test
	public final void testUpdateEventName() {
		// To test - initialize test.txt
		String event = "add event Dinner with mum from 22/11/2015 at 19:00 to 24/11/2015 at 19:00";
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		// clear up the file
		tempRH.processCommand("clear file");

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
		tempRH.processCommand("clear file");
	}

	@Test
	public final void testUpdateTaskName() {
		// To test - initialize test.txt
		String task = "add task Gymming due 12/12/2015";
		String fileName = "textfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		// clear up the file
		tempRH.processCommand("clear file");

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
		tempRH.processCommand("clear file");
	}

	@Test
	public final void testUpdateTaskDue() {
		// To test - initialize test.txt
		String task = "add task Gymming due 12/12/2015";
		String fileName = "textfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		// clear up the file
		tempRH.processCommand("clear file");

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
		tempRH.processCommand("clear file");

	}

	@Test
	public final void testDisplay() {
		String fileName = "testfile.txt";
		RequestHandler tempRH = RequestHandler.getInstance();
		tempRH.initialize(fileName);
		// String checkStr = "You have no event today\n" + "You have no upcoming
		// event\n" + "These are the list of past events\n" + "\n"
		// + "1) do something From: Saturday, 10/10/2015 10:00 To: Tuesday,
		// 10/11/2015 11:00 [ID:0] ";

		assertTrue(tempRH.processCommand("display events").isDisplayResult());
		assertTrue(tempRH.processCommand("display events").isSuccess());
		// HashMap<String, ArrayList<CalendarObject>> hm = new HashMap<String,
		// ArrayList<CalendarObject>>();

	}

}
