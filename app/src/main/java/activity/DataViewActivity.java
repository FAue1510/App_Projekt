package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a21q4_app_projekt.R;

import Utility.NetworkChangeListener;
import fontsUI.cairoTextView;
import model.ProfListAdapter;
import model.ProfManager;

public class DataViewActivity extends Activity{

    private RecyclerView recyclerView;
    private ProfListAdapter adapter;
    private ProfManager manager;

    cairoTextView dataview_TextView;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
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
        setContentView(R.layout.activity_dataview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        manager = ProfManager.getInstance();

        dataview_TextView = findViewById(R.id.id_dataview_TextView);
        dataview_TextView.setText("Gefundene Dozenten: " + manager.getDozentenList().size());

        recyclerView = findViewById(R.id.rvProfs);
        recyclerView.setHasFixedSize(false);    //erh??ht etwas die Performance

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new ProfListAdapter(getApplicationContext(), manager.getDozentenList(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }


}
