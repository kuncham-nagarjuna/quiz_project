package com.quiz.quiz;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private int result = 0;
    public int counter;
    private TextView question, qno, timer;
    private CountDownTimer countDownTimer;
    private RadioGroup radioGroup1, radioGroup2;
    private RadioButton op_1, op_2, op_3, op_4;
    private Button next_btn, submit_btn;
    private boolean isChecking = true;
    private int mCheckedId;
    private int currentQuestionIndex = 0;

    private Question[] questionBank = new Question[]{
            new Question(R.string.a, R.string.a_1, R.string.a_2, R.string.a_3, R.string.a_4, R.string.a_correct),
            new Question(R.string.b, R.string.b_1, R.string.b_2, R.string.b_3, R.string.b_4, R.string.b_correct),
            new Question(R.string.c, R.string.c_1, R.string.c_2, R.string.c_3, R.string.c_4, R.string.c_correct),
            new Question(R.string.d, R.string.d_1, R.string.d_2, R.string.d_3, R.string.d_4, R.string.d_correct),
            new Question(R.string.e, R.string.e_1, R.string.e_2, R.string.e_3, R.string.e_4, R.string.e_correct),
            new Question(R.string.f, R.string.f_1, R.string.f_2, R.string.f_3, R.string.f_4, R.string.f_correct),
            new Question(R.string.g, R.string.g_1, R.string.g_2, R.string.g_3, R.string.g_4, R.string.g_correct),
            new Question(R.string.h, R.string.h_1, R.string.h_2, R.string.h_3, R.string.h_4, R.string.h_correct),
            new Question(R.string.i, R.string.i_1, R.string.i_2, R.string.i_3, R.string.i_4, R.string.i_correct),
            new Question(R.string.j, R.string.j_1, R.string.j_2, R.string.j_3, R.string.j_4, R.string.j_correct),
            new Question(R.string.k, R.string.k_1, R.string.k_2, R.string.k_3, R.string.k_4, R.string.k_correct),
            new Question(R.string.l, R.string.l_1, R.string.l_2, R.string.l_3, R.string.l_4, R.string.l_correct),
            new Question(R.string.m, R.string.m_1, R.string.m_2, R.string.m_3, R.string.m_4, R.string.m_correct),
            new Question(R.string.n, R.string.n_1, R.string.n_2, R.string.n_3, R.string.n_4, R.string.n_correct),
            new Question(R.string.o, R.string.o_1, R.string.o_2, R.string.o_3, R.string.o_4, R.string.o_correct),
            new Question(R.string.p, R.string.p_1, R.string.p_2, R.string.p_3, R.string.p_4, R.string.p_correct),
            new Question(R.string.q, R.string.q_1, R.string.q_2, R.string.q_3, R.string.q_4, R.string.q_correct),
            new Question(R.string.r, R.string.r_1, R.string.r_2, R.string.r_3, R.string.r_4, R.string.r_correct),
            new Question(R.string.s, R.string.s_1, R.string.s_2, R.string.s_3, R.string.s_4, R.string.s_correct),
            new Question(R.string.t, R.string.t_1, R.string.t_2, R.string.t_3, R.string.t_4, R.string.t_correct),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        qno = findViewById(R.id.qno);
        timer = findViewById(R.id.timer);
        question = findViewById(R.id.question);
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        op_1 = findViewById(R.id.op_1);
        op_2 = findViewById(R.id.op_2);
        op_3 = findViewById(R.id.op_3);
        op_4 = findViewById(R.id.op_4);
        next_btn = findViewById(R.id.next_btn);
        submit_btn = findViewById(R.id.submit_btn);
        updateQuestion();

        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 && isChecking) {
                isChecking = false;
                radioGroup2.clearCheck();
                mCheckedId = checkedId;
            }
            isChecking = true;
        });

        radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 && isChecking) {
                isChecking = false;
                radioGroup1.clearCheck();
                mCheckedId = checkedId;
            }
            isChecking = true;
        });

        countDownTimer = new CountDownTimer(1200000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.e("millisUntilFinished--", String.valueOf(millisUntilFinished));
                Log.e("counter--", String.valueOf(counter));
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                String newtime = hours + ":" + minutes + ":" + seconds;

                if (hours == 0 && minutes == 0 && seconds <= 5) {
                    timer.setTextColor(Color.parseColor("#F44336"));
                    blink();
                } else {
                    timer.setTextColor(Color.parseColor("#E5C86F"));
                }
                if (newtime.equals("0:0:0")) {
                    timer.setText("00:00:00");
                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    timer.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                    timer.setText("0" + hours + ":0" + minutes + ":" + seconds);
                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    timer.setText("0" + hours + ":" + minutes + ":0" + seconds);
                } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    timer.setText(hours + ":0" + minutes + ":0" + seconds);
                } else if (String.valueOf(hours).length() == 1) {
                    timer.setText("0" + hours + ":" + minutes + ":" + seconds);
                } else if (String.valueOf(minutes).length() == 1) {
                    timer.setText(hours + ":0" + minutes + ":" + seconds);
                } else if (String.valueOf(seconds).length() == 1) {
                    timer.setText(hours + ":" + minutes + ":0" + seconds);
                } else {
                    timer.setText(hours + ":" + minutes + ":" + seconds);
                }
                counter++;
            }

            public void onFinish() {
                submit();
            }
        }.start();

        next_btn.setOnClickListener(view -> {
            Log.e("currentQuestionIndex--", String.valueOf(currentQuestionIndex));
            Log.e("length--", String.valueOf(questionBank.length));
            if (currentQuestionIndex < (questionBank.length + 1)) {
                check_answer();
                radioGroup1.clearCheck();
                radioGroup2.clearCheck();
                currentQuestionIndex = currentQuestionIndex + 1;
                if (currentQuestionIndex == questionBank.length - 1) {
                    next_btn.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.VISIBLE);
                    updateQuestion();
                } else {
                    updateQuestion();
                }
            }
        });
        submit_btn.setOnClickListener(view -> {
            submit();
        });
    }

    private void blink() {
        ObjectAnimator anim = ObjectAnimator.ofInt(timer, "textColor", Color.RED, Color.WHITE, Color.RED);
        anim.setDuration(900);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }

    private void submit() {
        countDownTimer.cancel();
        check_answer();
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("result", String.valueOf(result));
        startActivity(intent);
    }

    private void check_answer() {
        String user_an = "";
        if (op_1.isChecked()) {
            user_an = op_1.getText().toString();
        } else if (op_2.isChecked()) {
            user_an = op_2.getText().toString();
        } else if (op_3.isChecked()) {
            user_an = op_3.getText().toString();
        } else if (op_4.isChecked()) {
            user_an = op_4.getText().toString();
        }

        if (!user_an.isEmpty()) {
//            Toast.makeText(QuizActivity.this, user_an, Toast.LENGTH_SHORT).show();
            String get_co = getResources().getString(questionBank[currentQuestionIndex].getA_correct());
            Log.e("correct---", get_co);
            Log.e("user---", user_an);

            if (user_an.equals(get_co)) {
                Log.e("answer-check--", "true");
                result = result + 1;
                Log.e("result---", String.valueOf(result));
            } else {
                Log.e("answer-check--", "false");
            }
        }
    }

    private void updateQuestion() {
        qno.setText("# " + (currentQuestionIndex + 1));
        question.setText(questionBank[currentQuestionIndex].getQuestion());
        op_1.setText(questionBank[currentQuestionIndex].getA_1());
        op_2.setText(questionBank[currentQuestionIndex].getA_2());
        op_3.setText(questionBank[currentQuestionIndex].getA_3());
        op_4.setText(questionBank[currentQuestionIndex].getA_4());
    }

    @Override
    public void onBackPressed() {
    }
}