/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.neo.gas_ec.transitions;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.neo.gas_ec.R;
import com.neo.gas_ec.VolleyApp;
import com.neo.gas_ec.model.Data_test;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This example shows how to create a custom activity animation when you want something more
 * than window animations can provide. The idea is to disable window animations for the
 * activities and to instead launch or return from the sub-activity immediately, but use
 * property animations inside the activities to customize the transition.
 * <p/>
 * Watch the associated video for this demo on the DevBytes channel of developer.android.com
 * or on the DevBytes playlist in the androiddevelopers channel on YouTube at
 * https://www.youtube.com/playlist?list=PLWz5rJ2EKKc_XOgcRukSoKKjewFJZrKV0.
 */
public class ActivityAnimations extends Activity {

    private static final String PACKAGE = ActivityAnimations.class.getPackage().getName();
    static float sAnimatorScale = 1;

    CardView card;
    GridLayout mGridLayout;
    HashMap<CardView, Data_test> mCardsData = new HashMap<CardView, Data_test>();
    HashMap<ImageView, PictureData> mPicturesData = new HashMap<ImageView, PictureData>();
    BitmapUtils mBitmapUtils = new BitmapUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animations);

        // Grayscale filter used on all thumbnails
        ColorMatrix grayMatrix = new ColorMatrix();
        grayMatrix.setSaturation(0);
        ColorMatrixColorFilter grayscaleFilter = new ColorMatrixColorFilter(grayMatrix);

        card = (CardView) findViewById(R.id.card_view11);

        mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
        mGridLayout.setColumnCount(3);
        mGridLayout.setUseDefaultMargins(true);

        // add all photo thumbnails to layout
        Resources resources = getResources();
        ArrayList<PictureData> pictures = mBitmapUtils.loadPhotos(resources);
        for (int i = 0; i < pictures.size(); ++i) {
            PictureData pictureData = pictures.get(i);
            BitmapDrawable thumbnailDrawable =
                    new BitmapDrawable(resources, pictureData.thumbnail);
            thumbnailDrawable.setColorFilter(grayscaleFilter);
            ImageView imageView = new ImageView(this);
            imageView.setOnClickListener(thumbnailClickListener);
            imageView.setImageDrawable(thumbnailDrawable);
            mPicturesData.put(imageView, pictureData);
            mGridLayout.addView(imageView);
        }
        Data_test data_test = new Data_test();
        data_test.setTagData("hola");
        data_test.setTagTitle("titulo");
        card.setOnClickListener(cardClickListener);
        mCardsData.put(card, data_test);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_better_window_animations, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_slow) {
//            sAnimatorScale = item.isChecked() ? 1 : 5;
//            item.setChecked(!item.isChecked());
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * When the user clicks a thumbnail, bundle up information about it and launch the
     * details activity.
     */
    private View.OnClickListener thumbnailClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // Interesting data to pass across are the thumbnail size/location, the
            // resourceId of the source bitmap, the picture description, and the
            // orientation (to avoid returning back to an obsolete configuration if
            // the device rotates again in the meantime)
            int[] screenLocation = new int[2];
            v.getLocationOnScreen(screenLocation);
            PictureData info = mPicturesData.get(v);
            Intent subActivity = new Intent(ActivityAnimations.this,
                    PictureDetailsActivity.class);
            int orientation = getResources().getConfiguration().orientation;
            subActivity.
                    putExtra(PACKAGE + ".orientation", orientation).
                    putExtra(PACKAGE + ".resourceId", info.resourceId).
                    putExtra(PACKAGE + ".left", screenLocation[0]).
                    putExtra(PACKAGE + ".top", screenLocation[1]).
                    putExtra(PACKAGE + ".width", v.getWidth()).
                    putExtra(PACKAGE + ".height", v.getHeight()).
                    putExtra(PACKAGE + ".description", info.description);
            startActivity(subActivity);

            // Override transitions: we don't want the normal window animation in addition
            // to our custom one
            overridePendingTransition(0, 0);
        }
    };

    private View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int[] screenLocation = new int[2];
            v.getLocationOnScreen(screenLocation);
            Data_test info = mCardsData.get(v);
            Intent subActivity = new Intent(ActivityAnimations.this,
                    CardDetailsActivity.class);
            int orientation = getResources().getConfiguration().orientation;
            subActivity.
                    putExtra(PACKAGE + ".orientation", orientation).
                    putExtra(PACKAGE + ".titleCard", info.getTagTitle()).
                    putExtra(PACKAGE + ".left", screenLocation[0]).
                    putExtra(PACKAGE + ".top", screenLocation[1]).
                    putExtra(PACKAGE + ".width", v.getWidth()).
                    putExtra(PACKAGE + ".height", v.getHeight()).
                    putExtra(PACKAGE + ".descriptionCard", info.getTagData());
            startActivity(subActivity);

            // Override transitions: we don't want the normal window animation in addition
            // to our custom one
            overridePendingTransition(0, 0);
        }
    };
}
