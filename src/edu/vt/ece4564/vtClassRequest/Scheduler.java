package edu.vt.ece4564.vtClassRequest;

import java.util.ArrayList;

public class Scheduler {

	/***
	 * makeSchedules()
	 * 
	 * Makes different schedules between minCredits and maxCredits from the courses provided in courses.
	 * This method does not do any prereq checking.
	 * 
	 * @param minCredits
	 * @param maxCredits
	 * @param courses
	 * @return list of schedules
	 */
	public static ArrayList<Schedule> makeSchedules(int minCredits, int maxCredits, ArrayList<Course> courses) {
		// Add every combination of courses
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		for(int j = 0; j < courses.size(); ++j) {
			Schedule schedule = new Schedule();
			for(int i = j; i < courses.size(); ++i) {
				if(schedule.getTotalNumberOfCredits() >= maxCredits) break; // Break from unnecessary loop
				else if(schedule.addCourse(courses.get(i))) {
					// Add if within credit threshold
					if(schedule.getTotalNumberOfCredits() >= minCredits && schedule.getTotalNumberOfCredits() <= maxCredits) {
						schedules.add(schedule.clone());
					}
				}
			}
		}
		return schedules;
	}
}
