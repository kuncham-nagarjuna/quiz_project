package com.quiz.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    String get_result="", msg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView result_heading = findViewById(R.id.result_heading);
        TextView result = findViewById(R.id.result);
        Button feedback = findViewById(R.id.feedback);

        Intent i = getIntent();
        if (i.hasExtra("result")) {
            Log.e("has", "yes");
            get_result = i.getStringExtra("result");
        }else {
            Log.e("has", "no");
        }

        result.setText(get_result+" / 10");
        if (Integer.parseInt(get_result) >= 7){
            msg = "Congratulations, You won!";
        }else if (Integer.parseInt(get_result) <= 5){
            msg = "Too bad, You won only";
        }else {
            msg = "Please improve, You won only";
        }
        result_heading.setText(msg);

        feedback.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
            builder.setTitle("Feedback");

            LayoutInflater inflater = (LayoutInflater) ResultActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View mview = inflater.inflate(R.layout.feedback_review_custom, null);

            EditText review = mview.findViewById(R.id.review);

            builder.setPositiveButton("Submit", (dialog, id) -> {
            });
            builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
            builder.setView(mview);
            final AlertDialog alert = builder.create();
            alert.show();
            alert.setCanceledOnTouchOutside(false);
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v1 -> {

                if (!review.getText().toString().trim().isEmpty()) {
                    Intent intent = new Intent(ResultActivity.this, Feedback.class);
                    intent.putExtra("review", review.getText().toString().trim());
                    intent.putExtra("result", get_result);

                    SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
                    Date newDate = new Date();
                    try {
                        newDate = format.parse(String.valueOf(newDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    format = new SimpleDateFormat("dd, MMM yy hh:mm a");
                    String date = format.format(newDate);

                    intent.putExtra("test_date", date);
                    startActivity(intent);
                    alert.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(), "Submit Feedback", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultActivity.this,MainActivity.class);
        startActivity(intent);
    }
}