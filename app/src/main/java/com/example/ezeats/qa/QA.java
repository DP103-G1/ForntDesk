package com.example.ezeats.qa;

public class QA {
    private String id;
    private String title;
    private String detail;
    private  boolean expanded;

    public QA(String id, String title, String detail) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.expanded = false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    public boolean isExpanded() {

        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

}
