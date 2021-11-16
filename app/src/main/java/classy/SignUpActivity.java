package classy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.a21q4_app_projekt.R;

import fontsmaterialuiux.cairoButton;
import fontsmaterialuiux.cairoEditText;
import fontsmaterialuiux.cairoTextView;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{

    cairoEditText _nameEditText,_emailEditText,_passwordEditText;
    cairoButton _signUpButton;
    cairoTextView _login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /////*     initialize view   */////
        _login = findViewById(R.id.id_login_TextView);
        _nameEditText = findViewById(R.id.id_fullName_EditText);
        _emailEditText = findViewById(R.id.id_email_EditText);
        _passwordEditText = findViewById(R.id.id_password_EditText);
        _signUpButton = findViewById(R.id.id_signUp_Button);



        /////*     On Click         */////
        _login.setOnClickListener(this);
        _signUpButton.setOnClickListener(this);


    }



    @Override
    public void onClick(View v)
    {
        if (v == _login)
        {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
        }
        if (v==_signUpButton)
        {
            signupfunction();
        }

    }



    private void signupfunction()
    {

        /////*   Get  Email  && Name  && Password    */////
        String fullName = _nameEditText.getText().toString();
        String email = _emailEditText.getText().toString();
        String password = _passwordEditText.getText().toString();

        /////*   Check if email and password written and valid   */////
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

        String fullName = _nameEditText.getText().toString();
        String email = _emailEditText.getText().toString();
        String password = _passwordEditText.getText().toString();

        if (fullName.isEmpty())
        {
            _nameEditText.setError(getString(R.string.enteryourname));
            valid = false;
        } else
        {
            _nameEditText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            _emailEditText.setError(getString(R.string.validemail));
            valid = false;
        } else
        {
            _emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 12)
        {
            _passwordEditText.setError(getString(R.string.validpassword));
            valid = false;
        } else
        {
            _passwordEditText.setError(null);
        }

        return valid;
    }
    private void signup(String fullName, String email, String password)
    {
        /////*   Sign Up : success  */////
        Intent intent = new Intent(this, AboutYouActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);

    }

}
