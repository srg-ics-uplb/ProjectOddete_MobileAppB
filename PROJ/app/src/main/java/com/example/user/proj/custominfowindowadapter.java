package com.example.user.proj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class custominfowindowadapter implements GoogleMap.InfoWindowAdapter {
    private final View view;
    private Context context;

    public  custominfowindowadapter (Context context){
        this.context=context;
        view= LayoutInflater.from(context).inflate(R.layout.custominfowindow, null);
    }

    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tv1= (TextView)view.findViewById(R.id.title);

        if(!title.equals("")){
            tv1.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tv2= (TextView)view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            tv2.setText(snippet);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, view);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, view);
        return view;
    }
}
