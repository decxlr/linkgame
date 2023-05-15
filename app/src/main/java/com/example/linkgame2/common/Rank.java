package com.example.linkgame2.common;

import org.litepal.crud.LitePalSupport;

public class Rank extends LitePalSupport {

    private int num;
    private String name;
    private int score;
    private String time;

    public Rank(int num,String name,int score,String time) {
        this.num = num;
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public Rank() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
