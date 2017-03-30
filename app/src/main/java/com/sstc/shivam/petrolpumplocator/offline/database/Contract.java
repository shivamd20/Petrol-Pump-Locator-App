package com.sstc.shivam.petrolpumplocator.offline.database;

import android.provider.BaseColumns;

/**
 * Created by shiva on 21-03-2017.
 */

public class Contract {

    public static class PetrolPumpDetail implements BaseColumns {

        public static final String TABLE_NAME = "petrol_pump_details";
        public static final String PID = "pid";
        public static final String PNAME = "pname";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String OWNER_ID = "owner_id";
        public static final String CONTACT_NO = "CONTACT_NO";
        public static final String ADDRESS = "address";
        public static final String OPENING_TIME = "opening_time";
        public static final String CLOSING_TIME = "closing_time";
        public static final String NEAR_BY_HIGHWAY = "near_by_highway";
        public static final String WEBSITE = "website";
        public static final String EMAIL_ID = "email_id";
        public static final String VARIFIED = "varified";
        public static final String TOILETS = "toilets";
        public static final String AIR = "air";
        public static final String FIRST_AID = "first_aid";
        public static final String WATER = "water";
        public static final String REST_ROOM = "rest_room";
        public static final String CARD_ACCEPTED = "card_accepted";
        public static final String PETROL = "petrol";
        public static final String DISEL = "disel";
        public static final String DESCRIPTION = "description";
        public static final String VISITS = "visits";
        public static final String RATING = "rating";
        public static final String COMMENT = "comment";
        public static final String PRICE_PETROL = "price_petrol";
        public static final String PRICE_DIESEL = "price_diesel";
        public static final String LAST_UPDATED = "last_updated";
        public static final String ATM = "atm";
        public static final String STATE = "state";
    }

    public static class PetrolPumpDetail_FTS implements BaseColumns{

        public static final String TABLE_NAME = "petrol_pump_details_fts";
        public static final String CONTENT = "content";

        public static final String DOCID = "docid";

    }
}
