package edu.vt.ece4564.shared;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class CourseTime implements Serializable {

	public static final int NULL_TIME = -1;

	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRIDAY = 5;

	private int startTime;
	private int endTime;
	private ArrayList<Integer> days;

	public CourseTime() {
		startTime = -1;
		endTime = -1;
		days = new ArrayList<Integer>();
	}

	public CourseTime(int startTime, int endTime, Collection<? extends Integer> days) {
		this.startTime = startTime;
		this.endTime = endTime;
		for(int d : days) addDay(d);
	}

	public CourseTime(int startTime, int endTime, int[] days) {
		this.startTime = startTime;
		this.endTime = endTime;
		for(int d : days) addDay(d);
	}

	/***
	 * overlaps()
	 *
	 * Returns whether the two CourseTime's overlap
	 *
	 * @param c
	 * @return true if overlap, false otherwise
	 */
	public boolean overlaps(CourseTime c) {
		int[] cdays = c.getDays();
		for(int i : cdays) {
			for(int j : this.days) {
				if(i == j) {
					if(c.getStartTime() >= startTime && c.getStartTime() <= endTime) return true;
					else if(c.getEndTime() <= endTime && c.getEndTime() >= startTime) return true;
					else return false;
				}
			}
		}
		return false;
	}

	public void setStartTime(int time) {
		startTime = time;
	}

	public void setEndTime(int time) {
		endTime = time;
	}

	/***
	 * setStartTime(String)
	 *
	 * String should be formatted as H:M or H:M[AM|PM] where H is hours and M is minutes
	 *
	 * @param time
	 * @return
	 */
	public boolean setStartTime(String time) {
		try {
			startTime = getMinutes(time);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/***
	 * setEndTime(String)
	 *
	 * String should be formatted as H:M or H:M[AM|PM] where H is hours and M is minutes
	 *
	 * @param time
	 * @return
	 */
	public boolean setEndTime(String time) {
		try {
			endTime = getMinutes(time);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void addDays(Collection<? extends Integer> days) {
		for(int d : days) addDay(d);
	}

	/***
	 * addDays(String)
	 *
	 * Should include a letter representing each day separated by a space.
	 * Letters are:
	 * 	M = Monday
	 *  T = Tuesday
	 *  W = Wednesday
	 *  R = Thursday
	 *  F = Friday
	 * Example of valid input: "M W F" or "T R"
	 *
	 * @param daystr
	 * @return
	 */
	public boolean addDays(String daystr) {
		try {
			for(int d : parseDays(daystr)) addDay(d);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void addDay(int day) {
		for(int i = 0; i < days.size(); ++i) {
			if(days.get(i) == day) return;
		}
		days.add(day);
	}

	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public int[] getDays() {
		int[] dayArray = new int[days.size()];
		for(int i = 0; i < days.size(); ++i)
			dayArray[i] = days.get(i);
		return dayArray;
	}

	private int getMinutes(String time) throws Exception {
		int t = 60*(Integer.parseInt(time.substring(0,time.indexOf(':'))));
		t += (Integer.parseInt(time.substring(time.indexOf(':') + 1,time.indexOf(':') + 3)));
		if(time.contains("PM") && Integer.parseInt(time.substring(0,time.indexOf(':'))) != 12) t += 60*12;
		return t;
	}

	private ArrayList<Integer> parseDays(String daystr) throws Exception {
		ArrayList<Integer> darray = new ArrayList<Integer>();
		String[] strarray = daystr.split(" ");
		for(int i = 0; i < strarray.length; ++i) {
			int d = -1;
			if (strarray[i].equals("M")) d = MONDAY;
			else if (strarray[i].equals("T")) d = TUESDAY;
			else if (strarray[i].equals("W")) d = WEDNESDAY;
			else if (strarray[i].equals("R")) d = THURSDAY;
			else if (strarray[i].equals("F")) d = FRIDAY;
			darray.add(d);
		}
		return darray;
	}
	@Override
	public boolean equals(Object other) {
	    if (other instanceof String) {
	        String o = (String)other;
	        if (this.days.contains(dayStringToInt(o))) return true;
	        else return false;
	    }
	    else return false;
	}
	private int dayStringToInt(String day) {
	    int dayr = 0;
	    if (day.toLowerCase().equals("monday")) dayr = 1;
	    else if (day.toLowerCase().equals("tuesday")) dayr = 2;
	    else if (day.toLowerCase().equals("wednesday")) dayr = 3;
	    else if (day.toLowerCase().equals("thursday")) dayr = 4;
	    else if (day.toLowerCase().equals("friday")) dayr = 5;
	    return dayr;
	}

	public String toString() {
		int start = startTime / 60;
		int end = endTime / 60;
		String startStr, endStr;
		String startChar = "";
		String endChar = "";
		if (start > 12) {
			start = start - 11;
			startStr = "PM";
		} else {
			startStr = "AM";
		}
		if (end > 12) {
			end = end - 11;
			endStr = "PM";
		} else {
			endStr = "AM";
		}

		if (((int) (Math.log10(startTime % 60) + 1)) < 2) {
			startChar = "0";
		}
		if (((int) (Math.log10(endTime % 60) + 1)) < 2) {
			endChar = "0";
		}
		return daysToString(days) + " " + start + ":" + startTime % 60
				+ startChar + startStr + "-" + end + ":" + endTime % 60
				+ endChar + endStr;
	}

	private String daysToString(Collection<? extends Integer> days) {
		StringBuilder sb = new StringBuilder();
		for(int d : days) {
			switch(d) {
			case MONDAY:
				sb.append("M ");
				break;
			case TUESDAY:
				sb.append("T ");
				break;
			case WEDNESDAY:
				sb.append("W ");
				break;
			case THURSDAY:
				sb.append("R ");
				break;
			case FRIDAY:
				sb.append("F ");
				break;
			}
		}
		return sb.toString();
	}
}
