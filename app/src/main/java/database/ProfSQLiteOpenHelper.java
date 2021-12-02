package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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
    public static final String COL_PROF_POSTALCODE = "postalCode";
    public static final String COL_PROF_CITY = "city";
    public static final String COL_PROF_DEPARTMENTS = "departments";

    public static final String[] COLS_PROF = {COL_PROF_ID,
            COL_PROF_EMAIL,
            COL_PROF_FIRSTNAME,
            COL_PROF_LASTNAME,
            COL_PROF_BIRTHDAY,
            COL_PROF_STREET,
            COL_PROF_HOUSENUMBER,
            COL_PROF_POSTALCODE,
            COL_PROF_CITY,
            COL_PROF_DEPARTMENTS
    };


    public ProfSQLiteOpenHelper(Context context) {
        super(context, DATABASE, null, CURRENT_VERSION);
        // TODO Auto-generated constructor stub
        this.context = context;
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
                        + "%s TEXT NOT NULL)",
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
                COL_PROF_DEPARTMENTS);  //Fehlerpotenzial

        Log.i("CREATE", createTable);
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //kein Upgrade da Version 1

    }

    public void insertProf(Professors prof) {
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

        String departs = "";
        for (String s : prof.getDepartments()) {
            departs += "," + s;
        }
        departs = departs.replaceFirst(",", "");
        values.put(COL_PROF_DEPARTMENTS, departs);

        long _id = db.insert(TABLE_PROF, null, values);
        db.close();
    }

    public List<Professors> readAll(String name, String department) {
        SQLiteDatabase db = getReadableDatabase();
        List<Professors> profs = new Vector<>();

        Cursor c;
        if (!name.contains(" ")) {
            if (department.contains("Alle")) {
                c = db.query(TABLE_PROF, COLS_PROF, COL_PROF_FIRSTNAME + " like '%" + name + "%' or " + COL_PROF_LASTNAME + " like '%" + name + "%'", null, null, null, null);
            } else {
                c = db.query(TABLE_PROF, COLS_PROF, "(" + COL_PROF_FIRSTNAME + " like '%" + name + "%' or " + COL_PROF_LASTNAME + " like '%" + name + "%') and " + COL_PROF_DEPARTMENTS + " like '%" + department + "%'", null, null, null, null);
            }
        } else {
            if (department.contains("Alle")) {
                c = db.query(TABLE_PROF, COLS_PROF, COL_PROF_FIRSTNAME + " like '%" + name.split(" ")[0] + "%' and " + COL_PROF_LASTNAME + " like '%" + name.split(" ")[1] + "%'", null, null, null, null);
            } else {
                c = db.query(TABLE_PROF, COLS_PROF, COL_PROF_FIRSTNAME + " like '%" + name.split(" ")[0] + "%' and " + COL_PROF_LASTNAME + " like '%" + name.split(" ")[1] + "%' and " + COL_PROF_DEPARTMENTS + " like '%" + department + "%'", null, null, null, null);
            }
        }

        c.moveToFirst();
        while (!c.isAfterLast()) {
            profs.add(convertToProf(c));
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


        return new Professors(email, firstName, lastName, birth, street, housenumber, plz, city, departments, _id);
    }

    public void deleteData() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("delete from " + TABLE_PROF);
    }

}


