package com.amt.latestjokes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by Mujahid on 4/14/2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Context context;
    List<Item> list;

    public MainAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      /*  View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cardview, null);
        ViewHolder mh = new ViewHolder(v);
        return mh;*/
        View rootView = LayoutInflater.from(context).inflate(R.layout.main_cardview, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item data = list.get(position);
        Typeface fonts = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        Typeface fontawesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");
        holder.title.setTypeface(fonts);
        final Document document = Jsoup.parse(data.getContent());
        //Elements elements = document.select("img");
        holder.title.setText(document.text());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        String parseDate = null;
        Date result;
        try {
            //2018-02-20T17:48:52
            result = df.parse(data.getPublished());
            System.out.println("date:" + result); //prints date in current locale
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm aa");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            System.out.println(sdf.format(result)); //prints date in the format sdf
            parseDate = sdf.format(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.date.setText(parseDate);
        final String finalParseDate = parseDate;
        holder.gotoNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SingleActivity.class);
                i.putExtra("date", finalParseDate);
                i.putExtra("data", document.text());
                context.startActivity(i);
                final PublisherInterstitialAd mPublisherInterstitialAd = new PublisherInterstitialAd(context);
                mPublisherInterstitialAd.setAdUnitId("ca-app-pub-8267269636738230/5545007705");
                mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
                mPublisherInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        if (mPublisherInterstitialAd.isLoaded()) {
                            mPublisherInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when the ad is displayed.
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when when the interstitial ad is closed.
                    }
                });
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SingleActivity.class);
                i.putExtra("date", finalParseDate);
                i.putExtra("data", data.getContent());
                context.startActivity(i);
                final PublisherInterstitialAd mPublisherInterstitialAd = new PublisherInterstitialAd(context);
                mPublisherInterstitialAd.setAdUnitId("ca-app-pub-8267269636738230/5545007705");
                mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
                mPublisherInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        if (mPublisherInterstitialAd.isLoaded()) {
                            mPublisherInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when the ad is displayed.
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when when the interstitial ad is closed.
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date;
        LinearLayout gotoNextPage;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            gotoNextPage = itemView.findViewById(R.id.nextPage);
        }
    }
}