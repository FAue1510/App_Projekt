package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.a21q4_app_projekt.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends Activity {

    FirebaseAuth Auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void switchToPersonalData(View view) {
        Intent intent = new Intent(getApplicationContext(), PersonalDataActivity.class);
        startActivity(intent);
    }

    public void switchToOrder(View view) {
        Intent intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
        startActivity(intent);
    }

    public void switchToSettings(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void logout_OnClick(View view) {
        Auth.signOut();
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }
}