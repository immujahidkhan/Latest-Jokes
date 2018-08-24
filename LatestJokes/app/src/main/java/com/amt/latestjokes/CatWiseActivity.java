package com.amt.latestjokes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.ybq.android.spinkit.SpinKitView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amt.latestjokes.ApiKeys.OrderByCats;
import static com.amt.latestjokes.ApiKeys.Url;
import static com.amt.latestjokes.ApiKeys.keys;

public class CatWiseActivity extends AppCompatActivity {
    RecyclerView recyclerView, recyclerMenu;
    SpinKitView spin_kit;
    String Labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_wise);
        Labels = getIntent().getStringExtra("labels");
        getSupportActionBar().setTitle(Labels);
        recyclerView = findViewById(R.id.recyclerView);
        spin_kit = findViewById(R.id.spin_kit);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        String url = OrderByCats + Labels + "&maxResults=100&key=" + keys;
        Call<PostList> allStatusCall = ApiKeys.getPostService().getPostList(url);
        allStatusCall.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList list = response.body();
                Log.d("data", list.toString());
                recyclerView.setAdapter(new MainAdapter(CatWiseActivity.this, list.getItems()));
                spin_kit.setVisibility(View.GONE);
                //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.contact) {
            Intent contact = new Intent(Intent.ACTION_SEND);
            contact.setPackage("com.google.android.gm");
            contact.setData(Uri.parse("email"));
            String[] s = {"immujahidkhan6@gmail.com"};
            contact.putExtra(Intent.EXTRA_EMAIL, s);
            contact.putExtra(Intent.EXTRA_SUBJECT, "Recommend subject Here");
            contact.putExtra(Intent.EXTRA_TEXT, "Recommend Message Here");
            contact.setType("message/rfc822");
            Intent chooser = Intent.createChooser(contact, "Choose Gmail for contact us");
            startActivity(chooser);
        }
        if (id == R.id.rate_us) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        }
        if (id == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Latest Jokes  ,Download it from here : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}