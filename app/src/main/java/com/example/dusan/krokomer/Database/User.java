package com.example.dusan.krokomer.Database;

public class User {
    private long ID;
    private String birth;
    private String height;
    private String sex;
    private String weight;
    private String track;
    private String goal;
    private String steps;
    private String achievement;

    public User(long ID, String birth, String height, String sex, String weight, String track, String goal, String steps, String achievement){
        this.ID = ID;
        this.birth = birth;
        this.height = height;
        this.sex = sex;
        this.weight = weight;
        this.track = track;
        this.goal = goal;
        this.steps = steps;
        this.steps = achievement;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
