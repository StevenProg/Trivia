package com.example.gebruiker.trivia;

/**
 * Created by ${Steven} on ${22/2}.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{

    private String question, correctAnswer;
    private ArrayList<String> answers;
    private int points;


    public Question(String question, String correctAnswer, int points) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.points = points;
    }
    public Question(String question, String correctAnswer, int points, ArrayList<String> answers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.points = points;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getPoints() {
        return points;
    }

    public String getAnswers(int i) {
        return answers.get(i);
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
}