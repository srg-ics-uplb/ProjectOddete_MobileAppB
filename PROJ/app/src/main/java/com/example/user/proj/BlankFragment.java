package com.example.user.proj;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment implements OnMapReadyCallback{

    private Context context;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private Spinner spinner, areaspinner, missionspinner;

    public BlankFragment() {
        // Required empty public constructor
    }

    public BlankFragment (Context context){
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment==null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        final View view =  inflater.inflate(R.layout.fragment_blank, container, false);

        //==============================SPINNER FOR PROPERTIES=====================================//
        spinner = (Spinner)view.findViewById(R.id.spinner);
        areaspinner = (Spinner)view.findViewById(R.id.spinner_maparea);
        missionspinner = (Spinner)view.findViewById(R.id.spinner_mapmission);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.water_properties, android.R.layout.simple_spinner_item);    //adds the options in water_properties string array
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //========================================================================================//
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new custominfowindowadapter(this.getActivity()));
        fetchData process=new fetchData(this.context, mMap, spinner, areaspinner, missionspinner);
        process.execute();
    }
}