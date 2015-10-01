package Tempo;

//import java.util.Date;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CurrentDateAndTime {
	private static Calendar _date;

	public CurrentDateAndTime() {
		Calendar date = new GregorianCalendar();
		_date = date;
	}

	public int getHour() {
		return _date.get(Calendar.HOUR);
	}

	public int getMinute() {
		return _date.get(Calendar.MINUTE);
	}

	public int getSecond() {
		return _date.get(Calendar.SECOND);
	}

	public int getYear() {
		return _date.get(Calendar.YEAR);
	}

	public int getMonth() {
		return _date.get(Calendar.MONTH);
	}

	public int getDay() {
		return _date.get(Calendar.DAY_OF_MONTH);
	}

}
