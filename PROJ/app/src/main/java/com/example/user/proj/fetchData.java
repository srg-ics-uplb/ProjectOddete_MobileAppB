package com.example.user.proj;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fetchData extends AsyncTask<Void, Void, Void> {

    private Context context;
    private static TableLayout table;
    private GoogleMap mMap;
    private Spinner spinner, areaspinner, missionspinner;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private int curArea=0, curMission=0, curProp=0;
    //curProp legend= 0-All properties, 1-ph, 2-cels, 3-faht, 4-cond, 5-tds, 6-saln

    ArrayList<ArrayList<ArrayList<ArrayList<String>>>> allProperties= new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>();
    ArrayList<ArrayList<Double>> ph= new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Double>> cels= new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Double>> faht= new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Double>> cond= new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Double>> tds= new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Double>> saln= new ArrayList<ArrayList<Double>>();

    ArrayList<String> area = new ArrayList<String>();
    ArrayList<ArrayList<String>> missions = new ArrayList<ArrayList<String>>();

    public fetchData (Context context, TableLayout table, Spinner areaspinner, Spinner missionspinner){
        this.context=context;
        this.table=table;
        this.areaspinner = areaspinner;
        this.missionspinner = missionspinner;
    }

    public fetchData(Context context, GoogleMap mMap, Spinner spinner, Spinner areaspinner, Spinner missionspinner){
        this.context=context;
        this.mMap=mMap;
        this.spinner=spinner;
        this.areaspinner = areaspinner;
        this.missionspinner = missionspinner;
    }

    @Override
    protected Void doInBackground(Void... voids) {  //extracting JSON file
        //======================reading the Areas to be put in Spinner (findAreas())=======================//
        try{
            JSONArray ja=readURL("https://api.myjson.com/bins/17n9ac"); //TODO: change to proper link later
            for(int i=0; i<ja.length(); i++){
                JSONObject jo = (JSONObject) ja.get(i);
                area.add((String)jo.get("area_name"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        //=====================reading the Missions to be put in Spinner (findAllMissions())==================//
                //TODO: for real data, use for loop instead tapos nababago lang yung area_id sa link
//        for(int i=0; i<area.size(); i++){   //iterate all areas
//            try{
//                String link="enter/link/here/"+i;
//                JSONArray ja=readURL(link);
//                ArrayList<String> tempList = new ArrayList<String>();
//                for(int j=0; j<ja.length(); j++){   //iterate missions per area
//                    JSONObject jo = (JSONObject) ja.get(j);
//                    tempList.add(Integer.toString((int) jo.get("mission_id")));
//                }
//                missions.add(tempList);
//            }catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        dummyForMissions();

        //===========================reading the value of all properties (pin+table)============================//
        //TODO: change to this loop below for real data
//        for(int i=0; i<area.size(); i++){
//        ArrayList<ArrayList<String>> areas = new ArrayList<ArrayList<String>>();
//            for(int j=0; j<missions.get(i).size(); j++){
//                try{
//                    JSONArray ja=readURL("area/mission/results");
//                    ArrayList<ArrayList<String>> missions = new ArrayList<ArrayList<String>>();
//                    for(int k=0; k<ja.length(); k++) {
//                        Double temp;
//                        JSONObject jo = (JSONObject) ja.get(k);
//                        ArrayList<String> propertiesPerMission = new ArrayList<String>();
//
//                        if(jo.get("point_latitude") instanceof Integer){
//                            temp=1.0*(int)jo.get("point_latitude");
//                        }else temp=(Double)jo.get("point_latitude");
//                        propertiesPerMission.add(Double.toString(temp));
//
//                        if(jo.get("point_longitude") instanceof Integer){
//                            temp=1.0*(int)jo.get("point_longitude");
//                        }else temp=(Double)jo.get("point_longitude");
//                        propertiesPerMission.add(Double.toString(temp));
//
//                        if(jo.get("avg_ph") instanceof Integer){
//                            temp=1.0*(int)jo.get("avg_ph");
//                        }else temp=(Double)jo.get("avg_ph");
//                        propertiesPerMission.add(Double.toString(temp));
//
//                        if(jo.get("avg_cels") instanceof Integer){
//                            temp=1.0*(int)jo.get("avg_cels");
//                        }else temp=(Double)jo.get("avg_cels");
//                        propertiesPerMission.add(Double.toString(temp));
//
//                        if(jo.get("avg_faht") instanceof Integer){
//                            temp=1.0*(int)jo.get("avg_faht");
//                        }else temp=(Double)jo.get("avg_faht");
//                        propertiesPerMission.add(Double.toString(temp));
//
//                        if(jo.get("avg_cond") instanceof Integer){
//                            temp=1.0*(int)jo.get("avg_cond");
//                        }else temp=(Double)jo.get("avg_cond");
//                        propertiesPerMission.add(Double.toString(temp));
//
//                        if(jo.get("avg_tds") instanceof Integer){
//                            temp=1.0*(int)jo.get("avg_tds");
//                        }else temp=(Double)jo.get("avg_tds");
//                        propertiesPerMission.add(Double.toString(temp));
//
//                        if(jo.get("avg_saln") instanceof Integer){
//                            temp=1.0*(int)jo.get("avg_saln");
//                        }else temp=(Double)jo.get("avg_saln");
//                        propertiesPerMission.add(Double.toString(temp));
//
//                        missions.add(propertiesPerMission);
//                    }
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                areas.add(missions);
//            }
//        allProperties.add(areas);
//        }

        dummyForProperties();   //delete this

        //=============================reading the value of one property (heatMap)==============================//
        //TODO-- add other properties here!!
        if(mMap!=null){
            storeProperty("https://api.myjson.com/bins/brwi4", ph, "avg_ph");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //===========================Adding Areas and Missions to Spinner============================//
        if (this.context!=null){
            //initialize area spinner
            ArrayAdapter<String> adapter_area = new ArrayAdapter<String>(this.context,
                    android.R.layout.simple_spinner_item, area);
            adapter_area.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            areaspinner.setAdapter(adapter_area);

            areaspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String text = parentView.getItemAtPosition(position).toString();
                    curArea=areaspinner.getSelectedItemPosition();
                    ArrayAdapter<String> adapter_mission= new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, missions.get(curArea));
                    adapter_mission.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    missionspinner.setAdapter(adapter_mission);
                    spinnerChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {}
            });

            missionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String text = parentView.getItemAtPosition(position).toString();
                    curMission=missionspinner.getSelectedItemPosition();
                    spinnerChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {}
            });
        }

        //=================================ADDING THE TABLE======================================//
        if (table != null && this.context!=null && allProperties.size()!=0) {
            createTable();
        }
        //=============================ADDING PINS AND HEATMAP==================================//
        if(mMap!=null){
            if(allProperties.size()!=0) {
                Double lat=Double.parseDouble(allProperties.get(curArea).get(curMission).get(0).get(0));
                Double lng=Double.parseDouble(allProperties.get(curArea).get(curMission).get(0).get(1));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
                addPins();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String text = parentView.getItemAtPosition(position).toString();
                        if (text.equals("pH level")) {
                            curProp=1;
                        } else if (text.equals("Temperature (Celsius)")) {
                            curProp=2;
                        } else if (text.equals("Temperature (Fahrenheit)")) {
                            curProp=3;
                        } else if (text.equals("Conductivity")) {
                            curProp=4;
                        } else if (text.equals("TDS (Total Dissolved Solids)")) {
                            curProp=5;
                        } else if (text.equals("Salinity")) {
                            curProp=6;
                        } else {
                            curProp=0;
                        }
                        spinnerChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {}
                });
            }
        }
    }

    private void spinnerChanged(){  //detects if any of the spinners change and updates data displayed accordingly
        if (table != null && this.context!=null && allProperties.size()!=0) {
            createTable();
        }else{
            Double lat=Double.parseDouble(allProperties.get(curArea).get(curMission).get(0).get(0));
            Double lng=Double.parseDouble(allProperties.get(curArea).get(curMission).get(0).get(1));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
            if(curProp==0) addPins();
            else if(curProp==1) addHeatMap(ph);
            else if(curProp==2) addHeatMap(cels);
            else if(curProp==3) addHeatMap(faht);
            else if(curProp==4) addHeatMap(cond);
            else if(curProp==5) addHeatMap(tds);
            else addHeatMap(saln);
        }
    }

    private void createTable(){
        table.removeAllViews();
        ArrayList<ArrayList<String>> missionData = allProperties.get(curArea).get(curMission);
        int col=0;
        for(int i = 0; i <8; i++){
            TableRow row = new TableRow(this.context);
            TextView tvhead = new TextView(this.context);
            String head="";
            if(i==0){
                head="(Lat, Lng)";
            }else if(i==2){
                head="pH level";
            }else if(i==3){
                head="Temp in C";
            }else if(i==4){
                head="Temp in F";
            }else if(i==5){
                head="Conductivity";
            }else if(i==6){
                head="TDS";
            }else {
                head="Salinity";
            }

            if(i!=1){
                tvhead.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                tvhead.setTypeface(null, Typeface.BOLD);
                tvhead.setTextColor(Color.parseColor("#000000"));
                tvhead.setText(head);
                tvhead.setPadding(10, 10, 10, 10);
                tvhead.setTextSize(15);
                tvhead.setBackgroundColor(Color.parseColor("#50A6C2"));
                row.addView(tvhead);
            }

            for(int j=0; j<missionData.size(); j++){
                String str;
                TextView tv = new TextView(this.context);

                if(i==0){
                    str = "("+missionData.get(j).get(i)+", "+missionData.get(j).get(i+1)+")";
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setTextColor(Color.parseColor("#000000"));
                }else if(i==1){
                    continue;
                }else{
                    str = missionData.get(j).get(i);
                }

                tv.setText(str);
                row.addView(tv);

                if(i==0){
                    col = Color.parseColor("#50A6C2");
                } else if (i % 2 == 0) {
                    col = Color.parseColor("#FFFFFF");
                } else {
                    col = Color.parseColor("#D6F4FD");
                }
                tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                tv.setBackgroundColor(col);
                tv.setPadding(10, 10, 10, 10);
                tv.setTextSize(15);
            }
            this.table.addView(row);
        }
    }

    private void addHeatMap(ArrayList<ArrayList<Double>> storage) {
        mMap.clear();
//        if(storage.size()!=0){
//            ArrayList<WeightedLatLng> weighteddata = new ArrayList<WeightedLatLng>();
//
//            for(int i=0; i<storage.size(); i++){
//                LatLng latLng = new LatLng(storage.get(i).get(0), storage.get(i).get(1));
//                weighteddata.add(new WeightedLatLng(latLng, storage.get(i).get(2)));
//            }
//            mProvider = new HeatmapTileProvider.Builder()
//                    .weightedData(weighteddata)
//                    .build();
//            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
//        }
    }

    private void addPins(){
        mMap.clear();
        Double lat,lng;
        ArrayList<ArrayList<String>> thisMission = allProperties.get(curArea).get(curMission);
        for(int i=0; i<thisMission.size();i++){
            lat=Double.parseDouble(thisMission.get(i).get(0));
            lng=Double.parseDouble(thisMission.get(i).get(1));
            LatLng pin = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions()
                    .position(pin)
                    .title("("+thisMission.get(i).get(0)+", "+thisMission.get(i).get(1)+")")
                    .snippet("   pH level: "+thisMission.get(i).get(2)+"   \n"+
                            "   Temp in C: "+thisMission.get(i).get(3)+"   \n"+
                            "   Temp in F: "+thisMission.get(i).get(4)+"   \n"+
                            "   Conductivity: "+thisMission.get(i).get(5)+"   \n"+
                            "   TDS: "+thisMission.get(i).get(6)+"   \n"+
                            "   Salinity: "+thisMission.get(i).get(7)+"   "));
        }
    }

    private void storeProperty(String link, ArrayList<ArrayList<Double>> property, String toGet){
        try {
            JSONArray ja=readURL("https://api.myjson.com/bins/brwi4");
            for(int i=0; i<ja.length(); i++){
                Double temp;
                JSONObject jo = (JSONObject) ja.get(i);
                ArrayList<Double> tempList = new ArrayList<Double>();

                if(jo.get("point_latitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_latitude");
                }else temp=(Double)jo.get("point_latitude");
                tempList.add(temp);

                if(jo.get("point_longitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_longitude");
                }else temp=(Double)jo.get("point_longitude");
                tempList.add(temp);

                if(jo.get(toGet) instanceof Integer){
                    temp=1.0*(int)jo.get(toGet);
                }else temp=(Double)jo.get(toGet);
                tempList.add(temp);

                property.add(tempList);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray readURL(String link){
        JSONArray ja=null;
        String data="";
        URL url;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        BufferedReader br;

        try {
            url = new URL(link); //get the JSON file from this url
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream));

            String line="";
            while(line!=null){
                line=br.readLine();
                data+=line;
            }
            ja = new JSONArray(data);
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return ja;
    }

    private void dummyForProperties(){
        ArrayList<ArrayList<String>> forMission1 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> forMission2 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> forMission3 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> forMission4 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<ArrayList<String>>> forArea1 = new ArrayList<ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<ArrayList<String>>> forArea2 = new ArrayList<ArrayList<ArrayList<String>>>();

        try {
            JSONArray ja=readURL("https://api.myjson.com/bins/19fk3o");
            for(int i=0; i<ja.length(); i++){
                Double temp;
                JSONObject jo = (JSONObject) ja.get(i);
                ArrayList<String> perPoints = new ArrayList<String>();

                if(jo.get("point_latitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_latitude");
                }else temp=(Double)jo.get("point_latitude");
                perPoints.add(Double.toString(temp));

                if(jo.get("point_longitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_longitude");
                }else temp=(Double)jo.get("point_longitude");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_ph") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_ph");
                }else temp=(Double)jo.get("avg_ph");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_cels") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_cels");
                }else temp=(Double)jo.get("avg_cels");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_faht") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_faht");
                }else temp=(Double)jo.get("avg_faht");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_cond") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_cond");
                }else temp=(Double)jo.get("avg_cond");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_tds") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_tds");
                }else temp=(Double)jo.get("avg_tds");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_saln") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_saln");
                }else temp=(Double)jo.get("avg_saln");
                perPoints.add(Double.toString(temp));

                forMission1.add(perPoints);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        forArea1.add(forMission1);

        try {
            JSONArray ja=readURL("https://api.myjson.com/bins/r3u3o");
            for(int i=0; i<ja.length(); i++){
                Double temp;
                JSONObject jo = (JSONObject) ja.get(i);
                ArrayList<String> perPoints = new ArrayList<String>();

                if(jo.get("point_latitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_latitude");
                }else temp=(Double)jo.get("point_latitude");
                perPoints.add(Double.toString(temp));

                if(jo.get("point_longitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_longitude");
                }else temp=(Double)jo.get("point_longitude");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_ph") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_ph");
                }else temp=(Double)jo.get("avg_ph");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_cels") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_cels");
                }else temp=(Double)jo.get("avg_cels");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_faht") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_faht");
                }else temp=(Double)jo.get("avg_faht");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_cond") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_cond");
                }else temp=(Double)jo.get("avg_cond");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_tds") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_tds");
                }else temp=(Double)jo.get("avg_tds");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_saln") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_saln");
                }else temp=(Double)jo.get("avg_saln");
                perPoints.add(Double.toString(temp));

                forMission2.add(perPoints);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        forArea1.add(forMission2);
        allProperties.add(forArea1);

        try {
            JSONArray ja=readURL("https://api.myjson.com/bins/vc0hw");
            for(int i=0; i<ja.length(); i++){
                Double temp;
                JSONObject jo = (JSONObject) ja.get(i);
                ArrayList<String> perPoints = new ArrayList<String>();

                if(jo.get("point_latitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_latitude");
                }else temp=(Double)jo.get("point_latitude");
                perPoints.add(Double.toString(temp));

                if(jo.get("point_longitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_longitude");
                }else temp=(Double)jo.get("point_longitude");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_ph") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_ph");
                }else temp=(Double)jo.get("avg_ph");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_cels") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_cels");
                }else temp=(Double)jo.get("avg_cels");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_faht") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_faht");
                }else temp=(Double)jo.get("avg_faht");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_cond") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_cond");
                }else temp=(Double)jo.get("avg_cond");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_tds") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_tds");
                }else temp=(Double)jo.get("avg_tds");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_saln") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_saln");
                }else temp=(Double)jo.get("avg_saln");
                perPoints.add(Double.toString(temp));

                forMission3.add(perPoints);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        forArea2.add(forMission3);

        try {
            JSONArray ja=readURL("https://api.myjson.com/bins/i3rfo");
            for(int i=0; i<ja.length(); i++){
                Double temp;
                JSONObject jo = (JSONObject) ja.get(i);
                ArrayList<String> perPoints = new ArrayList<String>();

                if(jo.get("point_latitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_latitude");
                }else temp=(Double)jo.get("point_latitude");
                perPoints.add(Double.toString(temp));

                if(jo.get("point_longitude") instanceof Integer){
                    temp=1.0*(int)jo.get("point_longitude");
                }else temp=(Double)jo.get("point_longitude");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_ph") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_ph");
                }else temp=(Double)jo.get("avg_ph");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_cels") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_cels");
                }else temp=(Double)jo.get("avg_cels");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_faht") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_faht");
                }else temp=(Double)jo.get("avg_faht");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_cond") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_cond");
                }else temp=(Double)jo.get("avg_cond");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_tds") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_tds");
                }else temp=(Double)jo.get("avg_tds");
                perPoints.add(Double.toString(temp));

                if(jo.get("avg_saln") instanceof Integer){
                    temp=1.0*(int)jo.get("avg_saln");
                }else temp=(Double)jo.get("avg_saln");
                perPoints.add(Double.toString(temp));

                forMission4.add(perPoints);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        forArea2.add(forMission4);
        allProperties.add(forArea2);
    }

    private void dummyForMissions(){
        try{
            JSONArray ja=readURL("https://api.myjson.com/bins/1e70xw");
            ArrayList<String> tempList = new ArrayList<String>();
            for(int j=0; j<ja.length(); j++){   //iterate missions per area
                JSONObject jo = (JSONObject) ja.get(j);
                tempList.add(Integer.toString((int) jo.get("mission_id")));
            }
            missions.add(tempList);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            JSONArray ja=readURL("https://api.myjson.com/bins/1esgjo");
            ArrayList<String> tempList = new ArrayList<String>();
            for(int j=0; j<ja.length(); j++){   //iterate missions per area
                JSONObject jo = (JSONObject) ja.get(j);
                tempList.add(Integer.toString((int) jo.get("mission_id")));
            }
            missions.add(tempList);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}