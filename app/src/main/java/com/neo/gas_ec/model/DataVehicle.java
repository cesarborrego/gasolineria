package com.neo.gas_ec.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 25/02/16.
 */
public class DataVehicle implements Parcelable{

    private String placa;
    private String marca;
    private String subMarca;
    private String anioModelo;
    private String vin;

    public DataVehicle (String placa, String marca, String subMarca, String anioModelo, String vin) {
        this.placa = placa;
        this.marca = marca;
        this.subMarca = subMarca;
        this.anioModelo = anioModelo;
        this.vin = vin;
    }

    protected DataVehicle(Parcel in) {
        placa = in.readString();
        marca = in.readString();
        subMarca = in.readString();
        anioModelo = in.readString();
        vin = in.readString();
    }

    public static final Creator<DataVehicle> CREATOR = new Creator<DataVehicle>() {
        @Override
        public DataVehicle createFromParcel(Parcel in) {
            return new DataVehicle(in);
        }

        @Override
        public DataVehicle[] newArray(int size) {
            return new DataVehicle[size];
        }
    };

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getSubMarca() {
        return subMarca;
    }

    public void setSubMarca(String subMarca) {
        this.subMarca = subMarca;
    }

    public String getAnioModelo() {
        return anioModelo;
    }

    public void setAnioModelo(String anioModelo) {
        this.anioModelo = anioModelo;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placa);
        dest.writeString(marca);
        dest.writeString(subMarca);
        dest.writeString(anioModelo);
        dest.writeString(vin);
    }
}
