package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import Utility.NetworkChangeListener;
import fontsUI.cairoTextView;
import model.Account;
import model.Order;
import model.Professors;

public class OrderOverview extends Activity {

    SharedPreferences prefs;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Account ac = Account.getInstance();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Professors prof;
    Order order;

    cairoTextView id_name_TextView;
    cairoTextView id_street_housenumer_TextView;
    cairoTextView id_postalCode_city_TextView;
    cairoTextView id_birthday_TextView;
    cairoTextView id_email_TextView;
    cairoTextView id_prof_name_TextView;
    cairoTextView id_prof_address_TextView;
    cairoTextView id_prof_city_TextView;
    cairoTextView id_prof_birthday_TextView;
    cairoTextView id_prof_email_TextView;
    cairoTextView id_order_date_TextView;
    cairoTextView id_order_department_TextView;
    cairoTextView id_ordered_for_TextView;
    cairoTextView id_order_price_TextView;
    cairoTextView id_comment_TextView;

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
        setContentView(R.layout.activity_order_overview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        if(getIntent().getExtras() != null) {
            prof = (Professors) getIntent().getSerializableExtra("professor");
            order = (Order) getIntent().getSerializableExtra("order");
        }

        id_name_TextView = findViewById(R.id.id_name_TextView);
        id_street_housenumer_TextView = findViewById(R.id.id_street_housenumer_TextView);
        id_postalCode_city_TextView = findViewById(R.id.id_postalCode_city_TextView);
        id_birthday_TextView = findViewById(R.id.id_birthday_TextView);
        id_email_TextView = findViewById(R.id.id_email_TextView);
        id_prof_name_TextView = findViewById(R.id.id_prof_name_TextView);
        id_prof_address_TextView = findViewById(R.id.id_prof_address_TextView);
        id_prof_city_TextView = findViewById(R.id.id_prof_city_TextView);
        id_prof_birthday_TextView = findViewById(R.id.id_prof_birthday_TextView);
        id_prof_email_TextView = findViewById(R.id.id_prof_email_TextView);
        id_order_date_TextView = findViewById(R.id.id_order_date_TextView);
        id_order_department_TextView = findViewById(R.id.id_order_department_TextView);
        id_ordered_for_TextView = findViewById(R.id.id_ordered_for_TextView);
        id_order_price_TextView = findViewById(R.id.id_order_price_TextView);
        id_comment_TextView = findViewById(R.id.id_comment_TextView);

        id_name_TextView.setText(ac.getFirstName() + " " + ac.getLastName());
        id_street_housenumer_TextView.setText(ac.getStreet() + " " + ac.getHouseNumber());
        id_postalCode_city_TextView.setText(ac.getPlz() + " " + ac.getCity());
        id_birthday_TextView.setText(ac.getBirthday());
        id_email_TextView.setText(ac.getEmail());
        id_prof_name_TextView.setText(prof.getFirstName() + " " + prof.getLastName());
        id_prof_address_TextView.setText(prof.getStreet() + " " + prof.getHouseNumber());
        id_prof_city_TextView.setText(prof.getPlz() + " " + prof.getCity());
        id_prof_birthday_TextView.setText(prof.getCity());
        id_prof_email_TextView.setText(prof.getEmail());
        id_order_date_TextView.setText("Bestelldatum: " + order.getOrder_date());
        id_order_department_TextView.setText("Fachbereich: " + prefs.getString("selected_department",""));
        id_ordered_for_TextView.setText("Gemietet für den: " + order.getDate());
        id_order_price_TextView.setText("Gesamtpreis: 92€");
        id_comment_TextView.setText("Bemerkung: \n" + order.getDescription());
    }

    public void order_OnClick(View view) {
        db.collection("order").add(order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View layout_dialog = LayoutInflater.from(view.getContext()).inflate(R.layout.view_order_confirmed, null);
                builder.setView(layout_dialog);

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);

                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SmsManager m = SmsManager.getDefault();
                        String destination = "+" + order.getNumber();
                        String text = "Bestellbestätigung\n\nBestätigung vom Tutor:\nBestelldatum: " + order.getOrder_date() + "\nBestellt für den: " +  order.getDate() + "\nBestellter Tutor: " + prof.getFirstName() + " " + prof.getLastName();
                        m.sendTextMessage(destination, null, text, null, null);

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                }, 3000);
            }
        });
    }
}