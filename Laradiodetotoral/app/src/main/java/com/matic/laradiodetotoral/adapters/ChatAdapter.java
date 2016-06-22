package com.matic.laradiodetotoral.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.matic.laradiodetotoral.R;
import com.matic.laradiodetotoral.models.Chat;

import java.util.ArrayList;

/**
 * Created by matic on 22/06/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private ArrayList<Chat> items;
    private Context context;


    public ChatAdapter(Context context, ArrayList<Chat> items) {
        this.context=context;
        this.items = items;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView tvComment;
        private TextView tvDate;

        private CardView cardView;

        public ChatViewHolder(View v) {
            super(v);
            tvComment= (TextView) v.findViewById(R.id.lbl_comments);
            tvDate= (TextView) v.findViewById(R.id.lbl_date);
            cardView= (CardView) v.findViewById(R.id.card_view_chat);

        }

    }


    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_view_chat,parent,false);
        ChatViewHolder holder=new ChatViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int i) {

        final Chat currentItem=items.get(i);

        YoYo.with(Techniques.FadeInUp).playOn(holder.cardView);

        holder.tvComment.setText(currentItem.getComentario());
        holder.tvDate.setText(currentItem.getFecha());


        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act=new Intent(context, NewsDetailsActivity.class);
                act.putExtra("Link",currentItem.getLink());
                context.startActivity(act);
            }
        });*/


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