package com.soongsil.pilatime.member;

public class Memo {
    private String date;
    private String content;

    public Memo() { }
    public Memo(String date, String content) {
        this.date= date;
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public String getDate() {
        return this.date;
    }
}
