package com.dodi.ctrlteam.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.contact.RainbowPresence;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.activity.GlobalChat_Activity;
import com.dodi.ctrlteam.adapter.FriendAdapter;
import com.dodi.ctrlteam.rainbow.RainbowConnection;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllContactFragment extends Fragment implements Contact.ContactListener, FriendAdapter.FriendAdapterCallback {

    private List<IRainbowContact> friendList = new ArrayList<>();
    private FriendAdapter mAdapter;

    @BindView(R.id.list_item)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_all, container, false);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this::prepareFriendChatList);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        // recycle view init
        mAdapter = new FriendAdapter(getContext(), friendList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareFriendChatList();
    }

    private void prepareFriendChatList() {
        swipeRefreshLayout.setRefreshing(true);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        if (RainbowSdk.instance().connection().isConnected()) {
            updateContact();
        }
    }

    private void updateContact() {
        friendList = RainbowConnection.getRainbowContacts(Objects.requireNonNull(getContext()));

        if (friendList != null) {
            mAdapter.updateContact(friendList);
            swipeRefreshLayout.setRefreshing(false);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void contactUpdated(Contact contact) {
        updateContact();
    }

    @Override
    public void onPresenceChanged(Contact contact, RainbowPresence rainbowPresence) {

    }

    @Override
    public void onActionInProgress(boolean b) {

    }

    @Override
    public void onItemListClicked(int position) {
            Intent intent = new Intent(getContext(), GlobalChat_Activity.class);

        IRainbowContact contact = friendList.get(position);

        intent.putExtra("ContactJabberId", contact.getImJabberId());
        intent.putExtra("typeChat", "private");

        startActivity(intent);
    }
}
