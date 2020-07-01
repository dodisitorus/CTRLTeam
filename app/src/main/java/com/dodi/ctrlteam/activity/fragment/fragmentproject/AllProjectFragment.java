package com.dodi.ctrlteam.activity.fragment.fragmentproject;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ale.infra.manager.room.Room;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.activity.GlobalChat_Activity;
import com.dodi.ctrlteam.adapter.BubbleAdapter;
import com.dodi.ctrlteam.rainbow.RainbowBubble;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllProjectFragment extends Fragment implements BubbleAdapter.BubbleAdapterCallback{

    private List<Room> roomList = new ArrayList<>();
    private BubbleAdapter mAdapter;
    @BindView(R.id.list_item)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_all, container, false);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setRefreshing(true);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        // recycle view init
        mAdapter = new BubbleAdapter(Objects.requireNonNull(getContext()), roomList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(this::updateList);
        updateList();
    }

    private void updateList() {
        swipeRefreshLayout.setRefreshing(true);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        roomList = RainbowBubble.getMyList();
        if (roomList != null) {
            mAdapter.updateAdapter(roomList);
            swipeRefreshLayout.setRefreshing(false);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemListClicked(int position) {
        Intent intent = new Intent(getContext(), GlobalChat_Activity.class);
        intent.putExtra("typeChat", "room");
        intent.putExtra("roomId", roomList.get(position).getId());
        startActivity(intent);
    }
}
