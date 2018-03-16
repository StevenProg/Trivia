package com.example.gebruiker.trivia;

import android.support.annotation.NonNull;

/**
 * Created by ${Steven} on ${22/2}.
 */

public class Highscore implements Comparable<Highscore> {
    private String name;
    private Integer score;

    public Highscore() {}

    public Highscore(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public int compareTo(Highscore other) {
        return Integer.compare(this.score, other.score);
    }
}
