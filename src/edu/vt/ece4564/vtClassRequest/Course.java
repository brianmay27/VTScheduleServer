package edu.vt.ece4564.vtClassRequest;

import java.io.Serializable;

public class Course implements Serializable {
	
	private static final long serialVersionUID = 8026434170877104021L;

	private String name;
	private String subj;
	private String crse;
	private String crn;
	private CourseTime time;
	private String location;
	private CourseTime additionalTime;
	private String additionalLocation;
	private String instructor;
	private Course prereqs[];
	private String classRestrictions;
	private int credits;
	private int id;
	private String type;
	
	public Course() {
		// Initialize unnecessary stuff
		this.name = null;
		this.subj = null;
		this.crse = null;
		this.credits = -1;
		this.crn = null;
		this.time = null;
		this.location = null;
		this.additionalTime = null;
		this.additionalLocation = null;
		this.instructor = null;
		this.prereqs = null;
		this.classRestrictions = null;
	}
	
	public Course(String subj, String crse) {
		this.subj = subj;
		this.crse = crse;
		
		// Initialize unnecessary stuff
		this.name = null;
		this.credits = -1;
		this.crn = null;
		this.location = null;
		this.additionalTime = null;
		this.additionalLocation = null;
		this.instructor = null;
		this.prereqs = null;
		this.classRestrictions = null;
	}
	
	public Course(String name, String subj, String crse, int credits) {
		this.name = name;
		this.subj = subj;
		this.crse = crse;
		this.credits = credits;
		
		// Initialize unnecessary stuff
		this.crn = null;
		this.location = null;
		this.additionalTime = null;
		this.additionalLocation = null;
		this.instructor = null;
		this.prereqs = null;
		this.classRestrictions = null;
	}
	
	public Course(String name, String subj, String crse, String crn, int credits, CourseTime time) {
		this.name = name;
		this.subj = subj;
		this.crse = crse;
		this.crn = crn;
		this.credits = credits;
		this.time = time;
		
		// Initialize unnecessary stuff
		this.location = null;
		this.additionalTime = null;
		this.additionalLocation = null;
		this.instructor = null;
		this.prereqs = null;
		this.classRestrictions = null;
	}
	
	public boolean overlaps(Course c) {
		if(c.getType().startsWith("O") || type.startsWith("O")) return false; // Online class
		else if(c.getTime() == null || time == null) return true; // TODO change what happens if times are null and NOT online
		else if(c.getTime().overlaps(time)) return true;
		else if(additionalTime != null && c.getTime().overlaps(additionalTime)) return true;
		else if(c.getAdditionalTime() != null && c.getAdditionalTime().overlaps(time)) return true;
		else if(c.getAdditionalTime() != null && additionalTime != null && c.getAdditionalTime().overlaps(additionalTime)) return true;
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getClassRestrictions() {
		return classRestrictions;
	}

	public void setClassRestrictions(String classRestrictions) {
		this.classRestrictions = classRestrictions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public Course[] getPrereqs() {
		return prereqs;
	}

	public void setPrereqs(Course prereqs[]) {
		this.prereqs = prereqs;
	}

	public String getAdditionalLocation() {
		return additionalLocation;
	}

	public void setAdditionalLocation(String additionalLocation) {
		this.additionalLocation = additionalLocation;
	}
	
	public String getCrse() {
		return crse;
	}

	public void setCrse(String crse) {
		this.crse = crse;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public CourseTime getTime() {
		return time;
	}

	public void setTime(CourseTime time) {
		this.time = time;
	}

	public CourseTime getAdditionalTime() {
		return additionalTime;
	}

	public void setAdditionalTime(CourseTime additionalTime) {
		this.additionalTime = additionalTime;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Course: " + name + "\n" +
											 subj + "-" + crse + "\n" +
											 "CRN: " + crn + "\n");
		
		if(type != null && !type.startsWith("O")) {
			if(time != null) {
				sb.append(time.toString() + "\n");
			}
			
			if(additionalTime != null) {
				sb.append("Additional Times:" + additionalTime.toString() + "\n");
			}
		}
		else if(type != null) {
			sb.append("Online class\n");
		}
		
		if(location != null) {
			sb.append("Location: " + location + "\n");
		}
		
		sb.append("Instructor: " + instructor + "\n" +
				  "Credits: " + credits);
		
		return sb.toString();
	}
}