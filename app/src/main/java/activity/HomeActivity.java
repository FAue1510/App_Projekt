package activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import classy.CustomDatePicker.DatePicker;
import database.ProfSQLiteOpenHelper;
import fontsUI.cairoEditText;
import model.ProfManager;
import model.Professors;

public class HomeActivity extends Activity {

    DatePicker departmentPicker, circlingPicker;
    cairoEditText nameText;

    List<Professors> Profs;
    ProfManager manager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "PROFESSORS";

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        manager = ProfManager.getInstance();

        departmentPicker = findViewById(R.id.id_department_picker);
        circlingPicker = findViewById(R.id.id_circling_picker);

        circlingPicker.setItems(Arrays.asList(getResources().getStringArray(R.array.km)));
        departmentPicker.setItems(Arrays.asList(getResources().getStringArray(R.array.departments)));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Query search = db.collection("professors");
        readData(search);
    }

    public void search_click(View view) {
        Query search = db.collection("professors");
        //Query for departments
        /*if (!edt_fachbereich.getText().toString().equals(""))
            search = search.whereArrayContains("departments", edt_fachbereich.getText().toString());
        //Query for lat und long
        if (!edt_umkreis.getText().toString().equals("")) {
            double r = Double.parseDouble(edt_umkreis.getText().toString())/110;
            search = search.whereLessThanOrEqualTo("lat",getGeolocation().getLatitude() + r)
                    .whereGreaterThanOrEqualTo("lat", getGeolocation().getLatitude() - r);
            search = search.whereLessThanOrEqualTo("long", getGeolocation().getLongitude() + r)
                    .whereGreaterThanOrEqualTo("long", getGeolocation().getLongitude() - r);
        }
        //Query for firstName and lastName
        if (!edt_search.getText().toString().equals("")) {
            //Search for firstName and lastName
            if (edt_search.getText().toString().contains(" ")) {
                search = search.whereEqualTo("firstName", edt_search.getText().toString().split(" ")[0]);
                search = search.whereEqualTo("lastName", edt_search.getText().toString().split(" ")[1]);
            } else {
                //Search for firstName or lastName
                readData(search.whereEqualTo("firstName", edt_search.getText().toString()));
                readData(search.whereEqualTo("lastName", edt_search.getText().toString()));
                //go to next view
                Intent intent = new Intent(HomeActivity.this, DataViewActivity.class);
                startActivity(intent);
                return;
            }
        }*/
        //readData(search);

        manager.addProfList(new ProfSQLiteOpenHelper(getApplicationContext()).readAll());
        //go to next view
        Intent intent = new Intent(HomeActivity.this, DataViewActivity.class);
        startActivity(intent);
    }

    private void readData(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Professors prof = new Professors(
                                document.get("email").toString(),
                                document.get("firstName").toString(),
                                document.get("lastName").toString(),
                                document.get("birthday").toString(),
                                document.get("street").toString(),
                                document.get("housenumber").toString(),
                                document.get("postalCode").toString(),
                                document.get("city").toString(),
                                ((ArrayList<String>) document.get("departments")),
                                document.getId()
                        );
                        new ProfSQLiteOpenHelper(getApplicationContext()).insertProf(prof);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private Location getGeolocation() {

        return null;
    }

    public void switchProfile_click(View view){
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
}