package com.productions.mars.trumptweetsarchive;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Marshall on 7/23/2017.
 * Saves 2017 data and index
 */

public class NewDataSave extends AsyncTask<String, Integer, String> {

    MainActivity mainActivity;

    NewDataSave(MainActivity inAct){
        mainActivity = inAct;
    }
    @Override
    protected String doInBackground(String... params) {

        //save 2017.json data
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            System.out.println(mainActivity.getFilesDir().getAbsolutePath());
            saveFile = saveFile + "/2017.json";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            outputStream.write(mainActivity.str2017.getBytes());
        }
        catch (Exception ex) {
            System.out.println("Failed to save 2017 data" + ex );
        }
        //save index
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/index.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            for (HashMap.Entry<String,ArrayList<String>> list: mainActivity.index.entrySet()){
                String word = list.getKey() + '\t';
                outputStream.write(word.getBytes());
                for (String id: list.getValue()){
                    id = id + " ";
                    outputStream.write(id.getBytes());
                }
                outputStream.write('\n');
            }
        }
        catch (Exception ex) {
            System.out.println("Failed to save index" + ex );
        }



        return null;
    }
}
