package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a21q4_app_projekt.R;

public class SignUpActivity123 extends Activity {

    EditText edt_password;
    EditText edt_password_proof;
    EditText edt_email;
    EditText edt_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_old);

        edt_password = findViewById(R.id.edt_password_signup);
        edt_password_proof = findViewById(R.id.edt_password_proof);
        edt_email = findViewById(R.id.edt_email_signup);
        edt_username = findViewById(R.id.edt_username);
    }

    public void continue_click(View view) {
        if (edt_password.getText().toString().equals(edt_password_proof.getText().toString())) {
            Intent intent = new Intent(SignUpActivity123.this, SignUpActivity_2.class);
            intent.putExtra("email", edt_email.getText().toString());
            intent.putExtra("username", edt_username.getText().toString());
            intent.putExtra("password", edt_password.getText().toString());
            startActivity(intent);
        }else{
            Toast.makeText(SignUpActivity123.this, "Passwort stimmt nicht überein", Toast.LENGTH_LONG).show();
        }
    }
}