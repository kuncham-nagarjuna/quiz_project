package com.quiz.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    private FeedbackData[] listdata;

    public FeedbackAdapter(FeedbackData[] list) {
        this.listdata = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.feedback_card_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FeedbackData myListData = listdata[position];
        holder.review.setText(listdata[position].getReview());
        holder.score.setText("Score : " + listdata[position].getScore());
        holder.review_date.setText(listdata[position].getDate());
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView review_date, score, review;

        public ViewHolder(View itemView) {
            super(itemView);
            review_date = (TextView) itemView.findViewById(R.id.review_date);
            score = (TextView) itemView.findViewById(R.id.score);
            review = (TextView) itemView.findViewById(R.id.review);
        }
    }
}
