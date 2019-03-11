package Utilities;

import java.util.TreeMap;

import models.Course;
import models.Term;

public class CourseData {
    //class is used to store TermData that can be reused accross the app.
  private static TreeMap<String, Course> CourseList = new TreeMap<>();

    public static TreeMap<String, Course> getCourseList() {
        return CourseList;
    }

    public static void setCourseList(TreeMap<String, Course> courseList) {
        CourseList = courseList;
    }

}
