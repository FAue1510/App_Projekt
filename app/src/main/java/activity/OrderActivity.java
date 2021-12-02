package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CalendarView;

import com.example.a21q4_app_projekt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Utility.NetworkChangeListener;
import fontsUI.cairoEditText;
import fontsUI.cairoTextView;
import model.Account;
import model.Order;
import model.ProfManager;
import model.Professors;

public class OrderActivity extends Activity {

    FirebaseAuth Auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Account ac = Account.getInstance();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    cairoTextView id_street_housenumer_TextView;
    cairoTextView id_postalCode_city_TextView;
    cairoEditText id_comment_EditText;

    CalendarView cv_calender;

    private ProfManager manager;
    private Professors prof;
    private String curDate;
    private Order order;

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
        setContentView(R.layout.activity_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        id_street_housenumer_TextView = findViewById(R.id.id_street_housenumer_TextView);
        id_postalCode_city_TextView = findViewById(R.id.id_postalCode_city_TextView);
        id_comment_EditText = findViewById(R.id.id_comment_EditText);

        cv_calender = findViewById(R.id.cv_clender);

        manager = ProfManager.getInstance();
        prof = manager.getProf(getIntent().getStringExtra("id"));

        loadData();

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        curDate = dateFormat.format(cv_calender.getDate());
        cv_calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                curDate = new StringBuilder().append(dayOfMonth).append(".").append(month+1).append(".").append(year).toString();
            }
        });
        disableOrderedDates();
    }

    private void loadData(){
        id_street_housenumer_TextView.setText(ac.getStreet() + " " + ac.getHouseNumber());
        id_postalCode_city_TextView.setText(ac.getPlz() + " " + ac.getCity());

    }

    public void cancel_OnClick(View view)
    {
        Intent intent = new Intent(OrderActivity.this, ProfActivity.class);
        intent.putExtra("id", prof.getid());
        startActivity(intent);
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }

    public void order_OnClick2(View view)
    {
        order = new Order(Auth.getCurrentUser().getUid(), prof.getid(), ac.getStreet(), ac.getHouseNumber(), ac.getPlz(), ac.getCity(), curDate, id_comment_EditText.getText().toString());
        Intent intent = new Intent(getApplicationContext(), OrderOverview.class);
        intent.putExtra("order", order);
        intent.putExtra("professor", prof);
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    private void disableOrderedDates(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().getActualMinimum(Calendar.HOUR_OF_DAY));
        long date = calendar.getTime().getTime();
        cv_calender.setMinDate(date);
    }

}