package com.neo.gas_ec;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.neo.gas_ec.adapters.TAG_adapter;
import com.neo.gas_ec.dialogs.Dialog_Login;
import com.neo.gas_ec.dialogs.Dialog_Settings;
import com.neo.gas_ec.model.DataVehicle;
import com.neo.gas_ec.model.Data_test;
import com.neo.gas_ec.model.GallonsConsumed;
import com.neo.gas_ec.model.Movements;
import com.neo.gas_ec.utils.Constants_Settings;
import com.neo.gas_ec.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DataActivity extends AppCompatActivity {
    public static final String TAG = DataActivity.class.getSimpleName();

    SharedPreferences sharedPreferences;
    public String URL_MOVEMENTS;
    public String URL_VEHICLE_TAGID;

    private RecyclerView recyclerView;
    private FloatingActionButton searchBtn;
    private FloatingActionButton settingsBtn;
    private String data = "";
    private String ruc = "";
    private String placa = "";
    public static String plateNumber = "";
    ArrayList<Movements> movementsList = new ArrayList<Movements>();
    GallonsConsumed gallonsConsumed;
    DataVehicle dataVehicle;
    private int[] titleTagCard = {
            R.string.uidLbl,
            R.string.folioTagLbl,
            R.string.subsidyLbl,
            R.string.numberGallonsLbl,
            R.string.plateNumberLbl};

    boolean result = false;
    double gallonsPerMonth = 0;

    private TextView placaTxt;
    private TextView folioTxt;

    DialogFragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        sharedPreferences = getApplicationContext().getSharedPreferences(Constants_Settings.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        URL_MOVEMENTS = "http://" + sharedPreferences.getString(Constants_Settings.KEY_URL, null) + "/rest/suministros/movements/tag/";
        URL_VEHICLE_TAGID = "http://" + sharedPreferences.getString(Constants_Settings.KEY_URL, null) + "/rest/vehicles/vehicle/tag/";

        Intent intent = getIntent();
        data = intent.getStringExtra("dataTag");

        initElements();
        createCards(data);
    }

    private void initElements() {
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerID);
//        recyclerView.setHasFixedSize(true);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);

        placaTxt = (TextView) findViewById(R.id.plateID);
        folioTxt = (TextView) findViewById(R.id.folioID);

        searchBtn = (FloatingActionButton) findViewById(R.id.searchBtnID);
        searchDataOnline();

        settingsBtn = (FloatingActionButton) findViewById(R.id.settingsBtnID);
        settings();
    }

    private void createCards(String data) {
        String[] dataSplited = data.split(Pattern.quote("|"));
        List<Data_test> data_testList = new ArrayList<Data_test>();

        int x = 0;
        for (int i = 0; i < dataSplited.length; i++) {
            if (i == 2 || i == 3 || i == 4 || i == 5 || i == 6) {
                Data_test data_test = new Data_test();
                data_test.setTagTitle(getResources().getString(titleTagCard[x]));
                x++;
                data_test.setTagData(dataSplited[i].toString());
                data_testList.add(data_test);
                gallonsPerMonth = Double.parseDouble(dataSplited[5].toString());
            }
        }

        placa = dataSplited[6].toString();
        placaTxt.setText(placa);
        folioTxt.setText(dataSplited[3].toString());
        TAG_adapter tag_adapter = new TAG_adapter(data_testList, DataActivity.this);
//        recyclerView.setAdapter(tag_adapter);
    }

    //Call WS
    private void searchDataOnline() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_Login();
            }
        });
    }

    private void settings() {
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_Settings();
            }
        });
    }

    void showDialog_Login() {
        newFragment = Dialog_Login.newInstance();
//        newFragment.setCanceledOnTouchOutside(false);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick(String ruc) {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
        String[] dataSplited = data.split(Pattern.quote("|"));
        Log.i(null, "URL Vehicle " + URL_VEHICLE_TAGID);
        callREST_Vehicle_TAGID(URL_VEHICLE_TAGID + dataSplited[2], ruc, dataSplited[6]);
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
        newFragment.dismiss();
    }

    private void showDialog_Settings() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = Dialog_Settings.newInstance();
        newFragment.show(ft, "dialog");
    }

    private void callREST_Vehicle_TAGID(String url, final String mRuc, final String mPlate) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        readJson_Vehicle_TAGID(response.toString(), mRuc, mPlate);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
