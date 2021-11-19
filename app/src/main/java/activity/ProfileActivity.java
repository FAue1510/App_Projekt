package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.a21q4_app_projekt.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void switchToSettings(View view) {
        //Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        //startActivity(intent);
    }
}