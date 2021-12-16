package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.a21q4_app_projekt.R;

public class Splash extends Activity {
    private ProgressBar mProgressBar;
    private int progressStatus = 0;

    private Handler handler = new Handler();

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mProgressBar = (ProgressBar) findViewById (R.id.progress_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startloading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startloading();

    }

    private void startloading() {
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 4;
                    handler.post(new Runnable() {
                        public void run() {
                            mProgressBar.setProgress(progressStatus);
                            if (progressStatus == 100){
                                Intent i = new Intent(Splash.this, SignInActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                            }
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

}

