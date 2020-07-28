package com.example.voting_rights_rys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.String.join;

/** This class gets the JSON through the ElectionQuery and VoterInfoQuery <br>
<<<<<<< HEAD
and then parses it. **/
class GetFromAPI{
    static String address;
    static String API_KEY = "AIzaSyCWU07pSAk1CfApBN5K0T2Axzb0R5Kok-E";
    public GetFromAPI(String address){
        this.address = address;
    }
    /** Gets the JSON of elections **/
    String getElectionJSON() throws InterruptedException {
        final String[] result = new String[1];
        final String API_URL = "https://www.googleapis.com/civicinfo/v2/elections?key=";

        Thread electionThread = new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL(API_URL + API_KEY);
                    HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
=======
 and then parses it. **/
class GetFromAPI {
        static String address;
        static String API_KEY = "AIzaSyCWU07pSAk1CfApBN5K0T2Axzb0R5Kok-E";
        public GetFromAPI(String address){
                this.address = address;
        }
        /** Gets the JSON of elections **/
        String getElectionJSON() throws InterruptedException {
                final String[] result = new String[1];
                final String API_URL = "https://www.googleapis.com/civicinfo/v2/elections?key=";

                Thread electionThread = new Thread(new Runnable() {
                        public void run() {
                                try {
                                        URL url = new URL(API_URL + API_KEY);
                                        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                                        try {
                                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                                                StringBuilder stringBuilder = new StringBuilder();
                                                String line;
                                                while ((line = bufferedReader.readLine()) != null) {
                                                        stringBuilder.append(line).append("\n");
                                                }
                                                bufferedReader.close();
                                                result[0] = stringBuilder.toString();
                                        }
                                        finally{
                                                urlConnection.disconnect();
                                        }
                                }
                                catch(Exception e) {
                                        Log.e("ERROR", e.getMessage(), e);
                                        result[0] = null;
                                }
>>>>>>> 49d38f8f070c2bbe2edc45abc38de11a5e46633f
                        }
                });
                electionThread.start();
                electionThread.join();
                return result[0];
        }

        /** Gets the JSON from VoterInfoQuery for a particular election. **/
        String getVoterInfoJSON(Election election) throws InterruptedException {
                final String[] result = new String[1];
                final String API_URL =  "https://civicinfo.googleapis.com/civicinfo/v2/voterinfo?address=";
                final String electionID = election.getID();
                //System.out.println("the election id is " + electionID);

                Thread voterThread = new Thread(new Runnable() {
                        public void run() {
                                try {
                                        URL url = new URL(API_URL + address + "&electionID=" + electionID  + "&returnAllAvailableData=true" + "&key=" + API_KEY);
                                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                                        System.out.println("connecting...");
                                        try {
                                                System.out.println("in buffered reader...");
                                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                                                StringBuilder stringBuilder = new StringBuilder();
                                                String line;
                                                while ((line = bufferedReader.readLine()) != null) {
                                                        stringBuilder.append(line).append("\n");
                                                }
                                                bufferedReader.close();
                                                result[0] = stringBuilder.toString();
                                        }
                                        finally{
                                                System.out.println("disconnecting...");
                                                urlConnection.disconnect();
                                        }
                                }
                                catch(Exception e) {
                                        Log.e("ERROR", e.getMessage(), e);
                                        System.out.println("ran into an error...");
                                        result[0] = null;
                                }
                        }
                });
                voterThread.start();
                voterThread.join();
                return result[0];
        }

        /** Parses the elction JSON, creates Election objects for each election and <br>
         * returns the last election object.
         * Precondition: response is the JSON string of elections. userState is the <br>
         * lowercase abbrevation of the state name.
         *  Ex. "ny" for New York
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        Election parseElectionJSON(String response, String userState) {
                String id;
                String name;
                String date;
                String division;
                try {
                        JSONObject resp = new JSONObject(response);
                        JSONArray elections = resp.getJSONArray("elections");
                        Election x = null;
                        for (int i = 0; i < elections.length(); i++) {
                                JSONObject elec = elections.getJSONObject(i);
                                id = elec.getString("id");
                                if (!id.equals("2000")) {
                                        name = elec.getString("name");
                                        date = elec.getString("electionDay");
                                        division = elec.getString("ocdDivisionId");
                                        x = new Election(name, date, id, division, userState);
                                }
                        }
                        return x;
                }
                catch (JSONException e) {
                        Log.e("ERROR", e.getMessage(), e);
                        return null;
                }
        }

        String parseVoterJSON(String response, Election election){
                if (response.contains("earlyVotingSites")){
                        election.isEarlyVotingAllowed = true;
                }
                if (response.contains("pollingLocations")) {
                        System.out.println("inside polling if");
                        JSONObject resp;
                        try {
                                resp = new JSONObject(response);

                                JSONArray locations = resp.getJSONArray("pollingLocations");
                                JSONObject loc = (JSONObject) locations.get(0);
                                String address = "";
                                if (response.contains("locationName")){
                                        address += loc.getString("locationName") + "\n";
                                }
                                address += loc.getString("line1");
                                if (response.contains("line2")) {
                                        address += loc.getString("line2")+ "\n";
                                        if (response.contains("line3")){
                                                address += loc.getString("line3")+ "\n";
                                        }
                                }
                                address += loc.getString("city") + ",";
                                address += loc.getString("state");
                                address += loc.getString("zip");
                                //System.out.println(address);
                                return address;
                        }
                        catch (JSONException e) {
                                e.printStackTrace();

                        }
                }

                return "Your polling place is unavailable, check back later.";}
}

/** Elections class -- an election object has fields name, date, id, level <br>
 isEarlyVotingAllowed, going, and myElection **/
class Election {
        static LinkedList<Election> all_elections = new LinkedList<>();
        static LinkedList<Election> my_elections = new LinkedList<>();

        String name;
        String date;
        String id;
        String level;
        boolean isEarlyVotingAllowed = false;
        boolean going = false;
        boolean myElection = false;

        /** Returns the date in the following format: Month Day, Year. <br>
         Example: "July 2, 2020" or "November 8, 2021" <br>
         Precondition: date is the format YYYY-MM-DD. **/
        public String formattedDate(String date){
                String year = date.substring(0,4);
                String month = date.substring(5, 7);
                String day = date.substring(8,10);
                return month + " " + day + ", " + year;
        }
  
        /** Finds the level of the election. If the election is federal or in the same state as <br>
         userState, then myElection is set to true.
         Precondition: userState is the lowercase abbreviation of the state name <br>
         division is in the form of the following examples:
         "ocd-division/country:us" or "ocd-division/country:us/state:ny"**/
        public String findLevelandState(String division, String userState){
                if (division.contains("/state:")){
                        level = "State";
                        if(division.contains(userState)){
                                myElection = true;
                        }
                }
                else {
                        level = "Federal";
                        myElection = true; }

                return level;
        }

        /** Creates an election object with name n, date electionDay, id electionID, <br>
         and level indicated by division.<br>
         Federal elections and state elections occuring the same state as userState <br>
         are added to my_elections. All elections are added to all_elections. <br>
         Preconditions: electionDay is the date of the election in the format <br>
         YYYY-MM-DD; state is the lowercase abbreviation of the state (Ex: "ny" for <br>
         New York or "la" for Louisiana) **/
        public Election(String n, String electionDay, String electionID, String division, String userState){
                this.name = n;
                this.date = formattedDate(electionDay);
                this.id = electionID;
                this.level = findLevelandState(division, userState);
                all_elections.add(this);
                if (this.level.equals("Federal")){
                        my_elections.add(this);
                }
                else{
                        if (this.myElection){
                                my_elections.add(this);
                        }
                }
        }

        /* Getters and Setters */
        public LinkedList<Election> getAllElections() {return all_elections;}
        public LinkedList<Election> getMyElections() {return my_elections; }
        public String getName() {return this.name;}
        public String getDate() {return this.date;}
        public String getID() {return this.id;}
        public String getLevel(){return this.level;}
        public boolean getEarlyVoting() {return this.isEarlyVotingAllowed; }
        public boolean getGoing() {return going;}
        public void setGoing(boolean goingAns) {this.going = goingAns;}
        public void setIsEarlyVotingAllowed(boolean earlyVoting) {this.isEarlyVotingAllowed = earlyVoting ;}
        public boolean isMyElection() {return myElection; }

}

@RequiresApi(api = Build.VERSION_CODES.O)
public class Elections extends AppCompatActivity {
        public static final String pollingLocation = "com.example.voting_rights_rys.POLLINGPLACE";
        static String userState;
        static String userAddress;
        static LinkedList allElections;
        static LinkedList myElections;
        //static LinearLayout allElectionsLayout = new LinearLayout();
        //static LinearLayout myElectionsLayout = new LinearLayout();
        static ArrayList<String> allElectionDisplay;
        static ArrayList<String> myElectionDisplay;
        public static String pollingPlace ="";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_elections);

                //Get the Intent which started this activity (address string from Settings)
                Intent intent = getIntent();
                //Using the key to get the bundle (address)
                userAddress = intent.getStringExtra(Settings.EXTRA_ADDRESS);
                /**TEST THAT ADDRESS IS RECEIVED FROM SETTINGS*/
                /*TextView textView = findViewById(R.id.addressTest);
                textView.setText(userAddress); */

                int comma_index = 0;

                if (userAddress != null) {
                        comma_index = userAddress.indexOf("%2C");
                        userState = userAddress.substring(comma_index +6, comma_index + 8).toLowerCase();
                }

                //Initialize bottom nav bar and select "Elections"
                BottomNavigationView bottomNavView = findViewById(R.id.bot_nav);
                bottomNavView.setSelectedItemId(R.id.elections);

                //Start a new activity when a nav bar item is selected
                bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                        case R.id.home:
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                overridePendingTransition(0, 0);
                                                return true;
                                        case R.id.elections:
                                                return true;
                                        case R.id.resources:
                                                startActivity(new Intent(getApplicationContext(), Resources.class));
                                                overridePendingTransition(0, 0);
                                                return true;
                                        case R.id.settings:
                                                startActivity(new Intent(getApplicationContext(), Settings.class));
                                                overridePendingTransition(0, 0);
                                                return true;
                                }
                                return false;
                        }
                });

                try {
                        getData();
                        //sendpollingplace();

                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }

        public void getData () throws InterruptedException {

                if (userAddress != null) {
                        GetFromAPI get = new GetFromAPI(userAddress);
                        String electionJSON = get.getElectionJSON();
                        Election e = get.parseElectionJSON(electionJSON, userState);
                        LinkedList<TextView> all_election_views = new LinkedList<>();
                        allElections = e.getAllElections();
                        myElections = e.getMyElections();
                        int ae_size = allElections.size();
                        int mye_size = myElections.size();

                        Election node; /// = e.getAllElections().head();
                        String viewText = "";
                        String myViewText = "";
                        //int my_elec_index = 0;
                        for (int i = 0; i < ae_size; i++) {
                                node = e.getAllElections().get(i);
                                String voterJSON = get.getVoterInfoJSON(node);
                                pollingPlace = get.parseVoterJSON(voterJSON, node);
                                //System.out.println("This is the polling place  " + pollingPlace);
                                //System.out.println(node.getID());
                                String this_election = "Name: " + node.getName() + "\n" + "Date: " + node.getDate() + "\n";
                                viewText += this_election;
                                //System.out.println(viewText);
                                //myElectionDisplay.add(viewText);

                                if (node.isMyElection()) {
                                        myViewText += this_election;
                                        //myElectionsLayout.addView(rowTextView);
                                        //allElectionDisplay.add(viewText);
                                        //rowTextView.setVisibility(view.VISIBLE);
                                }
                        }
                        /**LinearLayout list = (LinearLayout) findViewById(R.id.allElections);
                         for (int j = 0; j < allElectionDisplay.size(); j ++) {
                         TextView wordView = new TextView(this);
                         wordView.setText(allElectionDisplay.get(j));
                         list.addView(wordView);
                         } **/

                        TextView electionstext =(TextView) findViewById(R.id.allElectionsTV);
                        electionstext.setText(viewText);
                        //System.out.println(viewText);
                        TextView mytext = (TextView) findViewById(R.id.myElectionsTV);
                        mytext.setText(myViewText);
                }
        }





        /**
         * Called when the user taps the "all elections" button
         **/
        public void displayAllElections(View view) {
                //System.out.println("all elections was clicked");
                //System.out.println(allElections.size());
                TextView error = (TextView) findViewById(R.id.errormsg);
                findViewById(R.id.errormsg).setVisibility(view.INVISIBLE);
                findViewById(R.id.myElectionsTV).setVisibility(view.INVISIBLE);
                try {
                        if (allElections.size() != 0) {
                                findViewById(R.id.allElectionsTV).setVisibility(view.VISIBLE);
                        }
                } catch (NullPointerException e) {
                        error.setText("There are no elections." + "\n" + "Check your address or try again later.");
                        findViewById(R.id.errormsg).setVisibility(view.VISIBLE);
                }


                /**for (int i = 0; i < myElections.size(); i++){
                 myElectionDisplay.get(i).setVisibility(view.INVISIBLE);
                 }
                 for (int j = 0; j < allElections.size(); j ++){
                 allElectionDisplay.get(j).setVisibility(view.VISIBLE);
                 } **/
        }
        /** Called when the user taps the "My elections" button **/
        public void displayMyElections(View view) {
                //System.out.println("my elections was clicked");
                //System.out.println(myElections.size());
                findViewById(R.id.allElectionsTV).setVisibility(view.INVISIBLE);
                TextView error =(TextView) findViewById(R.id.errormsg);
                findViewById(R.id.errormsg).setVisibility(view.INVISIBLE);
                try {
                        if(myElections.size() != 0) {
                                findViewById(R.id.myElectionsTV).setVisibility(view.VISIBLE);
                        }
                }
                catch (NullPointerException e) {
                        error.setText("There are no elections." + "\n" +  "Check your address or try again later.");
                        findViewById(R.id.errormsg).setVisibility(view.VISIBLE);
                }


                /**for (int i = 0; i < allElections.size(); i++){
                 allTextViews[i].setVisibility(view.INVISIBLE);
                 }
                 for (int j = 0; j < myElections.size(); j ++){
                 myTextViews[j].setVisibility(view.VISIBLE);
                 }**/
        }
        public void sendpollingplace(View view){
                System.out.println("inside send polling place function");
                if (pollingPlace != null) {
                        System.out.println(pollingPlace);
                        //Create bind between Settings and Elections activities
                        Intent intent = new Intent(this, MainActivity.class);
                        //Add value of EditText to intent
                        intent.putExtra(pollingLocation, pollingPlace);
                        //Starts the Elections activity and sends intent
                        startActivity(intent);
                }
        }
}
