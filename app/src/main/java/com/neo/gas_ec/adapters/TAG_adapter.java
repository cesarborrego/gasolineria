package com.neo.gas_ec.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neo.gas_ec.R;
import com.neo.gas_ec.model.Data_test;
import com.neo.gas_ec.transitions.ActivityAnimations;
import com.neo.gas_ec.transitions.CardDetailsActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cesar on 23/11/15.
 */
public class TAG_adapter extends RecyclerView.Adapter<TAG_adapter.TAG_ViewHolder> {

    private static final String PACKAGE = ActivityAnimations.class.getPackage().getName();
    static float sAnimatorScale = 1;
    HashMap<ImageView, Data_test> mCardsData = new HashMap<ImageView, Data_test>();
    private List<Data_test> data_testList;
    Activity a;

    public TAG_adapter(List<Data_test> tag_infoList, Activity a) {
        this.data_testList = tag_infoList;
        this.a = a;
    }

    @Override
    public TAG_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_datatag, parent, false);
        return new TAG_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TAG_ViewHolder holder, int position) {
        Data_test data_test = data_testList.get(position);
        holder.dataTitle.setText(data_test.getTagTitle());
        holder.dataTag.setText(data_test.getTagData());

        Data_test data_test1 = new Data_test();
        data_test.setTagData("hola");
        data_test.setTagTitle("titulo");
        data_test.setPosition(position);
        holder.card.setOnClickListener(cardClickListener);
        mCardsData.put(holder.card, data_test1);
//        final TextView t = holder.dataTitle;
//        t.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(a, t.getText().toString(), Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(a, ActivityAnimations.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                a.startActivity(i);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return data_testList.size();
    }

    public static class TAG_ViewHolder extends RecyclerView.ViewHolder {
        public TextView dataTitle;
        public TextView dataTag;
        public ImageView card;

        public TAG_ViewHolder(View itemView) {
            super(itemView);
            dataTitle = (TextView) itemView.findViewById(R.id.dataTitleID);
            dataTag = (TextView) itemView.findViewById(R.id.dataTag);
            card = (ImageView) itemView.findViewById(R.id.card_viewID);
//            card.setOnClickListener(cardClickListener);
        }
    }

    public View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int[] screenLocation = new int[2];
            v.getLocationOnScreen(screenLocation);
            Data_test info = mCardsData.get(v);
            Intent subActivity = new Intent(a, CardDetailsActivity.class);
            int orientation = a.getResources().getConfiguration().orientation;
            subActivity.
                    putExtra(PACKAGE + ".orientation", orientation).
                    putExtra(PACKAGE + ".titleCard", info.getTagTitle()).
                    putExtra(PACKAGE + ".left", screenLocation[0]).
                    putExtra(PACKAGE + ".top", screenLocation[1]).
                    putExtra(PACKAGE + ".width", v.getWidth()).
                    putExtra(PACKAGE + ".height", v.getHeight()).
                    putExtra(PACKAGE + ".descriptionCard", info.getTagData());
            subActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            subActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.startActivity(subActivity);

            // Override transitions: we don't want the normal window animation in addition
            // to our custom one
            a.overridePendingTransition(0, 0);
        }
    };
}
