package Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

    public static String getAssessmentsbyNamesReminders() {
        //load the terms by names
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);

        StringBuilder assessmentList = new StringBuilder();

        for (String AssessmentName : CreatedAssessment.keySet()) {

            if (CreatedAssessment.get(AssessmentName).isReminderSet().equals("Yes")){
                if (today.equals(CreatedAssessment.get(AssessmentName).getGoalDate())){
                    if (!assessmentList.toString().contains("Assessment Reminder/s: ")){
                        assessmentList.append("Assessment Reminder/s: ");
                    }
                   assessmentList.append(AssessmentName+ " - " +CreatedAssessment.get(AssessmentName).getGoalDate());
                   assessmentList.append("\n");
                }
            }
        }
        return assessmentList.toString().replace("[]", "").trim();
    }

    public static ArrayList<String> getWhereUsedAssessment(String AssessmentName){
        ArrayList<String> listOfUsedLocation = new ArrayList<>();
        for (String Course : CourseData.getAssignedAssessment().keySet()){
            ArrayList<String> contains = CourseData.getAssignedAssessment().get(Course);
            if (contains.contains(AssessmentName)){
                listOfUsedLocation.add(Course);
            }
        }
        return listOfUsedLocation;
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
