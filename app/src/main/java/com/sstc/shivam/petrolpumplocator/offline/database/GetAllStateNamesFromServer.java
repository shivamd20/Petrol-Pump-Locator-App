package com.sstc.shivam.petrolpumplocator.offline.database;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sstc.shivam.petrolpumplocator.offline.DownloadMapDilogue;
import com.sstc.shivam.petrolpumplocator.offline.OfflineFrag;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpDetails;

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

public class GetAllStateNamesFromServer extends AsyncTask<String,Integer,ArrayList<String>>{

    ProgressDialog progressDialog;
    OfflineFrag mContext;
  String  urlForFeitchingLocations="http://shivadwivedula.xyz/samavet/getAllStatesName.php";

    public GetAllStateNamesFromServer(OfflineFrag context)
    {
        super();
        mContext=context;
    }

    @Override
    protected     ArrayList<String> doInBackground(String [] ctx) {

        ArrayList list=new ArrayList();
        try {

            HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlForFeitchingLocations).openConnection();
            //  urlConnection.setReadTimeout(2000);


            InputStream is = urlConnection.getInputStream();

            int c;
            StringBuilder sb = new StringBuilder();

            while (!((c = is.read()) == -1)) {
                sb.append((char) c);
            }

            is.close();
            JSONObject jsonObject = new JSONObject(sb.toString());


            Log.i("json array", jsonObject.toString());


            JSONArray jsonArray = null;
            try {

                jsonArray = jsonObject.getJSONArray("world");
            } catch (Exception je) {
                // Toast.makeText(getActivity(), "json exception or In", Toast.LENGTH_SHORT).show();

                je.printStackTrace();
            }

            for (int i = 0; i < jsonArray.length(); i++) {

                list.add(jsonArray.getJSONObject(i).getString("state"));

            }
        }
        catch (Exception e){
            Log.e("fetching states name",e.toString());
        }

        return list;
    }

@Override
    protected void onProgressUpdate(Integer... progress) {
       // setProgressPercent(progress[0]);
    }

@Override
    protected void onPostExecute(ArrayList<String> result) {
    progressDialog.dismiss();

    DownloadMapDilogue downloadMapDilogue=DownloadMapDilogue.getInstance(result,mContext);
    downloadMapDilogue.show(mContext.getChildFragmentManager(),"Download Map");


    }
    @Override
    protected void onPreExecute() {
    progressDialog=ProgressDialog.show(mContext.getActivity(),"Getting State List..","");

    }

}
