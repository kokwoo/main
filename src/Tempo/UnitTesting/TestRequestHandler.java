package Tempo.UnitTesting;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Test;

import Tempo.Commands.Result;
import Tempo.Logic.Calendar;	
import Tempo.Logic.RequestHandler;

public class TestRequestHandler {

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testInit() {
		 String fileName = "testfile.txt";
		 RequestHandler tempRH = RequestHandler.getInstance();
		 assertEquals(fileName+" is ready to use.", tempRH.initialize(fileName));
	}
	
	@Test
	public final void testProcessCalendar() {
		 String fileName = "testfile.txt";
		 RequestHandler tempRH = RequestHandler.getInstance();
		 tempRH.initialize(fileName);
		 Result result = new Result("cmd", false, null);
		 assertEquals(result.getResults(), tempRH.processCommand("cmd").getResults());
		 assertEquals(result.getCmdPerformed(), tempRH.processCommand("cmd").getCmdPerformed());
		 assertEquals(result.isSuccess(), tempRH.processCommand("cmd").isSuccess());
		 
	}
	

}
