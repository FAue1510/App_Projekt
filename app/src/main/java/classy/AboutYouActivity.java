package classy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.a21q4_app_projekt.R;

import java.util.Arrays;

import classy.CustomDatePicker.DatePicker;
import classy.CustomSwitch.customSwitch;
import fontsUI.cairoButton;
import fontsUI.cairoEditText;
import model.Account;


public class AboutYouActivity extends Activity implements View.OnClickListener
{

    public static final int PICK_IMAGE = 1;
    DatePicker dayDatePicker, monthDatePicker, yearDatePicker;
    cairoEditText userNameEditText;
    cairoButton doneButton;
    Button uploadPictureButton;
    customSwitch genderCustomSwitch;

    Account ac = Account.getInstance();

    String fullName;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);

        if(savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            password = extra.getString("password");
            email = extra.getString("email");
            fullName = extra.getString("username");
        }

        /////*     initialize view   */////
        uploadPictureButton = findViewById(R.id.id_uploadPicture_Button);
        userNameEditText = findViewById(R.id.id_userName_EditText);
        dayDatePicker = (DatePicker) findViewById(R.id.id_day_DatePicker);
        monthDatePicker = (DatePicker) findViewById(R.id.id_month_DatePicker);
        yearDatePicker = (DatePicker) findViewById(R.id.id_year_DatePicker);
        genderCustomSwitch = findViewById(R.id.id_gender_customSwitch);
        doneButton = findViewById(R.id.id_done_Button);

        /////*    add days , month and year         */////
        dayDatePicker.setOffset(1);
        dayDatePicker.setSeletion(5);
        dayDatePicker.setItems(Arrays.asList(getResources().getStringArray(R.array.days)));

        monthDatePicker.setOffset(1);
        monthDatePicker.setSeletion(1);
        monthDatePicker.setItems(Arrays.asList(getResources().getStringArray(R.array.months)));

        yearDatePicker.setOffset(1);
        yearDatePicker.setSeletion(95);
        yearDatePicker.setItems(Arrays.asList(getResources().getStringArray(R.array.years)));

        /////*     On Click         */////
        doneButton.setOnClickListener(this);
        uploadPictureButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v)
    {
        if (v == doneButton)
        {
            complete();
        }
        if (v== uploadPictureButton)
        {
            pickImage();
        }
    }

    private void pickImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Foto auswählen"), PICK_IMAGE);
    }

    private void complete()
    {
        /////*   Get  Email && Password    */////
        String _gender = "";

        String _username = userNameEditText.getText().toString();
        String getGender = genderCustomSwitch.getChecked().toString();
        String _day = dayDatePicker.getSeletedItem();
        String _month = monthDatePicker.getSeletedItem();
        String _year = yearDatePicker.getSeletedItem();
        String dateofbirth = _day + " " + _month + " " + _year ;
        if (getGender.equals("LEFT")){
            _gender = "female";
        }else {
            _gender = "male";
        }

        /////*   Check if username ,gender and date of birth  are entered     */////
        if (!validate(_username, _gender,dateofbirth)) {
            return;
        } else {
            Login(_username, _gender,dateofbirth);
        }
    }

    private void Login(String username, String gender, String dateofbirth)
    {
        Toast.makeText(this, "success" + username + " " + gender + " " +dateofbirth, Toast.LENGTH_LONG).show();
    }


    public boolean validate(String username, String gender, String dateofbirth)
    {
        boolean valid = true;

        if (username.isEmpty())
        {
            userNameEditText.setError(getString(R.string.validusername));
            valid = false;
        }
        if (gender.isEmpty())
        {
            Toast.makeText(this, "please select your gender", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (dateofbirth.equals(""))
        {
            Toast.makeText(this, "please select your gender", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
