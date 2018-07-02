package com.example.user.proj;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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

public class fetchData extends AsyncTask<Void, Void, Void> {

    Context context;
    public static TableLayout table;
    public GoogleMap mMap;
    public Spinner spinner;

    static String data="";
    ArrayList<String> time=new ArrayList<String>();
    ArrayList<LatLng> latlng= new ArrayList<LatLng>();
    ArrayList<String> phlvls=new ArrayList<String>();
    ArrayList<String> tempcs=new ArrayList<String>();
    ArrayList<String> tempfr=new ArrayList<String>();
    ArrayList<String> cond=new ArrayList<String>();
    ArrayList<String> tds=new ArrayList<String>();
    ArrayList<String> sal=new ArrayList<String>();

    public fetchData (Context context, TableLayout table){
        this.context=context;
        this.table=table;
    }

    public fetchData(Context context, GoogleMap mMap, Spinner spinner){
        this.context=context;
        this.mMap=mMap;
        this.spinner=spinner;
    }

    @Override
    protected Void doInBackground(Void... voids) {  //extracting JSON file
        try {
            URL url = new URL("https://api.myjson.com/bins/8k6oe"); //get the JSON file from this url
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line="";
            while(line!=null){
                line=br.readLine();
                data+=line;
            }

            JSONArray ja = new JSONArray(data);
            for(int i=0; i<ja.length(); i++){
                Double temp, temp1;
                JSONObject jo = (JSONObject) ja.get(i);
                time.add((String)jo.get("time"));

                if(jo.get("lat") instanceof Integer){
                    temp=1.0*(int)jo.get("lat");
                }else temp=(Double)jo.get("lat");

                if(jo.get("lng") instanceof Integer){
                    temp1=1.0*(int)jo.get("lng");
                }else temp1=(Double)jo.get("lng");
                latlng.add(new LatLng(temp, temp1));

                temp=(Double) jo.get("ph_lvl");
                phlvls.add(Double.toString(temp));

                temp=(Double) jo.get("temp_celsius");
                tempcs.add(Double.toString(temp));

                temp=(Double) jo.get("temp_fahrenheit");
                tempfr.add(Double.toString(temp));

                temp=(Double) jo.get("conductivity");
                cond.add(Double.toString(temp));

                int temp2=(int) jo.get("tds");
                tds.add(Integer.toString(temp2));

                temp=(Double) jo.get("salinity");
                sal.add(Double.toString(temp));
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {  //adding the extracted JSON file to table
        super.onPostExecute(aVoid);

        //====================================POPULATING TABLE====================================//
        if (table != null) {
            for(int i=0;i<phlvls.size();i++){
                TableRow row=new TableRow(this.context);
                String time_txt = time.get(i);
                String lat_txt = Double.toString(latlng.get(i).latitude);
                String lng_txt = Double.toString(latlng.get(i).longitude);
                String ph_txt = phlvls.get(i);
                String tempcs_txt = tempcs.get(i);
                String tempfr_txt = tempcs.get(i);
                String cond_txt = tempcs.get(i);
                String tds_txt = tempcs.get(i);
                String sal_txt = tempcs.get(i);

                TextView time_tv=new TextView(this.context);
                time_tv.setText(time_txt);
                TextView latlng_tv=new TextView(this.context);
                latlng_tv.setText("("+lat_txt+", "+lng_txt+")");
                TextView ph_tv=new TextView(this.context);
                ph_tv.setText(ph_txt);
                TextView tempcs_tv=new TextView(this.context);
                tempcs_tv.setText(tempcs_txt);
                TextView tempfr_tv=new TextView(this.context);
                tempfr_tv.setText(tempfr_txt);
                TextView cond_tv=new TextView(this.context);
                cond_tv.setText(cond_txt);
                TextView tds_tv=new TextView(this.context);
                tds_tv.setText(tds_txt);
                TextView sal_tv=new TextView(this.context);
                sal_tv.setText(sal_txt);

                int col;
                if(i%2==0){
                    col=Color.parseColor("#FFFFFF");
                }else{
                    col=Color.parseColor("#D6F4FD");
                }

                //manually setting the style until textview.setTextAppearance() is fixed.
                time_tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                time_tv.setBackgroundColor(col);
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

                row.addView(time_tv);
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

        //======================ADDING PINS AND DETAILS TO MAP==================================//
        if(mMap!=null){
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String text = parentView.getItemAtPosition(position).toString();
                    if(text.equals("pH level")){

                    }else{
                        for(int i=0; i<phlvls.size();i++){
                            LatLng pin = latlng.get(i);
                            mMap.addMarker(new MarkerOptions()
                                    .position(pin)
                                    .title("("+latlng.get(i).latitude+", "+latlng.get(i).longitude+")")
                                    .snippet("pH level: "+phlvls.get(i)+"\n"+
                                            "Temp in C: "+tempcs.get(i)+"\n"+
                                            "Temp in F: "+tempfr.get(i)+"\n"+
                                            "Conductivity: "+cond.get(i)+"\n"+
                                            "TDS: "+tds.get(i)+"\n"+
                                            "Salinity: "+sal.get(i)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(pin));
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {}
            });
        }
        //======================================================================================//
    }
}