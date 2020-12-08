package com.soongsil.pilatime.member;

public class Member {
    private String email;
    private String name;
    private String phone;
    private String centerName;
    private String ackYn;
    public Member(){}

    public Member(String email, String name, String phone, String centerName) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.centerName = centerName;
        this.ackYn = "N";
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getCenterName() { return this.centerName; }

    public String getAck() { return this.ackYn;}
}
