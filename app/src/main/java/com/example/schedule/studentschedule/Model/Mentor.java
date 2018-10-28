package com.example.schedule.studentschedule.Model;

public class Mentor {
    private int mentorId;
    private String name;
    private String phone;
    private String email;

    public Mentor() {
    }

    public Mentor(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public int getMentorId() {
        return mentorId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
