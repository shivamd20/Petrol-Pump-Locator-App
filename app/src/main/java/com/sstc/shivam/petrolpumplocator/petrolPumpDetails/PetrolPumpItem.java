package com.sstc.shivam.petrolpumplocator.petrolPumpDetails;

import android.support.v4.content.Loader;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by shiva on 21-03-2017.
 */

public class PetrolPumpItem  implements Serializable{

    static public String EXTRASTRING="petrol pump item";

    public String pid = "";  //
    public String pname = "";  //
    public String longitude = "";  //
    public String latitude = "";   //
    public String owner_id = "";   //
    public String contact_no = "";
    public String address = "";
    public String opening_time = "";
    public String closing_time = "";
    public String near_by_highway = "";
    public String website = "";
    public String email_id = "";
    public String varified = "";
    public String toilets = "";
    public String air = "";
    public String first_aid = "";
    public String water = "";
    public String rest_room = "";
    public String card_accepted = "";
    public String petrol = "";
    public String disel = "";
    public String description = "";
    public String visits = "";
    public String rating = "";
    public String comment = "";
    public String price_petrol = "";
    public String price_diesel = "";
    public String last_updated = "";
    public String atm = "";
    public String distance = "";
    public String duration = "";
    public String state = "";

    public PetrolPumpItem doClone()
    {
        try {
            return (PetrolPumpItem) this.clone();
        }
        catch (Exception e){
            Log.e("cloning probs",e.toString());
            return null;

        }
    }

}

