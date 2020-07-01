package com.dodi.ctrlteam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ale.infra.manager.room.Room;
import com.dodi.ctrlteam.R;

import java.util.ArrayList;
import java.util.List;

public class BubbleAdapter extends RecyclerView.Adapter<BubbleHolder> {

    private List<Room> roomList;
    private BubbleAdapterCallback mAdapterCallback;
    private Context context;

    public BubbleAdapter(Context context, List<Room> rooms, BubbleAdapterCallback mAdapterCallback) {
        Log.d("TAG", context.toString());
        this.roomList = rooms;
        this.mAdapterCallback = mAdapterCallback;
        this.context = context;
    }

    @NonNull
    @Override
    public BubbleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        return new BubbleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BubbleHolder holder, int position) {

        holder.bindContent(roomList.get(position), context);

        holder.itemLayout.setOnClickListener(view -> mAdapterCallback.onItemListClicked(position));
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void updateAdapter(List<Room> rooms){
        this.roomList = new ArrayList<>();
        this.roomList = rooms;

        notifyDataSetChanged();
    }

    public interface BubbleAdapterCallback {
        void onItemListClicked(int position);
    }
}
