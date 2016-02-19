package com.kernusr.tulbath.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kernusr.tulbath.AppController;
import com.kernusr.tulbath.R;
import com.kernusr.tulbath.model.BathContent;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<BathContent> bathItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<BathContent> bathItems) {
        this.activity = activity;
        this.bathItems = bathItems;
    }

    @Override
    public int getCount() {
        return bathItems.size();
    }

    @Override
    public Object getItem(int location) {
        return bathItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.view_item_list, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView itemImage = (NetworkImageView) convertView
                .findViewById(R.id.image);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView phones = (TextView) convertView.findViewById(R.id.phones);

        // getting billionaires data for the row
        BathContent m = bathItems.get(position);

        // thumbnail image
        itemImage.setImageUrl(m.getItemImage(), imageLoader);

        // name
        name.setText(m.getName());
        address.setText(String.valueOf(m.getAddress()));
        price.setText("От " + String.valueOf(m.getPrice()) + " руб.");
        id.setText(String.valueOf(m.getId()));
        phones.setText(String.valueOf(m.getPhones()));

        return convertView;
    }

}
