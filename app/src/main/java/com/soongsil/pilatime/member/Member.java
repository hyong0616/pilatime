package com.soongsil.pilatime.member;

public class Member {
    private String email;
    private String name;
    private String phone;
    private String centerName;
    private String ackYn;

    private String classType;
    private String classCost;
    private String className;
    private String classCount;
    private int classRemain;
    public Member(){}

    public Member(String email, String name, String phone, String centerName) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.centerName = centerName;
        this.ackYn = "N";
        this.classType = "";
        this.classCost = "";
        this.className = "신청목록 없음";
        this.classCount = "-";
        this.classRemain = 0;
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

    public String getClassType() { return this.classType; }

    public String getClassCost() { return this.classCost; }

    public String getClassName() { return this.className;}

    public String getClassCount() { return this.classCount;}

    public int getClassRemain() {return  this.classRemain;}

}
