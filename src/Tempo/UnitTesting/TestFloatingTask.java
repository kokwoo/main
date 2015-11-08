package Tempo.UnitTesting;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import Tempo.CalendarObjects.FloatingTask;
import Tempo.CalendarObjects.Task;

public class TestFloatingTask {

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetName() {
		FloatingTask tempFT = new FloatingTask(0, 0, "Dinner", "done");
		assertEquals("Dinner", tempFT.getName());
	}
	
	@Test
	public final void testGetIndex() {
		FloatingTask tempFT = new FloatingTask(0, 0, "Dinner", "done");
		assertEquals(0, tempFT.getIndex());
	}
	
	@Test
	public final void testGetSeriesIndex() {
		FloatingTask tempFT = new FloatingTask(0, 0, "Dinner", "done");
		assertEquals(0, tempFT.getSeriesIndex());
	}
	
	@Test
	public final void testUpdateName() {
		FloatingTask tempFT = new FloatingTask(0, 0, "Dinner", "done");
		tempFT.update("name", "Lunch");
		assertEquals("Lunch", tempFT.getName());
	}
	
	@Test
	public final void testMarkDone() {
		FloatingTask tempFT = new FloatingTask(0, 0, "Dinner", "done");
		tempFT.markAsDone();
		assertTrue(tempFT.isDone());
	}
	
	@Test
	public final void testMarkUndone() {
		FloatingTask tempFT = new FloatingTask(0, 0, "Dinner", "done");
		tempFT.markAsUndone();
		assertFalse(tempFT.isDone());
	}
	
	@Test
	public final void testToString() {
		FloatingTask tempFT = new FloatingTask(0, 0, "Dinner", "done");
		assertEquals("0!!0!!Dinner!!false", tempFT.toString());
	}

}
