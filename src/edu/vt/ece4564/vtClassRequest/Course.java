package edu.vt.ece4564.vtClassRequest;

import edu.vt.ece4564.vtClassRequest.Date.Date;

public class Course {

	private String name;
	private String subj;
	private int crse;
	private int crn;
	private Date dates[];
	private String location;
	private String instructor;
	private int seats;
	private int capacity;
	private Course prereqs[];
	private String classRestrictions;
	private int credits;
	private int id;

	public Course(String name, String subj, int crse, int credits) {
		this.name = name;
		this.subj = subj;
		this.crse = crse;
		this.credits = credits;

		// Initialize unnecessary stuff
		this.crn = -1;
		this.dates = null;
		this.location = null;
		this.instructor = null;
		this.seats = -1;
		this.capacity = -1;
		this.prereqs = null;
		this.classRestrictions = null;
	}

	public Course(String name, String subj, int crse, int crn, int credits, Date[] dates) {
		this.name = name;
		this.subj = subj;
		this.crse = crse;
		this.crn = crn;
		this.credits = credits;
		System.arraycopy(dates, 0, this.dates, 0, dates.length);

		// Initialize unnecessary stuff
		this.location = null;
		this.instructor = null;
		this.seats = -1;
		this.capacity = -1;
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

	public int getCrse() {
		return crse;
	}

	public void setCrse(int crse) {
		this.crse = crse;
	}

	public int getCrn() {
		return crn;
	}

	public void setCrn(int crn) {
		this.crn = crn;
	}

	public Date[] getDates() {
		return dates;
	}

	public void setDates(Date[] dates) {
		this.dates = dates;
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

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
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
}