package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import Utility.NetworkChangeListener;
import fontsUI.cairoButton;
import fontsUI.cairoEditText;
import model.Account;

public class AboutYouActivity2 extends Activity implements View.OnClickListener {

    Account ac = Account.getInstance();
    
    FirebaseAuth Auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    cairoEditText edt_street, edt_housenumber, edt_postalCode, edt_city;
    cairoButton btn_finishButton;
    String fullName;
    String email;
    String password;
    String username;
    String dateofbirth;
    String gender;

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
        setContentView(R.layout.activity_about_you2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            password = extra.getString("password");
            email = extra.getString("email");
            fullName = extra.getString("full_name");
            username = extra.getString("username");
            dateofbirth = extra.getString("dateofbirth");
            gender = extra.getString("gender");

            edt_street = findViewById(R.id.id_street_EditText);
            edt_housenumber = findViewById(R.id.id_housenumber_EditText);
            edt_postalCode = findViewById(R.id.id_postalCode_EditText);
            edt_city = findViewById(R.id.id_city_EditText);
            btn_finishButton = findViewById(R.id.id_signUp_final_Button);

            btn_finishButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == btn_finishButton)
        {
            complete();
        }
    }

    private void complete()
    {
        String gender = "";

        String addressS = edt_street.getText().toString();
        String housenumberS = edt_housenumber.getText().toString();
        String postalCodeS = edt_postalCode.getText().toString();
        String cityS = edt_city.getText().toString();
        
        signUp(addressS, housenumberS, postalCodeS, cityS);
    }

    private void signUp(String street, String housenumber, String postalCode, String city){
        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                ac.setAccount(
                                        Auth.getCurrentUser().getUid(),
                                        username,
                                        email,
                                        fullName.split(" ")[0],
                                        fullName.split(" ")[1],
                                        dateofbirth,
                                        city,
                                        street,
                                        housenumber,
                                        postalCode);
                                db.collection("users").add(ac).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

}