package edu.vt.ece4564.vtClassRequest;

import java.io.Serializable;

public class Course implements Serializable{

	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRIDAY = 5;

	private String name;
	private String subj;
	private String crse;
	private String crn;
	private String location;
	private int startTime;
	private int endTime;
	private int days[];
	private int additionalStartTime;
	private int additionalEndTime;
	private int additionalDays[];
	private String additionalLocation;
	private String instructor;
	private Course prereqs[];
	private String classRestrictions;
	private int credits;
	private int id;

	public Course() {
		// Initialize unnecessary stuff
		this.name = null;
		this.subj = null;
		this.crse = null;
		this.credits = -1;
		this.crn = null;
		this.startTime = -1;
		this.endTime = -1;
		this.days = null;
		this.location = null;
		this.additionalDays = null;
		this.additionalStartTime = -1;
		this.additionalEndTime = -1;
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
		this.startTime = -1;
		this.endTime = -1;
		this.days = null;
		this.location = null;
		this.additionalDays = null;
		this.additionalStartTime = -1;
		this.additionalEndTime = -1;
		this.additionalLocation = null;
		this.instructor = null;
		this.prereqs = null;
		this.classRestrictions = null;
	}

	   public Course(String subj, String crse) {
	        this.name = null;
	        this.subj = subj;
	        this.crse = crse;
	        this.credits = -1;

	        // Initialize unnecessary stuff
	        this.crn = null;
	        this.startTime = -1;
	        this.endTime = -1;
	        this.days = null;
	        this.location = null;
	        this.additionalDays = null;
	        this.additionalStartTime = -1;
	        this.additionalEndTime = -1;
	        this.additionalLocation = null;
	        this.instructor = null;
	        this.prereqs = null;
	        this.classRestrictions = null;
	    }

	public Course(String name, String subj, String crse, String crn, int credits, int startTime, int endTime, int[] days) {
		this.name = name;
		this.subj = subj;
		this.crse = crse;
		this.crn = crn;
		this.credits = credits;
		this.startTime = startTime;
		this.endTime = endTime;
		this.days = days;

		// Initialize unnecessary stuff
		this.location = null;
		this.additionalDays = null;
		this.additionalStartTime = -1;
		this.additionalEndTime = -1;
		this.additionalLocation = null;
		this.instructor = null;
		this.prereqs = null;
		this.classRestrictions = null;
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

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int[] getDays() {
		return days;
	}

	public void setDays(int[] days) {
		this.days = days;
	}

	public int getAdditionalStartTime() {
		return additionalStartTime;
	}

	public void setAdditionalStartTime(int additionalStartTime) {
		this.additionalStartTime = additionalStartTime;
	}

	public int getAdditionalEndTime() {
		return additionalEndTime;
	}

	public void setAdditionalEndTime(int additionalEndTime) {
		this.additionalEndTime = additionalEndTime;
	}

	public int[] getAdditionalDays() {
		return additionalDays;
	}

	public void setAdditionalDays(int[] additionalDays) {
		this.additionalDays = additionalDays;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Course: " + name + "\n" +
											 subj + "-" + crse + "\n" +
											 "CRN: " + crn + "\n");

		if(startTime != -1 && endTime != -1 && days != null) {
			sb.append(daysToString(days) + " " + startTime / 60 + ":" + startTime % 60 + "-" + endTime / 60 + ":" + endTime % 60 + "\n");
		}

		if(additionalStartTime != -1 && additionalEndTime != -1 && additionalDays != null) {
			sb.append("Additional Times: " + daysToString(additionalDays) + " " + additionalStartTime / 60 + ":" +
					additionalStartTime % 60 + "-" + additionalEndTime / 60 + ":" + additionalEndTime % 60 + "\n");
		}

		if(location != null) {
			sb.append("Location: " + location + "\n");
		}

		sb.append("Instructor: " + instructor + "\n" +
				  "Credits: " + credits);

		return sb.toString();
	}

	private String daysToString(int[] days) {
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