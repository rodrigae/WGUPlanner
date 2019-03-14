package Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import models.Mentor;

public class MentorData {
    private static TreeMap<String, Mentor> CreatedMentor = new TreeMap<>();

    public static ArrayList<String> getMentorsbyNames() {
        //load the terms by names
        ArrayList<String> Mentors = new ArrayList<>();
        for (String MentorName : CreatedMentor.keySet()) {
            Mentors.add(MentorName);
        }

        if (Mentors != null) {
            //sort in order
            Collections.sort(Mentors);
        }
        return Mentors;
    }

    public static ArrayList<String> getWhereUsedMentor(String MentorName){
        ArrayList<String> listOfUsedLocation = new ArrayList<>();
        for (String Mentor : CourseData.getAssignedMentor().keySet()){
            ArrayList<String> contains = CourseData.getAssignedMentor().get(Mentor);
            if (contains.contains(MentorName)){
                listOfUsedLocation.add(Mentor);
            }
        }
        return listOfUsedLocation;
    }


    public static TreeMap<String, Mentor> getCreatedMentor() {
        return CreatedMentor;
    }

    public static void setCreatedMentor(TreeMap<String, Mentor> data) {
        CreatedMentor = data;
    }

    public static void AddCreatedMentor(String Title, Mentor createdTermList){
        CreatedMentor.put(Title, createdTermList);
    }
}
