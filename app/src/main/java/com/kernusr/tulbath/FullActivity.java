package com.kernusr.tulbath;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
        url = "http://tulbath.sitevtule.com/catalog/show?id="+pid;
        img_url = "https://bytebucket.org/tulbath/tulbath_content/raw/1cc09ade0fdabd6a1ed5555cf609fb7a54daf3fb/images/drawable-hdpi/b"+pid+".jpg";
        NetworkImageView itemFullImage = (NetworkImageView) findViewById(R.id.itemFullImage);
        final TextView itemFullName = (TextView) findViewById(R.id.itemFullName);
        //final TextView itemFullPrice = (TextView) findViewById(R.id.itemFullPrice);
        final TextView itemFullAddress = (TextView) findViewById(R.id.itemFullAddress);
        //final TextView itemFullDescription = (TextView) findViewById(R.id.itemFullDescription);

        itemFullImage.setImageUrl(img_url, imageLoader);

        JsonArrayRequest fullItem = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = response.getJSONObject(0);
                    // Parsing json object response
                    // response will be a json object
                    String name = obj.getString("name");
                    //String price = response.getString("price");
                    String address = obj.getString("address");
                    //String description = response.getString("description");

                    //Отрисовуем на экране усё
                    itemFullName.setText(name);
                    itemFullAddress.setText(address);
                    //itemFullPrice.setText(price);
                    //itemFullDescription.setText(description);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidePDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(fullItem);
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