package Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import models.Course;
import models.Term;

public class CourseData {
    //class is used to store TermData that can be reused accross the app.
    //Items used for CourseActivity
    private static ArrayList<String> assignedAssessmentItem = new ArrayList<>();
    private static ArrayList<String> AvailableAssessmentItems = new ArrayList<>();
    private static TreeMap<String, Course> CreatedCourse = new TreeMap<>();


    public static TreeMap<String, Course> getCreatedCourse() {
        return CreatedCourse;
    }

    public static void setCreatedCourse(TreeMap<String, Course> data) {
        CreatedCourse = data;
    }

    public static ArrayList<String> getCoursesbyNames(){
        //load the terms by names
        ArrayList<String> courses = new ArrayList<>();
        for (String CourseName : CreatedCourse.keySet()){
            courses.add(CourseName);
        }

        if (courses != null){
            //sort in order
            Collections.sort(courses);
        }
        return courses;
    }

    public static ArrayList<String> getAssignedAssessmentItem() {
        return assignedAssessmentItem;
    }

    public static void setAssignedAssessmentItem(ArrayList<String> Item) {
        assignedAssessmentItem = Item;
    }

    public static ArrayList<String> getAvailableAssessmentItems() {
        return AvailableAssessmentItems;
    }

    public static void setAvailableAssessmentItems(ArrayList<String> Items) {
        AvailableAssessmentItems = Items;
    }
}
