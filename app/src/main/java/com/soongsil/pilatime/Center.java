package com.soongsil.pilatime;

public class Center {
    private String name;
    private String email;
    private String address;
    private String tellNumber;

    public Center() {};
    public Center(String name, String email, String address, String tellNumber){
        this.name = name;
        this.email = email;
        this.address = address;
        this.tellNumber = tellNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }
    public String getAddress() {
        return this.address;
    }
    public String getTellNumber() {
        return this.tellNumber;
    }
}
