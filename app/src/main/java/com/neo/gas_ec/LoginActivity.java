package com.neo.gas_ec;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.jar.Manifest;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usrTxtLayout, pwdTxtLayout;
    private EditText usr, pwd;
    private CardView btnLogin;
    private RelativeLayout btnRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        initElementsUI();
    }

    private void initElementsUI() {
//        usrTxtLayout = (TextInputLayout) findViewById(R.id.textInputUserNameID);
//        pwdTxtLayout = (TextInputLayout) findViewById(R.id.textInputPassID);
//        usr = (EditText) findViewById(R.id.rucID);
//        pwd = (EditText) findViewById(R.id.passID);
//        btnLogin = (CardView) findViewById(R.id.btnLoginID);
//        btnRelative = (RelativeLayout) findViewById(R.id.relativeBtnID);
//        login();
    }

    private void login() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usr.getText().toString().equals("NEO") && pwd.getText().toString().equals("NEO")) {
                    if(btnRelative.isClickable()) {
                        btnRelative.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                    Intent i = new Intent(LoginActivity.this, ReadTagActivity.class);
                    startActivity(i);
                    usr.setText("");
                    pwd.setText("");
                }
            }
        });
    }
}
