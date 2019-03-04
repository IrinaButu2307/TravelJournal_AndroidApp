package com.example.traveljournal_androidapp;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ioana.travel_journal.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TripViewHolder extends RecyclerView.ViewHolder {
    public ImageView mImageViewPicture;
    public TextView mTextViewName;
    public TextView mTextViewDestination;
    public TextView mTextViewRating;
    public CheckBox mCheckBoxFavourite;


    public TripViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageViewPicture = itemView.findViewById(R.id.imageview_picture);
        mTextViewName = itemView.findViewById(R.id.textview_type);
        mTextViewDestination = itemView.findViewById(R.id.textview_type);
        mTextViewRating = itemView.findViewById(R.id.textview_rating);
        mCheckBoxFavourite = itemView.findViewById(R.id.checkbox_bookmark);
    }



}



