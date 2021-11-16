package classy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.Toast;

import com.example.a21q4_app_projekt.R;

import fontsmaterialuiux.cairoButton;
import fontsmaterialuiux.cairoEditText;
import fontsmaterialuiux.cairoTextView;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    cairoButton _signInButton;
    cairoEditText _emailEditText, _passwordEditText;
    cairoTextView _forgetPasswordTextView ,_signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        /////*     initialize view   */////
        _signInButton = findViewById(R.id.id_signIn_Button);
        _emailEditText = findViewById(R.id.id_email_EditText);
        _passwordEditText = findViewById(R.id.id_password_EditText);
        _forgetPasswordTextView = findViewById(R.id.id_forgetPassword_TextView);
        _signUpTextView = findViewById(R.id.id_SignUP_TextView);

        /////*     On Click         */////
        _signInButton.setOnClickListener(this);
        _forgetPasswordTextView.setOnClickListener(this);
        _signUpTextView.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if
        (v == _forgetPasswordTextView) {
            Toast.makeText(this, " please insert  forgot Password Activity", Toast.LENGTH_LONG).show();
        }

        if
        (v == _signInButton) {
            signinfunction();
        }

        if
        (v == _signUpTextView) {
            Intent i = new Intent(this, activity.StartActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        }
    }


    private void signinfunction() {
        /////*   Get  Email && Password    */////
        String _emailS = _emailEditText.getText().toString();
        String _passwordS = _passwordEditText.getText().toString();
        /////*   Check if email and password written  valid   */////
        if (!validate(_emailS, _passwordS)) {
            return;
        } else {
            Login(_emailS, _passwordS);
        }

    }

    public boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailEditText.setError(getString(R.string.validemail));
            valid = false;
        } else {
            _emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            _passwordEditText.setError(getString(R.string.validpassword));
            valid = false;
        } else {
            _passwordEditText.setError(null);
        }

        return valid;
    }

    private void Login(String email, String password) {
        Toast.makeText(this, "sign in : success" + email + " " + password, Toast.LENGTH_LONG).show();

    }
}
