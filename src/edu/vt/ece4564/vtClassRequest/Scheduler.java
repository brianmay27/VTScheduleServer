package edu.vt.ece4564.vtClassRequest;

import java.util.ArrayList;

public class Scheduler {

	public static ArrayList<Schedule> makeSchedules(int minCredits, int maxCredits, ArrayList<Course> courses) {
		// Add every combination of courses
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		for(int j = 0; j < courses.size(); ++j) {
			Schedule schedule = new Schedule();
			for(int i = j; i < courses.size(); ++i) {
				if(schedule.addCourse(courses.get(i))) {
					schedules.add(schedule.clone());
				}
			}
		}
		
		// Remove unwanted schedules
		for(int i = 0; i < schedules.size(); ++i) {
			if(schedules.get(i).getTotalNumberOfCredits() < minCredits || schedules.get(i).getTotalNumberOfCredits() > maxCredits) {
				schedules.remove(i--);
			}
		}
		return schedules;
	}
}
