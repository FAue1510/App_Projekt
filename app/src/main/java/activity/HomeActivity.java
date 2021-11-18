package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.a21q4_app_projekt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import model.Professors;

public class HomeActivity extends Activity {

    EditText edt_search;
    EditText edt_umkreis;
    DatePicker sp_date;
    EditText edt_fachbereich;

    Professors ProfList[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edt_search = findViewById(R.id.edt_search);
        edt_umkreis = findViewById(R.id.edt_umkreis);
        //sp_date = findViewById(R.id.sp_date);
        edt_fachbereich = findViewById(R.id.edt_fachbereich);
    }

    public void search_click(View view) {
        DatabaseReference mDatabaseRef =FirebaseDatabase.getInstance().getReference("professors");
        Query query=mDatabaseRef.orderByChild("firstName").equalTo(edt_search.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data:dataSnapshot.getChildren()){

                    String test = data.child("firstName").getValue(String.class);

                    Professors models=data.getValue(Professors.class);
                    String latitude=models.getFirstName();
                    String longitude=models.getLastName();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Intent intent = new Intent(HomeActivity.this, DataViewActivity.class);
        startActivity(intent);
    }

    public void switchToProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
    }
}