package com.example.gebruiker.trivia;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by ${Steven} on ${22/2}.
 */

public class StartScreenActivity  extends AppCompatActivity {
    ImageButton newGameButton;
    ImageButton highScoreButton;
    public static int lifes;
    public static int points;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setListeners();
    }

    private void setListeners(){
        setContentView(R.layout.activity_start);
        newGameButton = findViewById(R.id.new_game_button);
        highScoreButton = findViewById(R.id.high_score_button);
        newGameButton.setOnClickListener(new newGameClick());
        highScoreButton.setOnClickListener(new highScoreClick());
    }

    // button one to start a new game
    private class newGameClick implements View.OnClickListener {
        public void onClick(View view) {
            lifes = 0;
            points = 0;
            Intent intent = new Intent(StartScreenActivity.this, GameActivity.class);
            startActivity(intent);
        }
    }

    // button two to show the highscores
    private class highScoreClick implements View.OnClickListener {
        public void onClick(View view) {
            lifes = 0;
            Intent intent = new Intent(StartScreenActivity.this, HighScoreActivity.class);
            startActivity(intent);
        }
    }

}
