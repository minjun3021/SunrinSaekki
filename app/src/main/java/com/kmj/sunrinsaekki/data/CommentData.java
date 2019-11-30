package com.kmj.sunrinsaekki.data;

public class CommentData {
    private String name;
    private String date;
    private String comment;
    private String proURL;

    public CommentData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProURL() {
        return proURL;
    }

    public void setProURL(String proURL) {
        this.proURL = proURL;
    }

    public CommentData(String name, String date, String comment, String proURL) {
        this.name = name;
        this.date = date;
        this.comment = comment;
        this.proURL = proURL;
    }
}
