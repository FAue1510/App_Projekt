package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import fontsUI.cairoButton;
import fontsUI.cairoEditText;
import fontsUI.cairoTextView;
import model.Account;


public class SignInActivity extends Activity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    cairoButton signInButton;
    cairoEditText emailEditText, passwordEditText;
    cairoTextView forgetPasswordTextView, signUpTextView;

    SharedPreferences prefs;

    FirebaseAuth Auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Account ac = Account.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        prefs = this.getPreferences(Context.MODE_PRIVATE);
        prefs.edit().putBoolean("signedin", false).commit();

        //References
        signInButton = findViewById(R.id.id_signIn_Button);
        emailEditText = findViewById(R.id.id_email_EditText);
        passwordEditText = findViewById(R.id.id_password_EditText);
        forgetPasswordTextView = findViewById(R.id.id_forgetPassword_TextView);
        signUpTextView = findViewById(R.id.id_SignUP_TextView);

        //On Click Listener
        signInButton.setOnClickListener(this);
        forgetPasswordTextView.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == forgetPasswordTextView) {
            Toast.makeText(this, " Coming Soon", Toast.LENGTH_LONG).show();
        }

        if(v == signInButton) {
            signinfunction();
        }

        if(v == signUpTextView) {
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        }
    }


    private void signinfunction() {
        /////*   Get  Email && Password    */////
        String emailS = emailEditText.getText().toString();
        String passwordS = passwordEditText.getText().toString();
        /////*   Check if email and password written  valid   */////
        if (!validate(emailS, passwordS)) {
            return;
        } else {
            Auth.signInWithEmailAndPassword(emailS,passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        if (Auth.getCurrentUser().isEmailVerified()){
                            Login();
                        }else{
                            Toast.makeText(getApplicationContext(), "Bitte verifiziere zuerst deine Email-Adresse", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.validemail));
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.validpassword));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }

    private void Login() {
        readData(db.collection("users").whereEqualTo("userUID",Auth.getCurrentUser().getUid()));
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    private void readData(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ac.setAccount(
                                document.get("userUID").toString(),
                                document.get("username").toString(),
                                document.get("email").toString(),
                                document.get("firstName").toString(),
                                document.get("lastName").toString(),
                                document.get("birthday").toString(),
                                document.get("city").toString(),
                                document.get("street").toString(),
                                document.get("houseNumber").toString(),
                                document.get("plz").toString()
                        );
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error getting documents: ", Toast.LENGTH_LONG);
                }
            }
        });
    }
}
