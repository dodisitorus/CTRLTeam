package com.dodi.ctrlteam.activity.fragment.fragmentproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ale.infra.manager.room.Room;
import com.ale.infra.manager.room.RoomParticipant;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.adapter.BubbleAdapter;
import com.dodi.ctrlteam.rainbow.RainbowBubble;
import com.dodi.ctrlteam.rainbow.RainbowBubbleListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PendingProjectFragment extends Fragment implements BubbleAdapter.BubbleAdapterCallback,
        RainbowBubbleListener.AcceptInvite, RainbowBubbleListener.RejectInvite {

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
        View view = inflater.inflate(R.layout.fragment_project_pending, container, false);
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

        roomList = RainbowBubble.getPendingList();
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
        if (roomList.get(position).getUserStatus().isInvited()) {
            dialogInvite(position);
        }
    }

    private void dialogInvite(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setCancelable(true);
        // set icon image accept ask
        builder.setMessage("Are you accept this invitation ?");

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    swipeRefreshLayout.setRefreshing(true);
                    acceptThisBubble(position);
                });

        builder.setNegativeButton(
                "Reject",
                (dialog, id) -> {
                    dialog.cancel();
                    swipeRefreshLayout.setRefreshing(true);
                    rejectThisBubble(position);
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void acceptThisBubble(int position) {
        RainbowBubble.acceptRoom(roomList.get(position), this);
    }

    private void rejectThisBubble(int position) {
        RainbowBubble.rejectRoom(roomList.get(position), this);
    }

    @Override
    public void onSucessAcceptInvite(RoomParticipant roomParticipant) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Toast.makeText(getContext(), "Success Accepted", Toast.LENGTH_SHORT).show();
            updateList();
        });
    }

    @Override
    public void onFailedAcceptInvite() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), "Failed Accepted", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onSucessRejectInvite(RoomParticipant roomParticipant) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Toast.makeText(getContext(), "Success Rejected", Toast.LENGTH_SHORT).show();
            updateList();
        });
    }

    @Override
    public void onFailedRejectInvite() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), "Failed Rejected", Toast.LENGTH_SHORT).show());
    }
}
