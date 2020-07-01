package com.dodi.ctrlteam.activity.fragment;

import android.annotation.SuppressLint;
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

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.contact.RainbowPresence;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.adapter.FriendAdapter;
import com.dodi.ctrlteam.rainbow.RainbowConnection;
import com.dodi.ctrlteam.rainbow.RainbowContact;
import com.dodi.ctrlteam.rainbow.RainbowContactListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InviteReceiveFragment extends Fragment implements Contact.ContactListener,
        FriendAdapter.FriendAdapterCallback, RainbowContactListener.Accept, RainbowContactListener.RemoveRoster {

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
        View view = inflater.inflate(R.layout.fragment_invite_receive, container, false);
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
            RainbowSdk.instance().contacts().getReceivedInvitations().registerChangeListener(contactListener);
        }
    }

    private void updateContact() {
        RainbowConnection.registerAllContact(this, getContext());
        friendList = RainbowConnection.getReceivedInvitations(Objects.requireNonNull(getContext()));

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
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setCancelable(true);
        // set icon image accept ask
        builder.setMessage("Accept Invitation ?");

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    showProgress();
                    RainbowContact.acceptInvitation(friendList.get(position).getInvitationId(), this);
                });

        builder.setNegativeButton(
                "Decline",
                (dialog, id) -> {
                    dialog.cancel();
                    showProgress();
                    RainbowContact.declineInvitation(friendList.get(position).getInvitationId(), this);
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    AlertDialog alertDialog;
    @SuppressLint("SetTextI18n")
    private void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setCancelable(false);
        builder.setTitle(R.string.progress_inviting);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void hideProgress() {
        alertDialog.hide();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAcceptSuccess() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(getContext(), "Success Accept", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDeclineSuccess() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(getContext(), "Success Decline", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onError() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onNewReceivedInvitation(List<IRainbowContact> list) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            hideProgress();
            friendList = list;
            mAdapter.updateContact(friendList);
        });
    }

    @Override
    public void onSuccess() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(getContext(), "Success Decline", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onFailure(RainbowServiceException error) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(getContext(), error.getDetailsMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}

