package com.neo.gas_ec.subfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.neo.gas_ec.R;
import com.neo.gas_ec.model.GallonsConsumed;

import org.w3c.dom.Text;

/**
 * Created by cesar on 16/12/15.
 */
public class SubF_FuelConsumed extends Fragment {

    private GallonsConsumed gallonsConsumed;
    //con subsidio
    private TextView galonesConsumidos, galonesDisponiblesTxt;
    //sin subsidio
    private TextView galonesConsumidosSINSubsidio;
    //total de galones
    private TextView totalGalones;

    private TextView availablePercentage;
    private ImageView statePumpID;
    private ProgressBar progressBar;

    private static int totalPercent = 100;
    private static double totalGallons = 0.00;
    private static int availablePercent = 0;
    private static int availableGallons = 0;
    private static double consumedGallonsConSubsidio = 0.00;
    private static double consumedGallonsSINSubsidio = 0.00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2, container, false);
        gallonsConsumed = getArguments().getParcelable("gallonsConsumed");
//        jsonObject.getString("customerRuc"),
//                jsonObject.getInt("litersToConsume"),
//                jsonObject.getInt("litersConsumedWithSubsidy"),
//                jsonObject.getInt("litersConsumedWithoutSubsidy")
        Log.d(null, gallonsConsumed.getLitersToConsume()+"");
        Log.d(null, gallonsConsumed.getLitersConsumedWithSubsidy()+"");
        Log.d(null, gallonsConsumed.getLitersConsumedWithoutSubsidy()+"");
        initElements(v);
        return v;
    }

    private void initElements(View v) {
        galonesConsumidos = (TextView) v.findViewById(R.id.galonesConsumidosSubsidioTXTID);
        consumedGallonsConSubsidio = gallonsConsumed.getLitersConsumedWithSubsidy();


        consumedGallonsSINSubsidio = gallonsConsumed.getLitersConsumedWithoutSubsidy();
        totalGallons = gallonsConsumed.getLitersToConsume();

        statePumpID = (ImageView) v.findViewById(R.id.statePumpID);
        progressBar = (ProgressBar) v.findViewById(R.id.progressID);
        availablePercentage = (TextView) v.findViewById(R.id.availablePercentage);
        galonesDisponiblesTxt = (TextView) v.findViewById(R.id.galonesDisponiblesID);
        calculatePercentFuelAvailable();
    }

    private void calculatePercentFuelAvailable() {
        double totalGalonesConsumidos = consumedGallonsConSubsidio+consumedGallonsSINSubsidio;
        galonesConsumidos.setText(formatDuble(String.valueOf(totalGalonesConsumidos)));

        double galonesDisponibles = totalGallons-totalGalonesConsumidos;
        galonesDisponiblesTxt.setText(formatDuble(String.valueOf(galonesDisponibles)));

        Double porcentajeConsumido = (totalGalonesConsumidos*totalPercent)/totalGallons;

        progressBar.setProgress(porcentajeConsumido.intValue());
        availablePercentage.setText(String.valueOf(porcentajeConsumido.intValue()));

        if (porcentajeConsumido < 85) {
            statePumpID.setImageResource(R.drawable.galon_verde);
        } else if (porcentajeConsumido == 85 || porcentajeConsumido < 99) {
            statePumpID.setImageResource(R.drawable.galon_amarillo);
        } else if (porcentajeConsumido >= 99) {
            statePumpID.setImageResource(R.drawable.galon_rojo);
        }
    }

    private String formatDuble(String s) {
        int indiceDePunto = s.indexOf('.');
        String inicioHastaPunto = s.substring(0, indiceDePunto);
        String restoDespuesDePunto = s.substring(indiceDePunto, String.valueOf(s).length());
        if(restoDespuesDePunto.length()==2) {
            restoDespuesDePunto = restoDespuesDePunto + "0";
        }
        return inicioHastaPunto+restoDespuesDePunto;
    }
}
