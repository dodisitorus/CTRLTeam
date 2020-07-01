package com.dodi.ctrlteam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ale.infra.contact.IRainbowContact;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendHolder> {

    private List<IRainbowContact> friendList;
    private FriendAdapterCallback mAdapterCallback;
    private Context context;

    public FriendAdapter(Context context, List<IRainbowContact> friendList, FriendAdapterCallback friendAdapterCallback) {
        this.context = context;
        this.friendList = friendList;
        this.mAdapterCallback = friendAdapterCallback;
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        holder.bindContent(friendList.get(position), context);
        holder.itemLayout.setOnClickListener(view -> mAdapterCallback.onItemListClicked(position));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void updateContact(List<IRainbowContact> friendList) {
        this.friendList = new ArrayList<>();
        this.friendList.addAll(friendList);

        notifyDataSetChanged();
    }

    public interface FriendAdapterCallback {
        void onItemListClicked(int position);
    }
}
