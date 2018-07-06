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
    private TableLayout table;
    private GoogleMap mMap;
    private Spinner spinner, areaspinner, missionspinner;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private int curArea=0, curMission=0, curProp=0, numOfMissions=0;
    //curProp legend= 0-All properties, 1-ph, 2-cels, 3-faht, 4-cond, 5-tds, 6-saln

    private ArrayList<ArrayList<ArrayList<String>>> allProperties= new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<ArrayList<ArrayList<Double>>> ph= new ArrayList<ArrayList<ArrayList<Double>>>();
    private ArrayList<ArrayList<ArrayList<Double>>> cels= new ArrayList<ArrayList<ArrayList<Double>>>();
    private ArrayList<ArrayList<ArrayList<Double>>> faht= new ArrayList<ArrayList<ArrayList<Double>>>();
    private ArrayList<ArrayList<ArrayList<Double>>> cond= new ArrayList<ArrayList<ArrayList<Double>>>();
    private ArrayList<ArrayList<ArrayList<Double>>> tds= new ArrayList<ArrayList<ArrayList<Double>>>();
    private ArrayList<ArrayList<ArrayList<Double>>> saln= new ArrayList<ArrayList<ArrayList<Double>>>();

    private ArrayList<String> area = new ArrayList<String>();
    private ArrayList<ArrayList<String>> missions = new ArrayList<ArrayList<String>>();

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
            //JSONArray ja=readURL("http://10.0.3.57:6200/areas"); //TODO: change to proper link later
            JSONArray ja=readURL("https://api.myjson.com/bins/8nqbe");
            for(int i=0; i<ja.length(); i++){
                JSONObject jo = (JSONObject) ja.get(i);
                area.add((String)jo.get("area_name"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        //=====================reading the Missions to be put in Spinner (findAllMissions())==================//
        for(int i=0; i<area.size(); i++){   //iterate all areas
            try{
                String link="http://10.0.3.57:6200/areas/"+(i+1)+"/missions";
                JSONArray ja=readURL(link);
                ArrayList<String> tempList = new ArrayList<String>();
                for(int j=0; j<ja.length(); j++){   //iterate missions per area
                    JSONObject jo = (JSONObject) ja.get(j);
                    tempList.add(Integer.toString((int) jo.get("mission_id")));
                    numOfMissions++;
                }
                missions.add(tempList);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //===========================reading the value of all properties (findMission())============================//
        for(int i=0; i<numOfMissions; i++){
            try{
                String link="http://10.0.3.57:6200/missions/"+(i+1)+"/results";
                JSONArray ja=readURL(link);
                ArrayList<ArrayList<String>> perMission = new ArrayList<ArrayList<String>>();
                for(int k=0; k<ja.length(); k++) {
                    Double temp;
                    JSONObject jo = (JSONObject) ja.get(k);
                    ArrayList<String> propertiesPerMission = new ArrayList<String>();

                    //TODO: interchange lat and lng positions later when DB is fixed
                    if(jo.get("point_longitude") instanceof Integer){
                        temp=1.0*(int)jo.get("point_longitude");
                    }else temp=(Double)jo.get("point_longitude");
                    propertiesPerMission.add(Double.toString(temp));

                    if(jo.get("point_latitude") instanceof Integer){
                        temp=1.0*(int)jo.get("point_latitude");
                    }else temp=(Double)jo.get("point_latitude");
                    propertiesPerMission.add(Double.toString(temp));
                    //===================================================================//
                    if(jo.get("avg_ph") instanceof Integer){
                        temp=1.0*(int)jo.get("avg_ph");
                    }else temp=(Double)jo.get("avg_ph");
                    propertiesPerMission.add(Double.toString(temp));

                    if(jo.get("avg_cels") instanceof Integer){
                        temp=1.0*(int)jo.get("avg_cels");
                    }else temp=(Double)jo.get("avg_cels");
                    propertiesPerMission.add(Double.toString(temp));

                    if(jo.get("avg_faht") instanceof Integer){
                        temp=1.0*(int)jo.get("avg_faht");
                    }else temp=(Double)jo.get("avg_faht");
                    propertiesPerMission.add(Double.toString(temp));

                    if(jo.get("avg_cond") instanceof Integer){
                        temp=1.0*(int)jo.get("avg_cond");
                    }else temp=(Double)jo.get("avg_cond");
                    propertiesPerMission.add(Double.toString(temp));

                    if(jo.get("avg_tds") instanceof Integer){
                        temp=1.0*(int)jo.get("avg_tds");
                    }else temp=(Double)jo.get("avg_tds");
                    propertiesPerMission.add(Double.toString(temp));

                    if(jo.get("avg_saln") instanceof Integer){
                        temp=1.0*(int)jo.get("avg_saln");
                    }else temp=(Double)jo.get("avg_saln");
                    propertiesPerMission.add(Double.toString(temp));

                    perMission.add(propertiesPerMission);
                }
                allProperties.add(perMission);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //==========================reading the value of one property (findSensorByMission())===========================//
        if(mMap!=null){
                storeProperty(ph, "avg_ph");
                storeProperty(cels, "avg_cels");
                storeProperty(faht, "avg_faht");
                storeProperty(cond, "avg_cond");
                storeProperty(tds, "avg_tds");
                storeProperty(saln, "avg_saln");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //===========================ADDING AREAS AND MISSIONS TO SPINNER============================//
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
                    if(missions.size()!=0){
                        ArrayAdapter<String> adapter_mission= new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, missions.get(curArea));
                        adapter_mission.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        missionspinner.setAdapter(adapter_mission);
                        if(curArea!=0){
                            curMission=missions.get(curArea-1).size();
                        }else{
                            curMission=0;
                        }
                        spinnerChanged();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {}
            });

            missionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String text = parentView.getItemAtPosition(position).toString();
                    curMission=Integer.parseInt(text)-1;
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
                if(allProperties.get(curMission).size()!=0){
                    Double lat=Double.parseDouble(allProperties.get(0).get(0).get(0));
                    Double lng=Double.parseDouble(allProperties.get(0).get(0).get(1));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
                    addPins();
                }
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
            if(curProp==0) addPins();
            else if(curProp==1) addHeatMap(ph.get(curMission));
            else if(curProp==2) addHeatMap(cels.get(curMission));
            else if(curProp==3) addHeatMap(faht.get(curMission));
            else if(curProp==4) addHeatMap(cond.get(curMission));
            else if(curProp==5) addHeatMap(tds.get(curMission));
            else addHeatMap(saln.get(curMission));
        }
    }

    private void createTable(){
        table.removeAllViews();
        ArrayList<ArrayList<String>> missionData = allProperties.get(curMission);
        int col=0;
        for(int i = 0; i <8; i++){
            if(missionData.size()!=0){
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
    }

    private void addHeatMap(ArrayList<ArrayList<Double>> storage) {
        mMap.clear();
        if(storage.size()!=0){
            ArrayList<WeightedLatLng> weighteddata = new ArrayList<WeightedLatLng>();

            for(int i=0; i<storage.size(); i++){
                LatLng latLng = new LatLng(storage.get(i).get(0), storage.get(i).get(1));
                weighteddata.add(new WeightedLatLng(latLng, storage.get(i).get(2)));
            }
            mProvider = new HeatmapTileProvider.Builder()
                    .weightedData(weighteddata)
                    .build();
            mProvider.setRadius(50);
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    private void addPins(){
        mMap.clear();
        Double lat,lng;
        if(allProperties.size()!=0){
            ArrayList<ArrayList<String>> thisMission = allProperties.get(curMission);
            for(int i=0; i<thisMission.size();i++){
                if(thisMission.get(i).size()!=0){
                    lat=Double.parseDouble(thisMission.get(i).get(0));
                    lng=Double.parseDouble(thisMission.get(i).get(1));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
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
        }
    }

    private void storeProperty(ArrayList<ArrayList<ArrayList<Double>>> property, String toGet){
        for(int j=0; j<numOfMissions; j++){
            try {
                String link="http://10.0.3.57:6200/missions/"+(j+1)+"/results/point_latitude,point_longitude,"+toGet;
                JSONArray ja=readURL(link);
                ArrayList<ArrayList<Double>> oneMission = new ArrayList<ArrayList<Double>>();
                for(int i=0; i<ja.length(); i++){
                    Double temp;
                    JSONObject jo = (JSONObject) ja.get(i);
                    ArrayList<Double> onePoint = new ArrayList<Double>();

                    //TODO: change pos of long lat later
                    if(jo.get("point_longitude") instanceof Integer){
                        temp=1.0*(int)jo.get("point_longitude");
                    }else temp=(Double)jo.get("point_longitude");
                    onePoint.add(temp);

                    if(jo.get("point_latitude") instanceof Integer){
                        temp=1.0*(int)jo.get("point_latitude");
                    }else temp=(Double)jo.get("point_latitude");
                    onePoint.add(temp);

                    if(jo.get(toGet) instanceof Integer){
                        temp=1.0*(int)jo.get(toGet);
                    }else temp=(Double)jo.get(toGet);
                    onePoint.add(temp);

                    oneMission.add(onePoint);
                }
                property.add(oneMission);
            }catch (JSONException e) {
                e.printStackTrace();
            }
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
}