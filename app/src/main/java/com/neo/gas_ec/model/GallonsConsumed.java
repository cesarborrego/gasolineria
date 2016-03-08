package com.neo.gas_ec.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cesar on 30/12/15.
 */
public class GallonsConsumed implements Parcelable{
    private String customerRUC;
    private double litersToConsume;
    private double litersConsumedWithSubsidy;
    private double litersConsumedWithoutSubsidy;

    public GallonsConsumed (
            String customerRUC,
            double litersToConsume,
            double litersConsumedWithSubsidy,
            double litersConsumedWithoutSubsidy) {
        this.customerRUC = customerRUC;
        this.litersToConsume = litersToConsume;
        this.litersConsumedWithSubsidy = litersConsumedWithSubsidy;
        this.litersConsumedWithoutSubsidy = litersConsumedWithoutSubsidy;
    }

    protected GallonsConsumed(Parcel in) {
        customerRUC = in.readString();
        litersToConsume = in.readDouble();
        litersConsumedWithSubsidy = in.readDouble();
        litersConsumedWithoutSubsidy = in.readDouble();
    }

    public static final Creator<GallonsConsumed> CREATOR = new Creator<GallonsConsumed>() {
        @Override
        public GallonsConsumed createFromParcel(Parcel in) {
            return new GallonsConsumed(in);
        }

        @Override
        public GallonsConsumed[] newArray(int size) {
            return new GallonsConsumed[size];
        }
    };

    public String getCustomerRUC() {
        return customerRUC;
    }

    public void setCustomerRUC(String customerRUC) {
        this.customerRUC = customerRUC;
    }

    public double getLitersToConsume() {
        return litersToConsume;
    }

    public void setLitersToConsume(double litersToConsume) {
        this.litersToConsume = litersToConsume;
    }

    public double getLitersConsumedWithSubsidy() {
        return litersConsumedWithSubsidy;
    }

    public void setLitersConsumedWithSubsidy(double litersConsumedWithSubsidy) {
        this.litersConsumedWithSubsidy = litersConsumedWithSubsidy;
    }

    public double getLitersConsumedWithoutSubsidy() {
        return litersConsumedWithoutSubsidy;
    }

    public void setLitersConsumedWithoutSubsidy(double litersConsumedWithoutSubsidy) {
        this.litersConsumedWithoutSubsidy = litersConsumedWithoutSubsidy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerRUC);
        dest.writeDouble(litersToConsume);
        dest.writeDouble(litersConsumedWithSubsidy);
        dest.writeDouble(litersConsumedWithoutSubsidy);
    }
}
