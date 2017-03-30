package com.sstc.shivam.petrolpumplocator.offline.database;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpDetails;
import com.sstc.shivam.petrolpumplocator.startScreen.database.GetAllLocation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by shiva on 25-03-2017.
 */

public class GetAllListOfStates extends AsyncTask<Integer,Integer,ArrayList<String>>{

    String urlForFeitchingLocations="http://shivadwivedula.xyz/samavet/getAllStatesName.php";

    ArrayList<String> states=new ArrayList<String>();

    @Override
         protected ArrayList<String> doInBackground(Integer... args)
    {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlForFeitchingLocations).openConnection();

            InputStream is= urlConnection.getInputStream();

            int c;
            StringBuilder sb=new StringBuilder();

            while(!((c=is.read())==-1))
            {
                sb.append((char)c);
            }

            is.close();
            JSONObject jsonObject=new JSONObject(sb.toString());

            JSONArray jsonArray=jsonObject.getJSONArray("world");

            for (int i = 0; i < jsonArray.length(); i++) {
                states.add( jsonArray.getJSONObject(i).getString("state"));
            }
        }
        catch (Exception e)
        {
            Log.e("getting states",e.toString());
        }

        return states;
    }

}
