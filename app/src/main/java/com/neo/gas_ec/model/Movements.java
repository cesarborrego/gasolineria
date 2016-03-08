package com.neo.gas_ec.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cesar on 18/12/15.
 */
public class Movements implements Parcelable {

    private String stationRuc;
    private int pumpId;
    private long datetime;
    private String fuelType;
    private double litersSupplied;
    private boolean subsidy;
    private double pricePerLiter;
    private double taxPercentage;
    private double totalPaid;
    private String folio;
    private String urlPdf;
    private String namePDF;

    public Movements() {
    }

    public Movements(int pumpId,
                     boolean subsidy,
                     double litersSupplied,
                     String fuelType,
                     double pricePerLiter,
                     String stationRuc,
                     double taxPercentage,
                     long datetime,
                     double totalPaid,
                     String folio,
                     String urlPdf,
                     String namePDF) {
        this.pumpId = pumpId;
        this.subsidy = subsidy;
        this.litersSupplied = litersSupplied;
        this.fuelType = fuelType;
        this.pricePerLiter = pricePerLiter;
        this.stationRuc = stationRuc;
        this.taxPercentage = taxPercentage;
        this.datetime = datetime;
        this.totalPaid = totalPaid;
        this.folio = folio;
        this.urlPdf = urlPdf;
        this.namePDF = namePDF;
    }

    protected Movements(Parcel in) {
        pumpId = in.readInt();
        litersSupplied = in.readDouble();
        fuelType = in.readString();
        pricePerLiter = in.readDouble();
        stationRuc = in.readString();
        taxPercentage = in.readDouble();
        datetime = in.readLong();
        totalPaid = in.readDouble();
        folio = in.readString();
        urlPdf = in.readString();
        namePDF = in.readString();
    }

    public static final Creator<Movements> CREATOR = new Creator<Movements>() {
        @Override
        public Movements createFromParcel(Parcel in) {
            return new Movements(in);
        }

        @Override
        public Movements[] newArray(int size) {
            return new Movements[size];
        }
    };

    public int getPumpId() {
        return pumpId;
    }

    public void setPumpId(int pumpId) {
        this.pumpId = pumpId;
    }

    public boolean isSubsidy() {
        return subsidy;
    }

    public void setSubsidy(boolean subsidy) {
        this.subsidy = subsidy;
    }

    public double getLitersSupplied() {
        return litersSupplied;
    }

    public void setLitersSupplied(double litersSupplied) {
        this.litersSupplied = litersSupplied;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getPricePerLiter() {
        return pricePerLiter;
    }

    public void setPricePerLiter(double pricePerLiter) {
        this.pricePerLiter = pricePerLiter;
    }

    public String getStationRuc() {
        return stationRuc;
    }

    public void setStationRuc(String stationRuc) {
        this.stationRuc = stationRuc;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pumpId);
        dest.writeDouble(litersSupplied);
        dest.writeString(fuelType);
        dest.writeDouble(pricePerLiter);
        dest.writeString(stationRuc);
        dest.writeDouble(taxPercentage);
        dest.writeLong(datetime);
        dest.writeDouble(totalPaid);
        dest.writeString(folio);
        dest.writeString(urlPdf);
        dest.writeString(namePDF);

    }

    public String getNamePDF() {
        return namePDF;
    }

    public void setNamePDF(String namePDF) {
        this.namePDF = namePDF;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }
}
