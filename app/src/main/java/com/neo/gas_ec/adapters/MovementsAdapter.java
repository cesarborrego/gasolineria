package com.neo.gas_ec.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neo.gas_ec.DataActivity;
import com.neo.gas_ec.R;
import com.neo.gas_ec.model.Movements;
import com.neo.gas_ec.utils.Constants_Settings;
import com.neo.gas_ec.utils.DateUtil;
import com.neo.gas_ec.utils.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cesar on 29/12/15.
 */
public class MovementsAdapter extends RecyclerView.Adapter<MovementsAdapter.MovementsAdapter_ViewHOlder> {
    ArrayList<Movements> movementsArrayList;
    Activity a;
    SharedPreferences sharedPreferences;
    String urlPdf = "";
    String pdfName = "";

    public MovementsAdapter(ArrayList<Movements> movementsArrayList, Activity a) {
        this.movementsArrayList = movementsArrayList;
        this.a = a;
    }

    @Override
    public MovementsAdapter_ViewHOlder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_table_movements, parent, false);
        sharedPreferences = a.getApplicationContext().getSharedPreferences(Constants_Settings.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        urlPdf = "http://"+sharedPreferences.getString(Constants_Settings.KEY_URL, null)+"/rest/fuelLoads/fuelLoad/bill/";
        return new MovementsAdapter_ViewHOlder(v);
    }

    @Override
    public void onBindViewHolder(MovementsAdapter_ViewHOlder holder, int position) {
        final Movements movements = movementsArrayList.get(position);
//        holder.pump.setText(String.valueOf(movements.getPumpId()));
//        holder.gallonsSupplied.setText(String.valueOf(movements.getLitersSupplied()));
//        holder.fuelType.setText(movements.getFuelType());
//        holder.pricePerGallon.setText(String.valueOf(movements.getPricePerLiter()));
//        holder.stationRuc.setText(movements.getStationRuc());
//        holder.taxPercentage.setText(String.valueOf(movements.getTaxPercentage()));
//        holder.dateTime.setText(movements.getDatetime());
//        holder.totalPaid.setText(String.valueOf(movements.getTotalPaid()));


        holder.fecha.setText(DateUtil.dateToString(new Date(movements.getDatetime()), "dd/MM/yyyy HH:mm"));
//        holder.estacion.setText(String.valueOf(movements.getStationRuc()));
        holder.tipoGasolina.setText(formatDuble(String.valueOf(movements.getLitersSupplied()))+" Gls.");

        int indiceDePunto = String.valueOf(movements.getTotalPaid()).indexOf('.');
        // Subcadena a modificar
        String inicioHastaPunto = String.valueOf(movements.getTotalPaid()).substring(0, indiceDePunto);
        // Subcadena que no debe ser modificada
        String restoDespuesDePunto = String.valueOf(movements.getTotalPaid()).substring(indiceDePunto, String.valueOf(movements.getTotalPaid()).length());

//        Log.d(null, "Valorantes "+inicioHastaPunto);
//        Log.d(null, "valordespues "+restoDespuesDePunto);

        if(restoDespuesDePunto.length()==2) {
            restoDespuesDePunto = restoDespuesDePunto+"0";
        } else if(restoDespuesDePunto.length()>4) {
            restoDespuesDePunto = restoDespuesDePunto.substring(0,3);
        }
        holder.total.setText("$" + inicioHastaPunto + restoDespuesDePunto);
        Log.d(null, "FOLIO PDF " + movements.getFolio());
        urlPdf = urlPdf+movements.getUrlPdf();  //Obtiene la url y concatena el folio que se obtiene de los movimientos.getUrl
//        urlPdf = movements.getUrlPdf(); //Obtiene la url de ejemplo de la partitura que se agrega en los movimientos
//        Log.d(null, movements.getNamePDF());
//        Log.d(null, urlPdf);

        holder.iconPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(null, movements.getNamePDF()+movements.getFolio());
                Log.d(null, movements.getUrlPdf());
                new DownloadFile().execute(movements.getUrlPdf()+movements.getFolio(), movements.getNamePDF());
            }
        });
//        pdfName = movements.getUrlPdf()+"_"+DateUtil.dateToString(new Date(movements.getDatetime()), "dd_MM_yyyy_HH:mm")+".pdf";
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

    @Override
    public int getItemCount() {
        return movementsArrayList.size();
    }

    public static class MovementsAdapter_ViewHOlder extends RecyclerView.ViewHolder {
//        public TextView pump;
//        public TextView gallonsSupplied;
//        public TextView fuelType;
//        public TextView pricePerGallon;
//        public TextView stationRuc;
//        public TextView taxPercentage;
//        public TextView dateTime;
//        public TextView totalPaid;
//        public ImageView iconPdf;

        public TextView fecha;
        public TextView estacion;
        public TextView tipoGasolina;
        public TextView total;
        public ImageView iconPdf;

        public MovementsAdapter_ViewHOlder(View itemView) {
            super(itemView);
//            pump = (TextView) itemView.findViewById(R.id.pumpID);
//            gallonsSupplied = (TextView) itemView.findViewById(R.id.gallonsSuppliedID);
//            fuelType = (TextView) itemView.findViewById(R.id.fuelTypeID);
//            pricePerGallon = (TextView) itemView.findViewById(R.id.pricePerGallonID);
//            stationRuc = (TextView) itemView.findViewById(R.id.stationRucID);
//            taxPercentage = (TextView) itemView.findViewById(R.id.taxPercentageID);
//            dateTime = (TextView) itemView.findViewById(R.id.dateTimeID);
//            totalPaid = (TextView) itemView.findViewById(R.id.totalPaidID);
//            iconPdf = (ImageView) itemView.findViewById(R.id.iconPdfID);

            fecha = (TextView)itemView.findViewById(R.id.dateMovementsID);
//            estacion = (TextView)itemView.findViewById(R.id.stationMovementsID);
            tipoGasolina = (TextView)itemView.findViewById(R.id.fuelTypeMovementsID);
            total = (TextView)itemView.findViewById(R.id.totalPaidMovementsID);
            iconPdf = (ImageView)itemView.findViewById(R.id.pdfMovementsID);
        }
    }

    class DownloadFile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, a.getString(R.string.dirPDF));
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
//            loadPdf();
            return strings[1];
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(a.getApplicationContext(), a.getString(R.string.completePDF)+" "+aVoid, Toast.LENGTH_SHORT).show();
            showPdf(aVoid);
        }
    }

    public void showPdf(String namePDF)
    {
        File file = new File(Environment.getExternalStorageDirectory()+"/"+a.getString(R.string.dirPDF)+"/"+namePDF);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        a.startActivity(intent);
    }

    public void loadPdf() {

        File pdfFile = new File(Environment.getExternalStorageDirectory() +"/"+ a.getString(R.string.dirPDF) + "/" + pdfName);  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            a.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(a.getApplicationContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
