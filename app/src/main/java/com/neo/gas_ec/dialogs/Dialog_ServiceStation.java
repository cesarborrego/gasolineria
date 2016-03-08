package com.neo.gas_ec.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.neo.gas_ec.R;

/**
 * Created by cesar on 17/12/15.
 */
public class Dialog_ServiceStation extends DialogFragment {
    private Button closeDialog;

    public static Dialog_ServiceStation newInstance() {
        Dialog_ServiceStation frag = new Dialog_ServiceStation();
//        Bundle args = new Bundle();
//        args.putInt("title", title);
//        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_service_station, container, false);
        initElements(v);
        return v;
    }

    private void initElements(View v) {
        closeDialog = (Button)v.findViewById(R.id.closeServiceDialogBtnID);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        builder.setView(inflater.inflate(R.layout.dialog_service_station, null));
//
//        return builder.create();
//    }
}
