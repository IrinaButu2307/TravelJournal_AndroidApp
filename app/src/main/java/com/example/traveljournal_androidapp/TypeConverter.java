package com.example.traveljournal_androidapp;

public class TypeConverter {

    @androidx.room.TypeConverter
    public static String toString(Trip.TripType tripType){
        return tripType == null ? null : tripType.toString();
    }

    @androidx.room.TypeConverter
    public static Trip.TripType toTripType(String tripType){
        return tripType == null ? null : Trip.TripType.valueOf(tripType);
    }
}
