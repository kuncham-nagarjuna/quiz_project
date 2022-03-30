package com.quiz.quiz;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SessionManager sessionManager;
    Date added_time, current_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(MainActivity.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();

        Button start_btn = findViewById(R.id.start_btn);
        ImageView signout = findViewById(R.id.signout);

        start_btn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Do you want to start?")
                    .setCancelable(false)
                    .setPositiveButton("Continue", (dialog, id) -> {
                        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });

        signout.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Do you want to signout?")
                    .setCancelable(false)
                    .setPositiveButton(R.string.sign_out, (dialog, id) -> {
                        mAuth.signOut();
                        sessionManager.logoutUser();
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Log.e("login_time--MA", sessionManager.get_login_time());
        if (!sessionManager.get_login_time().isEmpty() && !sessionManager.get_login_time().equalsIgnoreCase("time")) {

            try {
                Date login_date = sdf.parse(sessionManager.get_login_time());
                current_date = Calendar.getInstance().getTime();
                Calendar c = Calendar.getInstance();
                if (login_date != null) {
                    c.setTime(login_date);
                }
                c.add(Calendar.MINUTE, 25);
                added_time = c.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (added_time.before(current_date)) {
                Log.e("date----1--", added_time + "----" + current_date);
                mAuth.signOut();
                sessionManager.logoutUser();
                finish();
            } else if (added_time.after(current_date)) {
                Log.e("date----2--", added_time + "----" + current_date);

            } else {
                mAuth.signOut();
                sessionManager.logoutUser();
                finish();
            }
        } else {
            mAuth.signOut();
            sessionManager.logoutUser();
            finish();
        }
    }
}