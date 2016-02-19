package com.kernusr.tulbath;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
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

    private boolean mHasData = false;
    private boolean mInError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        listAdapter = new CustomListAdapter(this, bathContentList);
        listView.setAdapter(listAdapter);
        listView.setOnScrollListener(new LoadOnScrollListener());

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
    }

    public class LoadOnScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 5;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public LoadOnScrollListener() {
        }
        public LoadOnScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                loadPage();
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public int getCurrentPage() {
            return currentPage;
        }
    }

    private void loadPage() {
        int page = 1 + (bathContentList.size()/10);
        JsonArrayRequest bathReq = new JsonArrayRequest(Request.Method.GET,
                url+"?page="+page,
                null,
                createReqSuccessListener(),
                createReqErrorListener());
        AppController.getInstance().addToRequestQueue(bathReq);
    }


    private Response.Listener<JSONArray> createReqSuccessListener() {
        return new Response.Listener<JSONArray>() {

            private static final String img_url = "https://bytebucket.org/tulbath/tulbath_content/raw/1cc09ade0fdabd6a1ed5555cf609fb7a54daf3fb/images/drawable-hdpi/";

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        BathContent bathItem = new BathContent();
                        bathItem.setName(obj.getString("name"));
                        bathItem.setItemImage(img_url + "banya" + obj.getInt("id") + ".jpg");
                        bathItem.setPrice(obj.getString("price"));
                        bathItem.setId(obj.getInt("id"));
                        bathItem.setAddress(obj.getString("address"));
                        JSONArray JSONPhones = obj.getJSONArray("phones");
                        List<String> phones = new ArrayList<String>();
                        for(int n = 0; n < JSONPhones.length(); n++){
                            phones.add(JSONPhones.getString(n));
                        }
                        bathItem.setPhones(phones);

                        bathContentList.add(bathItem);

                        listAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                hidePDialog();

            }
        };
    }


    private Response.ErrorListener createReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mInError = true;
                VolleyLog.d(TAG, "Error: " + error.toString());
                hidePDialog();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mHasData && !mInError) {
            loadPage();
        }
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
