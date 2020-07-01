package com.dodi.ctrlteam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.channel.Channel;
import com.ale.infra.proxy.channel.IChannelProxy;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;

import java.util.ArrayList;
import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelHolder> implements Filterable {

    private List<Channel> channelList;
    private ChannelAdapterCallback mAdapterCallback;
    private boolean isSearch = false;
    private boolean isHorizontal;
    private boolean isSubsList;

    public ChannelAdapter(List<Channel> list, ChannelAdapterCallback callback, boolean horizontal, boolean subsList, boolean isSearch) {
        this.channelList = list;
        this.mAdapterCallback = callback;
        this.isHorizontal = horizontal;
        this.isSubsList = subsList;
    }

    @NonNull
    @Override
    public ChannelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (isSubsList && isHorizontal) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_subs, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        }
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        itemView.setLayoutParams(layoutParams);
        return new ChannelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelHolder holder, int position) {
        holder.bindContent(channelList.get(position), isSearch);

        holder.itemLayout.setOnClickListener(view -> mAdapterCallback.onItemListClicked(position));

        holder.buttonFollow.setOnClickListener(View -> mAdapterCallback.onButtomFollowClicked(position));
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    public void updateAdapter(List<Channel> channels){
        this.channelList = new ArrayList<>();
        this.channelList = channels;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public interface ChannelAdapterCallback {
        void onItemListClicked(int position);

        void onButtomFollowClicked(int position);
    }
}
