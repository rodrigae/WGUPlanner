package models;

import android.support.annotation.NonNull;


public class Term extends Course{
    private String title;
    private String startDate;
    private String endDate;
    private String notes;

    public Term(String title, String startDate, String endDate, String notes) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
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


}
