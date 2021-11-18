package activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.a21q4_app_projekt.R;

import android.os.Bundle;

import fontsUI.cairoTextView;
import model.ProfManager;
import model.Professors;

public class ProfActivity extends AppCompatActivity {

    private cairoTextView id_Subject_TextView, id_Name_TextView, id_Birth_TextView, id_Str_TextView, id_Ort_TextView, id_Email_TextView;
    private ProfManager manager;
    private Professors prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        manager = ProfManager.getInstance();

        id_Name_TextView = findViewById(R.id.id_Name_TextView);
        id_Subject_TextView = findViewById(R.id.id_Subject_TextView);
        id_Str_TextView = findViewById(R.id.id_Str_TextView);
        id_Ort_TextView = findViewById(R.id.id_OrtPLZ_TextView);
        id_Birth_TextView = findViewById(R.id.id_Birth_TextView);
        id_Email_TextView = findViewById(R.id.id_Email_TextView);

        prof = manager.getProf(getIntent().getStringExtra("id"));

        id_Name_TextView.setText(prof.getFirstName() + " " + prof.getLastName());
        id_Subject_TextView.setText(prof.getSubject());
        id_Str_TextView.setText(prof.getStreet() + " " + prof.getHouseNumber());
        id_Ort_TextView.setText(prof.getPlz() + " " + prof.getCity());
        id_Birth_TextView.setText(prof.getBirthday());
        id_Email_TextView.setText(prof.getEmail());
    }
}