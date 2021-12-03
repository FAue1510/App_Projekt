package activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

import Utility.NetworkChangeListener;
import classy.CustomDatePicker.DatePicker;
import database.ProfSQLiteOpenHelper;
import fontsUI.cairoEditText;
import model.Department;
import model.DepartmentManager;
import model.ProfManager;
import model.Professors;

public class HomeActivity extends Activity {

    DatePicker departmentPicker, circlingPicker;
    cairoEditText nameText;

    ProfManager profManager;
    DepartmentManager depManager;
    SharedPreferences prefs;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth Auth = FirebaseAuth.getInstance();
    String TAG = "PROFESSORS";

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    //Bitmap bitmap;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Auth.signOut();
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
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
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                1);

        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        profManager = ProfManager.getInstance();
        depManager = DepartmentManager.getInstance();

        nameText = findViewById(R.id.id_fullName_EditText);

        departmentPicker = findViewById(R.id.id_department_picker);
        circlingPicker = findViewById(R.id.id_circling_picker);

        circlingPicker.setItems(Arrays.asList(getResources().getStringArray(R.array.km)));
        departmentPicker.setItems(Arrays.asList(getResources().getStringArray(R.array.departments)));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getDepartments();
        Query search = db.collection("professors");
        readData(search);
    }

    public void search_click(View view) {
        profManager.deleteList();
        profManager.addProfList(new ProfSQLiteOpenHelper(getApplicationContext()).readAll(nameText.getText().toString(), departmentPicker.getSeletedItem(), circlingPicker.getSeletedItem()));
        prefs.edit().putString("selected_department", departmentPicker.getSeletedItem()).apply();
        //go to next view
        Intent intent = new Intent(HomeActivity.this, DataViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void readData(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ProfSQLiteOpenHelper helper = new ProfSQLiteOpenHelper(getApplicationContext());
                    helper.deleteData();

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
                                document.getId(),
                                document.get("number").toString(),
                                document.get("lat").toString(),
                                document.get("long").toString(),
                                Integer.parseInt(document.get("price").toString())
                        );
                        downloadImage(prof, helper);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getDepartments() {
        db.collection("departments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    depManager.deleteList();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        depManager.addDep(new Department(
                                document.getId(),
                                document.get("departmentName").toString()
                        ));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error getting documents: ", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private Location getGeolocation() {

        return null;
    }

    private void downloadImage(Professors prof, ProfSQLiteOpenHelper helper) {
        StorageReference imageRef = storage.getReference().child("Image").child(prof.getid());

        imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                helper.insertProf(prof, bytes);
            }
        });
    }

    public void switchProfile_click(View view){
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
}