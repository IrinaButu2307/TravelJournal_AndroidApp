package com.example.traveljournal_androidapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ioana.travel_journal.R;
import com.squareup.picasso.Picasso;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class CardViewActivity extends AppCompatActivity {

    private Trip mTrip;

    private TextView mTextViewTitle;
    private TextView mTextViewDestination;
    private TextView mTextViewStartDate;
    private TextView mTextViewEndDate;
    private TextView mTextViewTripType;
    private RatingBar mRatingBarTripRating;
    private ImageView mImageViewTripPic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        Intent intent = getIntent();
        mTrip = (Trip) intent.getSerializableExtra("TripToShow");

        mTextViewTitle = findViewById(R.id.textView_details_title);
        mTextViewDestination = findViewById(R.id.textView_details_location);
        mTextViewTripType = findViewById(R.id.text_view_details_trip_type);
        mTextViewStartDate = findViewById(R.id.text_view_start_date);
        mTextViewEndDate = findViewById(R.id.text_view_end_date);
        mRatingBarTripRating = findViewById(R.id.rating_bar_details_rate);
        mImageViewTripPic = findViewById(R.id.imageView_details_image);


        showTrip();
    }

    private void showTrip() {

        mTextViewTitle.setText(mTrip.getmTitle());
        mTextViewDestination.setText(mTrip.getmLocation());

        mTextViewTripType.setText(mTrip.getTripType().toString());

        Date startDate = mTrip.getMStartDate();
        String startDateString = startDate.getDay() + "/" + startDate.getMonth() + "/" + startDate.getYear();
        Date endDate = mTrip.getMEndDate();
        String endDateString = endDate.getDay() + "/" + endDate.getMonth() + "/" + endDate.getYear();

        mTextViewStartDate.setText(startDateString);
        mTextViewEndDate.setText(endDateString);

        mRatingBarTripRating.setRating((float)mTrip.getMRating());
        Picasso.get().load(Uri.parse(mTrip.getMImageUri())).fit().into(mImageViewTripPic);

    }

}

