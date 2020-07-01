package com.dodi.ctrlteam.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.calendar.CalendarPresence;
import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IContact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.contact.PhoneNumber;
import com.ale.infra.contact.RainbowPresence;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.room.Room;
import com.ale.listener.IRainbowContactsSearchListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.adapter.SearchAdapter;
import com.dodi.ctrlteam.common.UserPreferences;
import com.dodi.ctrlteam.common.Utils;
import com.dodi.ctrlteam.rainbow.RainbowBubble;
import com.dodi.ctrlteam.rainbow.RainbowBubbleListener;
import com.dodi.ctrlteam.rainbow.RainbowConnection;
import com.dodi.ctrlteam.rainbow.RainbowContact;
import com.dodi.ctrlteam.rainbow.RainbowContactListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements Contact.ContactListener,
        SearchAdapter.SearchAdapterCallback, RainbowBubbleListener.AddParticipants,
        RainbowContactListener.Invite {

    private List<IRainbowContact> friendList = new ArrayList<>();
    private List<IRainbowContact> contactsChoose = new ArrayList<>();
    private SearchAdapter mAdapter;
    private boolean isStartSearch = false;

    @BindView(R.id.list_item)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bg_modal_progress)
    RelativeLayout bg_modal_progress;
    @BindView(R.id.progress_percent_text)
    TextView progress_percent_text;
    @BindView(R.id.label_progress_text)
    TextView label_progress_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        // set toolbar
        setToolbar();

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this::prepareFriendChatList);

        // recycle view init
        mAdapter = new SearchAdapter(this, friendList, this, contactsChoose);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        updateContact();
        prepareFriendChatList();
    }

    private void setToolbar() {
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(R.string.list_contact);
        toolbar.inflateMenu(R.menu.menu_seach);

        Menu menu = toolbar.getMenu();
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (getIntent().getStringExtra("roomId") != null) {
                    mAdapter.getFilter().filter(query);
                } else if (getIntent().getBooleanExtra("invite", false)) {
                    searchContactBy(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (getIntent().getStringExtra("roomId") != null) {
                    mAdapter.getFilter().filter(newText);
                } else if (getIntent().getBooleanExtra("invite", false)) {
                    searchContactBy(newText);
                }
                return false;
            }
        });
    }

    private void prepareFriendChatList() {
        swipeRefreshLayout.setRefreshing(true);
        updateContact();
        searchContactBy("A");
    }

    private void updateContact() {
        RainbowConnection.registerAllContact(this, this);

        // check if from room project
        if (getIntent().getStringExtra("roomId") != null) {

            friendList = RainbowConnection.getRainbowContacts(this);
            if (friendList.size() == 0 && !isStartSearch) {
                Toast.makeText(this, "Empty Contact List", Toast.LENGTH_SHORT).show();
            }

            List<IRainbowContact> contactList = RainbowConnection.getRainbowContacts(this);
            List<IRainbowContact> memberList = getRainbowRoomMember();

            // filtering contacts
            for (IRainbowContact member: memberList) {
                contactList.remove(member);
            }

            friendList = contactList;
        }

        mAdapter.updateContact(friendList, contactsChoose);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void searchContactBy(String name) {
        if (!name.isEmpty()) {
            swipeRefreshLayout.setRefreshing(true);
            RainbowSdk.instance().contacts().searchByName(name, new IRainbowContactsSearchListener() {
                @Override
                public void searchStarted() {
                    runOnUiThread(() -> isStartSearch = true);
                }

                @Override
                public void searchFinished(List<IContact> list) {
                    runOnUiThread(() -> {
                        friendList = new ArrayList<>();
                        for (IContact contact: list) {
                            friendList.add(iRainbowContact(contact));
                        }
                        updateContact();
                    });
                }

                @Override
                public void searchError(RainbowServiceException e) {
                    runOnUiThread(() -> Toast.makeText(SearchActivity.this, e.getDetailsMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

    private List<IRainbowContact> getRainbowRoomMember() {

        Room room = RainbowSdk.instance().bubbles().findBubbleById(getIntent().getStringExtra("roomId"));
        List<Contact> contacts = room.getParticipantsAsContactList();

        IRainbowContact myContact = RainbowSdk.instance().contacts().getContactFromJabberId(UserPreferences.getData(UserPreferences.contactJabberId, this));

        List<IRainbowContact> irainbowMembers = new ArrayList<>(contacts);

        // remove current user contact from member list
        irainbowMembers.remove(myContact);

        return irainbowMembers;
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
    public void onItemListClicked(int position, IRainbowContact contact) {
        if (getIntent().getBooleanExtra("invite", false)) {
            contactsChoose = new ArrayList<>();
        }
        if (!contactsChoose.contains(contact)) {
            contactsChoose.add(contact);
        } else {
            contactsChoose.remove(contact);
        }
        mAdapter.updateCheck(contactsChoose);
    }

    public void onNextAction(View view) {
        if (contactsChoose.size() != 0) {
            if (getIntent().getBooleanExtra("invite", false)) {
                askInviteContact();
            } else {
                askAddParticipants();
            }
        } else {
            Toast.makeText(this, "Please choose one or more member to invite.", Toast.LENGTH_SHORT).show();
        }
    }

    private void askAddParticipants() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // set icon image accept ask
        if (contactsChoose.size() == 1) {
            builder.setMessage("Are you want add this contact to room ?");
        } else {
            builder.setMessage("Are you want add this contacts to room ?");
        }

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    showProgress();
                    Room room;
                    room = RainbowSdk.instance().bubbles().findBubbleById(getIntent().getStringExtra("roomId"));
                    RainbowBubble.addParticipantsToBubble(room, contactsChoose, this);
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void askInviteContact() {
        Utils.hideSoftKey(toolbar);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // set icon image accept ask
        builder.setMessage("Are you want invite this contact ?");

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    inviteUserToNetwork();
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void inviteUserToNetwork() {
        showProgress();
        IRainbowContact contactInvite = contactsChoose.get(0);
        RainbowContact.addRoster(contactInvite.getCorporateId(), this);
    }

    AlertDialog alertDialog;
    @SuppressLint("SetTextI18n")
    private void showProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.progress_inviting);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void hideProgress() {
        alertDialog.hide();
    }

    @Override
    public void onAddParticipantsSuccess() {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(this, "Success Add Participants", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void onMaxParticipantsReached() {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(this, "Max Participants Reached", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onAddParticipantFailed(Contact contact) {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(this, "Failed Add Participants", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onInvitationSentSuccess() {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(getApplicationContext(), "Invitation Sent Success", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onInvitationSentError(RainbowServiceException error) {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(this, error.getDetailsMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onInvitationError() {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(this, "Invitation Failed/Error", Toast.LENGTH_SHORT).show();
        });
    }

    private IRainbowContact iRainbowContact(IContact contact) {
        return new IRainbowContact() {
            @Override
            public String getCorporateId() {
                return contact.getCorporateId();
            }

            @Override
            public String getContactId() {
                return contact.getId();
            }

            @Override
            public String getImJabberId() {
                return contact.getImJabberId();
            }

            @Override
            public String getCompanyId() {
                return contact.getCompanyId();
            }

            @Override
            public Bitmap getPhoto() {
                return contact.getPhoto();
            }

            @Override
            public String getFirstName() {
                return contact.getFirstName();
            }

            @Override
            public PhoneNumber getFirstOfficePhoneNumber() {
                return contact.getFirstOfficePhoneNumber();
            }

            @Override
            public String getLastName() {
                return contact.getLastName();
            }

            @Override
            public String getLoginEmail() {
                return contact.getFirstEmailAddress();
            }

            @Override
            public RainbowPresence getPresence() {
                return RainbowPresence.OFFLINE;
            }

            @Override
            public CalendarPresence getCalendarPresence() {
                return null;
            }

            @Override
            public String getCompanyName() {
                return null;
            }

            @Override
            public boolean isBot() {
                return false;
            }

            @Override
            public String getMainEmailAddress() {
                return contact.getMainEmailAddress();
            }

            @Override
            public String getFirstEmailAddress() {
                return contact.getFirstEmailAddress();
            }

            @Override
            public String getJobTitle() {
                return contact.getJobTitle();
            }

            @Override
            public String getNickName() {
                return contact.getNickName();
            }

            @Override
            public void setInvitationId(String s) {
                contact.setCorporateId(s);
            }

            @Override
            public String getInvitationId() {
                return null;
            }

            @Override
            public boolean isPublicChannelsAdminActivated() {
                return true;
            }

            @Override
            public boolean isPrivateChannelsAdminActivated() {
                return false;
            }

            @Override
            public void registerChangeListener(Contact.ContactListener contactListener) {

            }

            @Override
            public void unregisterChangeListener(Contact.ContactListener contactListener) {

            }
        };
    }
}
