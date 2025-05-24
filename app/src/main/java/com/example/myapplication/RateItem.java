package com.example.myapplication;

public class RateItem {
    private int id;
    private String curName;
    private String curRate;

    public RateItem() {
        super();
        curName = "";
        curRate = "";
    }

    public RateItem(String curRate, String curName) {
        super();
        this.curRate = curRate;
        this.curName = curName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getCurRate() {
        return curRate;
    }

    public void setCurRate(String curRate) {
        this.curRate = curRate;
    }

    @Override
    public String toString() {
        return "RateItem{" +
                "id=" + id +
                ", curName='" + curName + '\'' +
                ", curRate='" + curRate + '\'' +
                '}';
    }
}