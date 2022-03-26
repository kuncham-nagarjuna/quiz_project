package com.quiz.quiz;

public class FeedbackData {

    private String review, date, score;

    public String getReview() {
        return review;
    }

    public String getDate() {
        return date;
    }

    public String getScore() {
        return score;
    }

    public FeedbackData(String review, String date, String scrore){
        this.review = review;
        this.date = date;
        this.score = scrore;
    }
}
