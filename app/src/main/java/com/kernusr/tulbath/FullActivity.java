package com.kernusr.tulbath;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.kernusr.tulbath.model.BathContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kernusr on 01.02.16.
 */
public class FullActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String url="https://bitbucket.org/tulbath/tulbath_content/raw/1cc09ade0fdabd6a1ed5555cf609fb7a54daf3fb/full_items/";
    //private int pid;
    //private Intent intent;
    private ProgressDialog pDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<BathContent> bathFullContent = new ArrayList<BathContent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);
        //intent = getIntent();
        //pid = intent.getExtras().getInt("id");



        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Загрузка...");
        pDialog.show();

        NetworkImageView itemFullImage = (NetworkImageView) findViewById(R.id.itemFullImage);
        TextView itemFullName = (TextView) findViewById(R.id.itemFullName);
        TextView itemFullPrice = (TextView) findViewById(R.id.itemFullPrice);
        TextView itemFullAddress = (TextView) findViewById(R.id.itemFullAddress);
        TextView itemFullDescription = (TextView) findViewById(R.id.itemFullDescription);

        BathContent m = bathFullContent.get(1);

        itemFullImage.setImageUrl(m.getItemImage(), imageLoader);
        itemFullName.setText(m.getName());
        itemFullAddress.setText(String.valueOf(m.getAddress()));
        itemFullPrice.setText("От " + String.valueOf(m.getPrice()) + " руб.");
        itemFullDescription.setText(String.valueOf(m.getDescription()));

        JsonArrayRequest fullReq = new JsonArrayRequest(url+"full_item_1.json",
                new Response.Listener<JSONArray>() {

                    private static final String img_url = "https://bytebucket.org/tulbath/tulbath_content/raw/1cc09ade0fdabd6a1ed5555cf609fb7a54daf3fb/images/drawable-hdpi/";

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            JSONObject obj = response.getJSONObject(0);
                            BathContent bathFull = new BathContent();
                            bathFull.setName(obj.getString("name"));
                            bathFull.setItemImage(img_url + "b" + obj.getInt("id") + ".jpg");
                            bathFull.setPrice(obj.getString("price"));
                            bathFull.setId(obj.getInt("id"));
                            bathFull.setAddress(obj.getString("address"));
                            bathFull.setDescription(obj.getString("description"));

                            bathFullContent.add(bathFull);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hidePDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(fullReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
