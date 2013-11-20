package edu.vt.ece4564.vtClassRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.vt.ece4564.vtClassRequest.Course;

public class TimetableScraper {

	private static final String TIMETABLE_URL = "https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest";
	private static final String COURSE_URL = "https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcComments";
	private static final String USER_AGENTS = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 Firefox/11.0";

	public TimetableScraper() {}

	/***
	 * getCourses()
	 *
	 * Builds an ArrayList of Course objects given a subject, crse, and termyear string.
	 *
	 * @param subj: ex. ECE, ENGR, ENGL, PHYS
	 * @param crse: course number (4 digits)
	 * @param termyear: Concatenation of year and term (spring=01,fall=09), ex. 201401
	 * @return ArrayList of course objects
	 */
	public static ArrayList<Course> getCourses(String subj, String crse, String termyear) {

		ArrayList<Course> courses = new ArrayList<Course>();

		try {
			Map<String, String> data = new HashMap<String, String>();
			data.put("CAMPUS", "0");
			data.put("TERMYEAR", termyear);
			data.put("CORE_CODE", "AR%");
			data.put("SCHDTYPE", "%");
			data.put("subj_code", subj);
			data.put("CRSE_NUMBER", crse);
			data.put("crn", "");
			data.put("open_only", "");
			data.put("BTN_PRESSED", "FIND class sections");
			data.put("inst_name", "");

			// Connect to timetable and parse everything
			Document timetableDoc = Jsoup.connect(TIMETABLE_URL).data(data).userAgent(USER_AGENTS).post();
			Elements entrys = timetableDoc.select("table.dataentrytable tr");

			Course c = new Course();
			for(int i = 1; i < entrys.size(); ++i) {
				Elements info = entrys.get(i).getElementsByTag("td");

				// Extract info
				if(!info.get(4).hasAttr("colspan")) {

					// Extract info
					String crn = info.get(0).text().trim().substring(0,5);
					if(crse.isEmpty()) {
						crse = info.get(1).text().trim().split("-")[1];
					}
					String name = info.get(2).text().trim();
					String credit = info.get(4).text().trim();
					String instructor = info.get(6).text().trim();
					String daystr = info.get(7).text().trim();
					String start = info.get(8).text().trim();
					String end = info.get(9).text().trim();
					String location = info.get(10).text().trim();

					// Get credits
					int creditnum = Integer.parseInt(credit);

					// Get times
					int startTime = getMinutes(start);
					int endTime = getMinutes(end);

					// Get days
					int[] days = parseDays(daystr);

					c = new Course(name, subj, crse, crn, creditnum, startTime, endTime, days);
					c.setInstructor(instructor);
					c.setLocation(location);

					// Get Prereqs
					Course[] prereqs = new Course[1];
					prereqs = getPrereqs(crn, termyear.substring(4), termyear.substring(0,4), subj, crse).toArray(prereqs);
					c.setPrereqs(prereqs);

					// Add to arraylist
					courses.add(c);
				}

				// Additional times
				else {
					String additionalDays = info.get(5).text().trim();
					String additionalBeginTime = info.get(6).text().trim();
					String additionalEndTime = info.get(7).text().trim();
					String additionalLocation = info.get(8).text().trim();

					// Update previous course
					if(c != null) {
						c.setAdditionalDays(parseDays(additionalDays));
						c.setAdditionalStartTime(getMinutes(additionalBeginTime));
						c.setAdditionalEndTime(getMinutes(additionalEndTime));
						c.setAdditionalLocation(additionalLocation);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

		return courses;
	}

	// Returns prereqs needed for a course
	public static ArrayList<Course> getPrereqs(String crn, String term, String year, String subj, String crse) {
		ArrayList<Course> prereqs = new ArrayList<Course>();

		// Make url
		String courseUrl = COURSE_URL + "?CRN=" + crn + "&TERM=" + term + "&YEAR=" + year +
				"&SUBJ=" + subj + "&CRSE=" + crse + "&history=N";

		// Go to url
		Document courseDoc;
		try {
			courseDoc = Jsoup.connect(courseUrl).userAgent(USER_AGENTS).get();
		} catch (IOException e) {
			System.err.println(e);
			return null;
		}

		// Parse document
		Elements prereqHtml = courseDoc.select("table.plaintable tr");
		for(Element e : prereqHtml) {
			if(e.toString().contains("Prerequisites")) {
				Elements prereqLinks = e.getElementsByTag("td").get(1).getElementsByTag("a");
				for(Element pe : prereqLinks) {
					String prereqstr = pe.text();
					Course temp = new Course(prereqstr.substring(0,prereqstr.indexOf(' ')), prereqstr.substring(prereqstr.indexOf(' ') + 1));
					prereqs.add(temp);
				}
				break;
			}
		}

		// Return array
		return prereqs;
	}

	private static int getMinutes(String time) {
		int t = 60*(Integer.parseInt(time.substring(0,time.indexOf(':'))));
		t += (Integer.parseInt(time.substring(time.indexOf(':') + 1,time.indexOf(':') + 3)));
		if(time.contains("PM")) t += 60*12;
		return t;
	}

	public static int[] parseDays(String daystr) {
		String[] strarray = daystr.split(" ");
		int[] days = new int[strarray.length];
		for(int i = 0; i < strarray.length; ++i) {
			int d = -1;
			switch(strarray[i]) {
			case "M":
				d = Course.MONDAY;
				break;
			case "T":
				d = Course.TUESDAY;
				break;
			case "W":
				d = Course.WEDNESDAY;
				break;
			case "R":
				d = Course.THURSDAY;
				break;
			case "F":
				d = Course.FRIDAY;
				break;
			}
			days[i] = d;
		}
		return days;
	}
}
