package database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import model.Account;
import model.DepartmentManager;
import model.Professors;

public class ProfSQLiteOpenHelper extends SQLiteOpenHelper{
    Context context;
    public static final String DATABASE = "Professors.sqlite";
    public static final int CURRENT_VERSION = 1;

    public static final String TABLE_PROF = "prof";
    public static final String COL_PROF_ID = "_id";
    public static final String COL_PROF_EMAIL = "email";
    public static final String COL_PROF_FIRSTNAME = "firstName";
    public static final String COL_PROF_LASTNAME = "lastName";
    public static final String COL_PROF_BIRTHDAY = "birthday";
    public static final String COL_PROF_STREET = "street";
    public static final String COL_PROF_HOUSENUMBER = "housenumber";
    public static final String COL_PROF_MOBILENUMBER = "number";
    public static final String COL_PROF_POSTALCODE = "postalCode";
    public static final String COL_PROF_CITY = "city";
    public static final String COL_PROF_DEPARTMENTS = "departments";
    public static final String COL_PROF_LATI = "lati";
    public static final String COL_PROF_LONGI = "longi";
    public static final String COL_PROF_PRICE = "price";
    public static final String COL_PROF_IMAGE = "image";

    private DepartmentManager depManager;

    private double longi;
    private double lati;

    public static final String[] COLS_PROF = {COL_PROF_ID,
            COL_PROF_EMAIL,
            COL_PROF_FIRSTNAME,
            COL_PROF_LASTNAME,
            COL_PROF_BIRTHDAY,
            COL_PROF_STREET,
            COL_PROF_HOUSENUMBER,
            COL_PROF_POSTALCODE,
            COL_PROF_CITY,
            COL_PROF_DEPARTMENTS,
            COL_PROF_MOBILENUMBER,
            COL_PROF_LATI,
            COL_PROF_LONGI,
            COL_PROF_PRICE,
            COL_PROF_IMAGE
    };


    public ProfSQLiteOpenHelper(Context context) {
        super(context, DATABASE, null, CURRENT_VERSION);
        // TODO Auto-generated constructor stub
        this.context = context;
        depManager = DepartmentManager.getInstance();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = String.format("CREATE TABLE %s ("
                        + "%s TEXT PRIMARY KEY, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s TEXT NOT NULL, "
                        + "%s INTEGER, "
                        + "%s BLOB)",
                TABLE_PROF,
                COL_PROF_ID,
                COL_PROF_EMAIL,
                COL_PROF_FIRSTNAME,
                COL_PROF_LASTNAME,
                COL_PROF_BIRTHDAY,
                COL_PROF_STREET,
                COL_PROF_HOUSENUMBER,
                COL_PROF_POSTALCODE,
                COL_PROF_CITY,
                COL_PROF_DEPARTMENTS,
                COL_PROF_MOBILENUMBER,
                COL_PROF_LATI,
                COL_PROF_LONGI,
                COL_PROF_PRICE,
                COL_PROF_IMAGE);

        Log.i("CREATE", createTable);
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //kein Upgrade da Version 1

    }

