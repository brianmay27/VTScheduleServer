package edu.vt.ece4564.vtClassRequest;

import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Brian
 *  @version Nov 19, 2013
 */

public class ParseDars implements Serializable
{

    ArrayList<String> array17 = new ArrayList<String>();
    ArrayList<String> array25 = new ArrayList<String>();
    ArrayList<String> array29 = new ArrayList<String>();
    ArrayList<String> arrayHtml = new ArrayList<String>();
    // ArrayList<String> arrayImportantHtmlData= new ArrayList<String>();
    ArrayList<ArrayList<String>> arrayImportantHtmlData = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> arrayCourseRefined = new ArrayList<ArrayList<String>>();

    ArrayList<String> arrayPreBrian = new ArrayList<String>();
    ArrayList<String> arrayTakenCourse = new ArrayList<String>();
    String html, tempStorage;
    int maxID;
    boolean switcher;
    ArrayList<RequirementsStruct> arrayRequirments = new ArrayList<RequirementsStruct>();

    public ParseDars(String html) {
        this.html = html;

        arrayHtmlByLine();
        arrayFilterImportantHtml();
        arrayMakePreBrian();
        arrayMakeRequirements();

        arrayDisplayPreBrian();
        arrayDisplayRequirements();
        arrayDisplayTakenCourse();

        // arrayDisplayCourseRefined();
        //arrayDisplayCourseRaw();
    }
    public void arrayDisplayRequirements(){
        int id,hours,sameas;

        for(int i=0; i<arrayRequirments.size();i++){
            id=arrayRequirments.get(i).ID;
            sameas=arrayRequirments.get(i).sameAs;
            hours=arrayRequirments.get(i).requiremnetHours;
            System.out.println(((Integer)id).toString()+" - "+ arrayRequirments.get(i).requirementName + " - Hours:"+ ((Integer)hours).toString()+ " - SAMEAS:"+ ((Integer)sameas).toString());
        }
    }

    public void arrayMakeRequirements() {
        String temp;
        int areaHours=0;
        for (int i = 0; i < arrayCourseRefined.size(); i++) {
            RequirementsStruct maker = new RequirementsStruct();
            maker.requirementName = parse17(arrayCourseRefined.get(i).get(0), i);
            maker.ID = i;

            if(maker.requirementName.contains("AREA")&&(i!=0)){
                areaHours=parseArea(arrayCourseRefined.get(i-1).get((arrayCourseRefined.get(i-1).size())-1));
            }
            else{
                areaHours=0;
            }

            for (int j = 0; j < arrayCourseRefined.get(i).size(); j++) {
                if ((arrayCourseRefined
                        .get(i)
                        .get(j)
                        .contains(
                                "auditLineType_25_noSubrequirementNeedsSummaryLine"))) {

                    maker.requiremnetHours = parse25(arrayCourseRefined.get(i)
                            .get(j));
                }
            }
            if(maker.requirementName.contains("SAMEAS:")){
                temp=maker.requirementName.replace("SAMEAS:", "").trim();
                maker.sameAs=Integer.parseInt(temp);
            }
            else{
                maker.sameAs=maker.ID;
            }
            if(areaHours!=0){
                maker.requiremnetHours=areaHours;
            }
            arrayRequirments.add(maker);
        }
    }
    public int parseArea(String input){
        char temp = 0;
        for (String retval : input.split("NEEDS:")) {
            retval = retval.trim();
            temp=retval.charAt(0);
        }
        return Character.getNumericValue(temp);

    }
    public String parse17(String input, int index) {
        String output = null;
        if (input.contains("OR)")) {
            output = "SAMEAS:" + ((Integer) (index - 1)).toString();
        } else {
            for (String retval : input.split(">  -  ")) {

                for (String retval2 : retval.split("\\) ")) {
                    output = retval2;
                }
            }
        }
        output = output.replace("</span>", "").trim();
        return output;
    }

    public int parse25(String input) {

        String temp = null;
        int output = 0;
        for (String retval : input.split("NEEDS:")) {
            retval = retval.trim();
            retval = retval.replace("</span>", "");
            if (retval.contains("COURSES")) {
                retval = retval.replace("COURSES", "").trim();
                output = Integer.parseInt(retval) * 3;
            } else if (retval.contains("HOURS")) {
                retval = retval.replace("HOURS", "");
                retval = retval.replace(".00", "").trim();
                output = Integer.parseInt(retval);
            } else if (retval.contains("GPA")) {
                output = -1;
            }

        }

        // System.out.println(output);
        return output;

    }

