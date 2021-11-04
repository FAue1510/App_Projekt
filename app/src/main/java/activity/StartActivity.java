package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a21q4_app_projekt.R;
import database.TestDB;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends Activity {

    EditText edt_username;
    EditText edt_password;

    SharedPreferences prefs;

    FirebaseAuth Auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        prefs = this.getPreferences(Context.MODE_PRIVATE);

        //DELETE AFTER INSERTING SIGN OUT BUTTON
        prefs.edit().putBoolean("signedin", false).apply();
        //------------------------------------------

        edt_username = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        if(prefs.getBoolean("signedin", false)){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
    }

    public void login_click(View view) {
        Auth.signInWithEmailAndPassword(edt_username.toString(),edt_password.toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (Auth.getCurrentUser().isEmailVerified()){
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(StartActivity.this, "Bitte verifiziere zuerst deine Email-Adresse", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(StartActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signup_click(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);

    }
}