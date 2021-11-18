package activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a21q4_app_projekt.R;

import model.ProfListAdapter;
import model.ProfManager;

public class DataViewActivity extends Activity{

    private RecyclerView recyclerView;
    private ProfListAdapter adapter;
    private ProfManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataview);

        manager = ProfManager.getInstance();

        recyclerView = findViewById(R.id.rvProfs);
        recyclerView.setHasFixedSize(false);    //erhöht etwas die Performance

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new ProfListAdapter(getApplicationContext(), manager.getDozentenList());
        recyclerView.setAdapter(adapter);
    }


}
