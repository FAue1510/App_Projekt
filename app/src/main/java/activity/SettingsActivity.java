package activity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.a21q4_app_projekt.R;

import java.util.Arrays;
import java.util.List;

import Utility.NetworkChangeListener;
import classy.CustomDatePicker.DatePicker;
import model.DepartmentManager;

public class SettingsActivity extends Activity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    DatePicker departmentPicker, circlingPicker;
    SharedPreferences prefs;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        departmentPicker = findViewById(R.id.id_department_picker);
        circlingPicker = findViewById(R.id.id_circling_picker);

        circlingPicker.setItems(Arrays.asList(getResources().getStringArray(R.array.km)));
        List<String> circs = Arrays.asList(this.getResources().getStringArray(R.array.km));
        circlingPicker.setSeletion(circs.indexOf(prefs.getString("prefCirc", "Unbegrenzt")));

        departmentPicker.setItems(DepartmentManager.getInstance().getDepListString());
        List<String> deps = DepartmentManager.getInstance().getDepListString();
        departmentPicker.setSeletion(deps.indexOf(prefs.getString("prefDep", "Alle")));

    }

    public void save_onClick(View view) {
        prefs.edit().putString("prefDep", departmentPicker.getSeletedItem()).apply();
        prefs.edit().putString("prefCirc", circlingPicker.getSeletedItem()).apply();
        finish();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
}
