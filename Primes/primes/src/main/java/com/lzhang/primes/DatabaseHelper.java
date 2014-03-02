package com.lzhang.primes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Linda on 3/1/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "primes";

    public static final String TABLE_PRIMES = "primes";

    public static final String COL_ID = "_id";
    public static final String COL_COUNT = "count";
    public static final String COL_VALUE = "value";

    private static DatabaseHelper mSingleton = null;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(mSingleton == null) {
            mSingleton = new DatabaseHelper(context);
        }

        return mSingleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPrimesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PRIMES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COUNT + " INTEGER, " +
                COL_VALUE + " INTEGER);";

        db.execSQL(createPrimesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // no upgrades so far
    }

    // returns nth prime, -1 if we don't have that yet
    public int getPrimeAt(int n) {
        SQLiteDatabase db = getReadableDatabase();
        int value = -1;

        Cursor c = db.rawQuery("SELECT " + COL_VALUE + " FROM " + TABLE_PRIMES + " WHERE " + COL_COUNT + "=?", new String[] {String.valueOf(n)});
        if(c.moveToFirst()) {
            int valueCol = c.getColumnIndex(COL_VALUE);
            value = c.getInt(valueCol);
        }
        c.close();

        return value;
    }

    // returns true if we have the prime value
    public boolean havePrime(int n) {
        boolean havePrime;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_PRIMES + " WHERE " + COL_VALUE + "=?", new String[] {String.valueOf(n)});
        if(c.moveToFirst()) {
            havePrime = true;
        }
        else {
            havePrime = false;
        }

        c.close();
        return havePrime;
    }

    // returns largest prime we have
    public int getLargestPrime() {
        int largestPrime = -1;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + COL_VALUE + " FROM " + TABLE_PRIMES + " ORDER BY " + COL_VALUE + " DESC", null);
        if(c.moveToFirst()) {
            int valueCol = c.getColumnIndex(COL_VALUE);
            largestPrime = c.getInt(valueCol);
        }

        c.close();
        return largestPrime;
    }

    public void insertPrime(int index, int value) {
        SQLiteDatabase db = getWritableDatabase();

        // check if value already exists
        Cursor c = db.rawQuery("SELECT " + COL_VALUE + " FROM " + TABLE_PRIMES + " WHERE " + COL_COUNT + "=?", new String[] {String.valueOf(index)});
        if(c.moveToFirst()) {
            // value exists, do not insert
        }
        else {
            // value does not exist, insert
            ContentValues cv = new ContentValues();
            cv.put(COL_COUNT, index);
            cv.put(COL_VALUE, value);

            db.insert(TABLE_PRIMES, COL_COUNT, cv);
        }

        c.close();
    }

    public Cursor getPrimesCursor() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRIMES, null);
    }

    public void clearAllPrimes() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PRIMES, null, null);
    }

}
