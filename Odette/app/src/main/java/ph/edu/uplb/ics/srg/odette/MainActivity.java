package ph.edu.uplb.ics.srg.odette;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TableLayout;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public static TableLayout table;
    public static String apiEndpoint="http://10.0.3.57:6200";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(isInternetAvailable()){
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new BlankFragment(this));  //initial fragment when app loads
//        }else{
//            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
//        }
    }

    private boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment=null;
        switch (menuItem.getItemId()){
            case R.id.navigation_map:
                fragment= new BlankFragment(this);
                break;
            case R.id.navigation_table:
                fragment=new TableFragment(this);
                break;
            case R.id.navigation_settings:
                fragment=new SettingsFragment(this);
                break;
        }
        return loadFragment(fragment);
    }
}