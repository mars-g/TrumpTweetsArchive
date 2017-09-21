package com.productions.mars.trumptweetsarchive;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;
/**
 * Created by Marshall on 6/11/2017.
 * This file is used to load in the JSON files from the internet at the beginning of app startup
 * Thank you to stackoverflow user Kalpak for code in lines 26-37 : https://stackoverflow.com/questions/1485708/how-do-i-do-a-http-get-in-java
 * Thank you to trumptwitterarchive.com for the data used in this section
 */

class HTMLSearch extends AsyncTask<String,Integer, String> {
    String resultString;
    private JSONArray newData2017;
    @Override
    protected String doInBackground(String... params) {
        try {
            StringBuilder result = new StringBuilder();
            for (String urlToRead: params) {
                URL url = new URL(urlToRead);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
            }
            resultString = result.toString();
            try {
                newData2017 = (JSONArray) new JSONTokener(resultString).nextValue();
            }
            catch (Exception ex) {
                System.out.println("Error in loading new 2017 data " + ex);
            }
            return resultString;
        }
        catch (IOException ex){
            return null;
        }
    }


}