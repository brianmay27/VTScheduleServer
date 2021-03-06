package edu.vt.ece4564.vtClassRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.vt.ece4564.shared.Course;
import edu.vt.ece4564.shared.CourseTime;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TimetableScraper implements Runnable{

	private static final String TIMETABLE_URL = "https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest";
	private static final String COURSE_URL = "https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcComments";
	private static final String USER_AGENTS = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 Firefox/11.0";
	private String subj;
	private String crse;
	private String year;
	private ArrayList<Course> courses = null;
	public TimetableScraper(String subj, String crse, String termyear) {
	    this.crse = crse;
	    this.subj = subj;
	    this.year = termyear;
	}
	public ArrayList<Course> getCourses() {
	    return courses;
	}
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
	public ArrayList<Course> getCourses(String subj, String crse, String termyear) {

		ArrayList<Course> courses = new ArrayList<Course>();
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
		Document timetableDoc;
		try {
			timetableDoc = Jsoup.connect(TIMETABLE_URL).data(data).userAgent(USER_AGENTS).post();
		} catch (IOException e) {
			System.err.println("Jsoup Exception: " + e.getMessage());
			return null;
		}
		Elements entrys = timetableDoc.select("table.dataentrytable tr");

		Course c = new Course();
		for(int i = 1; i < entrys.size(); ++i) {
			Elements info = entrys.get(i).getElementsByTag("td");

			// Extract info
			if(!info.get(4).text().contains("Additional Times")) {
				c = new Course();

				// Extract info
				String crn = info.get(0).text().trim().substring(0,5);
				String crseparse = info.get(1).text().trim().split("-")[1];
				String name = info.get(2).text().trim();
				String type = info.get(3).text().trim();
				if(type.startsWith("R")) continue; // Skip research
				String credit = info.get(4).text().trim();
				if(credit.contains("1 TO 19")) continue; // Skip variable credits
				String instructor = info.get(6).text().trim();
				String daystr = info.get(7).text().trim();
				String start = info.get(8).text().trim();
				String end = info.get(9).text().trim();
				String location = info.get(10).text().trim();
				// Set info
				c.setCrn(crn);
				c.setSubj(subj);
				c.setCrse(crseparse);
				c.setName(name);
				c.setType(type);

				// Get credits
				c.setCredits(Integer.parseInt(credit));

				// Check if NOT online
				if(!type.startsWith("O")) {
					CourseTime t = new CourseTime();
					if(!t.addDays(daystr) || !t.setStartTime(start) || !t.setEndTime(end)) {
						System.err.println("Error adding time: " + crn);
					}
					else {
						c.setTime(t);
					}
				}

				// Set additional info
				c.setInstructor(instructor);
				c.setLocation(location);

				// Get Prereqs
				ArrayList<ArrayList<Course>> prereqs;
				try {
				    //TODO change this!
					prereqs = getPrereqs(crn, termyear.substring(4), termyear.substring(0,4), subj, crse);
					c.setPrereqs(prereqs);
				} catch (Exception e) {
					System.err.println("Error adding prereqs: " + e.getMessage() + ": " + crn);
				}

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
					CourseTime t = new CourseTime();
					if(!t.addDays(additionalDays) || !t.setStartTime(additionalBeginTime) || !t.setEndTime(additionalEndTime)) {
						System.err.println("Error adding additional time: " + c.getCrn());
					}
					else {
						c.setAdditionalTime(t);
					}
					c.setAdditionalLocation(additionalLocation);
				}
			}
		}

		return courses;
	}

	// Returns prereqs needed for a course
	public ArrayList<ArrayList<Course>> getPrereqs(String crn, String term, String year, String subj, String crse) throws Exception {
		ArrayList<ArrayList<Course>> prereqs = new ArrayList<ArrayList<Course>>();

		// Make url
		String courseUrl = COURSE_URL + "?CRN=" + crn + "&TERM=" + term + "&YEAR=" + year +
				"&SUBJ=" + subj + "&CRSE=" + crse + "&history=N";

		// Go to url
		Document courseDoc = Jsoup.connect(courseUrl).userAgent(USER_AGENTS).get();

		// Parse document
		Elements prereqHtml = courseDoc.select("table.plaintable tr");
		Pattern matchCourse = Pattern.compile("[a-zA-Z]{2,4} \\d{4}.*");
		for(Element e : prereqHtml) {
			if(e.toString().contains("Prerequisites:")) {
				String[] prereqLinks = e.getElementsByTag("td").get(1).toString().split(",");
				for(String pe : prereqLinks) {
					ArrayList<Course> prerecGroup = new ArrayList<>();
					if (pe.contains(" or ")) {
					    String[] orRecList = pe.split("<a h[^>]*>");
					    for (String opr : orRecList) {
					        Matcher matcher = matchCourse.matcher(opr);
					        if (!matcher.matches())
					            continue;
					        String[] name = opr.split(" ");
					        Course temp = new Course(name[0], name[1].substring(0, 4));
					        prerecGroup.add(temp);
					    }
					} else {
					    String[] orRecList = pe.split("<a h[^>]*>");
    					if (orRecList.length > 2) System.out.println("There was more than one element when expecting one prerec. TimeTableScrapper getPrerecs");
    					String[] name;
    					if (!matchCourse.matcher(orRecList[0]).matches())
    					    name = orRecList[1].split(" ");
    					else
    					    name = orRecList[0].split(" ");
    					Course temp = new Course(name[0], name[1].substring(0, 4));
    					prerecGroup.add(temp);
					}
					prereqs.add(prerecGroup);
				}
				break;
			}
		}

		// Return array
		return prereqs;
	}

    @Override
    public void run()
    {
        courses = getCourses(subj, crse, year);

    }
}
