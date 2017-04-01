package com.sstc.shivam.petrolpumplocator.startScreen.database;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sstc.shivam.petrolpumplocator.MainActivity;
import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;
import com.sstc.shivam.petrolpumplocator.startScreen.ListLocationFragFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.sstc.shivam.petrolpumplocator.startScreen.database.DistanceMatrixApiResponse.formGoogleMatrixApiURL;
import static com.sstc.shivam.petrolpumplocator.startScreen.database.DistanceMatrixApiResponse.key;

public class GetAllLocation extends AsyncTask<Object, Integer, JSONObject> {


    public static final List<PetrolPumpItem> ITEMS = new ArrayList<PetrolPumpItem>();


    static String urlForFeitchingLocations="http://shivadwivedula.xyz/samavet/executeQuery.php?";
    ProgressDialog progressDialog;
   public MainActivity context;

    @Override
    protected JSONObject doInBackground(Object ...args ) {

        Location latLng=(Location) args[0];
        int start=(int)args[1];
        int end=(int)args[2];

        if(!(args.length>3))
        {
            try {

                urlForFeitchingLocations = urlForFeitchingLocations + "lat="+latLng.getLatitude()+"&long="+latLng.getLongitude()+"&start="+start+"&end="+end+";";
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlForFeitchingLocations).openConnection();
              //  urlConnection.setReadTimeout(2000);


              InputStream is= urlConnection.getInputStream();

                int c;
                StringBuilder sb=new StringBuilder();

                while(!((c=is.read())==-1))
                {
                    sb.append((char)c);
                }

                is.close();
                JSONObject jsonObject=new JSONObject(sb.toString());


                Log.i("json array",jsonObject.toString());


              ITEMS.clear();

                JSONArray jsonArray = null;
                try {

                    jsonArray = jsonObject.getJSONArray("world");
                } catch (Exception je) {
                    // Toast.makeText(getActivity(), "json exception or In", Toast.LENGTH_SHORT).show();

                    je.printStackTrace();
                }

                 String dest="";

              String  origin=latLng.getLatitude()+","+latLng.getLongitude();


                for (int i = 0; i < jsonArray.length(); i++) {

                    String lat=jsonArray.getJSONObject(i).getString("latitude");
                    String longe =jsonArray.getJSONObject(i).getString("longitude");
                    dest=dest+"|"+lat+","+longe;

                }

                String urlForFetchingLocations = formGoogleMatrixApiURL(origin,dest,key);
                urlConnection = (HttpURLConnection) new URL(urlForFetchingLocations).openConnection();

                JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
                reader.setLenient(true);
                Response r = (new Gson().fromJson(reader, Response.class));

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {

                        PetrolPumpItem pumpItem = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),
                                PetrolPumpItem.class);
                        try {
                            pumpItem.duration = r.rows[0].elements[i].duration.text;
                            pumpItem.distance = r.rows[0].elements[i].distance.text;
                            pumpItem.address = r.destination_addresses[i];
                        }
                        catch (NullPointerException ne)
                        {
                            ne.printStackTrace();
                        }
                        ITEMS.add(i, pumpItem);
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                }

                return  jsonObject;


            }catch (Exception e)
            {
                Log.e("Error JSON",e.getMessage());
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
  progressDialog=ProgressDialog.show(MainActivity.mA,"Please wait","Fetching Locations",true,false);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        ListLocationFragFragment.listLocationFragRecyclerViewAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    ListLocationFragFragment listLocationFragFragment;
    public void setlistLocationFrag(ListLocationFragFragment l)
    {
        listLocationFragFragment=l;
    }

}
