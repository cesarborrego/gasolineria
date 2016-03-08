package com.neo.gas_ec;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.neo.gas_ec.utils.Constants_Settings;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    private CountDownTimer mCountDownTimer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
                Log.w(TAG, "Splash Activity is not the root.  Finishing Splash Activity instead of launching.");
                finish();
                return;
            }
        }

        sharedPreferences = getApplicationContext().getSharedPreferences(Constants_Settings.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (!sharedPreferences.contains(Constants_Settings.KEY_URL)) {
            editor.putString(Constants_Settings.KEY_URL, Constants_Settings.SHARED_PREF_URL);
            editor.putString(Constants_Settings.KEY_USR, Constants_Settings.SHARED_PREF_USR);
            editor.putString(Constants_Settings.KEY_PWD, Constants_Settings.SHARED_PREF_PWD);
            editor.putInt(Constants_Settings.KEY_LANGUAGE, Constants_Settings.SHARED_PREF_LANGUAGE);
            editor.commit();
        }

        mCountDownTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, ReadTagActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.animation, 0);
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        mCountDownTimer.cancel();
        super.onStop();
    }
}
