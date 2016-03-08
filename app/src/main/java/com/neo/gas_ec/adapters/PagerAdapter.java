package com.neo.gas_ec.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.neo.gas_ec.R;
import com.neo.gas_ec.model.DataVehicle;
import com.neo.gas_ec.model.GallonsConsumed;
import com.neo.gas_ec.model.Movements;
import com.neo.gas_ec.subfragments.SubF_GeneralInfo;
import com.neo.gas_ec.subfragments.SubF_Movements;
import com.neo.gas_ec.subfragments.SubF_Receipts;
import com.neo.gas_ec.subfragments.SubF_FuelConsumed;

import java.util.ArrayList;

/**
 * Created by cesar on 15/12/15.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    Context c;
    String[] tabs;
    String infoTag;
    ArrayList<Movements> list;
    GallonsConsumed gallonsConsumed;
    DataVehicle dataVehicle;

    int[] icons = {
            R.drawable.vehiculo_up,
            R.drawable.movimientos_up,
            R.drawable.gasolina_up
//            R.drawable.ic_receipt_white_24dp
    };

    public PagerAdapter(FragmentManager fm, Context c, String infoTag, DataVehicle dataVehicle, ArrayList<Movements> list, GallonsConsumed gallonsConsumed) {
        super(fm);
        this.c = c;
        this.infoTag = infoTag;
        this.dataVehicle = dataVehicle;
        this.list = list;
        this.gallonsConsumed = gallonsConsumed;
        tabs = c.getResources().getStringArray(R.array.tabs);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                Bundle args = new Bundle();
                args.putString("data", infoTag);
                args.putParcelable("dataVehicle", dataVehicle);
                fragment = new SubF_GeneralInfo();
                fragment.setArguments(args);
                break;
            case 1:
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("movementsList", list);
                fragment = new SubF_Movements();
                fragment.setArguments(bundle);
                break;
            case 2:
                Bundle bundleFuel = new Bundle();
                bundleFuel.putParcelable("gallonsConsumed", gallonsConsumed);
                fragment = new SubF_FuelConsumed();
                fragment.setArguments(bundleFuel);
                break;
//            case 3:
//                fragment = new SubF_Receipts();
//                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable drawable = ContextCompat.getDrawable(c, icons[position]);
        drawable.setBounds(0, 0, 45, 45);
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannableString = new SpannableString("  ");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    @Override
    public int getCount() {
        return 3;
    }
}
