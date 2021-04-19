package com.example.dusan.krokomer.Database;

public class Steps {
    private long ID;
    private String date;
    private String steps;

    public Steps(long ID, String date, String steps){
        this.ID = ID;
        this.date = date;
        this.steps = steps;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}
