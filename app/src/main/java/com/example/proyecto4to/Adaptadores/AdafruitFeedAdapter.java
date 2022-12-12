package com.example.proyecto4to.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto4to.Modelos.FeedData;
import com.example.proyecto4to.R;

import java.util.ArrayList;

public class AdafruitFeedAdapter extends RecyclerView.Adapter<AdafruitFeedAdapter.viewholder> {
    /*protected AdafruitFeedAdapter.ItemListener itemListener;*/
    ArrayList<FeedData> feedData;
    //Context context;

    public AdafruitFeedAdapter(ArrayList<FeedData> feedData) {
        this.feedData = feedData;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);

        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(feedData.get(position));
    }

    @Override
    public int getItemCount() {
        return feedData.size();
    }

    /*public interface ItemListener {
        void onItemClick(FeedData item);
    }*/

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button btnMisFeeds;
        FeedData dataHolder;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            btnMisFeeds = itemView.findViewById(R.id.btnMisFeeds);
            btnMisFeeds.setOnClickListener(this);
        }

        public void setData(FeedData feedData) {
            dataHolder = feedData;
            btnMisFeeds.setText(dataHolder.getName());
        }

        @Override
        public void onClick(View v) {
            /*if (itemListener != null) {
                itemListener.onItemClick(dataHolder);
            }*/
        }
    }
}
