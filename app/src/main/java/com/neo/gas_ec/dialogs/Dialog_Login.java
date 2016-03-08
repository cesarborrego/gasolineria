package com.neo.gas_ec.dialogs;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.neo.gas_ec.DataActivity;
import com.neo.gas_ec.R;

/**
 * Created by cesar on 12/22/15.
 */
public class Dialog_Login extends DialogFragment {

    RelativeLayout fondLayout;
    ImageView btnAceptar;
    ImageView btnCancelar;
    EditText ruc;

    public static Dialog_Login newInstance() {
        Dialog_Login dialog_login = new Dialog_Login();
        return dialog_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_login, container, false);
//        fondLayout = (RelativeLayout) v.findViewById(R.id.fondoID);
//        fondLayout.setBackgroundResource(R.drawable.back_gris);
//        fondLayout.getBackground().setAlpha(182);
        ruc = (EditText)v.findViewById(R.id._rucTxt);
        btnAceptar = (ImageView)v.findViewById(R.id.aceptarID);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ruc.getText().toString().length()>0) {
                    ((DataActivity)getActivity()).doPositiveClick(ruc.getText().toString());
                } else {
                    Toast.makeText(getContext(), "Ingresar RUC", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancelar = (ImageView)v.findViewById(R.id.cancelarID);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DataActivity)getActivity()).doNegativeClick();
            }
        });
        return v;
    }
}
