package models;

public class Assessment extends Mentor {
    private String name;
    private String type; ///true for pA false for oA
    private String status;
    private String goalDate;
    private String reminderSet;

    public Assessment(){

    }

    public Assessment(String name, String type, String status, String goalDate, String reminderSet) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.goalDate = goalDate;
        this.reminderSet = reminderSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() { return status; }

    public void setStatus(String status){ this.status = status; }

    public String getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(String goalDate) {
        this.goalDate = goalDate;
    }

    public String isReminderSet() {
        return reminderSet;
    }

    public void setReminderSet(String reminderSet) {
        this.reminderSet = reminderSet;
    }
}
