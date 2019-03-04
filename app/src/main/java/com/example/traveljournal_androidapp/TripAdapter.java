package com.example.traveljournal_androidapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ioana.travel_journal.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {
    private List<DocumentSnapshot> mList;
    private Context context;
    private boolean mIsFavourite;

    public TripAdapter(List<DocumentSnapshot> lista,boolean isFavourite){
        mList = lista;
        mIsFavourite = isFavourite;

    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //creem item-ul respectiv
        View tripView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip_item,null);
        context = viewGroup.getContext();
        //returnam un viewHolder
        return new TripViewHolder(tripView);

    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder tripViewHolder, int i) {
        DocumentSnapshot trip = mList.get(i);
        tripViewHolder.mTextViewName.setText(trip.get("name").toString());
        tripViewHolder.mTextViewDestination.setText(trip.get("destination").toString());
        /*byte[] decodedBytes = Base64.decode(trip.getmPicture(), 0);
        Bitmap tripImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);*/
        Bitmap tripImage = null;

        try {
            tripImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(),Uri.parse(trip.get("picture").toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tripViewHolder.mImageViewPicture.setImageBitmap(tripImage);
        tripViewHolder.mTextViewRating.setText(trip.get("rating") + "/5.0");
        if(mIsFavourite){
            tripViewHolder.mCheckBoxFavourite.setVisibility(View.INVISIBLE);
        }
        else{

            tripViewHolder.mCheckBoxFavourite.setChecked((boolean)trip.get("isFavourite"));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public DocumentSnapshot getItemAtPosition(int position){
        return this.mList.get(position);
    }


}
