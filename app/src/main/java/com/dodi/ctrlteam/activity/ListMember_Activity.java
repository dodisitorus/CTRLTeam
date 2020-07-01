package com.dodi.ctrlteam.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.manager.room.Room;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.adapter.FriendAdapter;
import com.dodi.ctrlteam.common.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMember_Activity extends AppCompatActivity implements FriendAdapter.FriendAdapterCallback {

    private List<IRainbowContact> friendList = new ArrayList<>();
    private FriendAdapter mAdapter;

    @BindView(R.id.listchat)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_member_);
        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(this::prepareFriendChatList);

        // toolbar init
        toolbar.setTitle(R.string.list_members);
        toolbar.setNavigationOnClickListener(v -> finish());

        // recycle view init
        mAdapter = new FriendAdapter(this, friendList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void prepareFriendChatList() {
        swipeRefreshLayout.setRefreshing(true);
        updateContact();
    }

    private void updateContact() {
        swipeRefreshLayout.setRefreshing(true);

        friendList = getRainbowRoomMember();

        if (friendList != null) {
            mAdapter.updateContact(friendList);
        } else {
            Toast.makeText(this, "Member Not Found", Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    private List<IRainbowContact> getRainbowRoomMember() {
        List<IRainbowContact> irainbowMembers = new ArrayList<>();

        Room room = RainbowSdk.instance().bubbles().findBubbleById(getIntent().getStringExtra("roomId"));
        List<Contact> contacts = room.getParticipantsAsContactList();

        for (int c = 0; c < contacts.size(); c++) {
            IRainbowContact contact = contacts.get(c);
            if (!contact.getContactId().equals(UserPreferences.getData(UserPreferences.userIdKey, this))) {
                irainbowMembers.add(contact);
            }
        }
        return irainbowMembers;
    }

    @Override
    public void onItemListClicked(int position) {
        Intent intent = new Intent(this, GlobalChat_Activity.class);

        IRainbowContact contact = friendList.get(position);

        intent.putExtra("ContactJabberId", contact.getImJabberId());
        intent.putExtra("typeChat", "private");

        startActivity(intent);
    }
}
