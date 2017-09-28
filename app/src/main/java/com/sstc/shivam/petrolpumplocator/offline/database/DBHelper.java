package com.sstc.shivam.petrolpumplocator.offline.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sstc.shivam.petrolpumplocator.offline.database.Contract.PetrolPumpDetail;

/**
 * Created by shiva on 21-03-2017.
 */


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "OFFLINE_LOCATIONS.db";

    final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + PetrolPumpDetail.TABLE_NAME + " (" +

          //  "  " + PetrolPumpDetail._ID + "  INTEGER AUTOINCREMENT ," +
            "  " + PetrolPumpDetail.PID + " varchar(45) NOT NULL ," +
            "  " + PetrolPumpDetail.PNAME + " varchar(45) NOT NULL," +
            "  " + PetrolPumpDetail.LONGITUDE + " double DEFAULT NULL," +
            "  " + PetrolPumpDetail.LATITUDE + " double DEFAULT NULL," +
            "  " + PetrolPumpDetail.OWNER_ID + " varchar(45) DEFAULT NULL," +
            "  " + PetrolPumpDetail.CONTACT_NO + " bigint(15) DEFAULT NULL," +
            "  " + PetrolPumpDetail.ADDRESS + " varchar(70) NOT NULL," +
            "  " + PetrolPumpDetail.OPENING_TIME + " time DEFAULT NULL," +
            "  " + PetrolPumpDetail.CLOSING_TIME + " time DEFAULT NULL," +
            "  " + PetrolPumpDetail.NEAR_BY_HIGHWAY + " tinyint(1) DEFAULT '0'," +
            "  " + PetrolPumpDetail.WEBSITE + " varchar(45) DEFAULT NULL," +
            "  " + PetrolPumpDetail.EMAIL_ID + " varchar(45) DEFAULT NULL," +
            "  " + PetrolPumpDetail.VARIFIED + " tinyint(1) DEFAULT '0'," +
            "  " + PetrolPumpDetail.TOILETS + " smallint(6) DEFAULT NULL," +
            "  " + PetrolPumpDetail.AIR + " tinyint(1) DEFAULT NULL," +
            "  " + PetrolPumpDetail.FIRST_AID + " tinyint(1) DEFAULT NULL," +
            "  " + PetrolPumpDetail.WATER + " tinyint(1) DEFAULT NULL," +
            "  " + PetrolPumpDetail.REST_ROOM + " tinyint(1) DEFAULT NULL," +
            "  " + PetrolPumpDetail.CARD_ACCEPTED + " tinyint(1) DEFAULT NULL," +
            "  " + PetrolPumpDetail.PETROL + " tinyint(1) DEFAULT NULL," +
            "  " + PetrolPumpDetail.DISEL + " tinyint(1) DEFAULT NULL," +
            "  " + PetrolPumpDetail.DESCRIPTION + " mediumtext," +
            "  " + PetrolPumpDetail.VISITS + " int(11) DEFAULT NULL," +
            "  " + PetrolPumpDetail.RATING + " int(11) DEFAULT NULL," +
            "  " + PetrolPumpDetail.COMMENT + " mediumtext," +
            "  " + PetrolPumpDetail.PRICE_PETROL + " int(11) DEFAULT NULL," +
            "  " + PetrolPumpDetail.PRICE_DIESEL + " int(11) DEFAULT NULL," +
            "  " + PetrolPumpDetail.LAST_UPDATED + " datetime DEFAULT NULL," +
            "  " + PetrolPumpDetail.ATM + " varchar(45) ," +
            "  " + PetrolPumpDetail.STATE + " varchar(45) ," +
            "  PRIMARY KEY (" + PetrolPumpDetail.PID + "))";


    final String SQL_CREATE_USER_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + Contract.USER_HISTORY.TABLE_NAME + " (" +

            "  " + Contract.USER_HISTORY.PID + " TEXT NOT NULL ," +
            "  " + Contract.USER_HISTORY.PNAME + " TEXT NOT NULL," +
            "  " + Contract.USER_HISTORY.ADDRESS+ " TEXT DEFAULT 'not available'," +
            "  " + Contract.USER_HISTORY.TIME+ " DATETIME DEFAULT CURRENT_TIMESTAMP ," +
            "  PRIMARY KEY (" + Contract.USER_HISTORY.TIME + "))";

    final String SQL_CREATE_FTS_ENTRIES =
            "CREATE VIRTUAL TABLE "
                    +Contract.PetrolPumpDetail_FTS.TABLE_NAME +" USING fts4 " +
            "(content='"+PetrolPumpDetail.TABLE_NAME+
                    "', "
                    +Contract.PetrolPumpDetail_FTS.CONTENT+");";


    final String UPDATE_FTS="INSERT OR IGNORE INTO "+Contract.PetrolPumpDetail_FTS.TABLE_NAME
            +" ( "+Contract.PetrolPumpDetail_FTS.CONTENT+") " +
            "SELECT "+PetrolPumpDetail.PNAME+" FROM "+PetrolPumpDetail.TABLE_NAME+";";

    final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Contract.PetrolPumpDetail.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_FTS_ENTRIES);
        db.execSQL(SQL_CREATE_USER_HISTORY_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {

       SQLiteDatabase db= super.getWritableDatabase();
        db.execSQL(UPDATE_FTS);
        return db;
    }
}
