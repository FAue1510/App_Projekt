package activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.List;
import java.util.Locale;

import model.Professors;

public class HomeActivity extends Activity {

    EditText edt_search;
    EditText edt_umkreis;
    DatePicker sp_date;
    EditText edt_fachbereich;

    List<Professors> Profs;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "PROFESSORS";

    private FusedLocationProviderClient fusedLocationClient;
    int LOCATION_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edt_search = findViewById(R.id.edt_search);
        edt_umkreis = findViewById(R.id.edt_umkreis);
        //sp_date = findViewById(R.id.sp_date);
        edt_fachbereich = findViewById(R.id.edt_fachbereich);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            getGeolocation();
        }else{
            askLocationPermission();
        }
    }

    public void search_click(View view) {
        Query search = db.collection("professors");
        //Query for departments
        if (!edt_fachbereich.getText().toString().equals(""))
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
        }
        readData(search);
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
                        //Profs.add(prof);
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

    private void askLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    public void switchProfile_click(View view){
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}