package com.quiz.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Feedback extends AppCompatActivity {

    String get_review, get_score, get_date;
    RecyclerView feedbackRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        feedbackRV = findViewById(R.id.feedbackRV);

        Intent intent = getIntent();
        get_review = intent.getStringExtra("review");
        get_score = intent.getStringExtra("result");
        get_date = intent.getStringExtra("test_date");

        FeedbackData[] feedbackData = new FeedbackData[]{
                new FeedbackData(get_review, get_date, get_score),
                new FeedbackData("Improve level of hardness. so that student can learn well as well as improve their brain", "22, Mar 22 05:12 pm", "9"),
                new FeedbackData("Please increase the time.", "22, Mar 22 05:12 pm", "6"),
                new FeedbackData("Change the questions every week.", "18, Mar 22 11:42 am", "8"),
                new FeedbackData("Questions are very difficult for me.", "20, Feb 22 06:22 am", "4")
        };

        FeedbackAdapter adapter = new FeedbackAdapter(feedbackData);
        feedbackRV.setHasFixedSize(true);
        feedbackRV.setLayoutManager(new LinearLayoutManager(this));
        feedbackRV.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Feedback.this, MainActivity.class);
        startActivity(intent);
    }
}