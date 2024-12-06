package com.example.carpooling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Login.db";
    private static final int DATABASE_VERSION = 18;
    private static final String DATABASE_TABLE_PASSENGERS = "Passengers";
    private static final String DATABASE_TABLE_DRIVERS = "Drivers";
    private static final String DATABASE_TABLE_RIDES = "Rides";
    private static final String DATABASE_TABLE_REQUESTS = "Requests";

    private static final String ID = "id";
    private static final String fullName = "fullName";
    private static final String emailAddress = "email";
    private static final String password = "password";
    private static final String userType = "userType";
    private static final String rating = "rating";
    private static final String carModel = "carModel";
    private static final String carLicensePlate = "carLicensePlate";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DATABASE_TABLE_PASSENGERS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                fullName+ " TEXT, "+ emailAddress +" TEXT, " + password + " TEXT, " + userType + " TEXT, "
        + rating + " REAL)";
        db.execSQL(query);

        query = "CREATE TABLE " + DATABASE_TABLE_DRIVERS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                fullName+ " TEXT, "+ emailAddress +" TEXT, " + password + " TEXT, " + userType + " TEXT, "
                + rating + " REAL, " + carModel + " TEXT, " + carLicensePlate + " TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE " + DATABASE_TABLE_RIDES + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "driverId INTEGER, passengerId INTEGER, carModel TEXT, carLicensePlate TEXT, " +
                "driverRating REAL, passengerRating REAL, " +
                "driverStartLat REAL, driverStartLng REAL, driverDestinationLat REAL, driverDestinationLng REAL, " +
                "driverCurrentLat REAL, driverCurrentLng REAL, " + "passengerCurrentLat REAL, passengerCurrentLng REAL, " +
                "passengerStartLat REAL, passengerStartLng REAL, passengerDestinationLat REAL, passengerDestinationLng REAL, " +
                "rideDateAndTime TEXT, status TEXT, cost REAL, " +
                "FOREIGN KEY (passengerId) REFERENCES " + DATABASE_TABLE_PASSENGERS+"(id), " +
                "FOREIGN KEY (driverId) REFERENCES " + DATABASE_TABLE_DRIVERS+"(id))";
        db.execSQL(query);
        query = "CREATE TABLE " + DATABASE_TABLE_REQUESTS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "rideId INTEGER, driverId INTEGER, passengerId INTEGER, status TEXT, " +
                "passengerStartLat REAL, passengerStartLng REAL, passengerDestinationLat REAL, passengerDestinationLng REAL, "+
                "FOREIGN KEY (passengerId) REFERENCES " + DATABASE_TABLE_PASSENGERS+"(id), " +
                "FOREIGN KEY (driverId) REFERENCES " + DATABASE_TABLE_DRIVERS+"(id), " +
                "FOREIGN KEY (rideId) REFERENCES " + DATABASE_TABLE_RIDES+"(id))";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PASSENGERS);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_DRIVERS);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_RIDES);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_REQUESTS);

        onCreate(db);
    }


    public boolean addPassenger(Passenger passenger){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {emailAddress};
        String selection = "email=?";
        String[] selectionArgs = {passenger.getEmail()};

        Cursor cursor = db.query(DATABASE_TABLE_PASSENGERS, columns, selection, selectionArgs, null, null, null);
        Cursor cursor2 = db.query(DATABASE_TABLE_DRIVERS, columns, selection, selectionArgs, null, null, null);

        if ( (cursor != null && cursor.moveToFirst()) || (cursor2 != null && cursor2.moveToFirst())) {
            cursor.close();
            cursor2.close();
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(fullName , passenger.getFullName());
        contentValues.put(emailAddress, passenger.getEmail());
        contentValues.put(password, passenger.getPassword());
        contentValues.put(userType, passenger.getUserType());
        contentValues.put(rating, 0);

        long result = db.insert(DATABASE_TABLE_PASSENGERS, null, contentValues);
        return result != -1;
    }
    public boolean addDriver(Driver driver){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {emailAddress};
        String selection = "email=?";
        String[] selectionArgs = {driver.getEmail()};

        Cursor cursor = db.query(DATABASE_TABLE_DRIVERS, columns, selection, selectionArgs, null, null, null);
        Cursor cursor2 = db.query(DATABASE_TABLE_PASSENGERS, columns, selection, selectionArgs, null, null, null);

        if ( (cursor != null && cursor.moveToFirst()) || (cursor2 != null && cursor2.moveToFirst())) {
            cursor.close();
            cursor2.close();
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(fullName , driver.getFullName());
        contentValues.put(emailAddress, driver.getEmail());
        contentValues.put(password, driver.getPassword());
        contentValues.put(userType, driver.getUserType());
        contentValues.put(carModel, driver.getCarModel());
        contentValues.put(carLicensePlate, driver.getCarLicensePlate());
        contentValues.put(rating, 0);

        long result = db.insert(DATABASE_TABLE_DRIVERS, null, contentValues);
        return result != -1;
    }

    public Passenger loginPassenger(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        //String[] columns = {fullName};
        String selection = "email=? AND password=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(DATABASE_TABLE_PASSENGERS, null, selection, selectionArgs, null, null ,null);
        Passenger passenger = null;
        if(cursor != null && cursor.moveToFirst()){
//            int columnIndex = cursor.getColumnIndex(fullName);
//            if(columnIndex != -1){
//                result = cursor.getString(columnIndex);
//            }
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(fullName));
            String emailAddr = cursor.getString(cursor.getColumnIndexOrThrow(emailAddress));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(userType));
            float rate = cursor.getFloat(cursor.getColumnIndexOrThrow(rating));

            passenger = new Passenger(id, name, emailAddr, type, rate);

            cursor.close();

        }

        return passenger;

    }

    public Driver logInDriver(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        //String[] columns = {fullName};
        String selection = "email=? AND password=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(DATABASE_TABLE_DRIVERS, null, selection, selectionArgs, null, null ,null);
        Driver driver = null;
        if(cursor != null && cursor.moveToFirst()){
//            int columnIndex = cursor.getColumnIndex(fullName);
//            if(columnIndex != -1){
//                result = cursor.getString(columnIndex);
//            }

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            Log.d("DriverID", String.valueOf(id));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(fullName));
            String emailAddr = cursor.getString(cursor.getColumnIndexOrThrow(emailAddress));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(userType));
            float rate = cursor.getFloat(cursor.getColumnIndexOrThrow(rating));
            String car = cursor.getString(cursor.getColumnIndexOrThrow(carModel));
            String plate = cursor.getString(cursor.getColumnIndexOrThrow(carLicensePlate));

            driver = new Driver(id, name, emailAddr, type, rate, car, plate);
            cursor.close();
        }

        return driver;
    }

    public boolean addRide(Ride ride){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //contentValues.put("passengerId", ride.getPassengerId()); Nema passenger na pochetoko
        contentValues.put("driverId", ride.getDriverId());
        contentValues.put("carModel", ride.getCarModel());
        contentValues.put("carLicensePlate", ride.getCarLicensePlate());
        contentValues.put("driverStartLat", ride.getDriverStartLat());
        contentValues.put("driverStartLng", ride.getDriverStartLng());
        contentValues.put("driverDestinationLat", ride.getDriverDestinationLat());
        contentValues.put("driverDestinationLng", ride.getDriverDestinationLng());
        contentValues.put("driverCurrentLat", ride.getDriverStartLat());
        contentValues.put("driverCurrentLng", ride.getDriverStartLng());
        contentValues.put("rideDateAndTime", ride.getRideDateAndTime());
        contentValues.put("status", ride.getStatus());
        contentValues.put("cost", ride.getCost());

        long result = db.insert(DATABASE_TABLE_RIDES, null, contentValues);
        return result != -1;
    }

    public boolean addPassengerToRide(int rideId, int passengerId, LatLng start, LatLng destination){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("passengerId", passengerId);
        contentValues.put("passengerStartLat", start.latitude);
        contentValues.put("passengerStartLng", start.longitude);
        contentValues.put("passengerDestinationLat", destination.latitude);
        contentValues.put("passengerDestinationLng", destination.longitude);
        contentValues.put("passengerCurrentLat", start.latitude);
        contentValues.put("passengerCurrentLng", start.longitude);
        contentValues.put("status", "pending");

        String[] selectionArgs = {String.valueOf(rideId)};

        int rowsUpdated = db.update(DATABASE_TABLE_RIDES, contentValues, "id = ?", selectionArgs);

        return rowsUpdated > 0;
    }

    public boolean updateRideStatus(int rideId, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("status", status);
        String[] selectionArgs = {String.valueOf(rideId)};

        int rowsUpdated = db.update(DATABASE_TABLE_RIDES, contentValues, "id=?", selectionArgs);

        return rowsUpdated > 0;
    }

    public boolean updateDriverCurrentLocation(int rideId, double lat, double lng){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("driverCurrentLat", lat);
        contentValues.put("driverCurrentLng", lng);

        String[] selectionArgs = {String.valueOf(rideId)};

        int rowsUpdated = db.update(DATABASE_TABLE_RIDES, contentValues, "id=?", selectionArgs);

        return rowsUpdated > 0;
    }
    public boolean updatePassengerCurrentLocation(int rideId, double lat, double lng){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("passengerCurrentLat", lat);
        contentValues.put("passengerCurrentLng", lng);

        String[] selectionArgs = {String.valueOf(rideId)};

        int rowsUpdated = db.update(DATABASE_TABLE_RIDES, contentValues, "id=?", selectionArgs);

        return rowsUpdated > 0;
    }

    public void setDriverRatingFromRide(int rideId, float driverRating){
        SQLiteDatabase db = this.getReadableDatabase();
        int driverId = 0;
        float overAllRating = driverRating;
        String query = "SELECT " + "driverId" + " FROM " + DATABASE_TABLE_RIDES + " WHERE id=?";
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(rideId)});
        if(cursor != null && cursor.moveToFirst()){
            driverId = cursor.getInt(cursor.getColumnIndexOrThrow("driverId"));
            cursor.close();
        }

        query = "SELECT " + rating + " FROM " + DATABASE_TABLE_DRIVERS + " WHERE id=?";
        Cursor cursor1 = db.rawQuery(query, new String[] {String.valueOf(driverId)});

        if(cursor1 != null && cursor1.moveToFirst()){
            overAllRating += cursor1.getFloat(cursor1.getColumnIndexOrThrow(rating));
            overAllRating /= 2;
            cursor1.close();
        }

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("driverRating", driverRating);
        String[] selectionArgs = {String.valueOf(rideId)};
        db.update(DATABASE_TABLE_RIDES, contentValues, "id=?", selectionArgs);

        contentValues = new ContentValues();
        contentValues.put(rating, overAllRating);

        String[] selectionArgsDriver = {String.valueOf(driverId)};
        db.update(DATABASE_TABLE_DRIVERS, contentValues, "id=?", selectionArgsDriver);
    }
    public void setPassengerRatingFromRide(int rideId, float passengerRating){
        SQLiteDatabase db = this.getReadableDatabase();
        int passengerId = 0;
        float overAllRating = passengerRating;
        String query = "SELECT " + "passengerId" + " FROM " + DATABASE_TABLE_RIDES + " WHERE id=?";
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(rideId)});
        if(cursor != null && cursor.moveToFirst()){
            passengerId = cursor.getInt(cursor.getColumnIndexOrThrow("passengerId"));
            cursor.close();
        }

        query = "SELECT " + rating + " FROM " + DATABASE_TABLE_PASSENGERS + " WHERE id=?";
        Cursor cursor1 = db.rawQuery(query, new String[] {String.valueOf(passengerId)});

        if(cursor1 != null && cursor1.moveToFirst()){
            overAllRating += cursor1.getFloat(cursor1.getColumnIndexOrThrow(rating));
            overAllRating /= 2;
            cursor1.close();
        }

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("passengerRating", passengerRating);
        String[] selectionArgs = {String.valueOf(rideId)};
        db.update(DATABASE_TABLE_RIDES, contentValues, "id=?", selectionArgs);

        contentValues = new ContentValues();
        contentValues.put(rating, overAllRating);

        String[] selectionArgsPassenger = {String.valueOf(passengerId)};
        db.update(DATABASE_TABLE_PASSENGERS, contentValues, "id=?", selectionArgsPassenger);
    }
    public LatLng getDriverCurrentLocation(int rideId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT driverCurrentLat, driverCurrentLng FROM " + DATABASE_TABLE_RIDES +
                " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(rideId)});
        double lat;
        double lng;
        LatLng result = null;
        if(cursor != null && cursor.moveToFirst()){
            lat = cursor.getDouble(cursor.getColumnIndexOrThrow("driverCurrentLat"));
            lng = cursor.getDouble(cursor.getColumnIndexOrThrow("driverCurrentLng"));
            result = new LatLng(lat, lng);
            cursor.close();
        }

        return result;
    }
    public LatLng getPassengerCurrentLocation(int rideId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT passengerCurrentLat, passengerCurrentLng FROM " + DATABASE_TABLE_RIDES +
                " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(rideId)});
        double lat;
        double lng;
        LatLng result = null;
        if(cursor != null && cursor.moveToFirst()){
            lat = cursor.getDouble(cursor.getColumnIndexOrThrow("passengerCurrentLat"));
            lng = cursor.getDouble(cursor.getColumnIndexOrThrow("passengerCurrentLng"));
            result = new LatLng(lat, lng);
            cursor.close();
        }

        return result;
    }


    public List<Ride> getRidesByStatus(String status){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Ride> rides = new ArrayList<>();

        String query = "SELECT * FROM " + DATABASE_TABLE_RIDES + " WHERE status = ?";
        Cursor cursor = db.rawQuery(query, new String[]{status});

        if(cursor != null && cursor.moveToFirst()){
            do{
                Ride ride = new Ride();
                ride.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                ride.setPassengerId(cursor.getInt(cursor.getColumnIndexOrThrow("passengerId")));
                ride.setDriverId(cursor.getInt(cursor.getColumnIndexOrThrow("driverId")));
                ride.setCarModel(cursor.getString(cursor.getColumnIndexOrThrow("carModel")));
                ride.setCarLicensePlate(cursor.getString(cursor.getColumnIndexOrThrow("carLicensePlate")));
                ride.setDriverRating(cursor.getFloat(cursor.getColumnIndexOrThrow("driverRating")));
                ride.setPassengerRating(cursor.getFloat(cursor.getColumnIndexOrThrow("passengerRating")));
                ride.setDriverStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("driverStartLat")));
                ride.setDriverStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("driverStartLng")));
                ride.setDriverDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("driverDestinationLat")));
                ride.setDriverDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("driverDestinationLng")));

                ride.setPassengerStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLat")));
                ride.setPassengerStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLng")));
                ride.setPassengerDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLat")));
                ride.setPassengerDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLng")));

                ride.setRideDateAndTime(cursor.getString(cursor.getColumnIndexOrThrow("rideDateAndTime")));
                ride.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                ride.setCost(cursor.getFloat(cursor.getColumnIndexOrThrow("cost")));
                rides.add(ride);
            }while(cursor.moveToNext());

            cursor.close();
            return rides;
        }

        return null;
    }

    public List<Ride> getRidesByPassengerId(int passengerId){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Ride> rides = new ArrayList<>();

        String query = "SELECT * FROM " + DATABASE_TABLE_RIDES + " WHERE passengerId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(passengerId)});

        if(cursor != null && cursor.moveToFirst()){
            do{
                Ride ride = new Ride();
                ride.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                ride.setPassengerId(cursor.getInt(cursor.getColumnIndexOrThrow("passengerId")));
                ride.setDriverId(cursor.getInt(cursor.getColumnIndexOrThrow("driverId")));
                ride.setCarModel(cursor.getString(cursor.getColumnIndexOrThrow("carModel")));
                ride.setCarLicensePlate(cursor.getString(cursor.getColumnIndexOrThrow("carLicensePlate")));
                ride.setDriverRating(cursor.getFloat(cursor.getColumnIndexOrThrow("driverRating")));
                ride.setPassengerRating(cursor.getFloat(cursor.getColumnIndexOrThrow("passengerRating")));
                ride.setDriverStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("driverStartLat")));
                ride.setDriverStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("driverStartLng")));
                ride.setDriverDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("driverDestinationLat")));
                ride.setDriverDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("driverDestinationLng")));

                ride.setPassengerStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLat")));
                ride.setPassengerStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLng")));
                ride.setPassengerDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLat")));
                ride.setPassengerDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLng")));

                ride.setRideDateAndTime(cursor.getString(cursor.getColumnIndexOrThrow("rideDateAndTime")));
                ride.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                ride.setCost(cursor.getFloat(cursor.getColumnIndexOrThrow("cost")));
                rides.add(ride);
            }while(cursor.moveToNext());

            cursor.close();
            return rides;
        }

        return null;
    }

    public List<Ride> getRidesByDriverId(int driverId){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Ride> rides = new ArrayList<>();

        String query = "SELECT * FROM " + DATABASE_TABLE_RIDES + " WHERE driverId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(driverId)});
        if(cursor != null && cursor.moveToFirst()){
            do{
                Ride ride = new Ride();
                ride.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                ride.setPassengerId(cursor.getInt(cursor.getColumnIndexOrThrow("passengerId")));
                ride.setDriverId(cursor.getInt(cursor.getColumnIndexOrThrow("driverId")));
                ride.setCarModel(cursor.getString(cursor.getColumnIndexOrThrow("carModel")));
                ride.setCarLicensePlate(cursor.getString(cursor.getColumnIndexOrThrow("carLicensePlate")));
                ride.setDriverRating(cursor.getFloat(cursor.getColumnIndexOrThrow("driverRating")));
                ride.setPassengerRating(cursor.getFloat(cursor.getColumnIndexOrThrow("passengerRating")));
                ride.setDriverStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("driverStartLat")));
                ride.setDriverStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("driverStartLng")));
                ride.setDriverDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("driverDestinationLat")));
                ride.setDriverDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("driverDestinationLng")));

                ride.setPassengerStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLat")));
                ride.setPassengerStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLng")));
                ride.setPassengerDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLat")));
                ride.setPassengerDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLng")));

                ride.setRideDateAndTime(cursor.getString(cursor.getColumnIndexOrThrow("rideDateAndTime")));
                ride.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                ride.setCost(cursor.getFloat(cursor.getColumnIndexOrThrow("cost")));
                rides.add(ride);
            }while(cursor.moveToNext());

            cursor.close();
            return rides;
        }

        return null;
    }

    public String getPassengerName(int passengerId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT fullName FROM " + DATABASE_TABLE_PASSENGERS +
                " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(passengerId)});
        String result = null;
        if(cursor != null && cursor.moveToFirst()){
            result = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));
            cursor.close();
        }

        return result;
    }
    public float getPassengerRating(int passengerId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT rating FROM " + DATABASE_TABLE_PASSENGERS +
                " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(passengerId)});
        float result = -1;
        if(cursor != null && cursor.moveToFirst()){
            result = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
            cursor.close();
        }

        return result;
    }

    public String getDriverName(int driverId){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {fullName};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(driverId)};
