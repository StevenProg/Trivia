package com.example.gebruiker.trivia;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.shuffle;

/**
 * Created by ${Steven} on ${22/2}.
 */

public class TriviaHelper implements Response.Listener<JSONArray>, Response.ErrorListener  {

    private Context contextGame;
    private Callback activityGame;
    private Random rand = new Random();
    private String currentCategory;


    // Callback interface
    public interface Callback {
        void gotQuestion(ArrayList<Question> categories, String category);
        void gotQuestionError(String message);
        void gotWrongCategory();
    }

    public void onResponse(JSONArray response) {
        ArrayList<String> question, correctAnswer;
        ArrayList<Integer> pointList;
        Integer points = 0;
        question = new ArrayList<String>();
        correctAnswer = new ArrayList<String>();
        pointList = new ArrayList<Integer>();

        // if the API's question is good (good value, question and answer) -> add the question
        try {
            JSONObject object = response.getJSONObject(0);
            JSONObject categoryObject = object.getJSONObject("category");
            currentCategory = categoryObject.getString("title");

            // loops over all questions within a category
            int length = response.length();
            for (int i = 0; i < length; i++) {
                if (!(response.getJSONObject(i).isNull("value")) &&
                        !(response.getJSONObject(i).getString("question").equals("=")) &&
                        !(response.getJSONObject(i).getString("answer").equals("="))) {

                    points = response.getJSONObject(i).getInt("value");
                    // creates a list to check if there are at least 4 clues with different values > 0
                    if (!pointList.contains(points)) {
                        question.add(response.getJSONObject(i).getString("question"));
                        correctAnswer.add(response.getJSONObject(i).getString("answer"));
                        pointList.add(points);
                    }
                }
            }
        } catch (JSONException e) {
            Log.d("JSON", e.toString());
        }

        // if the category has atleast 4 good questions, shuffle them, and send them to GameActivity
        if (pointList.size() > 3) {
            List<Integer> indexList = IntStream.rangeClosed(0, pointList.size() - 1).boxed().collect(Collectors.toList());
            // to be picked numbers
            shuffle(indexList, new Random());
            ArrayList<Question> questionList = new ArrayList<Question>();
            for (int i = 0; i < 4; i++) {
                questionList.add(new Question(question.get(indexList.get(i)), correctAnswer.get(indexList.get(i)), pointList.get(indexList.get(i))));
            }
            activityGame.gotQuestion(questionList, currentCategory);
        }
        else  {
            activityGame.gotWrongCategory();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        activityGame.gotQuestionError(error.getMessage());
    }

    public TriviaHelper(Context context) {
        contextGame = context;
    }

    // get a new questionlist from a random category
    public void getQuestion(final Callback activity, ArrayList<Question> questions, String category) {
        activityGame = activity;
        JsonArrayRequest jsonArrayRequest = null;
        Integer categoryIndex = rand.nextInt(251);
        System.out.println("category index: " + categoryIndex);
        String url = String.format("http://jservice.io/api/clues?category=%s", categoryIndex);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, this, this);

        RequestQueue queue = Volley.newRequestQueue(contextGame);
        queue.add(jsonArrayRequest);

    }
}