    public void insertProf(Professors prof, byte[] bytes) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_PROF_ID, prof.getid());
        values.put(COL_PROF_EMAIL, prof.getEmail());
        values.put(COL_PROF_FIRSTNAME, prof.getFirstName());
        values.put(COL_PROF_LASTNAME, prof.getLastName());
        values.put(COL_PROF_BIRTHDAY, prof.getBirthday());
        values.put(COL_PROF_STREET, prof.getStreet());
        values.put(COL_PROF_HOUSENUMBER, prof.getHouseNumber());
        values.put(COL_PROF_POSTALCODE, prof.getPlz());
        values.put(COL_PROF_CITY, prof.getCity());
        values.put(COL_PROF_MOBILENUMBER, prof.getMobileNumber());
        values.put(COL_PROF_LATI, prof.getLati());
        values.put(COL_PROF_LONGI, prof.getLongi());
        values.put(COL_PROF_PRICE, prof.getPrice());
        values.put(COL_PROF_IMAGE, bytes);

        String departs = "";
        for (String s : prof.getDepartments()) {
            departs += "," + s;
        }
        departs = departs.replaceFirst(",", "");
        values.put(COL_PROF_DEPARTMENTS, departs);

        long _id = db.insert(TABLE_PROF, null, values);
        db.close();
    }

    public List<Professors> readAll() {
        SQLiteDatabase db = getReadableDatabase();
        List<Professors> profs = new Vector<>();

        Cursor c = db.query(TABLE_PROF, COLS_PROF, null, null, null, null, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            profs.add(convertToProf(c));
            c.moveToNext();
        }
        db.close();

        return profs;
    }

    public List<Professors> readAll(String name, String department, String radius) {
        SQLiteDatabase db = getReadableDatabase();
        List<Professors> profs = new Vector<>();
        getGeolocation();

        double rad = 0;
        switch (radius) {
            case "Unbegrent": rad = 0; break;
            case "1km": rad = 1.0; break;
            case "4km": rad = 4.0; break;
            case "8km": rad = 8.0; break;
            case "10km": rad = 10.0; break;
            case "20km": rad = 20.0; break;
            case "40km": rad = 40.0; break;
            case "80km": rad = 80.0; break;
            case "100km": rad = 100.0; break;

        }

        Cursor c;
        if (!name.contains(" ")) {
            if (department.contains("Alle")) {
                c = db.query(TABLE_PROF, COLS_PROF, COL_PROF_FIRSTNAME + " like '%" + name + "%' or " + COL_PROF_LASTNAME + " like '%" + name + "%'", null, null, null, null);
            } else {
                department = depManager.getDepByName(department).getId();
                c = db.query(TABLE_PROF, COLS_PROF, "(" + COL_PROF_FIRSTNAME + " like '%" + name + "%' or " + COL_PROF_LASTNAME + " like '%" + name + "%') and " + COL_PROF_DEPARTMENTS + " like '%" + department + "%'", null, null, null, null);
            }
        } else {
            if (department.contains("Alle")) {
                c = db.query(TABLE_PROF, COLS_PROF, COL_PROF_FIRSTNAME + " like '%" + name.split(" ")[0] + "%' and " + COL_PROF_LASTNAME + " like '%" + name.split(" ")[1] + "%'", null, null, null, null);
            } else {
                department = depManager.getDepByName(department).getId();
                c = db.query(TABLE_PROF, COLS_PROF, COL_PROF_FIRSTNAME + " like '%" + name.split(" ")[0] + "%' and " + COL_PROF_LASTNAME + " like '%" + name.split(" ")[1] + "%' and " + COL_PROF_DEPARTMENTS + " like '%" + department + "%'", null, null, null, null);
            }
        }

        c.moveToFirst();
        while (!c.isAfterLast()) {
            Professors prof = convertToProf(c);

            if (rad != 0) {
                if (distFrom(lati, longi, Double.parseDouble(prof.getLati()), Double.parseDouble(prof.getLongi())) <= rad) {
                    profs.add(prof);
                }
            } else {
                profs.add(prof);
            }
            c.moveToNext();
        }
        db.close();

        return profs;
    }

    private Professors convertToProf(Cursor c) {
        String _id = c.getString(c.getColumnIndexOrThrow(COL_PROF_ID));
        String email = c.getString(c.getColumnIndexOrThrow(COL_PROF_EMAIL));
        String firstName = c.getString(c.getColumnIndexOrThrow(COL_PROF_FIRSTNAME));
        String lastName = c.getString(c.getColumnIndexOrThrow(COL_PROF_LASTNAME));
        String birth = c.getString(c.getColumnIndexOrThrow(COL_PROF_BIRTHDAY));
        String street = c.getString(c.getColumnIndexOrThrow(COL_PROF_STREET));
        String housenumber = c.getString(c.getColumnIndexOrThrow(COL_PROF_HOUSENUMBER));
        String plz = c.getString(c.getColumnIndexOrThrow(COL_PROF_POSTALCODE));
        String city = c.getString(c.getColumnIndexOrThrow(COL_PROF_CITY));
        List<String> departments = Arrays.asList(c.getString(c.getColumnIndexOrThrow(COL_PROF_DEPARTMENTS)).split(","));
        String number = c.getString(c.getColumnIndexOrThrow(COL_PROF_MOBILENUMBER));
        String lati = c.getString(c.getColumnIndexOrThrow(COL_PROF_LATI));
        String longi = c.getString(c.getColumnIndexOrThrow(COL_PROF_LONGI));
        int price = c.getInt(c.getColumnIndexOrThrow(COL_PROF_PRICE));

        Bitmap image = byteArrayToBitmap(c.getBlob(c.getColumnIndexOrThrow(COL_PROF_IMAGE)));

        return new Professors(email, firstName, lastName, birth, street, housenumber, plz, city, departments, _id, number, lati, longi, price, image);
    }

    private Bitmap byteArrayToBitmap(byte[] bArray) {
        return BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
    }

    private void getGeolocation() {
        Account ac = Account.getInstance();
        Geocoder coder = new Geocoder(context, Locale.GERMANY);

        String ad = ac.getStreet() + " " + ac.getHouseNumber() + " " + ac.getPlz() + " " + ac.getCity();
        try {
            Address address = coder.getFromLocationName(ad, 1).get(0);
            longi = address.getLongitude();
            lati = address.getLatitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (float) (earthRadius * c);

        return Math.abs(dist) / 1000;
    }

    public void deleteData() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("delete from " + TABLE_PROF);
    }
}


