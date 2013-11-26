package edu.vt.ece4564.vtClassRequest;

import java.util.ArrayList;

public class Schedule {

	private ArrayList<Course> courses;
	private int totalCredits;
	
	public Schedule() {
		totalCredits = 0;
		courses = new ArrayList<Course>();
	}
	
	public boolean addCourse(Course c) {
		for(Course cur : courses) { // Checks to make sure there are no time conflicts
			if(cur.overlaps(c)) return false;
		}
		courses.add(c);
		totalCredits += c.getCredits();
		return true;
	}
	
	public ArrayList<Course> getCourses() {
		return courses;
	}
	
	public void setCourses(ArrayList<Course> courses) {
		this.courses.clear();
		totalCredits = 0;
		for(int i = 0; i < courses.size(); ++i)
			addCourse(courses.get(i));
	}
	
	public Schedule clone() {
		Schedule s = new Schedule();
		s.setCourses(courses);
		return s;
	}
	
	public int getTotalNumberOfCredits() {
		return totalCredits;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Credits: " + this.totalCredits + "\n");
		for(Course c : courses) {
			sb.append(c.getSubj() + "-" + c.getCrse() + ": ");
			if(c.getTime() != null) sb.append(c.getTime().toString());
			if(c.getAdditionalTime() != null) sb.append(" & " + c.getAdditionalTime().toString());
			sb.append("\n");
		}
		return sb.toString().substring(0,sb.toString().length()-1);
	}
}
