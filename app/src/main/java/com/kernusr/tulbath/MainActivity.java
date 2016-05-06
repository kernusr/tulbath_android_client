package com.kernusr.tulbath;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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

    private ProgressDialog pDialog;
    private List<BathContent> bathContentList = new ArrayList<BathContent>();
    private ListView listView;
    private CustomListAdapter listAdapter;

    private boolean mHasData = false;
    private boolean mInError = false;

    private boolean loadLastPage = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
                String  c_id = ((TextView) view.findViewById(R.id.id)).getText().toString(),
                        address = ((TextView) view.findViewById(R.id.address)).getText().toString(),
                        name = ((TextView) view.findViewById(R.id.name)).getText().toString();

                // Запускаем новый intent который покажет нам Activity
                Intent intent = new Intent(getApplicationContext(), FullActivity.class);
                // отправляем данные в следующий activity
                intent.putExtra("c_id", c_id);
                intent.putExtra("address", address);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kernusr.tulbath/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kernusr.tulbath/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
                    mHasData = true;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                loadPage();
                loading = true;
                mHasData = false;
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
        if(!loadLastPage){
            int page = 1 + (bathContentList.size() / 10);
            JsonArrayRequest bathReq = new JsonArrayRequest(Request.Method.GET,
                    getString(R.string.content_link) + "?page=" + page,
                    null,
                    createReqSuccessListener(),
                    createReqErrorListener());
            AppController.getInstance().addToRequestQueue(bathReq);
        }
    }


    private Response.Listener<JSONArray> createReqSuccessListener() {
        return new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                loadLastPage = response.length()<10;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        BathContent bathItem = new BathContent();
                        bathItem.setName(obj.getString("name"));
                        bathItem.setItemImage(
                                getString(R.string.image_link)
                                +obj.getInt("id")
                                + "_"
                                +obj.getString("item_image")
                                +"_item_image"
                                + ".jpeg");
                        bathItem.setPrice(obj.getString("price"));
                        bathItem.setId(obj.getInt("id"));
                        bathItem.setAddress(obj.getString("address"));
                        bathItem.setPhones(obj.getJSONArray("phones"));

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
