package com.amt.latestjokes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amt.latestjokes.ApiKeys.CatsUrl;
import static com.amt.latestjokes.ApiKeys.Url;
import static com.amt.latestjokes.ApiKeys.keys;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView, recyclerMenu;
    SpinKitView spin_kit;
    RequestQueue requestQueue;
    ArrayList<Category> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerMenu = findViewById(R.id.recyclerMenu);
        spin_kit = findViewById(R.id.spin_kit);
        recyclerView.hasFixedSize();
        recyclerMenu.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        LoadApiData();
    }

    private void LoadApiData() {
        String url = Url + "?maxResults=100&key=" + keys;
        Call<PostList> allStatusCall = ApiKeys.getPostService().getPostList(url);
        allStatusCall.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList list = response.body();
                Log.d("data", list.toString());
                recyclerView.setAdapter(new MainAdapter(MainActivity.this, list.getItems()));
                spin_kit.setVisibility(View.GONE);
                //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {

            }
        });
        LoadMenus();
    }

    private void LoadMenus() {
        String url = "summary?alt=json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, CatsUrl + url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("json", response.toString());
                        try {
                            JSONObject jsonObject = response.getJSONObject("feed");
                            JSONArray jsonArray = jsonObject.getJSONArray("category");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String term = hit.getString("term");
                                list.add(new Category(term));
                            }
                            recyclerMenu.setAdapter(new MenuAdapter(MainActivity.this, list));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
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