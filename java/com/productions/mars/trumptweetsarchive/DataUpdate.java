package com.productions.mars.trumptweetsarchive;

import android.os.AsyncTask;

import org.json.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
/**
 * Created by Marshall on 7/15/2017.
 * This file updates the data with new tweets from 2018 and adds them to index and database
 */

class DataUpdate extends AsyncTask<String, Integer, String>{
    private MainActivity mainActivity;

    //constructor
    DataUpdate(MainActivity activity){
        mainActivity = activity;
        doInBackground(mainActivity.dataLoader.resultString, mainActivity.str2018);
    }

    @Override
    protected String doInBackground(String... params) {
        //convert both string to json objects to do comparison
        JSONArray newData;
        try {
           //load in new data into a json array
            newData = (JSONArray) new JSONTokener(params[0]).nextValue();
            //this loops isolates all the new data
            for (int i = newData.length() - mainActivity.data2018.length() - 1; i >= 0 ; i--){
                //add tweet text and date info to hashmap
                mainActivity.idText.put(
                        newData.getJSONObject(i).get("id_str").toString(),
                        newData.getJSONObject(i).get("text").toString()
                );
                mainActivity.idDate.put(
                        newData.getJSONObject(i).get("id_str").toString(),
                        newData.getJSONObject(i).get("created_at").toString()
                );
                // go through each word in the string
                HashSet<String> usedWords = new HashSet<>();
                for (String word:newData.getJSONObject(i).get("text").toString().split(" ")){
                    word = word.toLowerCase();
                    word = word.replaceAll("[^A-Za-z0-9 ]", "");
                    if (usedWords.contains(word)){
                        continue;
                    }//word is a repeat, should not be added to index
                    if (!mainActivity.index.containsKey(word)){
                        ArrayList<String> tempList = new ArrayList<>();
                        tempList.add(newData.getJSONObject(i).get("id_str").toString());
                        mainActivity.index.put(word,tempList);
                    }//word is new, gets added to index as new line
                    else {
                        ArrayList<String> tempList = mainActivity.index.get(word);
                        tempList.add(0,newData.getJSONObject(i).get("id_str").toString());
                        mainActivity.index.put(word,tempList);
                    }//is alreayd in index, tweet id is added
                    usedWords.add(word);
                }
            }
            mainActivity.beenUpdated = true;
            mainActivity.str2018 = params[0];

            //save this updated data
            NewDataSave newDataSave = new NewDataSave(mainActivity);
            newDataSave.doInBackground();
        }
        catch (Exception ex) {
            System.out.println("Error while loading in new index");
        }
        return null;
    }
}
