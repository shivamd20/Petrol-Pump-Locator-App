package com.sstc.shivam.petrolpumplocator.offline.database;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpDetails;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by shiva on 21-03-2017.
 */

public class OfflineLocationDownloadTask extends AsyncTask<Object, String, PetrolPumpDetails> {

 public  static  OfflineLocationDownloadTask getInstance(Fragment frag,String state)
    {
        OfflineLocationDownloadTask of=new OfflineLocationDownloadTask();

        of.context=frag;
        if(state!=null)
        of.state=state;
        else
            of.state="Downloading All States Data";
        return of;
    }


    static String urlForFeitchingLocations = "http://shivadwivedula.xyz/samavet/GETALLDATA.php";
     Fragment context;
    String state;


    @Override
    protected PetrolPumpDetails doInBackground(Object... params) {

        if(state.compareTo("Downloading All States Data")!=0)
        {
            new GetDataFromSQLite(context.getActivity()).deleteOfflineMap();
            state=state.replace(' ','+');
          urlForFeitchingLocations = "http://shivadwivedula.xyz/samavet/StateDetails.php?state="+state;
        }

        PetrolPumpDetails r = null;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlForFeitchingLocations).openConnection();

            JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
            reader.setLenient(true);


            r = (new Gson().fromJson(reader, PetrolPumpDetails.class));

            Log.e("OBJECT", r.world[0].pname);

        } catch (Exception e) {
            Log.e("JSON ERROR", e.toString());
            e.printStackTrace();
        }
        return r;
    }

    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=ProgressDialog.show(context.getActivity(),"Downloading Maps",state);
    }

    boolean putIntoDataBase(PetrolPumpItem[] details) {
        DBHelper mDbHelper = new DBHelper(context.getActivity());

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {


            for (int i = 0; i < details.length; i++) {
                ContentValues values = new ContentValues();

                values.put(Contract.PetrolPumpDetail.PID, details[i].pid);
                values.put(Contract.PetrolPumpDetail.PNAME, details[i].pname);
                values.put(Contract.PetrolPumpDetail.ADDRESS, details[i].address);

                values.put(Contract.PetrolPumpDetail.AIR, details[i].air);
                values.put(Contract.PetrolPumpDetail.ATM, details[i].atm);
                values.put(Contract.PetrolPumpDetail.CARD_ACCEPTED, details[i].card_accepted);

                values.put(Contract.PetrolPumpDetail.CLOSING_TIME, details[i].closing_time);
                values.put(Contract.PetrolPumpDetail.COMMENT, details[i].comment);
                values.put(Contract.PetrolPumpDetail.CONTACT_NO, details[i].contact_no);

                values.put(Contract.PetrolPumpDetail.DESCRIPTION, details[i].description);
                values.put(Contract.PetrolPumpDetail.DISEL, details[i].disel);
                values.put(Contract.PetrolPumpDetail.EMAIL_ID, details[i].email_id);

                values.put(Contract.PetrolPumpDetail.FIRST_AID, details[i].first_aid);
                values.put(Contract.PetrolPumpDetail.LAST_UPDATED, details[i].last_updated);
                values.put(Contract.PetrolPumpDetail.LATITUDE, details[i].latitude);

                values.put(Contract.PetrolPumpDetail.LONGITUDE, details[i].longitude);
                values.put(Contract.PetrolPumpDetail.NEAR_BY_HIGHWAY, details[i].near_by_highway);
                values.put(Contract.PetrolPumpDetail.OPENING_TIME, details[i].opening_time);


                values.put(Contract.PetrolPumpDetail.OWNER_ID, details[i].owner_id);
                values.put(Contract.PetrolPumpDetail.PETROL, details[i].petrol);
                values.put(Contract.PetrolPumpDetail.PRICE_PETROL, details[i].price_petrol);


                values.put(Contract.PetrolPumpDetail.PRICE_DIESEL, details[i].price_diesel);
                values.put(Contract.PetrolPumpDetail.RATING, details[i].rating);
                values.put(Contract.PetrolPumpDetail.REST_ROOM, details[i].rest_room);


                values.put(Contract.PetrolPumpDetail.TOILETS, details[i].toilets);
                values.put(Contract.PetrolPumpDetail.VARIFIED, details[i].varified);
                values.put(Contract.PetrolPumpDetail.VISITS, details[i].visits);


                values.put(Contract.PetrolPumpDetail.WATER, details[i].water);
                values.put(Contract.PetrolPumpDetail.WEBSITE, details[i].website);
                values.put(Contract.PetrolPumpDetail.STATE, details[i].state);

                db.insert(Contract.PetrolPumpDetail.TABLE_NAME, null, values);


            }

        } catch (SQLException sqE) {
            Log.e("sqllite error ", sqE.toString());
        }

        return false;
    }

    @Override
    protected void onPostExecute(PetrolPumpDetails s) {
       // super.onPostExecute(s);


        if( s!=null)
        {
            for (int i = 0; i < s.world.length; i++) {
                Log.e("result", s.world[i].latitude + "");
            }

            new GetDataFromSQLite(context.getActivity()).deleteOfflineMap();

            putIntoDataBase(s.world);
        }
        else
        {
          new AlertDialog.Builder(context.getActivity()).setMessage("There might be some problem in your network connectivity").setTitle("ERROR OCCURRED").show();
        }

        progressDialog.dismiss();
    }
}
