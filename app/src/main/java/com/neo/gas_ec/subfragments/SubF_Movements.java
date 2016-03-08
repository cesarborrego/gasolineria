package com.neo.gas_ec.subfragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.neo.gas_ec.R;
import com.neo.gas_ec.adapters.MovementsAdapter;
import com.neo.gas_ec.model.Movements;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cesar on 16/12/15.
 */
public class SubF_Movements extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    ArrayList<Movements> list;
    public static String pdfName = "";
    public static String pdfDir = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = getArguments().getParcelableArrayList("movementsList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movements, container, false);
        initElements(v);
        return v;
    }

    private void initElements(View v) {
        Log.d(null, "SIZE " + list.size());
        createRecycler(v);
        linearLayout = (LinearLayout) v.findViewById(R.id.cloudID);
        if (list.size() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void createRecycler(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerMovementsID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MovementsAdapter movementsAdapter = new MovementsAdapter(list, getActivity());
        recyclerView.setAdapter(movementsAdapter);
    }
}
