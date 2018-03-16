package com.example.gebruiker.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

/**
 * Created by ${Steven} on ${22/2}.
 */

public class HighScoreActivity extends AppCompatActivity implements HighScoreHelper.Callback{
    private FirebaseAuth mAuth;
    public static ArrayList<Highscore> score;

    // depending on the state of the game (playing or simply viewing) show or (add and show) the highscores using firebase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_loading);
        HighScoreHelper helper = new HighScoreHelper(getApplicationContext());

        if (StartScreenActivity.lifes == 3) {
            Intent intent= getIntent();
            String name = intent.getStringExtra("Name");
            helper.addToDatabse(name, StartScreenActivity.points);
            helper.getHighcores(this);
        }

        else {
            helper.getHighcores(this);
        }
    }

    // show the highscores in a listview using highscore_view as format
    @Override
    public void gotHighscores(ArrayList<Highscore> highscores) {
        setContentView(R.layout.highscore_listview);
        ListView highscoreView = findViewById(R.id.highscore_listID);
        HighScoreAdapter adapter = new HighScoreAdapter(this, R.layout.highscore_view, highscores);
        highscoreView.setAdapter(adapter);
    }

    @Override
    public void gotError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
