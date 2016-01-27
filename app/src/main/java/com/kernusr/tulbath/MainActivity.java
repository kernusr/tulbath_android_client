package com.kernusr.tulbath;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static String url = "http://tulbath.ru/android/itemList";

    // JSON Node names
    private static final String TAG_RESPONSE = "response";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRES = "addres";
    private static final String TAG_PRICE = "price";

    private ListView list;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        new GetItem().execute();


        //list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
        //        Intent intent = new Intent(view.getContext(), FullActivity.class);
        //        intent.putExtra("id", position);
        //        startActivity(intent);
        //    }
        //});
    }

    public class GetItem extends AsyncTask<Void, Void, Void> {

        ArrayList<HashMap<String, String>> itemList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Request request = new Request();
            String jsonStr = request.makeWebServiceCall(url, Request.GETRequest);

            Log.d("Response: ", "> " + jsonStr);

            itemList = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);

            ListView list = (ListView) findViewById(R.id.itemlist);

            list.setAdapter(new ListAdapter(
                    this, itemList,
                    new String[]{TAG_NAME, TAG_ADDRES,
                            TAG_PRICE}
            ));
        }
    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                ArrayList<HashMap<String, String>> itemList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

// Getting JSON Array node
                JSONArray students = jsonObj.getJSONArray(TAG_RESPONSE);

// looping through All Students
                for (int i = 0; i < students.length(); i++) {
                    JSONObject c = students.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String addres = c.getString(TAG_ADDRES);
                    String price = c.getString(TAG_PRICE);

// tmp hashmap for single student
                    HashMap<String, String> slist = new HashMap<String, String>();

// adding every child node to HashMap key => value
                    slist.put(TAG_ID, id);
                    slist.put(TAG_NAME, name);
                    slist.put(TAG_ADDRES, addres);
                    slist.put(TAG_PRICE, price);

// adding student to students list
                    itemList.add(slist);
                }
                return itemList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }
}