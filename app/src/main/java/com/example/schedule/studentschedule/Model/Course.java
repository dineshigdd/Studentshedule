package com.example.schedule.studentschedule.Model;

import android.util.Log;

public class Course extends DataItem {
    private int termId;
    private String status;
    private String notes;
    private String startDateAlert;
    private String endDateAlert;

    public Course() {
    }



    public Course(String item, String startDate, String endDate, String status, String notes) {
        super(item, startDate, endDate);

        this.status = status;
        this.notes = notes;


    }

    public String getStartDateAlert() {
        return startDateAlert;
    }

    public void setStartDateAlert(String startDateAlert) {
        this.startDateAlert = startDateAlert;
    }

    public String getEndDateAlert() {
        return endDateAlert;
    }

    public void setEndDateAlert(String endDateAlert) {
        this.endDateAlert = endDateAlert;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public void setNotes(String notes) {

        this.notes = notes;
    }
}
