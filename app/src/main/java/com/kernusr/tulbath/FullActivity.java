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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kernusr on 01.02.16.
 */
public class FullActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public String url;
    String img_url;

    //private Intent intent;
    private ProgressDialog pDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    //private List<BathContent> bathFullContent = new ArrayList<BathContent>();
    public String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Загрузка...");
        pDialog.show();

        // показываем форму про детальную информацию о продукте
        Intent i = getIntent();
        // получаем id продукта (pid) с формы
        pid = i.getStringExtra("client_id");
        url = "https://bitbucket.org/tulbath/tulbath_content/raw/1cc09ade0fdabd6a1ed5555cf609fb7a54daf3fb/full_items/full_item_"+pid+".json";
        img_url = "https://bytebucket.org/tulbath/tulbath_content/raw/1cc09ade0fdabd6a1ed5555cf609fb7a54daf3fb/images/drawable-hdpi/b"+pid+".jpg";
        NetworkImageView itemFullImage = (NetworkImageView) findViewById(R.id.itemFullImage);
        itemFullImage.setImageUrl(img_url, imageLoader);

        // Creating volley request obj
        JsonArrayRequest fullReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        //Log.d(img_url, response.toString());
                        Log.d(url, response.toString());


                        TextView itemFullName = (TextView) findViewById(R.id.itemFullName);
                        TextView itemFullPrice = (TextView) findViewById(R.id.itemFullPrice);
                        TextView itemFullAddress = (TextView) findViewById(R.id.itemFullAddress);
                        TextView itemFullDescription = (TextView) findViewById(R.id.itemFullDescription);

                        try {
                            JSONObject obj = response.getJSONObject(0);

                            itemFullName.setText(/*obj.getString("name")*/"Что за нах № "+pid);
                            itemFullAddress.setText(obj.getString("address"));
                            itemFullPrice.setText("От "+obj.getString("price")+" руб.");
                            itemFullDescription.setText(obj.getString("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();
                    }
                }
        );
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
