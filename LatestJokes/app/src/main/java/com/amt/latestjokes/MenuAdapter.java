package com.amt.latestjokes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mujahid on 4/14/2018.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    Context context;
    ArrayList<Category> list;

    public MenuAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      /*  View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cardview, null);
        ViewHolder mh = new ViewHolder(v);
        return mh;*/
        View rootView = LayoutInflater.from(context).inflate(R.layout.menu_cardview, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Category data = list.get(position);
        Typeface fonts = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        Typeface fontawesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");
        holder.title.setTypeface(fonts);
        Document document = Jsoup.parse(data.getTerm());
        //Elements elements = document.select("img");
        holder.title.setText(document.text());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoCatWise = new Intent(context, CatWiseActivity.class);
                gotoCatWise.putExtra("labels", data.getTerm());
                context.startActivity(gotoCatWise);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}