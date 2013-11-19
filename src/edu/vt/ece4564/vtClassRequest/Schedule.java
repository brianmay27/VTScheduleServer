package edu.vt.ece4564.vtClassRequest;

import java.util.ArrayList;

public class Schedule {

	private ArrayList<Course> courses;
	private int totalCredits;

	public Schedule() {
		totalCredits = 0;
		courses = new ArrayList<Course>();
	}

	public void addCourse(Course c) {
		courses.add(c);
		totalCredits += c.getCredits();
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
		totalCredits = 0;
		for(int i = 0; i < this.courses.size(); ++i)
			totalCredits += this.courses.get(i).getCredits();
	}

	public int getTotalNumberOfCredits() {
		return totalCredits;
	}
}
