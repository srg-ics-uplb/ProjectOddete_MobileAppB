package com.example.user.proj;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class TableFragment extends Fragment {

    private Context context;
    private View view;
    public static TableLayout table;

    String data;
    ArrayList<String> phlvls=new ArrayList<String>();
    ArrayList<String> tempcs=new ArrayList<String>();
    ArrayList<String> tempfr=new ArrayList<String>();
    ArrayList<String> cond=new ArrayList<String>();
    ArrayList<String> tds=new ArrayList<String>();
    ArrayList<String> sal=new ArrayList<String>();

    public TableFragment (Context context){
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.table_layout, null);
        table=(TableLayout)view.findViewById(R.id.table);
        fetchData process=new fetchData(this.context, table);
        process.execute();
        return view;
    }
}