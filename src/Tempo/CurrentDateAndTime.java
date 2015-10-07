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

	public String getMonth() {
		int month = _date.get(Calendar.MONTH)+1;
		if (month < 10) {
			return "0" + month;
		}
		return month + "";
	}

	public String getDay() {
		int day = _date.get(Calendar.DAY_OF_MONTH);
		if (day < 10) {
			return "0" + day;
		}
		return day + "";
	}
	
	public String getDate() {
		CurrentDateAndTime date = new CurrentDateAndTime();
		return date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
	}

}
