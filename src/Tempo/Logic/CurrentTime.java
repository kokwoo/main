package Tempo.Logic;
//@@author A0125303X
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CurrentTime {
	private static Calendar _date;
	private static Calendar cal = Calendar.getInstance();
	private static DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
	private static DateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static DateFormat time = new SimpleDateFormat("HH:mm");

	public CurrentTime() {
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
	
	public String getDate(){
		//System.out.println(df.format(cal.getTime()));
		
		return date.format(cal.getTime());
	}
	
	public String getDateAndTime() {
		return dateTime.format(cal.getTime());
	}
	
	public String getHoursAndMin() {
		return time.format(cal.getTime());
	}
	
//	public String getDate() {
//		CurrentDateAndTime date = new CurrentDateAndTime();
//		return date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
//	}

}
