package com.example.traveljournal_androidapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ioana.travel_journal.R;

import java.util.Calendar;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripDetailFragment extends Fragment {
    private TextView mTextViewName;
    private TextView mTextViewDestination;
    private TextView mTextViewType;
    private RatingBar mRatingBar;
    private TextView mTextViewPrice;
    private TextView mTextViewStarDate;
    private TextView mTextViewEndDate;
    private ImageView mImageViewPicture;
    private Button mButtonBack;
    private Trip mTrip;


    public TripDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_trip_detail, container, false);
        initView(view);
        mTextViewName.setText(mTrip.getMName());
        mTextViewDestination.setText(mTrip.getMDestination());
        mTextViewType.setText(mTrip.getMTripType().toString());
        mRatingBar.setRating(mTrip.getMRating());
        mImageViewPicture.setImageURI(mTrip.getMPicture());
        mTextViewPrice.setText(getString(R.string.price) + mTrip.getMPrice() + getString(R.string.eur));
        Calendar startDate = mTrip.getMStartDate();
        mTextViewStarDate.setText(getString(R.string.start) + startDate.get(Calendar.DAY_OF_MONTH) + "/" + (1 + startDate.get(Calendar.MONTH)) + "/" + startDate.get(Calendar.YEAR));
        Calendar endDate = mTrip.getMEndDate();
        mTextViewEndDate.setText(getString(R.string.end)+ endDate.get(Calendar.DAY_OF_MONTH) + "/" + (1 + endDate.get(Calendar.MONTH)) + "/" + endDate.get(Calendar.YEAR));
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
        return view;
    }

    private void initView(View view){
        mTextViewName = view.findViewById(R.id.textview_name);
        mTextViewDestination = view.findViewById(R.id.textview_destination);
        mTextViewPrice = view.findViewById(R.id.textview_price);
        mRatingBar = view.findViewById(R.id.ratingbar_detail);
        mTextViewType = view.findViewById(R.id.textview_type);
        mTextViewStarDate = view.findViewById(R.id.textview_start_date);
        mTextViewEndDate = view.findViewById(R.id.textView_end_date);
        mButtonBack = view.findViewById(R.id.button_back);
        mImageViewPicture = view.findViewById(R.id.imageview_trip_detail);
    }

    public void setTrip(Trip trip){
        mTrip = trip;
    }

}
