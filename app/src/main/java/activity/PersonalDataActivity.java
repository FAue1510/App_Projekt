package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import com.example.a21q4_app_projekt.R;
import model.Account;

public class PersonalDataActivity extends Activity {

    private Account acc;
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        name = acc.getFirstName();
    }
}