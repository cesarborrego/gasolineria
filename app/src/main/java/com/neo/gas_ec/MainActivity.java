package com.neo.gas_ec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.neo.gas_ec.adapters.PagerAdapter;
import com.neo.gas_ec.model.DataVehicle;
import com.neo.gas_ec.model.GallonsConsumed;
import com.neo.gas_ec.model.Movements;
import com.neo.gas_ec.tabs.SlidingTabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //UI
    private ViewPager pager;
    private SlidingTabLayout mTabs;
    private Toolbar toolBar;
    String data = "";
    ArrayList<Movements> movements_list;
    DataVehicle dataVehicle;
    GallonsConsumed gallonsConsumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        data = intent.getStringExtra("infoTag");
        movements_list = intent.getParcelableArrayListExtra("movements");
        gallonsConsumed = intent.getParcelableExtra("gallonsConsumed");
        dataVehicle = intent.getParcelableExtra("dataVehicle");
//        setSupportBar();
        slidingBar_Pager();
    }

    private void setSupportBar() {
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setElevation(2);
    }


    private void slidingBar_Pager() {
        pager = (ViewPager) findViewById(R.id.pagerID);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), getApplicationContext(), data, dataVehicle, movements_list, gallonsConsumed));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.custom_tab_layout, R.id.tabText);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.white);
            }
        });
        mTabs.setViewPager(pager);
    }
}