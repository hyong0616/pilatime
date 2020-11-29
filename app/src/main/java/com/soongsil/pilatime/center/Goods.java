package com.soongsil.pilatime.center;

public class Goods {
    private String name;
    private String status;
    private String time;
    private String capacity;
    private String count;
    private String cost;

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getName(){
        return this.name;
    }

    public String getStatus() {
        return this.status;
    }

    public String getTime(){
        return this.time;
    }

    public String getCapacity(){
        return this.capacity;
    }

    public String getCount() {
        return this.count;
    }

    public String getCost() {
        return this.cost;
    }
}
