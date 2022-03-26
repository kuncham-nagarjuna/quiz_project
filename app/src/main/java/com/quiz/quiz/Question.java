package com.quiz.quiz;

public class Question {
    private int question, a_1, a_2, a_3, a_4, a_correct;

    public int getQuestion() {
        return question;
    }

    public int getA_1() {
        return a_1;
    }

    public int getA_2() {
        return a_2;
    }

    public int getA_3() {
        return a_3;
    }

    public int getA_4() {
        return a_4;
    }

    public int getA_correct() {
        return a_correct;
    }

    public Question(int question, int a_1, int a_2, int a_3, int a_4, int a_correct) {
        this.question = question;
        this.a_1 = a_1;
        this.a_2 = a_2;
        this.a_3 = a_3;
        this.a_4 = a_4;
        this.a_correct = a_correct;
    }
}
