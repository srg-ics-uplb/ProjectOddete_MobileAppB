package ph.edu.uplb.ics.srg.odette;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TableLayout;

import java.util.ArrayList;

public class TableFragment extends Fragment {

    private Context context;
    private View view;
    private TableLayout table;
    private Spinner areaspinner, missionspinner;

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
        areaspinner=(Spinner) view.findViewById(R.id.spinner_tablearea);
        missionspinner=(Spinner)view.findViewById(R.id.spinner_tablemission);
        fetchData process=new fetchData(this.context, table, areaspinner, missionspinner);
        process.execute();
        return view;
    }
}