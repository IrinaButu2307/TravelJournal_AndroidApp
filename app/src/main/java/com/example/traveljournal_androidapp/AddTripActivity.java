package com.example.traveljournal_androidapp;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.ioana.travel_journal.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddTripActivity extends AppCompatActivity {
    private EditText editTextDestination;
    private EditText editTextName;
    private RadioButton radioButtonCityBreak;
    private RadioButton radioButtonSeaSide;
    private RadioButton radioButtonMountains;
    private SeekBar seekBarPrice;
    private Button buttonStartDate;
    private Button buttonEndDate;
    private RatingBar ratingBar;
    private Button buttonTakePhoto;
    private Button buttonSelectPhoto;
    private ImageView imageViewTrip;
    private Trip trip;
    private static final int REQUEST_CODE = 500;
    Bitmap imagine;
    private static final int MY_CAMERA_PERMISSION_CODE = 600;
    private static final int CAMERA_REQUEST = 1888;
    Intent intent;
    private Uri uri;
    private Calendar calendarStart = Calendar.getInstance();
    private Calendar calendarEnd = Calendar.getInstance();
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        trip =  new Trip();
        initView();
        intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null){

            trip = bundle.getParcelable("trip");
            editTextDestination.setText(trip.getMDestination());
            editTextName.setText(trip.getMName());
            switch(trip.getMTripType()){
                case SEA_SIDE: radioButtonSeaSide.setChecked(true);break;
                case MOUNTAINS: radioButtonMountains.setChecked(true);break;
                case CITY_BREAK: radioButtonCityBreak.setChecked(true);break;
            }
            ratingBar.setRating(trip.getMRating());
            seekBarPrice.setProgress((int)trip.getMPrice());
            imageViewTrip.setVisibility(View.VISIBLE);
            try {
                imagine = MediaStore.Images.Media.getBitmap(this.getContentResolver(),trip.getMPicture());
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageViewTrip.setImageBitmap(imagine);
            calendarStart = trip.getMStartDate();
            calendarEnd = trip.getMEndDate();

        }

        buttonStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar startDate = Calendar.getInstance();
                        startDate.set(year,month,dayOfMonth);
                        trip.setMStartDate(startDate);
                    }
                },calendarStart.get(Calendar.YEAR),calendarStart.get(Calendar.MONTH),calendarStart.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        buttonEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar endDate = Calendar.getInstance();
                        endDate.set(year,month,dayOfMonth);
                        trip.setMEndDate(endDate);
                    }
                },calendarEnd.get(Calendar.YEAR),calendarEnd.get(Calendar.MONTH),calendarEnd.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        buttonSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
            }
        });

        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });


    }


    private void takePicture() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2020);
            }
            else{
                uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues());
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        }
    }

    private void getImageFromGallery() {
        Intent implicitIntent = new Intent();
        implicitIntent.setAction(Intent.ACTION_PICK);
        implicitIntent.setType("image/*");
        if (implicitIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(implicitIntent, REQUEST_CODE);
        }
        else{
            Toast.makeText(this,getString(R.string.cannot_pick),Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2020);
                } else {
                    uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            new ContentValues());
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        }
        else if(requestCode == 2020){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues());
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (imagine != null) {
                imagine.recycle();
            }
            InputStream inputStream = null;
            try {
                //inputStream = getContentResolver().openInputStream(data.getData());
                //imagine = BitmapFactory.decodeStream(inputStream);
                Uri selectedImage = data.getData();
                trip.setMPicture(selectedImage);
                imagine = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imageViewTrip.setImageBitmap(imagine);
                imageViewTrip.setScaleType(ImageView.ScaleType.FIT_XY);
                imageViewTrip.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } /*finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

        }
        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

                //imagine = (Bitmap)data.getExtras().get("data");
            try {
                imagine =  MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                imageViewTrip.setImageBitmap(imagine);
                imageViewTrip.setScaleType(ImageView.ScaleType.FIT_XY);
                imageViewTrip.setVisibility(View.VISIBLE);
                trip.setMPicture(uri);

               // Toast.makeText(this,uri.toString(),Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }





        }

    }


    private void initView(){
        editTextDestination = findViewById(R.id.destinationET);
        editTextName = findViewById(R.id.tripNameET);
        radioButtonCityBreak = findViewById(R.id.radiobutton_city_break);
        radioButtonMountains = findViewById(R.id.radiobutton_mountains);
        radioButtonSeaSide = findViewById(R.id.radiobutton_sea_side);
        seekBarPrice = findViewById(R.id.seekbar_price);
        buttonStartDate = findViewById(R.id.startDateBtn);
        buttonEndDate = findViewById(R.id.endDateBtn);
        ratingBar = findViewById(R.id.ratingbar);
        buttonTakePhoto = findViewById(R.id.button_take_picture);
        buttonSelectPhoto = findViewById(R.id.button_select_picture);
        imageViewTrip = findViewById(R.id.imageview_trip_picture);

    }

    private boolean validDestination(){
        if(editTextDestination !=null){
            String destination = editTextDestination.getText().toString();
            if(destination != null && !destination.isEmpty()){
                trip.setMDestination(destination);
                return true;
            }
        }
        editTextDestination.setError("Please specify the destination");
        return false;
    }

    private boolean validName(){
        if(editTextName !=null){
            String name = editTextName.getText().toString();
            if(name != null && !name.isEmpty()){
                trip.setMName(name);
                return true;
            }
        }
        editTextName.setError("Please specify a name for the trip");
        return false;
    }

    private boolean validType(){
        return radioButtonSeaSide.isChecked() || radioButtonCityBreak.isChecked() || radioButtonMountains.isChecked();
    }

    private boolean validStartDate(){
        if(trip.getMStartDate() != null){
            return true;
        }
        else{
            buttonStartDate.setError("Please specify a start date for the trip");
            return false;
        }

    }

    private boolean validEndDate(){
        if(trip.getMEndDate() != null){
            return true;
        }
        else{
            buttonStartDate.setError("Please specify an end date for the trip");
            return false;
        }
    }

    private boolean validPicture(){
        return (trip.getMPicture() != null);
    }

    public void saveOnClick(View view) {
        if(validDestination() && validName() && validType() && validStartDate() && validEndDate() && validPicture()) {
            //Bitmap imag = ((BitmapDrawable)imageViewTrip.getDrawable()).getBitmap();
            /*if(imagine != null){
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                imagine.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                imagine.recycle();
                byte[] array = outputStream.toByteArray();
                String imagineEncoded = Base64.encodeToString(array, Base64.DEFAULT);
                Toast.makeText(view.getContext(),imagineEncoded,Toast.LENGTH_LONG).show();
                trip.setmPicture(imagineEncoded);

            }*/
            /*CompressBitmap compressBitmap = new CompressBitmap(){
                @Override
                protected void onPostExecute(byte[] bytes) {
                    if(bytes != null){

                        trip.setmPicture(bytes);
                        if(radioButtonCityBreak.isChecked()){
                            trip.setmTripType(Trip.TripType.CITY_BREAK);
                        }
                        if(radioButtonMountains.isChecked()){
                            trip.setmTripType(Trip.TripType.MOUNTAINS);
                        }
                        if(radioButtonSeaSide.isChecked()){
                            trip.setmTripType(Trip.TripType.SEA_SIDE);
                        }
                        trip.setmPrice(seekBarPrice.getProgress());
                        trip.setmRating(ratingBar.getRating());

                        intent.putExtra("trip",trip);
                        setResult(RESULT_OK,intent);
                        finish();
                        Toast.makeText(getApplicationContext(),"i got here",Toast.LENGTH_LONG).show();
                        return;

                    }
                }
            };
            compressBitmap.execute(imagine);*/


            if (radioButtonCityBreak.isChecked()) {
                trip.setMTripType(Trip.TripType.CITY_BREAK);
            }
            if (radioButtonMountains.isChecked()) {
                trip.setMTripType(Trip.TripType.MOUNTAINS);
            }
            if (radioButtonSeaSide.isChecked()) {
                trip.setMTripType(Trip.TripType.SEA_SIDE);
            }
            trip.setMPrice(seekBarPrice.getProgress());
            trip.setMRating(ratingBar.getRating());
            trip.setmIsFavourite(false);
            intent.putExtra("trip", trip);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
