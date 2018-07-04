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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fetchData extends AsyncTask<Void, Void, Void> {

    Context context;
    public static TableLayout table;
    public GoogleMap mMap;
    public Spinner spinner, areaspinner, missionspinner;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    static String data="";
    URL url;
    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader br;

    ArrayList<LatLng> latlng= new ArrayList<LatLng>();
    ArrayList<Double> phlvls=new ArrayList<Double>();
    ArrayList<Double> tempcs=new ArrayList<Double>();
    ArrayList<Double> tempfr=new ArrayList<Double>();
    ArrayList<Double> cond=new ArrayList<Double>();
    ArrayList<Double> tds=new ArrayList<Double>();
    ArrayList<Double> sal=new ArrayList<Double>();

    ArrayList<String> area = new ArrayList<String>();

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
        //==========================reading the Area and Mission to be put in spinner===========================//
        try{
            JSONArray ja=readURL("https://api.myjson.com/bins/841l8");
            for(int i=0; i<ja.length(); i++){
                JSONObject jo = (JSONObject) ja.get(i);
                area.add((String)jo.get("area_name"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        //======================================================================================================//

        //=================================reading the value of properties======================================//
        try {
            JSONArray ja=readURL("https://api.myjson.com/bins/16sgmy");
            for(int i=0; i<ja.length(); i++){
                Double temp, temp1;
                JSONObject jo = (JSONObject) ja.get(i);

                if(jo.get("lat") instanceof Integer){
                    temp=1.0*(int)jo.get("lat");
                }else temp=(Double)jo.get("lat");

                if(jo.get("lng") instanceof Integer){
                    temp1=1.0*(int)jo.get("lng");
                }else temp1=(Double)jo.get("lng");
                latlng.add(new LatLng(temp, temp1));

                if(jo.get("ph_lvl") instanceof Integer){
                    temp=1.0*(int)jo.get("ph_lvl");
                }else temp=(Double)jo.get("ph_lvl");
                phlvls.add(temp);

                if(jo.get("temp_celsius") instanceof Integer){
                    temp=1.0*(int)jo.get("temp_celsius");
                }else temp=(Double)jo.get("temp_celsius");
                tempcs.add(temp);

                if(jo.get("temp_fahrenheit") instanceof Integer){
                    temp=1.0*(int)jo.get("temp_fahrenheit");
                }else temp=(Double)jo.get("temp_fahrenheit");
                tempfr.add(temp);

                if(jo.get("conductivity") instanceof Integer){
                    temp=1.0*(int)jo.get("conductivity");
                }else temp=(Double)jo.get("conductivity");
                cond.add(temp);

                if(jo.get("tds") instanceof Integer){
                    temp=1.0*(int)jo.get("tds");
                }else temp=(Double)jo.get("tds");
                tds.add(temp);

                if(jo.get("salinity") instanceof Integer){
                    temp=1.0*(int)jo.get("salinity");
                }else temp=(Double)jo.get("salinity");
                sal.add(temp);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {  //adding the extracted JSON file to table
        super.onPostExecute(aVoid);
        if (this.context!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context,
                    android.R.layout.simple_spinner_item, area);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            areaspinner.setAdapter(adapter);
        }
        //====================================POPULATING TABLE====================================//
        if (table != null && this.context!=null) {
            while (table.getChildCount() > 1)
                table.removeView(table.getChildAt(table.getChildCount() - 1));

            for (int i = 0; i < phlvls.size(); i++) {
                TableRow row = new TableRow(this.context);
                String lat_txt = Double.toString(latlng.get(i).latitude);
                String lng_txt = Double.toString(latlng.get(i).longitude);
                String ph_txt = Double.toString(phlvls.get(i));
                String tempcs_txt = Double.toString(tempcs.get(i));
                String tempfr_txt = Double.toString(tempcs.get(i));
                String cond_txt = Double.toString(tempcs.get(i));
                String tds_txt = Double.toString(tempcs.get(i));
                String sal_txt = Double.toString(tempcs.get(i));

                TextView latlng_tv = new TextView(this.context);
                latlng_tv.setText("(" + lat_txt + ", " + lng_txt + ")");
                TextView ph_tv = new TextView(this.context);
                ph_tv.setText(ph_txt);
                TextView tempcs_tv = new TextView(this.context);
                tempcs_tv.setText(tempcs_txt);
                TextView tempfr_tv = new TextView(this.context);
                tempfr_tv.setText(tempfr_txt);
                TextView cond_tv = new TextView(this.context);
                cond_tv.setText(cond_txt);
                TextView tds_tv = new TextView(this.context);
                tds_tv.setText(tds_txt);
                TextView sal_tv = new TextView(this.context);
                sal_tv.setText(sal_txt);

                int col;
                if (i % 2 == 0) {
                    col = Color.parseColor("#FFFFFF");
                } else {
                    col = Color.parseColor("#D6F4FD");
                }

                //manually setting the style until textview.setTextAppearance() is fixed.
                latlng_tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                latlng_tv.setBackgroundColor(col);
                ph_tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                ph_tv.setBackgroundColor(col);
                tempcs_tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                tempcs_tv.setBackgroundColor(col);
                tempfr_tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                tempfr_tv.setBackgroundColor(col);
                cond_tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                cond_tv.setBackgroundColor(col);
                tds_tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                tds_tv.setBackgroundColor(col);
                sal_tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                sal_tv.setBackgroundColor(col);

                row.addView(latlng_tv);
                row.addView(ph_tv);
                row.addView(tempcs_tv);
                row.addView(tempfr_tv);
                row.addView(cond_tv);
                row.addView(tds_tv);
                row.addView(sal_tv);
                this.table.addView(row);
            }
        }
        //======================================================================================//

        //=============================ADDING PINS AND HEATMAP==================================//
        if(mMap!=null){
            if(latlng.size()!=0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng.get(0)));
                addPins();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        mMap.addMarker(new MarkerOptions()
                                .position(latlng.get(0)));
                        String text = parentView.getItemAtPosition(position).toString();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng.get(0)));
                        mMap.clear();
                        if (text.equals("pH level")) {
                            addHeatMap(phlvls);
                        } else if (text.equals("Temperature (Celsius)")) {
                            addHeatMap(tempcs);
                        } else if (text.equals("Temperature (Fahrenheit)")) {
                            addHeatMap(tempfr);
                        } else if (text.equals("Conductivity")) {
                            addHeatMap(cond);
                        } else if (text.equals("TDS (Total Dissolved Solids)")) {
                            addHeatMap(tds);
                        } else if (text.equals("Salinity")) {
                            addHeatMap(sal);
                        } else {
                            addPins();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });
            }
        }
        //======================================================================================//
    }

    private void addHeatMap(ArrayList<Double> weight) {
        ArrayList<WeightedLatLng> weighteddata = new ArrayList<WeightedLatLng>();

        for(int i=0; i<weight.size(); i++){
            weighteddata.add(new WeightedLatLng(latlng.get(i), weight.get(i)));
        }
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(weighteddata)
                .build();
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private void addPins(){
        for(int i=0; i<phlvls.size();i++){
            LatLng pin = latlng.get(i);
            mMap.addMarker(new MarkerOptions()
                    .position(pin)
                    .title("("+latlng.get(i).latitude+", "+latlng.get(i).longitude+")")
                    .snippet("   pH level: "+phlvls.get(i)+"   \n"+
                            "   Temp in C: "+tempcs.get(i)+"   \n"+
                            "   Temp in F: "+tempfr.get(i)+"   \n"+
                            "   Conductivity: "+cond.get(i)+"   \n"+
                            "   TDS: "+tds.get(i)+"   \n"+
                            "   Salinity: "+sal.get(i)+"   "));
        }
    }

    private JSONArray readURL(String link){
        JSONArray ja=null;
        data="";
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