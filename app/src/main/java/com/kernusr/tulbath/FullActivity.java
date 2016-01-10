package com.kernusr.tulbath;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class FullActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_view);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        Intent intent = getIntent();

        int position = intent.getExtras().getInt("id");
        ListAdapter listAdapter = new ListAdapter(this);


        setTitle(listAdapter.cnt[position][1]);

        ImageView fullimage = (ImageView) findViewById(R.id.full_image);
        TextView fulltitle = (TextView) findViewById(R.id.full_title);
        TextView fulldescription = (TextView) findViewById(R.id.full_description);
        TextView fulladres = (TextView) findViewById(R.id.full_adres);

        fullimage.setImageResource(listAdapter.cnt[position][3]);
        fulltitle.setText(listAdapter.cnt[position][1]);
        fulldescription.setText(listAdapter.cnt[position][4]);
        fulladres.setText(listAdapter.cnt[position][2]);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                /*startActivity(new Intent(this, MainActivity.class));*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}