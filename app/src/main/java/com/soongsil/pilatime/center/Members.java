package com.soongsil.pilatime.center;

public class Members {
    private String name;
    private String goods;
    private int remain;

    public Members(){}

    public Members(String name, String goods, int remain) {
        this.name = name;
        this.goods = goods;
        this.remain = remain;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getName() {
        return this.name;
    }

    public String getGoods() {
        return this.goods;
    }

    public int getRemain() {
        return this.remain;
    }
}
