package com.example.gebruiker.trivia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback{
    private String category;
    ArrayList<Question> questions;
    private Random rand = new Random();
    private ArrayList answerList;
    private Integer correctIndex;
    private Integer points;
    final Context context = this;
    TextView Title;
    Button butOne;
    Button butTwo;
    Button butThree;
    Button butFour;
    Question choosenQuestion;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            choosenQuestion = (Question) savedInstanceState.getSerializable("question");
            if (choosenQuestion != null){
                findViews();
                answerGame(choosenQuestion);
                setHearts();
            }
            else {
                chooseDifficulty();
            }
        }
        chooseDifficulty();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("question", choosenQuestion);

    }


    // gets 4 difficulty levels of a random category from TrivialHelper.java
    public void chooseDifficulty() {
        setContentView(R.layout.loading);
        TriviaHelper helper = new TriviaHelper(getApplicationContext());
        helper.getQuestion(this, questions, category);
    }

    // sets screen and let player choose the difficulty when questions are received
    @Override
    public void gotQuestion(ArrayList<Question> questions, String category) {
        findViews();
        butTwo.setVisibility(View.VISIBLE);
        butThree.setVisibility(View.VISIBLE);
        Title.setText("Your category is " + category + "! Select your difficulty");
        butOne.setText(String.valueOf(questions.get(0).getPoints()));
        butTwo.setText(String.valueOf(questions.get(1).getPoints()));
        butThree.setText(String.valueOf(questions.get(2).getPoints()));
        butFour.setText(String.valueOf(questions.get(3).getPoints()));
        butOne.setOnClickListener(new DifficultyListener(questions));
        butTwo.setOnClickListener(new DifficultyListener(questions));
        butThree.setOnClickListener(new DifficultyListener(questions));
        butFour.setOnClickListener(new DifficultyListener(questions));
        setHearts();

    }
    @Override
    public void gotQuestionError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void findViews() {
        setContentView(R.layout.activity_game);
        Title = findViewById(R.id.Title);
        butOne = findViewById(R.id.but_1);
        butTwo = findViewById(R.id.but_2);
        butThree = findViewById(R.id.but_3);
        butFour = findViewById(R.id.but_4);
    }

    // a category containing invalid answers, questions and/or points is selected -> new random category
    @Override
    public void gotWrongCategory() {
        TriviaHelper helper = new TriviaHelper(getApplicationContext());
        helper.getQuestion(this, questions, category);
    }

    // keep track of player's lifes
    public void setHearts() {
        ImageView heartOne = findViewById(R.id.heart_one);
        ImageView heartTwo = findViewById(R.id.heart_two);
        ImageView heartThree = findViewById(R.id.heart_three);
        switch (StartScreenActivity.lifes) {
            case 1:
                heartOne.setImageDrawable(getDrawable(R.drawable.pixel_heart_empty));
                break;
            case 2:
                heartOne.setImageDrawable(getDrawable(R.drawable.pixel_heart_empty));
                heartTwo.setImageDrawable(getDrawable(R.drawable.pixel_heart_empty));
                break;
            case 3:
                heartOne.setImageDrawable(getDrawable(R.drawable.pixel_heart_empty));
                heartTwo.setImageDrawable(getDrawable(R.drawable.pixel_heart_empty));
                heartThree.setImageDrawable(getDrawable(R.drawable.pixel_heart_empty));
                break;
        }
    }

    // starts next screen, randomizes options order
    public void answerGame(Question currentQuestion) {
        points = currentQuestion.getPoints();
        Title.setText(currentQuestion.getQuestion() + " (for " + String.valueOf(points) + " points)");
        correctIndex = rand.nextInt(4);
        answerList = new ArrayList(4);
        Integer wrongIndex = 0;
        for (int i = 0; i < 4; i++) {
            if (i == correctIndex) {
                answerList.add(currentQuestion.getCorrectAnswer());
            } else {
                answerList.add(currentQuestion.getAnswers(wrongIndex));
                wrongIndex += 1;
            }
        }
        setAnswerOptions();
    }

    public void setAnswerOptions() {
        butOne.setText((CharSequence) answerList.get(0));
        butTwo.setText((CharSequence) answerList.get(1));
        butThree.setText((CharSequence) answerList.get(2));
        butFour.setText((CharSequence) answerList.get(3));

        butOne.setOnClickListener(new AnswerListener(answerList, correctIndex, points));
        butTwo.setOnClickListener(new AnswerListener(answerList, correctIndex, points));
        butThree.setOnClickListener(new AnswerListener(answerList, correctIndex, points));
        butFour.setOnClickListener(new AnswerListener(answerList, correctIndex, points));
    }

    // creates the screen when an answer is given, giving options depending on the player's lifes
    public void postAnswer(String currentAnswer, Boolean currentCorrect, Integer currentPoints) {
        butOne.setText("Next Category!");
        butFour.setText("Quit game");
        butTwo.setVisibility(View.INVISIBLE);
        butThree.setVisibility(View.INVISIBLE);
        butOne.setOnClickListener(new categoryListener());
        butTwo.setOnClickListener(new SubmitListener());
        butFour.setOnClickListener(new QuitListener());

        if (currentCorrect) {
            StartScreenActivity.points += currentPoints;
            Title.setText("Correct! Total Score: " + StartScreenActivity.points);
        }
        else {
            StartScreenActivity.lifes += 1;
            Title.setText("Incorrect, the correct answer was: " + "'" + currentAnswer + "'. " + "Your total score is: " + StartScreenActivity.points);
            if (StartScreenActivity.lifes == 3) {
                butOne.isOpaque();
                butOne.setEnabled(false);
                butTwo.setVisibility(View.VISIBLE);
                butTwo.setText("Submit Score!");
            }
        }
        setHearts();
    }

    // when a name is entered, send an intent to HighscoreActivity
    public void submitted(String input) {
        Intent intent = new Intent(GameActivity.this, HighScoreActivity.class);
        intent.putExtra("Name", input);
        startActivity(intent);
        finish();
    }

    public class AnswerListener implements AdapterView.OnClickListener {
        ArrayList listAnswers;
        Integer indexCorrect, totalPoints;
        AnswerListener(ArrayList listAnswers, Integer indexCorrect, Integer totalPoints) {
            this.listAnswers = listAnswers;
            this.indexCorrect = indexCorrect;
            this.totalPoints = totalPoints;
        }

        // checks whether the correct answer is pressed
        @Override
        public void onClick(View view){
            String answerGiven = (String) listAnswers.get(correctIndex);
            Boolean correct = (Integer.parseInt((String) view.getTag()) == correctIndex);
            postAnswer(answerGiven, correct, totalPoints);
        }
    }

    public class SubmitListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View view) {
            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.prompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Submit!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // get user input and set it to userInput
                            userInput.setText(userInput.getText());
                            if (String.valueOf(userInput.getText()).equals("")) {
                                submitted("ANON");
                            }

                            else {
                                submitted(String.valueOf(userInput.getText()));
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    // listener to send clicked menu item to MenuItemActivity
    public class DifficultyListener implements AdapterView.OnClickListener {
        ArrayList<Question> questions;
        DifficultyListener(ArrayList<Question> questions) {
            this.questions = questions;
        }

        // a question is choosen, use this question in next view and pick the answers of the other questions as other choices
        @Override
        public void onClick(View view) {
            Integer tag = Integer.parseInt((String) view.getTag());
            ArrayList<String> answerList = new ArrayList<String>();
            String question = null, answer = null;
            Integer points = null;
            for (int i = 0; i < 4; i ++) {
                if (i == tag) {
                    question = questions.get(i).getQuestion();
                    answer = questions.get(i).getCorrectAnswer();
                    points = questions.get(i).getPoints();
                }
                else {
                    answerList.add(questions.get(i).getCorrectAnswer());
                }
            }
            choosenQuestion = new Question(question, answer, points, answerList);
            answerGame(choosenQuestion);
        }
    }

    public class categoryListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View view) {
            chooseDifficulty();
        }
    }

    public class QuitListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View view) {
            StartScreenActivity.lifes = 0;
            Intent intent = new Intent(GameActivity.this, StartScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