//        String query = "SELECT fullName FROM " + DATABASE_TABLE_DRIVERS +
//                " WHERE id = ?";
//
        Cursor cursor = db.query(DATABASE_TABLE_DRIVERS, columns, selection, selectionArgs, null, null, null);
        String result = null;
        if(cursor != null && cursor.moveToFirst()){
            result = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));
            cursor.close();
        }

        return result;
    }
    public float getDriverRating(int driverId){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {rating};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(driverId)};

        Cursor cursor = db.query(DATABASE_TABLE_DRIVERS, columns, selection, selectionArgs, null, null, null);
        float result = 0;
        if(cursor != null && cursor.moveToFirst()){
            result = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
            cursor.close();
        }

        return result;
    }
    public Ride getRideById(int rideId){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(rideId)};

        Cursor cursor = db.query(DATABASE_TABLE_RIDES, null, selection, selectionArgs, null, null, null);
        Ride ride = new Ride();
        if(cursor != null && cursor.moveToFirst()){
            ride.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            ride.setPassengerId(cursor.getInt(cursor.getColumnIndexOrThrow("passengerId")));
            ride.setDriverId(cursor.getInt(cursor.getColumnIndexOrThrow("driverId")));
            ride.setCarModel(cursor.getString(cursor.getColumnIndexOrThrow("carModel")));
            ride.setCarLicensePlate(cursor.getString(cursor.getColumnIndexOrThrow("carLicensePlate")));
            ride.setDriverRating(cursor.getFloat(cursor.getColumnIndexOrThrow("driverRating")));
            ride.setPassengerRating(cursor.getFloat(cursor.getColumnIndexOrThrow("passengerRating")));
            ride.setDriverStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("driverStartLat")));
            ride.setDriverStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("driverStartLng")));
            ride.setDriverDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("driverDestinationLat")));
            ride.setDriverDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("driverDestinationLng")));

            ride.setPassengerStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLat")));
            ride.setPassengerStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLng")));
            ride.setPassengerDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLat")));
            ride.setPassengerDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLng")));

            ride.setRideDateAndTime(cursor.getString(cursor.getColumnIndexOrThrow("rideDateAndTime")));
            ride.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            ride.setCost(cursor.getFloat(cursor.getColumnIndexOrThrow("cost")));
            cursor.close();
        }

        return ride;
    }

    public boolean addRideRequest(RequestRide requestRide){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {"id"};
        String selection = "rideId=? AND passengerId=?";
        String[] selectionArgs = {String.valueOf(requestRide.getRideId()), String.valueOf(requestRide.getPassengerId())};

        Cursor cursor = db.query(DATABASE_TABLE_REQUESTS, columns, selection, selectionArgs, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            cursor.close();
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("rideId", requestRide.getRideId());
        contentValues.put("driverId", requestRide.getDriverId());
        contentValues.put("passengerId", requestRide.getPassengerId());
        contentValues.put("status", requestRide.getStatus());
        contentValues.put("passengerStartLat", requestRide.getPassengerStartLat());
        contentValues.put("passengerStartLng", requestRide.getPassengerStartLng());
        contentValues.put("passengerDestinationLat", requestRide.getPassengerDestinationLat());
        contentValues.put("passengerDestinationLng", requestRide.getPassengerDestinationLng());


        long result = db.insert(DATABASE_TABLE_REQUESTS, null, contentValues);

        return result != -1;
    }

    public List<RequestRide> getPassengerRequests(int passengerId){
        SQLiteDatabase db = getReadableDatabase();
        List<RequestRide> requestRides = new ArrayList<>();
        String query = "SELECT * FROM " + DATABASE_TABLE_REQUESTS + " WHERE passengerId=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(passengerId)});
        if(cursor != null && cursor.moveToFirst()){
            do {
                RequestRide newRequest = new RequestRide();
                newRequest.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                newRequest.setRideId(cursor.getInt(cursor.getColumnIndexOrThrow("rideId")));
                newRequest.setDriverId(cursor.getInt(cursor.getColumnIndexOrThrow("driverId")));
                newRequest.setPassengerId(cursor.getInt(cursor.getColumnIndexOrThrow("passengerId")));
                newRequest.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                newRequest.setPassengerStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLat")));
                newRequest.setPassengerStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLng")));
                newRequest.setPassengerDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLat")));
                newRequest.setPassengerDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLng")));

                requestRides.add(newRequest);
            }while(cursor.moveToNext());
            cursor.close();
        }

        return requestRides;
    }
    public List<RequestRide> getDriverRequests(int driverId){
        SQLiteDatabase db = getReadableDatabase();
        List<RequestRide> requestRides = new ArrayList<>();
        String query = "SELECT * FROM " + DATABASE_TABLE_REQUESTS + " WHERE driverId=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(driverId)});
        if(cursor != null && cursor.moveToFirst()){
            do {
                RequestRide newRequest = new RequestRide();
                newRequest.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                newRequest.setRideId(cursor.getInt(cursor.getColumnIndexOrThrow("rideId")));
                newRequest.setDriverId(cursor.getInt(cursor.getColumnIndexOrThrow("driverId")));
                newRequest.setPassengerId(cursor.getInt(cursor.getColumnIndexOrThrow("passengerId")));
                newRequest.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                newRequest.setPassengerStartLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLat")));
                newRequest.setPassengerStartLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerStartLng")));
                newRequest.setPassengerDestinationLat(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLat")));
                newRequest.setPassengerDestinationLng(cursor.getDouble(cursor.getColumnIndexOrThrow("passengerDestinationLng")));

                requestRides.add(newRequest);
            }while(cursor.moveToNext());
            cursor.close();
        }

        return requestRides;
    }

    public boolean updateRideRequest(int requestId, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("status", status);
        String[] selectionArgs = {String.valueOf(requestId)};

        int rowsUpdated = db.update(DATABASE_TABLE_REQUESTS,contentValues, "id=?", selectionArgs);
        return rowsUpdated > 0;
    }

    public boolean alreadyRequested(int rideId, int passengerId){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {"id"};
        String selection = "rideId=? AND passengerId=?";
        String[] selectionArgs = {String.valueOf(rideId), String.valueOf(passengerId)};

        Cursor cursor = db.query(DATABASE_TABLE_REQUESTS, columns, selection, selectionArgs, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            cursor.close();
            Log.d("TUKA VRAKJA TRUE", String.valueOf(rideId) + " " + String.valueOf(passengerId));
            return true;
        }
        Log.d("TUKA VRAKJA FALSE", String.valueOf(rideId) + " " + String.valueOf(passengerId));
        return false;
    }

}
