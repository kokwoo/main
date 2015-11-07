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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Queue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class TestEvent extends TestCase {
	private static final String DELIMETER = "!!";
	private static final String DATE_DELIMETER = "/";

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testUpdateName() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		tempEvent.update("name", "hello");
		assertEquals("hello", tempEvent.getName());
		tempEvent.update("name", "!!!!!!!!!!!!!!!!!!!!!!!!!!");
		assertEquals("!!!!!!!!!!!!!!!!!!!!!!!!!!", tempEvent.getName());
	}

	@Test
	public final void testUpdateStartDate() throws ParseException {
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
	public final void testGetIndex() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		assertEquals(0, tempEvent.getIndex());

	}

	@Test
	public final void testGetSeries() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		assertEquals(0, tempEvent.getSeriesIndex());

	}

	@Test
	public final void testName() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		assertEquals("Dinner with mum", tempEvent.getName());
	}

	@Test
	public final void testGetStartDate() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		assertEquals("22/12/2015", tempEvent.getStartDate());
	}

	@Test
	public final void testGetStartTime() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "22/12/2015/19:00", "23/12/2015/19:00");
		assertEquals("19:00", tempEvent.getStartTime());
	}

	@Test
	public final void testGetStartDateTime() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "07/11/2015/19:00", "23/12/2015/19:00");
		assertEquals("Saturday, 07/11/2015 19:00", tempEvent.getStartDateTime());
	}

	@Test
	public final void testGetStartDateTimeSimplified() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "07/11/2015/19:00", "23/12/2015/19:00");
		assertEquals("07/11/2015/19:00", tempEvent.getStartDateTimeSimplified());
	}
	
	@Test
	public final void testGetEndDate() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "21/12/2015/19:00", "22/12/2015/19:00");
		assertEquals("22/12/2015", tempEvent.getEndDate());
	}
	
	@Test
	public final void testGetEndTime() {
		// String fileName = "testfile.txt";
		// RequestHandler tempRH = RequestHandler.getInstance();
		// tempRH.initialize(fileName);
		// Calendar tempCal = tempRH.getCalendar();

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

	@Test
	public final void testGetEndDateTime() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "02/11/2015/19:00", "07/11/2015/19:00");
		assertEquals("Saturday, 07/11/2015 19:00", tempEvent.getEndDateTime());
	}

	@Test
	public final void testGetEndDateTimeSimplified() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "02/11/2015/19:00", "07/11/2015/19:00");
		assertEquals("07/11/2015/19:00", tempEvent.getEndDateTimeSimplified());
	}	

	
	@Test
	public final void testClashesWith() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "07/11/2015/19:00", "07/11/2015/19:00");
		assertFalse(tempEvent.clashesWith(tempEvent));
		
		Event tempEvent1 = new Event(0, 0, "Dinner with mum", "08/11/2015/19:00", "07/11/2015/19:00");
		assertFalse(tempEvent1.clashesWith(tempEvent1));
		
		Event tempEvent2 = new Event(0, 0, "Dinner with mum", "07/11/2015/19:00", "09/11/2015/19:00");
		assertTrue(tempEvent2.clashesWith(tempEvent2));
				
	}
	
	@Test
	public final void testToString() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "07/11/2015/19:00", "23/12/2015/19:00");
		assertEquals("0!!0!!Dinner with mum!!07/11/2015/19:00!!23/12/2015/19:00", tempEvent.getIndex() + DELIMETER + tempEvent.getSeriesIndex()+ DELIMETER + tempEvent.getName() + DELIMETER + tempEvent.getStartDate() + DATE_DELIMETER + tempEvent.getStartTime() + DELIMETER + tempEvent.getEndDate()+ DATE_DELIMETER +tempEvent.getEndTime());
	}
	
	
	@Test
	public final void testCompareTo() {
		Event tempEvent = new Event(0, 0, "Dinner with mum", "07/11/2015/19:00", "23/12/2015/19:00");
		Event tempEvent1 = new Event(0, 0, "Dinner with mum", "11/11/2015/19:00", "23/12/2015/19:00");
		Event tempEvent2 = new Event(0, 0, "Dinner with mum", "06/11/2015/19:00", "23/12/2015/19:00");
		assertEquals(-1, tempEvent.compareTo(tempEvent1));
		assertEquals(0, tempEvent.compareTo(tempEvent));
		assertEquals(1, tempEvent.compareTo(tempEvent2));
	}
	

}