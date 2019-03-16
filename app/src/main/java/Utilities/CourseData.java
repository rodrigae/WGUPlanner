package Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

import models.Course;
import models.Term;

public class CourseData {
    //class is used to store TermData that can be reused accross the app.
      private static TreeMap<String, Course> CreatedCourse = new TreeMap<>();



    private static TreeMap<String, ArrayList<String>> AssignedAssessment = new TreeMap<>();
    private static TreeMap<String, ArrayList<String>> AssignedMentor = new TreeMap<>();


    public static ArrayList<String> getAssignedAssessmentByTerm(String CourseName) {
        ArrayList<String> assessment = new ArrayList<>();
        assessment = AssignedAssessment.get(CourseName);
        if (assessment != null){
            Collections.sort(assessment);
        }
        return assessment;
    }

    public static String getCoursesbyNamesReminders() {
        //load the terms by names
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);

        StringBuilder courseList = new StringBuilder();

        for (String CourseName : CreatedCourse.keySet()) {


            if (CreatedCourse.get(CourseName).getReminderStartDate().equals("Yes")){
                if (today.equals(CreatedCourse.get(CourseName).getStartDate())){
                    if (!courseList.toString().contains("Course Start Date Reminder/s: \n")){
                        courseList.append("Course Start Date Reminder/s: ");
                    }
                    courseList.append(CourseName+ " - " +CreatedCourse.get(CourseName).getStartDate());
                    courseList.append("\n");
                }
            }
            if (CreatedCourse.get(CourseName).getIsReminderEndDate().equals("Yes")){
                if (today.equals(CreatedCourse.get(CourseName).getEndDate())){
                    if (!courseList.toString().contains("Course End Date Reminder/s: \n")){
                        courseList.append("Course End Date Reminder/s: ");
                    }
                    courseList.append(CourseName+ " - " +CreatedCourse.get(CourseName).getEndDate());
                    courseList.append("\n");
                }
            }
        }
        return courseList.toString().trim();
    }

    public static ArrayList<String> getAvailableAssessmentByTerm(String CourseName) {
        ArrayList<String> Assessment = new ArrayList<>();
        ArrayList<String> available = new ArrayList<>();
        ArrayList<String> CurrentAssessment = AssessmentData.getAssessmentsbyNames();
        Assessment = AssignedAssessment.get(CourseName);

        for (int i = 0; i < CurrentAssessment.size(); i++){
            if (!Assessment.contains(CurrentAssessment.get(i))){
                available.add(CurrentAssessment.get(i));
            }
        }

        if (available != null){
            Collections.sort(available);
        }

        return available;
    }
    public static TreeMap<String, ArrayList<String>> getAssignedAssessment() {
        return AssignedAssessment;
    }
    public static void setAssignedAssessment(TreeMap<String, ArrayList<String>> assignedAssessment) {
        AssignedAssessment = assignedAssessment;
    }



    public static void addNewAssignedAssessment(String CourseName, ArrayList<String> assessment){
        AssignedAssessment.put(CourseName, assessment);
    }
    public static TreeMap<String, ArrayList<String>> getAssignedMentor() {
        return AssignedMentor;
    }


    public static ArrayList<String> getAssignedMentorByTerm(String CourseName) {
        ArrayList<String> mentor = new ArrayList<>();
        mentor = AssignedMentor.get(CourseName);
        if (mentor != null){
            Collections.sort(mentor);
        }
        return mentor;
    }

    public static ArrayList<String> getAvailableMentorByTerm(String CourseName) {
        ArrayList<String> Mentor = new ArrayList<>();
        ArrayList<String> available = new ArrayList<>();
        ArrayList<String> CurrentMentor = MentorData.getMentorsbyNames();
        Mentor = AssignedMentor.get(CourseName);

        for (int i = 0; i < CurrentMentor.size(); i++){
            if (!Mentor.contains(CurrentMentor.get(i))){
                available.add(CurrentMentor.get(i));
            }
        }

        if (available != null){
            Collections.sort(available);
        }

        return available;
    }

    public static void setAssignedMentor(TreeMap<String, ArrayList<String>> assignedMentor) {
        AssignedMentor = assignedMentor;
    }

    public static void addNewAssignedMentor(String CourseName, ArrayList<String> mentor){
        AssignedMentor.put(CourseName, mentor);
    }


    public static ArrayList<String> getCoursesbyNamesWithDates() {
        //load the terms by names
        ArrayList<String> courses = new ArrayList<>();
        for (String CourseName : CreatedCourse.keySet()) {
            courses.add(CourseName + " \n from " + CreatedCourse.get(CourseName).getStartDate() + " to " + CreatedCourse.get(CourseName).getEndDate());
        }

        if (courses != null) {
            //sort in order
            Collections.sort(courses);
        }
        return courses;
    }

    public static ArrayList<String> getCoursesbyNames() {
        //load the terms by names
        ArrayList<String> courses = new ArrayList<>();
        for (String CourseName : CreatedCourse.keySet()) {
            courses.add(CourseName);
        }

        if (courses != null) {
            //sort in order
            Collections.sort(courses);
        }
        return courses;
    }

    public static TreeMap<String, Course> getCreatedCourse() {
        return CreatedCourse;
    }

    public static void setCreatedCourse(TreeMap<String, Course> data) {
        CreatedCourse = data;
    }

    public static void AddNewCreatedCourse(String CourseName, Course createdTermList){
        CreatedCourse.put(CourseName, createdTermList);
    }





}
