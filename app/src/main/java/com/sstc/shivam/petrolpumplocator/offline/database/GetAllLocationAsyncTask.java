package com.sstc.shivam.petrolpumplocator.offline.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sstc.shivam.petrolpumplocator.offline.ListLocationFragFragmentOffline;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;


public class GetAllLocationAsyncTask extends AsyncTask<Object, Integer, Boolean> {

    ProgressDialog progressDialog;
    Context mContext;
    Location latLng;

    public GetAllLocationAsyncTask(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    protected Boolean doInBackground(Object... args) {

        latLng = (Location) args[0];
        int start = (int) args[1];
        int end = (int) args[2];

        GetDataFromSQLite gd = new GetDataFromSQLite(mContext);

        Cursor cur = gd.getLocations(latLng,start,end);

        while (cur.moveToNext()) {

            PetrolPumpItem item = rowToObject(cur);

            item.distance=GetDataFromSQLite.CaloculateDistance.distance(latLng.getLatitude(),
                    latLng.getLongitude(),Double.parseDouble(item.latitude),Double.parseDouble(item.longitude),'K')+"";

            GetDataFromSQLite.ITEMS.add(item);
        }
        return true;
    }

  public static  PetrolPumpItem rowToObject(Cursor cur) {
        PetrolPumpItem item = new PetrolPumpItem();

        item.address = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.ADDRESS));

        item.air = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.AIR));

        item.atm = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.ATM));

        item.card_accepted = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.CARD_ACCEPTED));

        item.closing_time = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.CLOSING_TIME));

        item.comment = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.COMMENT));

        item.contact_no = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.CONTACT_NO));

        item.description = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.DESCRIPTION));

        item.disel = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.DISEL));

        item.email_id = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.EMAIL_ID));

        item.first_aid = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.FIRST_AID));

        item.last_updated = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.LAST_UPDATED));

        item.latitude = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.LATITUDE));

        item.longitude = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.LONGITUDE));

        item.near_by_highway = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.NEAR_BY_HIGHWAY));

        item.opening_time = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.OPENING_TIME));

        item.owner_id = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.OWNER_ID));

        item.petrol = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.PETROL));

        item.pid = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.PID));

        item.pname = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.PNAME));

        item.price_diesel = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.PRICE_DIESEL));

        item.price_petrol = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.PRICE_PETROL));

        item.rating = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.RATING));

        item.rest_room = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.REST_ROOM));

        item.toilets = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.TOILETS));

        item.varified = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.VARIFIED));

        item.visits = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.VISITS));

        item.water = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.WATER));

        item.website = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.WEBSITE));

        item.state = cur.getString(cur.getColumnIndex(Contract.PetrolPumpDetail.STATE));

        return item;
    }


    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(mContext, "Please wait", "Fetching Locations from local database", true, false);
    }

    @Override
    protected void onPostExecute(Boolean result) {

        for(PetrolPumpItem X:GetDataFromSQLite.ITEMS)
        {
            Log.i("lat long db",""+X.latitude+X.longitude+X.pname);
        }

        ListLocationFragFragmentOffline.listLocationFragRecyclerOfflineViewAdapter.notifyDataSetChanged();
        progressDialog.dismiss();

        Toast.makeText(mContext, "" + GetDataFromSQLite.ITEMS.size(), Toast.LENGTH_SHORT).show();
    }
}
