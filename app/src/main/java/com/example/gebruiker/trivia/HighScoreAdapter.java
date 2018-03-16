package com.example.gebruiker.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ${Steven} on ${22/2}.
 */

public class HighScoreAdapter extends ArrayAdapter<Highscore> {

    private ArrayList<Highscore> HighscoreList;

    HighScoreAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Highscore> objects) {
        super(context, resource, objects);
        HighscoreList = objects;
    }


    // sets the input for the highscore list
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.highscore_view, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.Name);
        TextView itemPrice = convertView.findViewById(R.id.Score);

        Highscore highscore = HighscoreList.get(position);
        String name = highscore.getName();
        String score = highscore.getScore().toString();


        itemName.setText(name);
        itemPrice.setText(score + " Points");

        return convertView;
    }
}