//                        getString(R.string.errorHost)+" "+sharedPreferences.getString(Constants_Settings.KEY_URL, null)
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Auth();
            }

        };
        VolleyApp.getmInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void readJson_Vehicle_TAGID(String response, String _ruc, String _plate) {
        JsonVehicleTag jsonVehcleTag = new JsonVehicleTag();
        jsonVehcleTag.execute(response, _ruc, _plate);
    }

    class JsonVehicleTag extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject(params[0]);
                jsonObject.getString("status");
                ruc = jsonObject.getString("ruc");
                plateNumber = jsonObject.getString("plate");

                Log.d(TAG, "ruc WS " + ruc + "----plate WS " + plateNumber + "----");
                Log.d(TAG, "ruc Usr " + params[1] + "----plate Usr " + params[2] + "----");
                //Quitamos la placa de la validación
                if (ruc.equals(params[1])) {
                    result = true;
                    dataVehicle = new DataVehicle(
                            jsonObject.getString("plate"),
                            jsonObject.getString("vehicleBrand"),
                            jsonObject.getString("vehicleSubBrand"),
                            String.valueOf(jsonObject.getInt("vehicleModel")),
                            jsonObject.getString("vin")
                    );
                } else {
                    result = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Toast.makeText(getApplicationContext(), getString(R.string.correctData), Toast.LENGTH_SHORT).show();
                String[] dataSplited = data.split(Pattern.quote("|"));
                callRestMovements(URL_MOVEMENTS + dataSplited[2]);
                if(newFragment.isVisible()) {
                    newFragment.dismiss();
                }

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.errorData), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callRestMovements(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        readJson_Movements(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Auth();
            }

        };

        VolleyApp.getmInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void readJson_Movements(String response) {
        JsonMovements jsonMovements = new JsonMovements();
        jsonMovements.execute(response);
    }

    class JsonMovements extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject(params[0]);
                gallonsConsumed = new GallonsConsumed(
                        jsonObject.getString("customerRuc"),
                        jsonObject.getDouble("litersToConsume"),
                        jsonObject.getDouble("litersConsumedWithSubsidy"),
                        jsonObject.getDouble("litersConsumedWithoutSubsidy")
                );
                JSONArray jsonArray = jsonObject.getJSONArray("movements");
                movementsList = new ArrayList<Movements>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dataJson = jsonArray.getJSONObject(i);
                    Movements movements = new Movements(
                            dataJson.getInt("pumpId"),
                            dataJson.getBoolean("subsidy"),
                            dataJson.getDouble("litersSupplied"),
                            dataJson.getString("fuelType"),
                            dataJson.getDouble("pricePerLiter"),
                            dataJson.getString("stationRuc"),
                            dataJson.getDouble("taxPercentage"),
                            dataJson.getLong("datetime"),
                            dataJson.getDouble("totalPaid"),
//                            "http://uroki-guitary.ru/sites/default/files/files/Tears_In_Heaven.pdf"
//                            "001-001-01001");
                    dataJson.getString("folio"),
                            "http://"+sharedPreferences.getString(Constants_Settings.KEY_URL, null)+"/rest/fuelLoads/fuelLoad/bill/",
                            dataJson.getString("folio")+"_"+ DateUtil.dateToString(new Date(dataJson.getLong("datetime")), "dd_MM_yyyy_HH:mm")+".pdf");
                    movementsList.add(movements);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("infoTag", data);
                intent.putExtra("dataVehicle", dataVehicle);
                intent.putParcelableArrayListExtra("movements", movementsList);
                intent.putExtra("gallonsConsumed", gallonsConsumed);
                startActivity(intent);
                finish();
            }
        }
    }

    private HashMap Auth() {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s", "demo", "Ne0l0gy");
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }

    public void ok() {
//        Intent i = getPackageManager().getLaunchIntentForPackage( getPackageName() );
        Intent i = new Intent(getApplicationContext(), SplashActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
