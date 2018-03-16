package com.example.gebruiker.trivia;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by ${Steven} on ${22/2}.
 */

public class HighScoreHelper implements ValueEventListener{
    public interface Callback {
        void gotHighscores(ArrayList<Highscore> highscores);
        void gotError(String message);
    }

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    ArrayList<Highscore> score = new ArrayList<Highscore>();
    private Context highscoreContext;
    private Callback activity;


    public HighScoreHelper(Context c) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        highscoreContext = c;

    }

    // gets highscores from firebase
    public void getHighcores(final Callback activityBack) {
        activity = activityBack;
        DatabaseReference ref = mDatabase.child("HighscoreList");
        Query query = ref.orderByChild("Score");
        query.addValueEventListener(this);
    }

    // iterates over every child and creates a Highscore object for each
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Highscore post = postSnapshot.getValue(Highscore.class);
            Highscore newScore = new Highscore(post.getName(), post.getScore());
            score.add(newScore);
            }

        // sorts the highscores in descending order
        Collections.sort(score, Collections.reverseOrder());
        activity.gotHighscores(score);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        activity.gotError(String.valueOf(databaseError));
    }



    // adds new highscore to firebase
    public void addToDatabse(String name, Integer score) {
        Highscore aHighscore = new Highscore(name, score);
        String currentTime = String.valueOf(Calendar.getInstance().getTime());
        mDatabase.child("HighscoreList").child(currentTime).setValue(aHighscore);
    }

}
