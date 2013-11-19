package edu.vt.ece4564.vtClassRequest.Date;

public class Date {

	private Day day;
	private int hour;
	private int minute;

	public Date(Day day, int hour, int minute) throws DateException {
		if (hour < 0 || hour > 23) throw new DateException();
		if (minute < 0 || minute > 59) throw new DateException();
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) throws DateException {
		if (hour < 0 || hour > 23) throw new DateException();
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) throws DateException {
		if (minute < 0 || minute > 59) throw new DateException();
		this.minute = minute;
	}
}
