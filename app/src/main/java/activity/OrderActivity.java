package activity;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.Account;
import model.Order;
import model.ProfManager;
import model.Professors;

public class OrderActivity extends Activity {

    FirebaseAuth Auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Account ac = Account.getInstance();

    EditText edt_street;
    EditText edt_housenumber;
    EditText edt_plz;
    EditText edt_city;
    EditText edt_description;

    Button b_cancel;
    Button b_order;

    CalendarView cv_calender;

    private ProfManager manager;
    private Professors prof;
    private String curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edt_street = findViewById(R.id.edt_street_b);
        edt_housenumber = findViewById(R.id.edt_houseNumbere_b);
        edt_plz = findViewById(R.id.edt_plz_b);
        edt_city = findViewById(R.id.edt_city_b);
        edt_description = findViewById(R.id.edt_description_b);

        b_cancel = findViewById(R.id.b_cancel_b);
        b_order = findViewById(R.id.b_order_b);

        cv_calender = findViewById(R.id.cv_clender);

        manager = ProfManager.getInstance();
        prof = manager.getProf(getIntent().getStringExtra("id"));

        loadData();

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        curDate = dateFormat.format(cv_calender.getDate());
        cv_calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                curDate = new StringBuilder().append(dayOfMonth).append(".").append(month).append(".").append(year).toString();
            }
        });
        disableOrderedDates();
    }

    private void loadData(){
        edt_street.setText(ac.getStreet());
        edt_housenumber.setText(ac.getHouseNumber());
        edt_plz.setText(ac.getPlz());
        edt_city.setText(ac.getCity());
    }

    public void cancel_OnClick(View view)
    {
        Intent intent = new Intent(OrderActivity.this, ProfActivity.class);
        intent.putExtra("id", prof.getid());
        startActivity(intent);
    }

    public void order_OnClick2(View view)
    {
        Order order = new Order(Auth.getCurrentUser().getUid(), prof.getid(), edt_street.getText().toString(), edt_housenumber.getText().toString(), edt_plz.getText().toString(), edt_city.getText().toString(), curDate, edt_description.getText().toString());
        db.collection("order").add(order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(getApplicationContext(), "Buchung erfolgreich", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void disableOrderedDates(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().getActualMinimum(Calendar.HOUR_OF_DAY));
        long date = calendar.getTime().getTime();
        cv_calender.setMinDate(date);
    }

}