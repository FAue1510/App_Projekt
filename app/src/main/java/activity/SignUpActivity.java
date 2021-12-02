package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.a21q4_app_projekt.R;

import Utility.NetworkChangeListener;
import fontsUI.cairoButton;
import fontsUI.cairoEditText;
import fontsUI.cairoTextView;


public class SignUpActivity extends Activity implements View.OnClickListener
{

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    cairoEditText nameEditText, emailEditText, passwordEditText;
    cairoButton signUpButton;
    cairoTextView login;

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
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login = findViewById(R.id.id_login_TextView);
        nameEditText = findViewById(R.id.id_fullName_EditText);
        emailEditText = findViewById(R.id.id_email_EditText);
        passwordEditText = findViewById(R.id.id_password_EditText);
        signUpButton = findViewById(R.id.id_signUp_Button);

        login.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == login)
        {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
        }
        if (v== signUpButton)
        {
            signupfunction();
        }
    }



    private void signupfunction()
    {
        String fullName = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!validate())
        {
            return;
        }else
        {
            signup(fullName,email,password);

        }
    }


    public boolean validate()
    {
        boolean valid = true;

        String fullName = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (fullName.isEmpty())
        {
            nameEditText.setError(getString(R.string.enteryourname));
            valid = false;
        } else
        {
            nameEditText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError(getString(R.string.validemail));
            valid = false;
        } else
        {
            emailEditText.setError(null);
        }

        if (password.isEmpty())
        {
            passwordEditText.setError(getString(R.string.validpassword));
            valid = false;
        } else
        {
            passwordEditText.setError(null);
        }

        return valid;
    }
    private void signup(String fullName, String email, String password)
    {
        Intent intent = new Intent(this, AboutYouActivity.class);
        intent.putExtra("email", emailEditText.getText().toString());
        intent.putExtra("full_name", nameEditText.getText().toString());
        intent.putExtra("password", passwordEditText.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);

    }

}
