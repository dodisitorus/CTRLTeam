package com.dodi.ctrlteam.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.contact.RainbowPresence;
import com.ale.infra.http.GetFileResponse;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.manager.IMMessage;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.ale.infra.manager.room.Room;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.adapter.MessageListAdapter;
import com.dodi.ctrlteam.common.UserPreferences;
import com.dodi.ctrlteam.permissions.PermissionsHelper;
import com.dodi.ctrlteam.rainbow.RainbowContact;
import com.dodi.ctrlteam.rainbow.RainbowContactListener;
import com.dodi.ctrlteam.rainbow.RainbowConversation;
import com.dodi.ctrlteam.rainbow.RainbowConversationListener;
import com.dodi.ctrlteam.rainbow.RainbowFile;
import com.dodi.ctrlteam.rainbow.RainbowFileListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlobalChat_Activity extends AppCompatActivity implements RainbowConversationListener.Conversation,
        MessageListAdapter.MessageListCallbackZoom, RainbowConversationListener.UploadFile,
        RainbowFileListener.DownloadFile, RainbowContactListener.Accept {

    private MessageListAdapter messageListAdapter;
    private ArrayItemList<IMMessage> messageList;
    private IRainbowConversation conversation;
    private Room room;
    private int PICK_FILE_RESULT_CODE = 2;
    private Uri fileUriMessageFinal;
    private LinearLayoutManager layoutManager;
    private boolean isScrollReached = false;
    private boolean isValidGetChat = false;
    private IRainbowContact userReceiver;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.reyclerview_message_list)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_conversations)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.edittext_chatbox)
    TextView messageText;
    @BindView(R.id.message_image_preview)
    ImageView message_image_preview;
    @BindView(R.id.bg_modal_progress)
    RelativeLayout bg_modal_progress;
    @BindView(R.id.progress_percent_text)
    TextView progress_percent_text;
    @BindView(R.id.label_progress_text)
    TextView label_progress_text;

    private IItemListChangeListener messageChangeListener = () -> runOnUiThread(() -> {
        updateConversation(conversation.getMessages());
        if (conversation.getMessages().getCount() > 0) {
            if (!isScrollReached) {
                recyclerView.smoothScrollToPosition(conversation.getMessages().getCount() - 1);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat_);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(v -> finish());

        swipeRefreshLayout.setRefreshing(true);

        if (getIntent().getStringExtra("typeChat").equals("private")) {
            userReceiver = RainbowSdk.instance().contacts().getContactFromJabberId(getIntent().getStringExtra("ContactJabberId"));
            RainbowConversation.getConversationFromContact(userReceiver.getContactId(), this);
            messageListAdapter = new MessageListAdapter(this, messageList, userReceiver, this);
            setToolbarChat(userReceiver);
        } else {
            room = RainbowSdk.instance().bubbles().findBubbleById(getIntent().getStringExtra("roomId"));
            RainbowConversation.getConversationFromRoom(room, this);
            messageListAdapter = new MessageListAdapter(this, messageList, this);
            setToolbarRoom(room);
        }

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(messageListAdapter);

        // get more message
        addScrollReachedListener();

        swipeRefreshLayout.setOnRefreshListener(this::getMoreMessage);
    }

    private void setToolbarChat(IRainbowContact contact) {
        toolbar.setTitle(contact.getFirstName() + " " + contact.getLastName());
        RainbowPresence presence = contact.getPresence();
        switch (presence) {
            case ONLINE:
                toolbar.setSubtitle("Online");
                break;
            case OFFLINE:
                toolbar.setSubtitle("Offline");
                break;
            case MOBILE_ONLINE:
                toolbar.setSubtitle("On Mobile");
                break;
            case AWAY:
                toolbar.setSubtitle("Away");
                break;
            case MANUAL_AWAY:
                toolbar.setSubtitle("Away");
                break;
            case BUSY:
                toolbar.setSubtitle("Busy");
                break;
            case DND:
                toolbar.setSubtitle("Do Not Disturb");
                break;
            case DND_PRESENTATION:
                toolbar.setSubtitle("Presenting");
                break;
        }

        // set option on room chat
        toolbar.inflateMenu(R.menu.main_menu_personal);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_file:
                    navigateFiles();
                    return true;
                case R.id.decline_contact:
                    showDialogDeclineUser();
                    return true;
                default:
                    return false;
            }
        });
    }

    private void showDialogDeclineUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // set icon image accept ask
        builder.setMessage("Decline this contact ?.");

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    declineUser();
                    dialog.cancel();
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void declineUser() {
        RainbowContact.declineInvitation(userReceiver.getInvitationId(), this);
    }

    private void navigateMembers() {
        Intent intent = new Intent(this, ListMember_Activity.class);
        intent.putExtra("typeNav", "room");
        intent.putExtra("roomId", getIntent().getStringExtra("roomId"));
        startActivity(intent);
    }

    private void navigateFiles() {
        Intent intent = new Intent(this, ListFile_Activity.class);
        if (conversation.isRoomType()) {
            intent.putExtra("typeNav", "bubble");
            intent.putExtra("roomId", room.getId());
        } else {
            intent.putExtra("typeNav", "contact");
            intent.putExtra("conversationId", conversation.getId());
        }

        startActivity(intent);
    }

    private void setToolbarRoom(Room room) {
        toolbar.setTitle(room.getName());

        List<Contact> contacts = room.getParticipantsAsContactList();
        if (contacts.size() <= 2) {
            showDialogAskAddMember();
        }

        String member1 = contacts.get(0).getFirstName() + " " + contacts.get(0).getLastName();
        StringBuilder subtitle = new StringBuilder(itsMe(contacts.get(0)) ? "You" : member1);

        for (int m = 1; m < contacts.size(); m++) {
            subtitle.append(", ").append(contacts.get(m).getFirstName()).append(" ").append(contacts.get(m).getLastName());
        }

        toolbar.setSubtitle(subtitle.toString());

        // set option on room chat
        toolbar.inflateMenu(R.menu.main_menu_room);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_member:
                    navigateMembers();
                    return true;
                case R.id.navigation_file:
                    navigateFiles();
                    return true;
                case R.id.navigation_invite:
                    navigateInvites();
                    return true;
                default:
                    return false;
            }
        });
    }

    private void showDialogAskAddMember() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // set icon image accept ask
        builder.setMessage("This project have " + room.getParticipantsAsContactList().size() + " members, add new members.");

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    navigateInvites();
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void navigateInvites() {
        Intent intent = new Intent(GlobalChat_Activity.this, SearchActivity.class);
        intent.putExtra("roomId", room.getId());
        startActivity(intent);
    }

    private void addScrollReachedListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerViewOn, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems == 0) {
                    if (messageList != null && messageList.getCount() > 15) {
                        Log.d("TAG", "message");
                        //getMoreMessage();
                    }
                }
            }
        });
    }

    private void getMoreMessage() {
        isScrollReached = true;
        RainbowSdk.instance().im().getMoreMessagesFromConversation(conversation, 20);
    }

    private void updateConversation(ArrayItemList<IMMessage> messages) {
        messageList = messages;
        if (messageList != null) {
            messageListAdapter.updateListMessage(messages);
        }
        swipeRefreshLayout.setRefreshing(false);
        isScrollReached = false;
    }

    private boolean itsMe(Contact contact) {
        return contact.getContactId().equals(UserPreferences.getData(UserPreferences.userIdKey, this));
    }

    @Override
    public void onSuccessConversation(IRainbowConversation iRainbowConversation) {
        conversation = iRainbowConversation;
        conversation.getMessages().registerChangeListener(messageChangeListener);
        runOnUiThread(() -> {
            messageListAdapter.updateListMessage(conversation.getMessages());
            swipeRefreshLayout.setRefreshing(false);
            if (conversation.getMessages().getCount() > 0) {
                if (!isScrollReached) {
                    recyclerView.smoothScrollToPosition(conversation.getMessages().getCount() - 1);
                }
            }
        });
    }

    @Override
    public void onFailedConversation(String error) {
        runOnUiThread(() -> {
            isValidGetChat = true;
            Toast.makeText(GlobalChat_Activity.this, error, Toast.LENGTH_SHORT).show();
        });
    }

    public void sendMessage(View view) {
        if (!isValidGetChat) {
            if (fileUriMessageFinal != null) {
                String message = messageText.getText().toString().equals("")? "-" : messageText.getText().toString();
                if (conversation.isRoomType()) {
                    RainbowConversation.uploadFileToBubble(conversation.getRoom(), fileUriMessageFinal, message, this);
                } else {
                    RainbowConversation.uploadFileToConversation(conversation, fileUriMessageFinal, message, this);
                }
                message_image_preview.setVisibility(View.GONE);
                fileUriMessageFinal = null;
                showProgress(0, R.string.progress_upload);
            } else {
                RainbowSdk.instance().im().sendMessageToConversation(conversation, messageText.getText().toString());
            }
            messageText.setText("");
        } else {
            Toast.makeText(this, "Cannot get/send conversation", Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseFile(View view) {
        if(PermissionsHelper.instance().isExternalStorageAllowed(this, this)) {
            if(conversation != null) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICK_FILE_RESULT_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {

            if (data == null)
                return;

            Uri imageUri = data.getData();
            fileUriMessageFinal = imageUri;
            String path = Objects.requireNonNull(data.getData()).getPath();
            assert path != null;
            try {
                assert imageUri != null;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                message_image_preview.setVisibility(View.VISIBLE);
                message_image_preview.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    AlertDialog alertDialog;
    @SuppressLint("SetTextI18n")
    private void showProgress(int progress, int label) {
        Log.d("TAG", "" + progress);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(label);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void hideProgress() {
        alertDialog.hide();
    }

    @Override
    public void onUploadSuccess(RainbowFileDescriptor rainbowFileDescriptor) {
        runOnUiThread(() -> {
            Toast.makeText(GlobalChat_Activity.this, "Upload Success", Toast.LENGTH_SHORT).show();
            hideProgress();
        });
    }

    @Override
    public void onUploadFailed(RainbowServiceException e) {
        runOnUiThread(() -> {
            Toast.makeText(GlobalChat_Activity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            hideProgress();
        });
    }

    @Override
    public void onUploadProgress(int progress) {
        runOnUiThread(() -> showProgress(progress, R.string.progress_upload));
    }

    private void downloadFile(IMMessage message) {
        RainbowFile.download(message.getFileDescriptor(), this);
    }

    @Override
    public void onFileClicked(IMMessage message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to download this file ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    downloadFile(message);
                    showProgress(0, R.string.progress_download);
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        if (message.getFileDescriptor().isImageType()) {
            builder.setNegativeButton(
                    "Zoom Image",
                    (dialog, id) -> {
                        Intent intent = new Intent(GlobalChat_Activity.this, ZoomImage.class);

                        intent.putExtra("image", message.getFileDescriptor().getImage());

                        startActivity(intent);
                    });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDownloadSuccess(GetFileResponse getFileResponse) {
        runOnUiThread(() -> {
            Toast.makeText(GlobalChat_Activity.this, "Download Success", Toast.LENGTH_SHORT).show();
            hideProgress();
        });
    }

    @Override
    public void onDownloadFailed(boolean b) {
        runOnUiThread(() -> {
            Toast.makeText(GlobalChat_Activity.this, "Download Failed", Toast.LENGTH_SHORT).show();
            hideProgress();
        });
    }

    @Override
    public void onDownloadInProgress(GetFileResponse getFileResponse) {
        runOnUiThread(() -> showProgress(getFileResponse.getPercentDownloaded(), R.string.progress_download));
    }

    @Override
    public void onAcceptSuccess() {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(this, "Success Accept", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDeclineSuccess() {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(this, "Success Decline", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onError() {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onNewReceivedInvitation(List<IRainbowContact> list) {
        runOnUiThread(this::hideProgress);
    }
}
