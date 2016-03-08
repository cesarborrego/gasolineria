package com.neo.gas_ec.subfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neo.gas_ec.R;
import com.neo.gas_ec.adapters.TAG_adapter;
import com.neo.gas_ec.model.DataVehicle;
import com.neo.gas_ec.model.Data_test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by cesar on 15/12/15.
 */
public class SubF_GeneralInfo extends Fragment {

    public SubF_GeneralInfo() {
    }

    RecyclerView recyclerView;

    int[] titleNewTagCard = {
            R.string.placa,
            R.string.marca,
            R.string.subMarca,
            R.string.modelo,
            R.string.vin};

    DataVehicle dataVehicle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gral_info, container, false);
        String data = getArguments().getString("data");
        dataVehicle = getArguments().getParcelable("dataVehicle");

        if (data != null) {
            initElements(v);
            createNewCards();
        }

        return v;
    }

    private void initElements(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerGeneralID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void createNewCards() {
        ArrayList<Data_test> data_testList = new ArrayList<Data_test>();
        Data_test data_test = new Data_test(getResources().getString(titleNewTagCard[0]), dataVehicle.getPlaca(), 0);
        data_testList.add(data_test);

        Data_test data_test1 = new Data_test(getResources().getString(titleNewTagCard[1]), dataVehicle.getMarca(), 0);
        data_testList.add(data_test1);

        Data_test data_test2 = new Data_test(getResources().getString(titleNewTagCard[2]), dataVehicle.getSubMarca(), 0);
        data_testList.add(data_test2);

        Data_test data_test3 = new Data_test(getResources().getString(titleNewTagCard[3]), dataVehicle.getAnioModelo(), 0);
        data_testList.add(data_test3);

        Data_test data_test4 = new Data_test(getResources().getString(titleNewTagCard[4]), dataVehicle.getVin(), 0);
        data_testList.add(data_test4);

        TAG_adapter tag_adapter = new TAG_adapter(data_testList, getActivity());
        recyclerView.setAdapter(tag_adapter);
    }
}