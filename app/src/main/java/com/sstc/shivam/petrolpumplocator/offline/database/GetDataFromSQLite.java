package com.sstc.shivam.petrolpumplocator.offline.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shiva on 23-03-2017.
 */


public class GetDataFromSQLite {

    public static final List<PetrolPumpItem> ITEMS = new ArrayList<PetrolPumpItem>();


    Context mContext;

    public GetDataFromSQLite(Context context) {
        this.mContext = context;
    }


    public Cursor getLocationsWithFilter(Location l,int star,int en,String where,String wherevalues[] ) {

// Filter results WHERE "title" = My Title

        DBHelper mDbHelper = new DBHelper(mContext);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String start = star+"", end = en+"";

        double lat = l.getLatitude();
        double longe = l.getLongitude();

        String query = "SELECT *, ( (" + lat + " - " + Contract.PetrolPumpDetail.LATITUDE + ")" +
                "*(" + lat + " - " + Contract.PetrolPumpDetail.LATITUDE + ") + " +
                "(" + longe + " - " + Contract.PetrolPumpDetail.LONGITUDE + ")*" +
                "(" + longe + " - " + Contract.PetrolPumpDetail.LONGITUDE + ") ) " +
                "as dist FROM " + Contract.PetrolPumpDetail.TABLE_NAME + " WHERE "+
                Contract.PetrolPumpDetail.LONGITUDE +" IS NOT NULL "+" ORDER BY dist LIMIT " + start + "," + end + ";";

        Cursor cursor =

        db.query(Contract.PetrolPumpDetail.TABLE_NAME,
               new String[] {"*","( (" + lat + " - " + Contract.PetrolPumpDetail.LATITUDE + ")" +
                       "*(" + lat + " - " + Contract.PetrolPumpDetail.LATITUDE + ") + " +
                       "(" + longe + " - " + Contract.PetrolPumpDetail.LONGITUDE + ")*" +
                       "(" + longe + " - " + Contract.PetrolPumpDetail.LONGITUDE + ") ) " +
                       "as dist "},
               where,
                wherevalues,
                null,
                null,
                "dist",start+","+end);



        /*query(boolean distinct,
                                     String table,
                                     String[] columns,
                                     String selection,
                                     String[] selectionArgs,
                                     String groupBy,
                                     String having,
                                     String orderBy,
                                     String limit)*/
        return cursor;
    }

    public Cursor getLocations(Location l,int star,int en) {


// Filter results WHERE "title" = My Title

        DBHelper mDbHelper = new DBHelper(mContext);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String start = star+"", end = en+"";

        double lat = l.getLatitude();
        double longe = l.getLongitude();

        String query = "SELECT *, ( (" + lat + " - " + Contract.PetrolPumpDetail.LATITUDE + ")" +
                "*(" + lat + " - " + Contract.PetrolPumpDetail.LATITUDE + ") + " +
                "(" + longe + " - " + Contract.PetrolPumpDetail.LONGITUDE + ")*" +
                "(" + longe + " - " + Contract.PetrolPumpDetail.LONGITUDE + ") ) " +
                "as dist FROM " + Contract.PetrolPumpDetail.TABLE_NAME + " WHERE "+
                Contract.PetrolPumpDetail.LONGITUDE +" IS NOT NULL "+" ORDER BY dist LIMIT " + start + "," + end + ";";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public boolean deleteOfflineMap()
    {
        DBHelper mDbHelper = new DBHelper(mContext);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        db.delete(Contract.PetrolPumpDetail.TABLE_NAME,null,null);

        return true;
    }

    public ArrayList<String> getAllStateList()
    {
        DBHelper mDbHelper = new DBHelper(mContext);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ArrayList<String> list=new ArrayList<String >();

        String start = "0", end = "10";

        double lat = 21;
        double longe = 80;

        String query = "SELECT DISTINCT "+Contract.PetrolPumpDetail.STATE+" From "+Contract.PetrolPumpDetail.TABLE_NAME+" ORDER BY +"+
                Contract.PetrolPumpDetail.STATE+";";

        Cursor cursor = db.rawQuery(query, null);

        Log.e("null cursor length",""+cursor.getCount());

        while(cursor.moveToNext())
        {
            String str=cursor.getString(cursor.getColumnIndex(Contract.PetrolPumpDetail.STATE));

            if(str!=null) {
                list.add(str);
            }
            else
            {
                Log.e("nulll cursor value","");
            }
        }
        return list;
    }



    public Boolean deleteStateFromSQlite(ArrayList<String> state)
    {
        DBHelper mDbHelper = new DBHelper(mContext);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        for(Object  X: state) Log.i("deleteselected",""+X.toString());

        for(String  X: state) {
            db.delete(Contract.PetrolPumpDetail.TABLE_NAME, "" + Contract.PetrolPumpDetail.STATE + " ==?", new String[]{X});
        }

        return true;
    }

  synchronized public Cursor searchInSqlliteDatabase(String searchString)
    {
//        String sql = "SELECT *,"+Contract.PetrolPumpDetail.PID+" AS _id"
//                +" FROM "+Contract.PetrolPumpDetail.TABLE_NAME+" WHERE "+Contract.PetrolPumpDetail.PID+" IN " +
//                "(SELECT  "
//                +Contract.PetrolPumpDetail_FTS.DOCID+" FROM  "
//                +Contract.PetrolPumpDetail_FTS.TABLE_NAME+" WHERE   "+Contract.PetrolPumpDetail_FTS.TABLE_NAME+" MATCH ?)";

        String sql="SELECT *,"+Contract.PetrolPumpDetail.PID+" AS _id "+" FROM "+Contract.PetrolPumpDetail.TABLE_NAME+" " +
                "where "+Contract.PetrolPumpDetail.PNAME+" like '%"+searchString+"%' " +
                " or "+Contract.PetrolPumpDetail.ADDRESS+" like '%"+searchString+"%' or "
                +Contract.PetrolPumpDetail.STATE+" like '%"+searchString+"%'  or "
                +Contract.PetrolPumpDetail.WEBSITE+" like '%"+searchString+"%'" ;
        String[] selectionArgs = { searchString };
        Cursor cur=new DBHelper(mContext).getWritableDatabase().rawQuery(sql,null);
        return cur;
    }

    public static class CaloculateDistance {
        public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (unit == 'K') {
                dist = dist * 1.609344;
            } else if (unit == 'N') {
                dist = dist * 0.8684;
            }
            return (dist);
        }

        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      static   private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      static   private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }
    }

}





