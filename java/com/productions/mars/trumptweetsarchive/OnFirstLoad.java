package com.productions.mars.trumptweetsarchive;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Marshall on 7/21/2017.
 * Loads data from assets into internal storage upon the app being opened for the first time
 */

public class OnFirstLoad {
    MainActivity mainActivity;

    OnFirstLoad(MainActivity inAct){
        mainActivity = inAct;
        System.out.println("HERE");

    }

    void SaveData(){

        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/index.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            //String indexName = LoadFile("index.txt", false);
            BufferedInputStream in = new BufferedInputStream(mainActivity.resources.getAssets().open("index.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                outputStream.write(line.getBytes());
                outputStream.write('\n');
            }
        }
        catch (Exception ex){
            System.out.println("Error saving index" + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2018.json";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2018.json", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2018 " + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2017.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2017.txt", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2017 " + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2016.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2016.txt", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2016 " + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2015.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2015.txt", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2015 " + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2014.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2014.txt", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2014 " + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2013.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2013.txt", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2013 " + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2012.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2012.txt", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2012 " + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2011.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2011.txt", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2011 " + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2010.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2010.txt", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2010 " + ex);
        }
        try {
            String saveFile = mainActivity.getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2009.txt";
            FileOutputStream outputStream = new FileOutputStream(new File(saveFile));
            String strJSON = mainActivity.LoadFile("2009.txt", false);
            outputStream.write(strJSON.getBytes());
        }
        catch (Exception ex){
            System.out.println("Problem saving data 2009 " + ex);
        }
    }


}
