package com.example.traveljournal_androidapp;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.ioana.travel_journal.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<DocumentSnapshot> mData;
    private String mDocumentID;
    private static final int REQUEST_CODE = 5123;
    TripAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AppDatabase appDatabase;
    private boolean check = false;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mData= new ArrayList<>();
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        appDatabase = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
        mRecyclerView = view.findViewById(R.id.recyclerview_trips);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity()); //ca parametru primeste contextul activitatii
        mRecyclerView.setLayoutManager(layoutManager);

        CollectionReference trips = db.collection("users").document(mDocumentID).collection("trips");
        /*trips.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot trip : task.getResult()){
                        mData.add(trip);
                    }
                    adapter = new TripAdapter(mData,false);
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }
        });*/
        adapter = new TripAdapter(mData,false);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        trips.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange change : queryDocumentSnapshots.getDocumentChanges()){
                    if(change.getType() == DocumentChange.Type.MODIFIED){
                        mData.set(change.getOldIndex(),change.getDocument());
                        adapter.notifyItemChanged(change.getOldIndex());
                    }

                    else if(change.getType() == DocumentChange.Type.ADDED && adapter != null){
                        mData.add(change.getDocument());
                        adapter.notifyItemInserted(change.getNewIndex());
                    }
                    else if(change.getType() == DocumentChange.Type.REMOVED && adapter != null){
                        mData.remove(change.getOldIndex());
                        adapter.notifyItemRemoved(change.getOldIndex());
                    }
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getActivity(), mRecyclerView, new TripClickListener() {

            private Trip getTrip(DocumentSnapshot trip){
                Trip newTrip = new Trip();
                newTrip.setMName((String)trip.get("name"));
                newTrip.setMDestination((String)trip.get("destination"));
                newTrip.setMPrice(((Double)trip.get("price")).floatValue());
                newTrip.setMRating(((Double)trip.get("rating")).floatValue());
                newTrip.setMTripType(Trip.TripType.valueOf(trip.get("tripType").toString()));
                newTrip.setMPicture(Uri.parse((String)trip.get("picture")));
                Calendar startDate = new GregorianCalendar();
                startDate.setTimeInMillis((long)trip.get("startDate"));
                newTrip.setMStartDate(startDate);
                Calendar endDate = new GregorianCalendar();
                startDate.setTimeInMillis((long)trip.get("endDate"));
                newTrip.setMEndDate(endDate);
                newTrip.setMDocumentId((String)trip.get("documentId"));


                return newTrip;
            }



            @Override
            public void onClick(View view, final int position) {
                final CheckBox checkBoxFavourite = view.findViewById(R.id.checkbox_bookmark);
                checkBoxFavourite.requestFocus();
                checkBoxFavourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check = true;
                        DocumentSnapshot trip = ((TripAdapter)mRecyclerView.getAdapter()).getItemAtPosition(position);
                        final DocumentReference ref = db.collection("users").document(mDocumentID).collection("trips").document(trip.getId());
                        final Trip favourite = getTrip(trip);
                        if(checkBoxFavourite.isChecked() == true){

                            ref.update("isFavourite",true);
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Insert Data
                                    appDatabase.tripDao().insertTrip(favourite);
                                    List<Trip> listOfTrips = appDatabase.tripDao().getAllTrips();
                                    Log.i("ListOfTrips",listOfTrips.toString());
                                }
                            });

                        }
                        else{
                            ref.update("isFavourite",false);
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Delete Data
                                    appDatabase.tripDao().deleteTrip(favourite);
                                    List<Trip> listOfTrips = appDatabase.tripDao().getAllTrips();
                                    Log.i("ListOfTrips",listOfTrips.toString());
                                }
                            });
                        }
                        return;
                    }

                });
                if(check == false){
                    DocumentSnapshot trip = ((TripAdapter)mRecyclerView.getAdapter()).getItemAtPosition(position);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    TripDetailFragment fragment = new TripDetailFragment();
                    fragment.setTrip(getTrip(trip));
                    transaction.replace(R.id.fragment_container,fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
                else{
                    check = false;
                    return;
                }




            }

            @Override
            public void onLongClick(View view, int position) {
                Intent intent = new Intent(getActivity(),AddTripActivity.class);
                DocumentSnapshot trip = ((TripAdapter)mRecyclerView.getAdapter()).getItemAtPosition(position);
                Trip newTrip = getTrip(trip);
                intent.putExtra("trip",newTrip);
                getActivity().startActivityForResult(intent,REQUEST_CODE);
                //Toast.makeText(getActivity().getApplicationContext(),position + " " + newTrip,Toast.LENGTH_LONG).show();

            }
        }));

        return view;
    }



    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                Trip trip = bundle.getParcelable("trip");
                int position = bundle.getInt("position");
                mData.set(position,trip);
            }
        }
    }*/



    /*private List<Trip> getData(){
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip("Holiday 2017","Islands","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSlHt7m4IqKmbwwtA6JvpTO0bs7djlf8__OTzyWgPJ0EsuD7Bw8Q",5));
        trips.add(new Trip("Fall 2017","Rome","https://brightcove04pmdo-a.akamaihd.net/5104226627001/5104226627001_5232386545001_5215063851001-vs.jpg?pubId=5104226627001&videoId=5215063851001",4.5f));
        trips.add(new Trip("Summer 2017","London","https://cdn.londonandpartners.com/visit/general-london/areas/river/76709-640x360-houses-of-parliament-and-london-eye-on-thames-from-above-640.jpg",3.5f));
        trips.add(new Trip("Winter 2017","Paris","http://tourio.ro/wp-content/uploads/2017/02/paris-toamna.jpg",3));
        trips.add(new Trip("Spring 2017","Singapore","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTM4va7LFM3J-qOLkh6cdzPI4EPX7j8GeqNmuaR2S0vx8TVwcT-",4));
        trips.add(new Trip("Winter 2018","Dubai","http://oferte-vacante-interturism.ro/wp-content/uploads/2014/05/Oferta-Speciala-Dubai.jpg",4.5f));
        return trips;
    }*/


    public void setmDocumentID(String documentID) {
        this.mDocumentID = documentID;
    }
}
