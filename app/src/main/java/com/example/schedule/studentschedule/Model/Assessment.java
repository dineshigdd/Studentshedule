package com.example.schedule.studentschedule.Model;

import java.io.Serializable;

public class Assessment implements Serializable {
    private int assessmentId;
    private String title;
    private String type;
    private String dueDate;


    public Assessment() {
    }

    public Assessment(String title, String type, String dueDate) {

        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
    }


    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


}