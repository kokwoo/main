package Tempo.UnitTesting;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import Tempo.CalendarObjects.Task;

public class TestTasks {

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public final void testGetName() {
		Task tempTask = new Task(0, 0, "Dinner with mum", "done", "22/12/2015");
		assertEquals("Dinner with mum", tempTask.getName());
	}
	
	@Test
	public final void testGetIndex() {
		Task tempTask = new Task(0, 0, "Dinner with mum", "done", "22/12/2015");
		assertEquals(0, tempTask.getIndex());
	}
	
	@Test
	public final void testGetSeriesIndex() {
		Task tempTask = new Task(0, 0, "Dinner with mum", "done", "22/12/2015");
		assertEquals(0, tempTask.getSeriesIndex());
	}
	
	@Test
	public final void testGetDueDate() {
		Task tempTask = new Task(0, 0, "Dinner with mum", "done", "22/12/2015");
		assertEquals("Tuesday, 22/12/2015", tempTask.getDueDate());
	}
	
	@Test
	public final void testGetDueDateSimplified() {
		Task tempTask = new Task(0, 0, "Dinner with mum", "done", "22/12/2015");
		assertEquals( "22/12/2015", tempTask.getDueDateSimplified());
	}

	@Test
	public final void testUpdateName() {
		Task tempTask = new Task(0, 0, "Dinner with mum", "done", "22/12/2015");
		tempTask.update("name", "changed");
		assertEquals("changed", tempTask.getName());
		
		Task tempTask1 = new Task(0, 0, "Dinner with mum", "22/12/2015");
		tempTask1.update("name", "changed");
		assertEquals("changed", tempTask1.getName());
	}
	
	@Test
	public final void testUpdateDueDate() {
		Task tempTask = new Task(0, 0, "Dinner with mum", "done", "22/12/2015");
		tempTask.update("due", "23/12/2015");
		assertEquals( "23/12/2015", tempTask.getDueDateSimplified());
		
		Task tempTask1 = new Task(0, 0, "Dinner with mum", "22/12/2015");
		tempTask1.update("due", "23/12/2015");
		assertEquals( "23/12/2015", tempTask1.getDueDateSimplified());
	}

	@Test
	public final void testCompareTo() {
		Task tempTask = new Task(0, 0, "Dinner with mum", "done", "22/12/2015");
		Task tempTask1 = new Task(0, 0, "Dinner with mum", "done", "21/12/2015");
		Task tempTask2 = new Task(0, 0, "Dinner with mum", "done", "23/12/2015");
		
		assertEquals(1, tempTask.compareTo(tempTask1));
		assertEquals(0, tempTask.compareTo(tempTask));
		assertEquals(-1, tempTask.compareTo(tempTask2));
		
	}


}
