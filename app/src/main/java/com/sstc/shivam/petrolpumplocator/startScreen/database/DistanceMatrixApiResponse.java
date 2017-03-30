package com.sstc.shivam.petrolpumplocator.startScreen.database;

/**
 * Created by shiva on 18-03-2017.
 */


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


class Response {

    public String status;
    public String[] destination_addresses;
    public String[] origin_addresses;
    public Item[] rows;

    class Element {
        public Duration duration;
        public Distance distance;
        public String status;
    }

    class Item {
        public Element[] elements;
    }
}

class Distance {
    public String text;
    public String value;

}

class Duration {
    public String text;
    public String value;
}

class Elements {
    public Duration duration[];
    public Distance distance[];
    public String status;
}

public class DistanceMatrixApiResponse {

    static String urlForFeitchingLocations = "http://shivadwivedula.xyz/samavet/executeQuery.php?";
    static String key = "AIzaSyBfwOdZBbRtecHhdA8eWo1vYNNiPjZtRA8";
    static String origin = "21,81";
    static String dest = "21,85";

    public static String formGoogleMatrixApiURL(String origin, String dest, String key) {
        return "https://maps.googleapis.com/maps/api/distancematrix/json?units=km&origins=" +
                origin + "&destinations=" + dest + "&key=" + key;
    }


    public static void main(String[] args) throws Exception {

        String urlForFeitchingLocations = formGoogleMatrixApiURL(origin, dest, key);
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlForFeitchingLocations).openConnection();

        JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
        reader.setLenient(true);
        Response r = (new Gson().fromJson(reader, Response.class));

        System.out.println(r.destination_addresses[0]);

        System.out.println(r.origin_addresses[0]);


    }
}