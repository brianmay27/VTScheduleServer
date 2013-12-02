package edu.vt.ece4564.shared;


import java.io.Serializable;
import java.util.ArrayList;

public class Schedule implements Serializable{

    //private static final long serialVersionUID  = 0x98F22BF5;
    private ArrayList<Course> courses;
	private int totalCredits;
	private int hashID;

	public Schedule() {
		totalCredits = 0;
		courses = new ArrayList<Course>();
	}

	/***
	 * addCourse()
	 *
	 * Adds a course to the schedule if there are no time/course conflicts.
	 *
	 * @param c
	 * @return true if successful, false if there are time/course conflicts
	 */
	public boolean addCourse(Course c) {
		// Checks to make sure there are no time conflicts and they are different courses
		for(Course cur : courses) {
			if(cur.overlaps(c)) return false;
			else if(cur.getSubj().equalsIgnoreCase(c.getSubj()) && cur.getCrse().equalsIgnoreCase(c.getCrse())) return false;
		}

		// Adds course
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
			sb.append(c.getCrn() + ": " + c.getSubj() + "-" + c.getCrse() + ": ");
			if(c.getTime() != null) sb.append(c.getTime().toString());
			if(c.getAdditionalTime() != null) sb.append(" & " + c.getAdditionalTime().toString());
			sb.append("\n");
		}
		return sb.toString().substring(0,sb.toString().length()-1);
	}
}
