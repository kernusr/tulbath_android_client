package com.kernusr.tulbath;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kernusr on 27.01.16.
 */
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

        list.setAdapter(new ListAdapter(
                MainActivity.this, ItemList,
                new String[]{TAG_NAME, TAG_ADDRES,
                TAG_PRICE}
        ));

        setListAdapter(adapter);
    }
}
