package com.neo.gas_ec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.neo.gas_ec.dialogs.Dialog_Settings;
import com.neo.gas_ec.utils.Constants_Settings;
import com.neo.gas_ec.utils.Preferences;
import com.neology.ecunfc.NFC;
import com.neology.ecunfc.NFC_Listener;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class ReadTagActivity extends AppCompatActivity implements NFC_Listener {

    SharedPreferences sharedPreferences;

    public static final String TAG = ReadTagActivity.class.getSimpleName();
    private NFC _nfc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_tag);

        sharedPreferences = getApplicationContext().getSharedPreferences(Constants_Settings.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        _nfc = new NFC();
        _nfc.Initialize(new WeakReference<Activity>(this), this);
        _nfc.modoLectura();

        changeLocation();
        loadWebView();
    }

    private void changeLocation() {
        Configuration configuration = new Configuration();
        int i = sharedPreferences.getInt(Constants_Settings.KEY_LANGUAGE, 0);
        switch (i) {
            case 1:
                configuration.locale = Locale.ENGLISH;
                break;
            case 2:
                configuration.locale = Locale.getDefault();
                break;
        }
        getResources().updateConfiguration(configuration, null);
    }

    private void loadWebView() {
        WebView wv = (WebView) findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/tap.html");
    }


    @Override
    public void Event_NFC(final String s) {
        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {

            @Override
            public void run() {
                writeToTagTV(s);
            }
        });
    }

    @Override
    public void Event_Log_NFC(String s) {

    }

    private void writeToTagTV(String data) {
        if (data.startsWith("0")) {
            Intent intent = new Intent(ReadTagActivity.this, DataActivity.class);
            intent.putExtra("dataTag", data);
            startActivity(intent);
            finish();

        } else if (data.startsWith("2")) {
        } else {
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            Dialog_Settings.showSettingsDialog(getApplicationContext(), ReadTagActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.d(TAG, "onResume()");
        _nfc.onResumeProcess();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.d(TAG, "onPause()");
        _nfc.onPauseProcess();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent()");
        _nfc.onNewIntentProcess(intent);
    }
}
