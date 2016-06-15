package com.matic.laradiodetotoral.rss;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matic.laradiodetotoral.R;

import java.util.ArrayList;

/**
 * Created by matic on 14/06/16.
 */
public class RSSAdapter extends RecyclerView.Adapter<RSSAdapter.RSSViewHolder>{

    private ArrayList<FeedItem> items;
    private Context context;


    public RSSAdapter(Context context, ArrayList<FeedItem> items) {
        this.context=context;
        this.items = items;
    }

    public static class RSSViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvDescripcion;
        private TextView tvCategoria;
        private TextView tvLink;
        private TextView tvPubDate;

        public RSSViewHolder(View v) {
            super(v);
            tvTitle= (TextView) v.findViewById(R.id.lbl_title);
            tvDescripcion= (TextView) v.findViewById(R.id.lbl_descripcion);
            tvCategoria= (TextView) v.findViewById(R.id.lbl_category);
            tvLink= (TextView) v.findViewById(R.id.lbl_link);
            tvPubDate= (TextView) v.findViewById(R.id.lbl_pubDate);
        }

    }


    @Override
    public RSSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.card_view_rss,parent,false);
        RSSViewHolder holder=new RSSViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RSSViewHolder holder, int i) {

        holder.tvTitle.setText(items.get(i).getTitle());
        holder.tvDescripcion.setText(items.get(i).getDescripcion());
        holder.tvCategoria.setText(items.get(i).getCategory());
        holder.tvLink.setText(items.get(i).getLink());
        holder.tvPubDate.setText(items.get(i).getPubDate());


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}