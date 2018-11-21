package com.example.schedule.studentschedule.Model;

public class Assign {
    private int assignId;
    private int termId;
    private int courseId;
//    private int assessmentId;
    private int mentorId;


    public Assign() {
    }

    public Assign(int termId, int courseId, int mentorId) {
        this.termId = termId;
        this.courseId = courseId;
        this.mentorId = mentorId;
    }
    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }
}