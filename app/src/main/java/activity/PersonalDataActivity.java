package activity;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import Utility.NetworkChangeListener;
import classy.CustomDatePicker.DatePicker;
import fontsUI.cairoEditText;
import model.Account;

public class PersonalDataActivity extends Activity {

    private Account acc = Account.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    cairoEditText id_first_name_EditText;
    cairoEditText id_last_name_EditText;
    cairoEditText id_street_EditText;
    cairoEditText id_housenumber_EditText;
    cairoEditText id_postal_code_EditText;
    cairoEditText id_city_EditText;
    DatePicker dayDatePicker, monthDatePicker, yearDatePicker;

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
        setContentView(R.layout.activity_personal_data);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        id_first_name_EditText = findViewById(R.id.id_first_name_EditText);
        id_last_name_EditText = findViewById(R.id.id_last_name_EditText);
        id_street_EditText = findViewById(R.id.id_street_EditText);
        id_housenumber_EditText = findViewById(R.id.id_housenumber_EditText);
        id_postal_code_EditText = findViewById(R.id.id_postal_code_EditText);
        id_city_EditText = findViewById(R.id.id_city_EditText);

        id_first_name_EditText.setText(acc.getFirstName());
        id_last_name_EditText.setText(acc.getLastName());
        id_street_EditText.setText(acc.getStreet());
        id_housenumber_EditText.setText(acc.getHouseNumber());
        id_postal_code_EditText.setText(acc.getPlz());
        id_city_EditText.setText(acc.getCity());

        dayDatePicker = (DatePicker) findViewById(R.id.id_day_DatePicker);
        monthDatePicker = (DatePicker) findViewById(R.id.id_month_DatePicker);
        yearDatePicker = (DatePicker) findViewById(R.id.id_year_DatePicker);

        dayDatePicker.setOffset(1);
        dayDatePicker.setSeletion(acc.getDay() - 1);
        dayDatePicker.setItems(Arrays.asList(getResources().getStringArray(R.array.days)));

        monthDatePicker.setOffset(1);
        monthDatePicker.setSeletion(acc.getMonth() - 1);
        monthDatePicker.setItems(Arrays.asList(getResources().getStringArray(R.array.months)));

        List<String> years = Arrays.asList(this.getResources().getStringArray(R.array.years));

        yearDatePicker.setOffset(1);
        yearDatePicker.setSeletion(years.indexOf(acc.getYear() + ""));
        yearDatePicker.setItems(Arrays.asList(getResources().getStringArray(R.array.years)));
    }

    public void save_onClick(View view) {

        acc.setFirstName(id_first_name_EditText.getText().toString());
        acc.setLastName(id_last_name_EditText.getText().toString());
        acc.setStreet(id_street_EditText.getText().toString());
        acc.setHouseNumber(id_housenumber_EditText.getText().toString());
        acc.setPlz(id_postal_code_EditText.getText().toString());
        acc.setCity(id_city_EditText.getText().toString());
        acc.setBirthday(dayDatePicker.getSeletedItem() + "." + monthDatePicker.getSeletedItem() + "." + yearDatePicker.getSeletedItem());


        DocumentReference userRef = db.collection("users").document(acc.getdocid());

        userRef.update(
                "firstName",id_first_name_EditText.getText().toString(),
                "lastName",id_last_name_EditText.getText().toString(),
                "street",id_street_EditText.getText().toString(),
                "houseNumber",id_housenumber_EditText.getText().toString(),
                "plz",id_postal_code_EditText.getText().toString(),
                "city",id_city_EditText.getText().toString(),
                "birthday",dayDatePicker.getSeletedItem() + "." + monthDatePicker.getSeletedItem() + "." + yearDatePicker.getSeletedItem()
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
                }
            }
        });
    }
}