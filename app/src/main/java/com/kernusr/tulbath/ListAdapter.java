package com.kernusr.tulbath;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends BaseAdapter {

    private Context ctx;


    @Override
    public int getCount() {
        return cnt.length;
    }

    @Override
    public Object getItem(int position) {
        return cnt[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item;

        if (convertView == null) {
            item = new View(ctx);
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            item = inflater.inflate(R.layout.item_list_view, parent, false);
        } else {
            item = (View) convertView;
        }



        ImageView itemimage = (ImageView) item.findViewById(R.id.image);
        TextView itemtitle = (TextView) item.findViewById(R.id.title);
        TextView itemadres = (TextView) item.findViewById(R.id.address);
        TextView itemprice = (TextView) item.findViewById(R.id.price);

        /* Добавить fonawesome в textview *
        Typeface iconFont = Typeface.createFromAsset(ctx.getAssets(),"fonts/fontawesome-webfont.ttf");
        TextView itemadresicon = (TextView) item.findViewById(R.id.icon_addres);
        itemadresicon.setTypeface(iconFont);
        itemadresicon.setText(R.string.fa_icon_areachart);*/

        itemimage.setImageResource(cnt[position][0]);
        itemtitle.setText(cnt[position][1]);
        itemadres.setText(cnt[position][2]);
        itemprice.setText(cnt[position][5]);

        return item;
    }

    public int[][] cnt = {
            {R.drawable.banya0, R.string.tittle_banya0, R.string.addres_banya0,R.drawable.b1, R.string.description_banya0,R.string.price_banya0},
            {R.drawable.banya1, R.string.tittle_banya1, R.string.addres_banya1,R.drawable.b2, R.string.description_banya1,R.string.price_banya1},
            {R.drawable.banya2, R.string.tittle_banya2, R.string.addres_banya2,R.drawable.b3, R.string.description_banya2,R.string.price_banya2},
            {R.drawable.banya3, R.string.tittle_banya3, R.string.addres_banya3,R.drawable.b4, R.string.description_banya3,R.string.price_banya3},
            {R.drawable.banya4, R.string.tittle_banya4, R.string.addres_banya4,R.drawable.b5, R.string.description_banya4,R.string.price_banya4},
            {R.drawable.banya5, R.string.tittle_banya5, R.string.addres_banya5,R.drawable.b6, R.string.description_banya5,R.string.price_banya5},
            {R.drawable.banya6, R.string.tittle_banya6, R.string.addres_banya6,R.drawable.b7, R.string.description_banya6,R.string.price_banya6},
            {R.drawable.banya7, R.string.tittle_banya7, R.string.addres_banya7,R.drawable.b8, R.string.description_banya7,R.string.price_banya7},
            {R.drawable.banya8, R.string.tittle_banya8, R.string.addres_banya8,R.drawable.b9, R.string.description_banya8,R.string.price_banya8},
            {R.drawable.banya9, R.string.tittle_banya9, R.string.addres_banya9,R.drawable.b10,R.string.description_banya9,R.string.price_banya9}
    };
}
