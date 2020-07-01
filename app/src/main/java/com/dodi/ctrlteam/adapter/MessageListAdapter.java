package com.dodi.ctrlteam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.manager.IMMessage;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.common.UserPreferences;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListHolder>  {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private ArrayItemList<IMMessage> mMessageList;
    private IRainbowContact userReceiver;
    private MessageListCallbackZoom mAdapterCallbackZoom;

    public MessageListAdapter(Context context, ArrayItemList<IMMessage> messageList, IRainbowContact receiver, MessageListCallbackZoom callbackZoom) {
        mContext = context;
        mMessageList = messageList;
        userReceiver = receiver;
        mAdapterCallbackZoom = callbackZoom;
    }

    public MessageListAdapter(Context context, ArrayItemList<IMMessage> messageList, MessageListCallbackZoom callbackZoom) {
        mContext = context;
        mMessageList = messageList;
        mAdapterCallbackZoom = callbackZoom;
    }

    @NonNull
    @Override
    public MessageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sender, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
        }

        return new MessageListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListHolder holder, int position) {
        if (userReceiver == null) {
            userReceiver = RainbowSdk.instance().contacts().getContactFromJabberId(mMessageList.get(position).getContactJid());
        }

        holder.bind(mMessageList.get(position), getItemViewType(position), userReceiver);

        // set view type
        getItemViewType(position);

        holder.messageImage.setOnClickListener(view -> mAdapterCallbackZoom.onFileClicked(mMessageList.get(position)));
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        IMMessage message = mMessageList.get(position);

        if (message.getContactJid().equals(UserPreferences.getData(UserPreferences.userIdKey, mContext))) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        if (mMessageList == null) {
            return 0;
        } else {
            return mMessageList.getCount();
        }
    }

    public void updateListMessage(ArrayItemList<IMMessage> messages){
        this.mMessageList = new ArrayItemList<>();
        this.mMessageList = messages;

        notifyDataSetChanged();
    }

    public interface MessageListCallbackZoom {
        void onFileClicked(IMMessage message);
    }
}
