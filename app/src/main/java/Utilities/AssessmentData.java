package Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import models.Assessment;
import models.Course;

public class AssessmentData {
    private static TreeMap<String, Assessment> CreatedAssessment = new TreeMap<>();
    private static TreeMap<String, ArrayList<String>> AssessmentWhereUsed = new TreeMap<>();

    public static ArrayList<String> getAssessmentsbyNames() {
        //load the terms by names
        ArrayList<String> Assessments = new ArrayList<>();
        for (String AssessmentName : CreatedAssessment.keySet()) {
            Assessments.add(AssessmentName);
        }

        if (Assessments != null) {
            //sort in order
            Collections.sort(Assessments);
        }
        return Assessments;
    }

    public static TreeMap<String, ArrayList<String>> getAssessmentWhereUsed() {
        return AssessmentWhereUsed;
    }

    public static void setAssessmentWhereUsed(TreeMap<String, ArrayList<String>> assessmentWhereUsed) {
        AssessmentWhereUsed = assessmentWhereUsed;
    }

    public static TreeMap<String, Assessment> getCreatedAssessment() {
        return CreatedAssessment;
    }

    public static void setCreatedAssessment(TreeMap<String, Assessment> data) {
        CreatedAssessment = data;
    }
    public static void AddCreatedAssessment(String Title, Assessment createdTermList){
        CreatedAssessment.put(Title, createdTermList);
    }
    
}
