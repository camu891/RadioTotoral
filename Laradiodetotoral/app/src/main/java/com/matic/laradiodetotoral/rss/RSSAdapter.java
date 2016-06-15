package com.matic.laradiodetotoral.rss;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.matic.laradiodetotoral.NewsDetailsActivity;
import com.matic.laradiodetotoral.R;
import com.squareup.picasso.Picasso;

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
        private ImageView thumbnail;
        private CardView cardView;

        public RSSViewHolder(View v) {
            super(v);
            tvTitle= (TextView) v.findViewById(R.id.lbl_title);
            tvDescripcion= (TextView) v.findViewById(R.id.lbl_descripcion);
            tvCategoria= (TextView) v.findViewById(R.id.lbl_category);
            tvLink= (TextView) v.findViewById(R.id.lbl_link);
            tvPubDate= (TextView) v.findViewById(R.id.lbl_pubDate);
            thumbnail= (ImageView) v.findViewById(R.id.thumbnail);

            cardView= (CardView) v.findViewById(R.id.card_view);

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

        final FeedItem currentItem=items.get(i);

        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);

        holder.tvTitle.setText(currentItem.getTitle());
        holder.tvDescripcion.setText(currentItem.getDescripcion()+"...");
        holder.tvCategoria.setText(currentItem.getCategory());
        holder.tvLink.setText(currentItem.getLink());
        holder.tvPubDate.setText(currentItem.getPubDate());
        Picasso.with(context).load(currentItem.getThumbnailUrl()).into(holder.thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act=new Intent(context, NewsDetailsActivity.class);
                act.putExtra("Link",currentItem.getLink());
                context.startActivity(act);
            }
        });


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