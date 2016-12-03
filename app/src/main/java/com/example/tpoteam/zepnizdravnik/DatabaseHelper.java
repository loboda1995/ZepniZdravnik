package com.example.tpoteam.zepnizdravnik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Thinkpad on 21. 11. 2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "ZepniZdravnikBaza";
    private static final int DATABASE_VERSION = 2;

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    //Called when the database connection is being configured.
    //Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // In any activity just pass the context and use the singleton method
    // DatabaseHelper helper = DatabaseHelper.getInstance(this);


    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_ZDRAVNIKI = "CREATE TABLE Zdravniki (id INTEGER PRIMARY KEY, ime TEXT, priimek TEXT)";
        String FIRST_DOCTOR = "INSERT INTO Zdravniki (id, ime, priimek) VALUES (NULL, 'Janez', 'Novak');";
        db.execSQL(CREATE_TABLE_ZDRAVNIKI);
        db.execSQL(FIRST_DOCTOR);
    }


    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + "Zdravniki");
            onCreate(db);
        }
    }

    public void addZdravnik(Zdravnik zdr) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("ime", zdr.getIme());
            values.put("priimek", zdr.getPriimek());
            db.insertOrThrow("Zdravniki", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add zdravnik to database");
        } finally {
            db.endTransaction();
        }
    }




    public List<Zdravnik> getAllZdravniki() {
        List<Zdravnik> zdravniki = new ArrayList<>();
        String ZDRAVNIKI_SELECT_QUERY = String.format("SELECT * FROM Zdravniki");
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ZDRAVNIKI_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Zdravnik zdr = new Zdravnik();
                    zdr.setIme(cursor.getString((cursor.getColumnIndex("ime"))));
                    zdr.setPriimek(cursor.getString((cursor.getColumnIndex("priimek"))));
                    zdravniki.add(zdr);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get zdravnik from zdravniki");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return zdravniki;
    }


}
