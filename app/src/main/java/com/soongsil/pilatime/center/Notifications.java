package com.soongsil.pilatime.center;

public class Notifications {

    private String idx;
    private String date;
    private String title;
    private String contents;
    private String writer;

    public Notifications() {};
    public Notifications(String idx, String date, String title, String contents, String writer) {
        this.idx = idx;
        this.date = date;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) { this.contents = contents; }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getIdx() {
        return this.idx;
    }

    public String getDate() {
        return this.date;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContents() {
        return this.contents;
    }

    public String getWriter() {
        return this.writer;
    }
}
