package com.example.schedule.studentschedule.Model;

import java.io.Serializable;

public class Assessment implements Serializable {
    private int assessmentId;
    private String title;
    private String type;
    private String dueDate;
    private String dueDateAlert;
    private int termID;
    private int courseID;

    public Assessment() {
    }

    public Assessment(String title, String type, String dueDate, int termID, int courseID) {
        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
        this.termID = termID;
        this.courseID = courseID;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
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

    public String getDueDateAlert() {
        return dueDateAlert;
    }

    public void setDueDateAlert(String dueDateAlert) {
        this.dueDateAlert = dueDateAlert;
    }
}