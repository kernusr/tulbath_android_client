package com.kernusr.tulbath.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kernusr.tulbath.AppController;
import com.kernusr.tulbath.R;
import com.kernusr.tulbath.model.BathContent;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
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
        TextView dialBtn = (Button) convertView.findViewById(R.id.dial_btn);

        // getting billionaires data for the row
        BathContent m = bathItems.get(position);
        final ArrayList<String> phonesArr = m.getPhones();

        // thumbnail image
        itemImage.setImageUrl(m.getItemImage(), imageLoader);

        // name
        name.setText(m.getName());
        address.setText(String.valueOf(m.getAddress()));
        price.setText("От " + String.valueOf(m.getPrice()) + " руб.");
        id.setText(String.valueOf(m.getId()));
        switch (phonesArr.size()){
            case 0: //ничего не происходит;
                dialBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Toast toast = Toast.makeText(activity, "Номер не задан", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                break;
            case 1:
                //показываем кнопку вызова номера
                dialBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                public void onClick(View v){
                        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phonesArr.get(0)));
                        activity.startActivity(i);
                    }
                });
                break;
            default: //вызываем диалоговое окно для выбора номера;
                dialBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v){
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Я звоню!");
                        builder.setItems(phonesArr.toArray(new CharSequence[phonesArr.size()]), new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phonesArr.get(which)));
                                activity.startActivity(i);
                                dialog.dismiss();
                            }
                        });
                        builder.setCancelable(true);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //Toast toast = Toast.makeText(v.getContext(), "Задано несколько номеров", Toast.LENGTH_SHORT);
                        //toast.show();
                    }
                });
                break;
        }

        return convertView;
    }

}
