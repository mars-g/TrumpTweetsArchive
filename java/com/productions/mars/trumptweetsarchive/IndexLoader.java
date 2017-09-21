package com.productions.mars.trumptweetsarchive;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Marshall on 8/6/2017.
 * THis file loads in the index from the .txt file into memory
 */

class IndexLoader extends AsyncTask<String, Integer, String>{
    private MainActivity mainActivity;

    //constructor
    IndexLoader(MainActivity activity){
        mainActivity = activity;
        doInBackground();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String openString = mainActivity.getFilesDir().getAbsolutePath() + "/index.txt";
            File openFile = new File(openString);
            FileInputStream fileStream = new FileInputStream(openFile);
            BufferedInputStream in = new BufferedInputStream(fileStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null){
                String parts[] = line.split("\t");
                //quick check to avoid erroneous lines ruining the whole dict
                if (parts.length >= 2){
                    String numbers[] = parts[1].split(" ");
                    ArrayList<String> ids = new ArrayList<>();
                    Collections.addAll(ids,numbers);
                    mainActivity.index.put(parts[0],ids);
                }
            }
        }
        catch (Exception ex){
            System.out.println(ex + "Occurred loading index data");
        }
        //load in the json object array, then add it to hashes
        try { //2017
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2017.json";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = mainActivity.convertStreamToString(inputStream);
            mainActivity.data2017 = (JSONArray) new JSONTokener(strJSON).nextValue();
            mainActivity.str2017 = strJSON;
            for (int i = 0; i < mainActivity.data2017.length(); i++){
                mainActivity.idText.put(
                        mainActivity.data2017.getJSONObject(i).get("id_str").toString(),
                        mainActivity.data2017.getJSONObject(i).get("text").toString()
                );
                mainActivity.idDate.put(
                        mainActivity.data2017.getJSONObject(i).get("id_str").toString(),
                        mainActivity.data2017.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2017 data not loaded\n" + ex);
        }
        try { //2016
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2016.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = mainActivity.convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                mainActivity.idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                mainActivity.idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2016 data not loaded\n" + ex);
        }
        try { //2015
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2015.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = mainActivity.convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                mainActivity.idText.put(
                        (data.getJSONObject(i).get("id_str").toString()),
                        (data.getJSONObject(i).get("text").toString())
                );
                mainActivity.idDate.put(
                        (data.getJSONObject(i).get("id_str").toString()),
                        (data.getJSONObject(i).get("created_at").toString())
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2015 data not loaded\n" + ex);
        }
        try { //2014
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2014.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = mainActivity.convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                mainActivity.idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                mainActivity.idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2014 data not loaded\n" + ex);
        }
        try { //2013
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2013.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = mainActivity.convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                mainActivity.idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                mainActivity.idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2013 data not loaded\n" + ex);
        }
        try { //2012
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2012.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = mainActivity.convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                mainActivity.idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                mainActivity.idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2012 data not loaded\n" + ex);
        }
        try { //2011
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2011.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = mainActivity.convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                mainActivity.idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                mainActivity.idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2011 data not loaded\n" + ex);
        }
        try { //2010
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2010.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = mainActivity.convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                mainActivity.idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                mainActivity.idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2010 data not loaded\n" + ex);
        }
        try { //2009
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2009.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = mainActivity.convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                mainActivity.idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                mainActivity.idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2009 data not loaded\n" + ex);
        }

        //check to see if we can update tweets right now
        if (mainActivity.dataLoader != null && mainActivity.dataLoader.resultString != null && !mainActivity.beenUpdated){
            mainActivity.dataUpdate = new DataUpdate(mainActivity);
            mainActivity.dataUpdate.doInBackground(mainActivity.dataLoader.resultString, mainActivity.str2017);

        }
        return null;
    }
}
