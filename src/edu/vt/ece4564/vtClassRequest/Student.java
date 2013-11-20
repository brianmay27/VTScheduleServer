package edu.vt.ece4564.vtClassRequest;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Student extends Persistable implements Serializable{

	private Map<String,String> courseHistory;
	private String pid;
	private String passwordHash;
	private String major;
	private String latestDars;
	private ParseDars classesNeeded;
	private HashMap<String, ArrayList<Course>> ClassMap;

	public HashMap<String, ArrayList<Course>> getClassMap()
    {
        return ClassMap;
    }

    public void setClassMap(HashMap<String, ArrayList<Course>> classMap)
    {
        ClassMap = classMap;
        Persist(this);
    }

    public ParseDars getClassesNeeded()
    {
        return classesNeeded;
    }

    public void setClassesNeeded(ParseDars classesNeeded)
    {
        this.classesNeeded = classesNeeded;
        Persist(this);
    }

    public String getLatestDars()
    {
        return latestDars;
    }

    public void setLatestDars(String latestDars)
    {
        this.latestDars = latestDars;
        Persist(this);
    }

    public Student() {
        super();
		courseHistory = new HashMap<String,String>();
		pid = null;
		passwordHash = null;
		major = null;
	}

	public Student(String pid, String passwordHash, String major) {
	    super();
		this.pid = pid;
		this.passwordHash = passwordHash;
		this.major = major;
	}

	public void addCourseToHistory(String course, String grade) {
		courseHistory.put(course, grade);
		Persist(this);
	}

	public Map<String, String> getCourseHistory() {
		return courseHistory;
	}

	public void setCourseHistory(Map<String, String> courseHistory) {
		this.courseHistory = courseHistory;
		Persist(this);
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
		Persist(this);
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
		Persist(this);
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
		Persist(this);
	}
}
