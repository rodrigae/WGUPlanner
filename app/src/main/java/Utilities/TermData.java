package Utilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import models.Term;

public class TermData {
    //class is used to store TermData that can be reused accross the app.
    private static TreeMap<String, Term> CreatedTermList = new TreeMap<>();
    private static TreeMap<String, ArrayList<String>> AssignedCourses = new TreeMap<>();
  //  private static TreeMap<String, ArrayList<String>> AssignCourseList = new TreeMap<>();


    public static ArrayList<String> getTermbyNamesWithDates(){
        //load the terms by names
        ArrayList<String> terms = new ArrayList<>();
        for (String TermName : CreatedTermList.keySet()){
            terms.add(TermName + " \n from " + CreatedTermList.get(TermName).getStartDate() + " to " + CreatedTermList.get(TermName).getEndDate());
        }

        if (terms != null){
            //sort in order
            Collections.sort(terms);
        }
        return terms;
    }

    public static ArrayList<String> getTermbyNames(){
        //load the terms by names
        ArrayList<String> terms = new ArrayList<>();
        for (String TermName : CreatedTermList.keySet()){
            terms.add(TermName);
        }

        if (terms != null){
            //sort in order
            Collections.sort(terms);
        }
        return terms;
    }
    public static ArrayList<String> getAssignedCoursesByTerm(String TermName) {
        ArrayList<String> courses = new ArrayList<>();
        courses = AssignedCourses.get(TermName);
        if (courses != null){
            Collections.sort(courses);
        }

        return courses;
    }

    public static ArrayList<String> getAvailableCoursesByTerm(String TermName) {
        ArrayList<String> courses = new ArrayList<>();
        ArrayList<String> available = new ArrayList<>();
        ArrayList<String> CurrentCourses = CourseData.getCoursesbyNames();
        courses = AssignedCourses.get(TermName);

        for (int i = 0; i < CurrentCourses.size(); i++){
            if (!courses.contains(CurrentCourses.get(i))){
                available.add(CurrentCourses.get(i));
            }
        }

        if (available != null){
            Collections.sort(available);
        }

        return available;
    }



    public static void setAssignedCourses(TreeMap<String, ArrayList<String>> assignedCourses) {
        AssignedCourses = assignedCourses;
    }

    public static void addNewAssignedCourses(String TermName, ArrayList<String> courses){
        AssignedCourses.put(TermName, courses);
    }

    public static TreeMap<String, Term> getCreatedTermList() {
        return CreatedTermList;
    }

    public static void setCreatedTermList(TreeMap<String, Term> createdTermList) {
        CreatedTermList = createdTermList;
    }
    public static void addCreatedTermList(String Title, Term createdTermList) {
        CreatedTermList.put(Title, createdTermList);
    }

    //used in CourseDetails
    public static TreeMap<String, ArrayList<String>> getAssignCourseList() {
        return AssignedCourses ;
    }

    /*
    public static void addAssignCourseList(String TermName, ArrayList<String> assignCourseList) {
        AssignCourseList.put(TermName,assignCourseList);
    }*/




}
