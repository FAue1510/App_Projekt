package activity;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import Utility.NetworkChangeListener;
import de.hdodenhof.circleimageview.CircleImageView;
import fontsUI.cairoTextView;
import model.Department;
import model.DepartmentManager;
import model.ProfManager;
import model.Professors;

public class ProfActivity extends Activity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private cairoTextView id_name_TextView, id_email_TextView, id_street_housenumer_TextView, id_postalCode_city_TextView, id_mobilenumer_TextView, id_departments_TextView;
    private RatingBar rb_prof_rating;
    private LinearLayout linear_profile_container;
    private CircleImageView img_tutor_picture;
    private ProfManager manager;
    private Professors prof;
    private ArrayList ratings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DataViewActivity.class);
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
        setContentView(R.layout.activity_prof);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        manager = ProfManager.getInstance();

        id_name_TextView = findViewById(R.id.id_name_TextView);
        id_email_TextView = findViewById(R.id.id_email_TextView);
        id_street_housenumer_TextView = findViewById(R.id.id_street_housenumer_TextView);
        id_postalCode_city_TextView = findViewById(R.id.id_postalCode_city_TextView);
        id_mobilenumer_TextView = findViewById(R.id.id_mobilenumer_TextView);
        id_departments_TextView = findViewById(R.id.id_departments_TextView);
        rb_prof_rating = findViewById(R.id.rb_prof_rating);
        linear_profile_container = findViewById(R.id.linear_profile_container);
        img_tutor_picture = findViewById(R.id.img_tutor_picture);

        prof = manager.getProf(getIntent().getStringExtra("id"));
        id_name_TextView.setText(prof.getFirstName() + " " + prof.getLastName());
        id_email_TextView.setText(prof.getEmail());
        id_street_housenumer_TextView.setText(prof.getStreet() + " " + prof.getHouseNumber());
        id_postalCode_city_TextView.setText(prof.getPlz() + " " + prof.getCity());
        id_mobilenumer_TextView.setText(prof.getMobileNumber());
        img_tutor_picture.setImageBitmap(prof.getImage());
        ratings = new ArrayList();

        for (Department department: DepartmentManager.getInstance().getDepList()) {
            if(prof.getDepartments().contains(department.getId())){
                id_departments_TextView.setText(id_departments_TextView.getText() + department.getName() + "\n");
            }
        }
        //documentReference = db.collection("professors").document(prof.getid());
        //readData(documentReference);

        //id_departments_TextView.setText("TEST");
    }
    public void order_OnClick(View view){
        Intent intent = new Intent(ProfActivity.this, OrderActivity.class);
        intent.putExtra("id",prof.getid());
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    public void call_onClick(View view) {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},1);

        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + prof.getMobileNumber()));
            startActivity(intent);
        }
    }

    private void readData(DocumentReference documentReference) {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ratings = (ArrayList) document.get("valuation");
                        long rating = 0;
                        for ( Object r : ratings) {
                            rating += (long)r;
                        }
                        rb_prof_rating.setRating(rating / (ratings.toArray().length + 1));
                    }
                    else{

                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}