package activity;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.a21q4_app_projekt.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import Utility.NetworkChangeListener;
import classy.CustomDatePicker.DatePicker;
import classy.CustomSwitch.customSwitch;
import fontsUI.cairoButton;
import fontsUI.cairoEditText;


public class AboutYouActivity extends Activity implements View.OnClickListener
{

    public static final int PICK_IMAGE = 1;
    DatePicker dayDatePicker, monthDatePicker, yearDatePicker;
    cairoEditText userNameEditText;
    cairoButton doneButton;
    Button uploadPictureButton;
    customSwitch genderCustomSwitch;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    String fullName;
    String email;
    String password;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(savedInstanceState == null) {
            Bundle extra = getIntent().getExtras();
            password = extra.getString("password");
            email = extra.getString("email");
            fullName = extra.getString("full_name");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i("foto_finished", "ActivityResult get in Method");
        if(resultCode==RESULT_OK)
        {
            Uri selectedimg = data.getData();
            try { //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedimg);
                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                File myDir = new File(root + "/saved_images");
                myDir.mkdirs();

                String fname = "profile.jpg";
                File file = new File (myDir, fname);
                if (file.exists ()) file.delete ();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Log.i("foto_finished", "FOTO FINISHED");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void complete()
    {
        /////*   Get  Email && Password    */////
        String gender = "";

        String username = userNameEditText.getText().toString();
        String getGender = genderCustomSwitch.getChecked().toString();
        String day = dayDatePicker.getSeletedItem();
        String month = monthDatePicker.getSeletedItem();
        String year = yearDatePicker.getSeletedItem();
        String dateofbirth = day + "." + month + "." + year ;
        if (getGender.equals("LEFT")){
            gender = "Frau";
        }else {
            gender = "Mann";
        }

        /////*   Check if username ,gender and date of birth  are entered     */////
        if (!validate(username, gender,dateofbirth)) {
            return;
        } else {
            switchToAboutYou2(username, gender,dateofbirth, fullName, email, password);
        }
    }

    private void switchToAboutYou2(String username, String gender, String dateofbirth, String fullName, String email, String password)
    {
        Intent intent = new Intent(getApplicationContext(), AboutYouActivity2.class);
        intent.putExtra("email", email);
        intent.putExtra("full_name", fullName);
        intent.putExtra("password", password);
        intent.putExtra("username", username);
        intent.putExtra("gender", gender);
        intent.putExtra("dateofbirth", dateofbirth);
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
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
            Toast.makeText(this, "Bitte Geschlecht auswählen", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (dateofbirth.equals(""))
        {
            Toast.makeText(this, "Bitte Geburtsdatum auswählen", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

}
