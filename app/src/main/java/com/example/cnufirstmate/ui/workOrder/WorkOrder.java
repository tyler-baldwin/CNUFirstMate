package com.example.cnufirstmate.ui.workOrder;

public class WorkOrder {
    private String building;
    private String date;
    private String email;
    private String issue;
    private String name;
    private String room;
    public WorkOrder(String building, String date, String email, String issue, String name, String room) {
        this.building = building;
        this.date = date;
        this.email = email;
        this.issue = issue;
        this.name = name;
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }



}
