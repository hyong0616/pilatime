package com.soongsil.pilatime.member;

public class Member {
    private String email;
    private String name;
    private String phone;

    public Member(){}

    public Member(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
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
}
