package models;

public class Course {
    private String title;
    private String startDate;
    private String endDate;
    private String notes;
    private String reminderStartDate;
    private String isReminderEndDate;
    private String AssementId;
    private String MentorId;

    public Course(){

    }
    public Course(String rStartDate, String rEndDate, String title, String startDate, String endDate, String notes, String AssessmentId, String MentorId) {

        this.title = title; this.AssementId = AssessmentId; this.MentorId = MentorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.reminderStartDate = rStartDate;
        this.isReminderEndDate = rEndDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReminderStartDate() {
        return reminderStartDate;
    }

    public void setReminderStartDate(String reminderStartDate) {
        this.reminderStartDate = reminderStartDate;
    }

    public String getIsReminderEndDate() {
        return isReminderEndDate;
    }

    public void setIsReminderEndDate(String isReminderEndDate) {
        this.isReminderEndDate = isReminderEndDate;
    }

    public String getAssementId() {
        return AssementId;
    }

    public void setAssementId(String assementId) {
        AssementId = assementId;
    }

    public String getMentorId() {
        return MentorId;
    }

    public void setMentorId(String mentorId) {
        MentorId = mentorId;
    }
}
