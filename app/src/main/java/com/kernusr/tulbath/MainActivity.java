package com.kernusr.tulbath;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kernusr.tulbath.adapter.CustomListAdapter;
import com.kernusr.tulbath.model.BathContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String url = "http://tulbath.sitevtule.com/catalog";
    private ProgressDialog pDialog;
    private List<BathContent> bathContentList = new ArrayList<BathContent>();
    private ListView listView;
    private CustomListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        listAdapter = new CustomListAdapter(this, bathContentList);
        listView.setAdapter(listAdapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Загрузка...");
        pDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.id)).getText()
                        .toString();

                // Запускаем новый intent который покажет нам Activity
                Intent intent = new Intent(getApplicationContext(), FullActivity.class);
                // отправляем pid в следующий activity
                intent.putExtra("client_id", pid);
                startActivity(intent);
            }
        });

        // Creating volley request obj
        JsonArrayRequest bathReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    private static final String img_url = "https://bytebucket.org/tulbath/tulbath_content/raw/1cc09ade0fdabd6a1ed5555cf609fb7a54daf3fb/images/drawable-hdpi/";

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                BathContent bathItem = new BathContent();
                                bathItem.setName(obj.getString("name"));
                                bathItem.setItemImage(img_url + "banya" + obj.getInt("id") + ".jpg");
                                bathItem.setPrice(obj.getString("price"));
                                bathItem.setId(obj.getInt("id"));
                                bathItem.setAddress(obj.getString("address"));

                                // adding Billionaire to worldsBillionaires array
                                bathContentList.add(bathItem);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        hidePDialog();

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        listAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(bathReq);
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
