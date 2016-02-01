package com.kernusr.tulbath;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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

    // Billionaires json url
    private static final String url = "https://raw.githubusercontent.com/mobilesiri/Android-Custom-Listview-Using-Volley/master/richman.json";
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
        // Showing progress dialog before making http request
        pDialog.setMessage("Загрузка...");
        pDialog.show();

        // Creating volley request obj
        JsonArrayRequest bathReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                BathContent bathItem = new BathContent();
                                bathItem.setName(obj.getString("name"));
                                bathItem.setThumbnailUrl(obj.getString("image"));
                                bathItem.setWorth(obj.getString("worth"));
                                bathItem.setYear(obj.getInt("InYear"));
                                bathItem.setSource(obj.getString("source"));

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
