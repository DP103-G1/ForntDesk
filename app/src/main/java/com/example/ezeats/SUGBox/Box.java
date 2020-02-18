package com.example.ezeats.SUGBox;

public class Box {
    private int id;
    private int member;
    private String topic;
    private String purpose;
    private String info;
    private String date;
    private float satisfied;
    private String feed_back;
    private boolean expanded;

    public Box(int id, int member, String purpose, String info, String date, float satisfied, String feed_back) {
        this.id = id;
        this.member = member;
        this.purpose = purpose;
        this.info = info;
        this.date = date;
        this.satisfied = satisfied;
        this.feed_back = feed_back;
        this.expanded = false;
    }

    public Box(String topic, String purpose, String info, String dateTime, float satisfied, String feed_back) {
        this.topic = topic;
        this.purpose = purpose;
        this.info = info;
        this.date = dateTime;
        this.satisfied = satisfied;
        this.feed_back = feed_back;
        this.expanded = false;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(float satisfied) {
        this.satisfied = satisfied;
    }

    public String getFeed_back() {
        return feed_back;
    }

    public void setFeed_back(String feed_back) {
        this.feed_back = feed_back;
    }

    public void  setExpanded(boolean expanded){this.expanded = expanded;}

    public boolean isExpanded(){return expanded;}


    @Override
    public String toString() {
        return "Box{" +
                "id='" + id + '\'' +
                ", member='" + member + '\'' +
                ", purpose='" + purpose + '\'' +
                ", info='" + info + '\'' +
                ", date='" + date + '\'' +
                ", satisfied='" + satisfied + '\'' +
                ", feed_back='" + feed_back + '\'' +
                ", expanded='"+expanded + '\'' +
                '}';
    }


}
