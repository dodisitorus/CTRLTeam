package com.dodi.ctrlteam.activity.fragment;

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
import com.ale.infra.list.IItemListChangeListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.adapter.FriendAdapter;
import com.dodi.ctrlteam.rainbow.RainbowConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteSentFragment extends Fragment implements Contact.ContactListener, FriendAdapter.FriendAdapterCallback {

    private List<IRainbowContact> friendList = new ArrayList<>();
    private FriendAdapter mAdapter;

    @BindView(R.id.list_item)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_list)
    SwipeRefreshLayout swipeRefreshLayout;

    private IItemListChangeListener contactListener = () -> Objects.requireNonNull(getActivity()).runOnUiThread(this::updateContact);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_sent, container, false);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this::prepareFriendChatList);

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
        if (RainbowSdk.instance().connection().isConnected()) {
            updateContact();
            RainbowSdk.instance().contacts().getSentInvitations().registerChangeListener(contactListener);
        }
    }

    private void updateContact() {
        RainbowConnection.registerAllContact(this, getContext());
        friendList = RainbowConnection.getSentInvitations(Objects.requireNonNull(getContext()));

        if (friendList != null) {
            mAdapter.updateContact(friendList);
            swipeRefreshLayout.setRefreshing(false);
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

    }
}