    public void arrayHtmlByLine() {
        boolean switchme=false;
        String[] temp = html.split("</span>");
        for (int i = 0; i < temp.length; i++) {
            arrayHtml.add(temp[i]);
            if(temp[i].contains("COURSE HISTORY")){
                switchme=true;
            }
            if(switchme==true && temp[i].contains("auditLineType_22_okSubrequirementCourses")){
                parse22(temp[i]);

            }

        }
    }
    public void parse22(String input){
        char[] temp = new char[13];
        input.trim();
        for (String retval : input.split(">      ")) {

            retval.getChars(5, 18, temp, 0);
            if(Character.isDigit(temp[12])){
                if(temp[12]!='0'){
                    arrayTakenCourse.add(String.copyValueOf(temp));
                }
            }
            if(retval.length()>48){
            retval.getChars(35, 48, temp, 0);
            if(Character.isDigit(temp[12])){
                if(temp[12]!='0'){
                    arrayTakenCourse.add(String.copyValueOf(temp));
                }
            }
            }

        }
    }
    public void arrayDisplayTakenCourse(){
        for(int i=0;i<arrayTakenCourse.size();i++){
            System.out.print(i);
            System.out.println(": "+arrayTakenCourse.get(i));
        }
    }

    public void arrayFilterImportantHtml() {
        boolean B25 = true, B9 = false;
        int start = 0, end = 0;

        String temp = "";
        for (int i = 0; i < arrayHtml.size(); i++) {
            ArrayList<String> arrayTemp = new ArrayList<String>();
            if (B25) {
                if (arrayHtml.get(i).contains(
                        "auditLineType_17_noSubrequirementTLine")) {
                    B25 = false;
                    B9 = true;
                    start = i;

                }
            }
            if (B9) {
                if (arrayHtml.get(i).contains("auditLineType_09_blankLine")) {
                    B9 = false;
                    B25 = true;
                    end = i;

                    for (int i1 = start; i1 < end; i1++) {
                        arrayTemp.add(arrayHtml.get(i1));
                    }
                    arrayImportantHtmlData.add(arrayTemp);
                    temp = "";

                }
            }
        }

    }

    public void arrayDisplayCourseRaw() {
        for (int k = 0; k < arrayImportantHtmlData.size(); k++) {
            for (int j = 0; j < arrayImportantHtmlData.get(k).size(); j++) {
                System.out.print(k + 1);
                System.out.print(":");
                System.out.print(j + 1);
                System.out
                        .println(" = " + arrayImportantHtmlData.get(k).get(j));
            }
        }
    }

    public void arrayMakePreBrian() {
        arrayCourseRefined.addAll(arrayImportantHtmlData);
        for (int k = 0; k < arrayCourseRefined.size(); k++) {
            maxID = k;
            for (int j = 0; j < arrayCourseRefined.get(k).size(); j++) {
                // System.out.print(arrayImportantHtmlData.get(k).get(j));
                if (arrayCourseRefined
                        .get(k)
                        .get(j)
                        .contains(
                                "auditLineType_29_noSubrequirementAcceptCourses")
                        || arrayCourseRefined
                                .get(k)
                                .get(j)
                                .contains(
                                        "auditLineType_25_noSubrequirementNeedsSummaryLine")
                        || arrayCourseRefined
                                .get(k)
                                .get(j)
                                .contains(
                                        "auditLineType_17_noSubrequirementTLine")) {

                    if (arrayCourseRefined
                            .get(k)
                            .get(j)
                            .contains(
                                    "auditLineType_29_noSubrequirementAcceptCourses")) {

                        collectClasses(arrayCourseRefined.get(k).get(j), k);
                    }
                } else {
                    // arrayCourseRefined.get(k).set(j, "");
                    // arrayCourseRefined.get(k).remove(j);
                }
            }
        }

    }

    public String collectClasses(String classes, int index) {
        for (String retval : classes.split("&department=")) {
            for (String retval2 : retval.split("&number=")) {
                for (String retval3 : retval2.split("','")) {
                    if (retval3.length() < 7) {
                        if (!switcher) {
                            tempStorage = retval3;
                            switcher = true;
                        } else if (switcher) {
                            tempStorage = ((Integer) index).toString() + " "
                                    + tempStorage.replaceAll("\\s", "") + " "
                                    + retval3;
                            switcher = false;
                            arrayPreBrian.add(tempStorage);
                        }

                    }
                }
            }
        }
        return classes;
    }

    public void arrayDisplayPreBrian() {
        for (int i = 0; i < arrayPreBrian.size(); i++) {
            System.out.println(arrayPreBrian.get(i));
        }
    }

    public void arrayDisplayCourseRefined() {

        for (int k = 0; k < arrayCourseRefined.size(); k++) {
            for (int j = 0; j < arrayCourseRefined.get(k).size(); j++) {

                System.out.print(k + 1);
                System.out.print(":");
                System.out.print(j + 1);
                System.out.println(" = " + arrayCourseRefined.get(k).get(j));

            }
        }
    }

    private static String readFile(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");
        try {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }
}
