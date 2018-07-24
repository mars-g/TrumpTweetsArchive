package com.productions.mars.trumptweetsarchive;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.*;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


import org.json.*;

//import static com.productions.mars.trumptweetsarchive.R.id.parent;

public class MainActivity extends AppCompatActivity {
    public HTMLSearch dataLoader;
    public DataUpdate dataUpdate;
    public JSONArray data2018;
    //used for spinner selections
    public Integer startChoice;
    public Integer endChoice;

    Boolean beenUpdated;

    public String str2018;

    //map of index
    public HashMap<String,ArrayList<String>> index;
    //map of ids to text,data
    public HashMap<String,String> idText;
    public HashMap<String,String> idDate;

    Dialog yearDialog;
    Resources resources;
    SharedPreferences sharedpreferences;

    //used to store results from previous search
    ArrayList<String> results;
    Integer resultsCounter;

    //IndexLoader indexLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load shared preferences
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        resources = getResources();
        //check if this is the first load
        if (!sharedpreferences.getBoolean("firstLoad",false)){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("firstLoad",true);
            editor.apply();
            OnFirstLoad onFirstLoad = new OnFirstLoad(this);
            onFirstLoad.SaveData();
        }
        else {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("firstLoad",false);
            editor.apply();
        }
        //System.out.println(getFilesDir().getAbsolutePath());
        setContentView(R.layout.activity_main);
        resources = getResources();
        //set button listener
        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = editText.getText().toString();
                    searchTweets(searchText.toLowerCase());
                    editText.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    handled = true;
                }
                return handled;
            }
        });




        //Set initial values
        startChoice = 2009;
        endChoice = 2018;
        beenUpdated = false;
        index = new HashMap<>();
        idText = new HashMap<>();
        idDate = new HashMap<>();
        resultsCounter = 0;
        results = new ArrayList<>();

        //load saved 2017 data from file
        dataLoader = new HTMLSearch();

        try {
            dataLoader.execute("http://www.trumptwitterarchive.com/data/realdonaldtrump/2018.json");
        } catch (Exception ex) {
            System.out.println("Failed to load 2018 data, use backup version");
        }
        //loads index, id text and dates
        //indexLoader = new IndexLoader(this);
        loadIndex();
        loadTweets();

        //setting year string
        String yearString = startChoice + " - " + endChoice;
        TextView yearText = (TextView) findViewById(R.id.textView2);
        yearText.setText(yearString);
        if (dataLoader != null && dataLoader.resultString != null && !beenUpdated){
            dataUpdate = new DataUpdate(this);
            //dataUpdate.doInBackground(dataLoader.resultString, str2017);

        }
    }
    //Onclick function handles button click
    //Does the following:
        //Checks if it should update data
            //If yes, then it saves data and calls async task to store data
        //Initiates search
    public void onClick(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        searchTweets(editText.getText().toString().toLowerCase());
        editText.clearFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    //controls the spinner selection for the year
    public void yearClick(View view){
        yearDialog = new Dialog(this);
        yearDialog.setContentView(R.layout.year_dialog);
        yearDialog.setTitle("Choose the years you want to search");
        //set spinner listener
        final Spinner startSpinner = (Spinner) yearDialog.findViewById(R.id.startSpinner);
        ArrayAdapter<CharSequence> startAdapter = ArrayAdapter.createFromResource(
                this, R.array.year_choices2, R.layout.spinnerlayout);
        startAdapter.setDropDownViewResource(R.layout.spinnerlayout);
        startSpinner.setAdapter(startAdapter);
        startSpinner.setSelection(startChoice - 2009);
        startSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                startChoice = 2009 + pos;
                //setting year string
                String yearString = startChoice + " - " + endChoice;
                TextView yearText = (TextView) findViewById(R.id.textView2);
                yearText.setText(yearString);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        final Spinner endSpinner = (Spinner) yearDialog.findViewById(R.id.endSpinner);
        ArrayAdapter<CharSequence> endAdapter = ArrayAdapter.createFromResource(
                this, R.array.year_choices, R.layout.spinnerlayout);
        endAdapter.setDropDownViewResource(R.layout.spinnerlayout);
        endSpinner.setAdapter(endAdapter);
        endSpinner.setSelection(2018 - endChoice);
        endSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                endChoice = 2018 - pos;
                //setting year string
                String yearString = startChoice + " - " + endChoice;
                TextView yearText = (TextView) findViewById(R.id.textView2);
                yearText.setText(yearString);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        yearDialog.show();
    }
    //does the searching for the app
    public void searchTweets(CharSequence text) {

        if (text.equals("")){
            return;

        }

        resultsCounter = 0;
        results.clear();

        String textString = text.toString();
        textString = textString.toLowerCase();

        textString = textString.replaceAll("[^A-Za-z0-9 ]", "");
        String parts[] = textString.split(" ");
        //get linear layout and remove previous content
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.results);
        linearLayout.removeAllViews();

        //only one word so can use abridged version
        if (parts.length == 1){
            ArrayList<String> idList = index.get(textString);
            System.out.println(idList);
            TextView numberOfTweets = (TextView) findViewById(R.id.textView);
            linearLayout.removeAllViews();
            int counter = 0;
            if (idList == null){
                String numberString = "No results found";
                numberOfTweets.setText(numberString);
                return;
            }
            for (int i = 0; i < idList.size(); i++){
                if (idText.get(idList.get(i)) == null || getYear(idDate.get(idList.get(i))) < startChoice || getYear(idDate.get(idList.get(i))) > endChoice){
                    continue;
                }
                counter++;


                String tweetString = idText.get(idList.get(i)) + " - created at: " +
                        idDate.get(idList.get(i));
                //linearLayout.addView(detail);
                results.add(tweetString);
            }
            //call display results
            displayResults();
            String numberString = "Results found: " + counter;
            numberOfTweets.setTextSize(14);
            numberOfTweets.setText(numberString);
        }
        //else clause indicates multiple words
        else {
            TextView numberOfTweets = (TextView) findViewById(R.id.textView);
            linearLayout.removeAllViews();
            HashMap<String,Integer> ids = new HashMap<>();
            for (String word:parts){
                ArrayList<String> tempList = index.get(word);
                if (tempList == null){
                    String numberString = "No results found";
                    numberOfTweets.setText(numberString);
                    return;
                }
                for (int i = 0; i < tempList.size(); i++){
                    if (ids.containsKey(tempList.get(i))){
                        ids.put(tempList.get(i),ids.get(tempList.get(i)) + 1);
                    }//already in hashmap
                    else {
                        ids.put(tempList.get(i),1);
                    }
                }
            }
            Integer counter = 0;
            for (HashMap.Entry<String,Integer> entry : ids.entrySet()){
                String key = entry.getKey();
                Integer value = entry.getValue();
                Boolean goOn = true;
                if (value == parts.length && idText.get(key) != null && getYear(idDate.get(key)) >= startChoice && getYear(idDate.get(key)) <= endChoice){
                    for (int j = 0; j < parts.length; j++){
                        if (!idText.get(key).toLowerCase().replaceAll("[^A-Za-z0-9 ]", "").contains(parts[j])){
                            goOn = false;
                        }
                    }
                    if (!goOn){
                        continue;
                    }
                    counter++;
                    View line = new View(this);
                    line.setBackgroundColor(0xFF0000FF);
                    linearLayout.addView(line, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 2));
                    TextView detail = new TextView(this);
                    detail.setTextSize(18);
                    String tweetString = idText.get(key) + " - created at: " +
                            idDate.get(key);
                    detail.setText(tweetString);
                    linearLayout.addView(detail);
                }//good one!
            }
            numberOfTweets.setTextSize(16);
            String numberString = "Results found: " + counter;
            numberOfTweets.setText(numberString);
        }
    }

    //displays results
    public void displayResults(){
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.results);
        int i = 0;
        for (i = resultsCounter; i < results.size() && i < resultsCounter+100; i++){
            TextView detail = new TextView(this);
            detail.setTextSize(18);
            detail.setText(results.get(i));
            linearLayout.addView(detail);
            View line = new View(this);
            line.setBackgroundColor(0xFF0000FF);
            linearLayout.addView(line, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 2));
        }
        //add button
        if (i != results.size()){
            resultsCounter+= 100;
            final Button loadMoreButton  = new Button(this);
            loadMoreButton.setText("Load More");
            loadMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayout.removeView(loadMoreButton);
                    displayResults();
                }
            });
            linearLayout.addView(loadMoreButton);
        }
    }


    //loads in index and the json ids and text lengths into the appropriate maps
    public void loadIndex(){
        try {
            //String indexName = LoadFile("index.txt", false);
            String openString = getFilesDir().getAbsolutePath() + "/index.txt";
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
                    index.put(parts[0],ids);
                }
            }
        }
        catch (Exception ex){
            System.out.println(ex + "Occurred loading index data");
        }
    }
    //loads in all the ids to text and date
    public void loadTweets(){
        //load in the json object array, then add it to hashes
        try { //2018
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2018.json";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            data2018 = (JSONArray) new JSONTokener(strJSON).nextValue();
            System.out.println("Length = " + data2018.length());
            str2018 = strJSON;
            for (int i = 0; i < data2018.length(); i++){
                if (data2018.getJSONObject(i).has("text")) {
                    idText.put(
                            data2018.getJSONObject(i).get("id_str").toString(),
                            data2018.getJSONObject(i).get("text").toString()
                    );
                }
                else {
                    idText.put(
                            data2018.getJSONObject(i).get("id_str").toString(),
                            data2018.getJSONObject(i).get("full_text").toString()
                    );
                }
                idDate.put(
                        data2018.getJSONObject(i).get("id_str").toString(),
                        data2018.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2018 data not loaded\n" + ex);
        }
        try { //2017
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2017.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                if (data.getJSONObject(i).has("full_text")) {
                    idText.put(
                            data.getJSONObject(i).get("id_str").toString(),
                            data.getJSONObject(i).get("full_text").toString()
                    );
                }
                else {
                    idText.put(
                            data.getJSONObject(i).get("id_str").toString(),
                            data.getJSONObject(i).get("text").toString()
                    );
                }
                idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2017 data not loaded\n" + ex);
        }
        try { //2016
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2016.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2016 data not loaded\n" + ex);
        }
        try { //2015
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2015.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                idText.put(
                        (data.getJSONObject(i).get("id_str").toString()),
                        (data.getJSONObject(i).get("text").toString())
                );
                idDate.put(
                        (data.getJSONObject(i).get("id_str").toString()),
                        (data.getJSONObject(i).get("created_at").toString())
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2015 data not loaded\n" + ex);
        }
        try { //2014
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2014.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2014 data not loaded\n" + ex);
        }
        try { //2013
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2013.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2013 data not loaded\n" + ex);
        }
        try { //2012
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2012.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2012 data not loaded\n" + ex);
        }
        try { //2011
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2011.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2011 data not loaded\n" + ex);
        }
        try { //2010
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2010.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2010 data not loaded\n" + ex);
        }
        try { //2009
            String saveFile = getFilesDir().getAbsolutePath();
            saveFile = saveFile + "/2009.txt";
            FileInputStream inputStream = new FileInputStream(new File(saveFile));
            String strJSON = convertStreamToString(inputStream);
            JSONArray data = (JSONArray) new JSONTokener(strJSON).nextValue();
            for (int i = 0; i < data.length(); i++){
                idText.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("text").toString()
                );
                idDate.put(
                        data.getJSONObject(i).get("id_str").toString(),
                        data.getJSONObject(i).get("created_at").toString()
                );

            }
        }
        catch (Exception ex) {
            System.out.println("2009 data not loaded\n" + ex);
        }
    }


    //thank you to Dmitri for the code for this function http://www.41post.com/3985/programming/android-loading-files-from-the-assets-and-raw-folders
    //load file from apps res/raw folder or Assets folder
    public String LoadFile(String fileName, boolean loadFromRawFolder) throws IOException
    {
        //Create a InputStream to read the file into
        InputStream iS;

        if (loadFromRawFolder)
        {
            //get the resource id from the file name
            int rID = resources.getIdentifier("com.mars.productions:raw/"+fileName, null, null);
            //get the file as a stream
            iS = resources.openRawResource(rID);
        }
        else
        {
            //get the file as a stream
            iS = resources.getAssets().open(fileName);
        }

        //create a buffer that has the same size as the InputStream
        byte[] buffer = new byte[iS.available()];
        //read the text file as a stream, into the buffer
        iS.read(buffer);
        //create a output stream to write the buffer into
        ByteArrayOutputStream oS = new ByteArrayOutputStream();
        //write this buffer to the output stream
        oS.write(buffer);
        //Close the Input and Output streams
        oS.close();
        iS.close();

        //return the output stream as a String
        return oS.toString();
    }

    public Integer getYear(String yearString){
        if (yearString.length() <= 3){
            return null;
        }
        String substr = yearString.substring(yearString.length() - 4);
        return Integer.parseInt(substr);
    }

    //Thak you to Pavel Repin for this function https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}


