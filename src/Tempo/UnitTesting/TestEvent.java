package Tempo.UnitTesting;
/*
 * UNIT TESTS FOR TEMPO.
 */

import Tempo.CalendarObjects.Event;
import Tempo.Commands.Result;
import Tempo.Logic.Calendar;
import Tempo.Logic.RequestHandler;
import junit.framework.TestCase;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Queue;

import org.junit.After;
import org.junit.Test;

public class TestEvent extends TestCase {

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public final void testUpdateName() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		tempEvent.update("name", "hello");
		assertEquals("hello", tempEvent.getName());
		
	}
	
	@Test
	public final void testUpdateStartDate() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		tempEvent.update("start date", "12/12/2015");
		assertEquals("12/12/2015", tempEvent.getStartDate());
		
	}
	
	@Test
	public final void testUpdateStartTime() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		tempEvent.update("start time", "23:59");
		assertEquals("23:59", tempEvent.getStartTime());
		
	}
	
	@Test
	public final void testUpdateEndDate() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		tempEvent.update("end date", "30/12/2015");
		assertEquals("30/12/2015", tempEvent.getEndDate());
		
	}
	
	@Test
	public final void testUpdateEndTime() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		tempEvent.update("end time", "12:00");
		assertEquals("12:00", tempEvent.getEndTime());
		
	}
	
	@Test
	public final void testGetSeries() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		assertEquals(0, tempEvent.getSeriesIndex());
		
	}
	
	@Test
	public final void testGetEndTime() {
//		String fileName = "testfile.txt";
//		RequestHandler tempRH = RequestHandler.getInstance();
//		tempRH.initialize(fileName);
//		Calendar tempCal = tempRH.getCalendar();
		
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		Event tempEvent1 = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/23:59");
		ArrayList<String> actualArray = new ArrayList<String>(); 
		actualArray.add(tempEvent.getEndTime());
		actualArray.add(tempEvent1.getEndTime());
		
		
		
		ArrayList<String> expectedArray = new ArrayList<String>();
		expectedArray.add("19:00");
		expectedArray.add("23:59");
		assertEquals(expectedArray, actualArray);
		
	}

}