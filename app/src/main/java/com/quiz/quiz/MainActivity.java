package com.quiz.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SessionManager sessionManager;

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
                        Intent intent = new Intent(MainActivity.this,QuizActivity.class);
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
}