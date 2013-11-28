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
		addToSchedules(schedules,new Schedule(),0,minCredits,maxCredits,courses);
		return schedules;
	}
	
	private static void addToSchedules(ArrayList<Schedule> schedules, Schedule s, int pos, int minCredits, int maxCredits, ArrayList<Course> courses) {
		if(s.getTotalNumberOfCredits() >= maxCredits) return;
		
		// Terminator
		if(pos < courses.size()) {
			// Call next function
			addToSchedules(schedules,s.clone(),pos+1,minCredits,maxCredits,courses);
			
			// Add to schedules if it meets criteria
			if(s.addCourse(courses.get(pos))) {
				if(s.getTotalNumberOfCredits() >= minCredits && s.getTotalNumberOfCredits() <= maxCredits) {
					schedules.add(s.clone());
				}
				addToSchedules(schedules,s.clone(),pos+1,minCredits,maxCredits,courses);
			}
		}
	}
}
