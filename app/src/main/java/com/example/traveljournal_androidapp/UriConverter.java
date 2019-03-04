package com.example.traveljournal_androidapp;

import android.net.Uri;

public class UriConverter {


    @TypeConverter
    public static String toString(Uri image){
        return image == null ? null : image.toString();
    }

    @TypeConverter
    public static Uri touri(String image){
        return image == null ? null : Uri.parse(image);
    }
}
