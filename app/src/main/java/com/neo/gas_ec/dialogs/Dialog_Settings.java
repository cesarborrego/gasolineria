package com.neo.gas_ec.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.neo.gas_ec.DataActivity;
import com.neo.gas_ec.R;
import com.neo.gas_ec.utils.Constants_Settings;

/**
 * Created by cesar on 9/24/15.
 */
public class Dialog_Settings extends DialogFragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    RadioButton enBtn;
    RadioButton esBtn;

    TextInputLayout textUrl;
    TextInputLayout textUsr;
    TextInputLayout textPwd;

    EditText ip;
    EditText usr;
    EditText psw;

    Button ok;
    Button cancel;

    public static Dialog_Settings newInstance() {
        Dialog_Settings dialog_settings = new Dialog_Settings();
        return dialog_settings;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Constants_Settings.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_dialog, container, false);
        initElements(v);
        return v;
    }

    private void initElements(View v) {
        enBtn = (RadioButton) v.findViewById(R.id.englishLangID);
        esBtn = (RadioButton) v.findViewById(R.id.spanishLangID);

        if (sharedPreferences.contains(Constants_Settings.KEY_LANGUAGE)) {
            int i = sharedPreferences.getInt(Constants_Settings.KEY_LANGUAGE, 0);
            if (i == 1) {
                enBtn.setChecked(true);
            } else if (i == 2) {
                esBtn.setChecked(true);
            }
        }

        textUrl = (TextInputLayout) v.findViewById(R.id.textInputAddressID);
        textUsr = (TextInputLayout) v.findViewById(R.id.textInputAddressID);
        textPwd = (TextInputLayout) v.findViewById(R.id.textInputAddressID);

        ip = (EditText) v.findViewById(R.id.ipAddress);
        usr = (EditText) v.findViewById(R.id.usr);
        psw = (EditText) v.findViewById(R.id.psw);

        ip.addTextChangedListener(new MyTextWatcher(ip));
        usr.addTextChangedListener(new MyTextWatcher(usr));
        psw.addTextChangedListener(new MyTextWatcher(psw));

        ok = (Button) v.findViewById(R.id.okSettingsBtnId);
        okAction();
        cancel = (Button) v.findViewById(R.id.cancelSettingsBtnId);
        cancelSettings();
    }

    private void okAction() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ip != null && ip.getText() != null && !ip.getText().toString().isEmpty() &&
                        usr != null && usr.getText() != null && !usr.getText().toString().isEmpty() &&
                        psw != null && psw.getText() != null && !psw.getText().toString().isEmpty()) {
                    if (usr.getText().toString().equals("NEOLOGY") &&
                            psw.getText().toString().equals("NeologyNFC")) {
                        if (enBtn.isChecked()) {
                            editor.putInt(Constants_Settings.KEY_LANGUAGE, 1);
                            editor.commit();
                        } else if (esBtn.isChecked()) {
                            editor.putInt(Constants_Settings.KEY_LANGUAGE, 2);
                            editor.commit();
                        }
                        editor.putString(Constants_Settings.KEY_URL, ip.getText().toString());
                        editor.commit();
                        Toast.makeText(getContext(), getString(R.string.settings_set), Toast.LENGTH_SHORT).show();
                        dismiss();
                        ((DataActivity) getActivity()).ok();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.validateUsrPwd), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.all_fields_req), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void cancelSettings() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    static class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.ipAddress:
//                    validateName();
                    break;
                case R.id.usr:
//                    validateEmail();
                    break;
                case R.id.psw:
//                    validatePassword();
                    break;
            }
        }
    }
}
