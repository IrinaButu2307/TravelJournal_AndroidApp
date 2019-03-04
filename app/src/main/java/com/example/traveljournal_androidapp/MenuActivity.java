package com.example.traveljournal_androidapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ioana.travel_journal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE = 200;
    private FirebaseFirestore mFirestore;
    private String mDocumentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initFirestore();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddTripActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String email = bundle.getString("email");
            String nume = bundle.getString("nume");
            Uri imageUri = (Uri)bundle.get("imageUri");
            TextView textViewName = headerView.findViewById(R.id.textview_type);
            TextView textViewEmail = headerView.findViewById(R.id.textview_email);
            ImageView imageViewUser = headerView.findViewById(R.id.imageview_user);
            Picasso.get().load(imageUri).into(imageViewUser);
            textViewName.setText(nume);
            textViewEmail.setText(email);
            final Map<String,Object> user = new HashMap<>();
            user.put("email",email);
            user.put("nume",nume);
            user.put("uriImagine",imageUri.toString());
            final CollectionReference users = mFirestore.collection("users");
            users.whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().size() > 0){
                            for(QueryDocumentSnapshot snapshot : task.getResult()){
                                mDocumentId = snapshot.getId();
                            }
                        }
                        else{
                            DocumentReference ref = users.document();
                            ref.set(user);
                            mDocumentId = ref.getId();
                        }
                    }
                    else{


                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                Trip trip = bundle.getParcelable("trip");
                CollectionReference trips = mFirestore.collection("users").document(mDocumentId).collection("trips");
                Map<String,Object> mappedTrip = new HashMap<>();
                mappedTrip.put("name",trip.getMName());
                mappedTrip.put("destination",trip.getMDestination());
                mappedTrip.put("tripType",trip.getMTripType());
                mappedTrip.put("rating",trip.getMRating());
                mappedTrip.put("price",trip.getMPrice());
                mappedTrip.put("startDate",trip.getMStartDate().getTimeInMillis());
                mappedTrip.put("endDate",trip.getMEndDate().getTimeInMillis());
                mappedTrip.put("picture",trip.getMPicture().toString());
                mappedTrip.put("isFavourite",false);
                DocumentReference ref = trips.document();
                mappedTrip.put("documentId",ref.getId());
                ref.set(mappedTrip);
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            8080);

                }
                else{
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    HomeFragment fragment = new HomeFragment();
                    fragment.setmDocumentID(mDocumentId);
                    transaction.replace(R.id.fragment_container,fragment);
                    transaction.commit();
                }

            }
        }
        else if(requestCode == 5123 && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                Trip trip = bundle.getParcelable("trip");
                DocumentReference trips = mFirestore.collection("users").document(mDocumentId).collection("trips").document(trip.getMDocumentId());
                Map<String,Object> mappedTrip = new HashMap<>();
                mappedTrip.put("name",trip.getMName());
                mappedTrip.put("destination",trip.getMDestination());
                mappedTrip.put("tripType",trip.getMTripType());
                mappedTrip.put("rating",trip.getMRating());
                mappedTrip.put("price",trip.getMPrice());
                mappedTrip.put("startDate",trip.getMStartDate().getTimeInMillis());
                mappedTrip.put("endDate",trip.getMEndDate().getTimeInMillis());
                mappedTrip.put("picture",trip.getMPicture().toString());
                mappedTrip.put("isFavourite",trip.ismIsFavourite());
                trips.set(mappedTrip);
                /*FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                HomeFragment fragment = new HomeFragment();
                fragment.setmDocumentID(mDocumentId);
                transaction.replace(R.id.fragment_container,fragment);
                transaction.commit();*/
            }
        }
    }

    private void initFirestore(){
        mFirestore = FirebaseFirestore.getInstance();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (id == R.id.nav_home) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        8080);

            }
            else{
                HomeFragment fragment = new HomeFragment();
                fragment.setmDocumentID(mDocumentId);
                transaction.replace(R.id.fragment_container,fragment);
                transaction.commit();
            }

        } else if (id == R.id.nav_favourite) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        8080);

            }
            else{
                FavouriteFragment fragment = new FavouriteFragment();
                fragment.setmDocumentID(mDocumentId);
                transaction.replace(R.id.fragment_container,fragment);
                transaction.commit();
            }

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 8080) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                HomeFragment fragment = new HomeFragment();
                fragment.setmDocumentID(mDocumentId);
                transaction.replace(R.id.fragment_container,fragment);
                transaction.commit();
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }
}
