package edu.vt.ece4564.vtClassRequest;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Student extends Persistable implements Serializable {

	private Map<String,String> courseHistory;
	private String pid;
	private int passwordHash;
	private String major;
	private String latestDars;
	private ParseDars classesNeeded;
	private HashMap<String, ArrayList<Course>> ClassMap;
	private HashMap<Integer, ArrayList<Schedule>> schedules = null;

	public ArrayList<Schedule> getSchedules(int id)
    {
	    if (schedules == null) return null;
        return schedules.get(id);
    }

    public synchronized void setSchedules(ArrayList<Schedule> schedules, int ID)
    {
        if (this.schedules == null) this.schedules = new HashMap<Integer, ArrayList<Schedule>>();
        this.schedules.put(ID, schedules);
        Persist(this);
    }

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

    public synchronized void setClassesNeeded(ParseDars classesNeeded)
    {
        this.classesNeeded = classesNeeded;
        Persist(this);
    }

    public String getLatestDars()
    {
        return latestDars;
    }

    public synchronized void setLatestDars(String latestDars)
    {
        this.latestDars = latestDars;
        Persist(this);
    }

    public Student() {
        super();
		courseHistory = new HashMap<String,String>();
		pid = null;
		passwordHash = 0;
		major = null;
	}

	public Student(String pid, int passwd, String major) {
	    super();
		this.pid = pid;
		this.passwordHash = passwd;
		this.major = major;
	}

	public synchronized void addCourseToHistory(String course, String grade) {
		courseHistory.put(course, grade);
		Persist(this);
	}

	public Map<String, String> getCourseHistory() {
		return courseHistory;
	}

	public synchronized void setCourseHistory(Map<String, String> courseHistory) {
		this.courseHistory = courseHistory;
		Persist(this);
	}

	public String getPid() {
		return pid;
	}

	public synchronized void setPid(String pid) {
		this.pid = pid;
		Persist(this);
	}

	public int getPasswordHash() {
		return passwordHash;
	}

	public synchronized void setPasswordHash(int passwordHash) {
		this.passwordHash = passwordHash;
		Persist(this);
	}

	public String getMajor() {
		return major;
	}

	public synchronized void setMajor(String major) {
		this.major = major;
		Persist(this);
	}
}
