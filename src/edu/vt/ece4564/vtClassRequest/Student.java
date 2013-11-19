package edu.vt.ece4564.vtClassRequest;

import java.util.HashMap;
import java.util.Map;

public class Student {

	private Map<String,String> courseHistory;
	private String pid;
	private String passwordHash;
	private String major;
	
	public Student() {
	private String latestDars;

	public String getLatestDars()
    {
        return latestDars;
    }

    public void setLatestDars(String latestDars)
    {
        this.latestDars = latestDars;
    }

    public Student() {
		courseHistory = new HashMap<String,String>();
		pid = null;
		passwordHash = null;
		major = null;
	}
	
	public Student(String pid, String passwordHash, String major) {
		this.pid = pid;
		this.passwordHash = passwordHash;
		this.major = major;
	}
	
	public void addCourseToHistory(String course, String grade) {
		courseHistory.put(course, grade);
	}

	public Map<String, String> getCourseHistory() {
		return courseHistory;
	}

	public void setCourseHistory(Map<String, String> courseHistory) {
		this.courseHistory = courseHistory;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
}
