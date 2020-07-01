package com.dodi.ctrlteam.rainbow;

import android.content.Context;
import android.os.Looper;
import android.os.Handler;
import android.util.Log;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.manager.room.Room;
import com.ale.infra.manager.room.RoomParticipant;
import com.ale.infra.proxy.room.IRoomProxy;
import com.ale.rainbowsdk.RainbowSdk;

import java.util.List;


public class RainbowBubble {
    public static void createNew(String name, String deskripsi, boolean disableNotifications, Context context, RainbowBubbleListener.CreateBubble createBubble) {
        Log.d("TAG", context.toString());
        RainbowSdk.instance().bubbles().createBubble(name, deskripsi, disableNotifications, new IRoomProxy.IRoomCreationListener() {
            @Override
            public void onCreationSuccess(Room room) {
                new Handler(Looper.getMainLooper()).post(createBubble::onSuccessCreateBubble);
            }

            @Override
            public void onCreationFailed() {
                new Handler(Looper.getMainLooper()).post(createBubble::onFailedCreateBubble);
            }
        });
    }

    public static void acceptRoom(Room room, RainbowBubbleListener.AcceptInvite acceptInvite) {
        RainbowSdk.instance().bubbles().acceptInvitation(room, new IRoomProxy.IChangeUserRoomDataListener() {
            @Override
            public void onChangeUserRoomDataSuccess(RoomParticipant roomParticipant) {
                new Handler(Looper.getMainLooper()).post(() -> acceptInvite.onSucessAcceptInvite(roomParticipant));
            }

            @Override
            public void onChangeUserRoomDataFailed() {
                new Handler(Looper.getMainLooper()).post(acceptInvite::onFailedAcceptInvite);
            }
        });
    }

    public static void rejectRoom(Room room, RainbowBubbleListener.RejectInvite rejectInvite) {
        RainbowSdk.instance().bubbles().rejectInvitation(room, new IRoomProxy.IChangeUserRoomDataListener() {
            @Override
            public void onChangeUserRoomDataSuccess(RoomParticipant roomParticipant) {
                new Handler(Looper.getMainLooper()).post(() -> rejectInvite.onSucessRejectInvite(roomParticipant));
            }

            @Override
            public void onChangeUserRoomDataFailed() {
                new Handler(Looper.getMainLooper()).post(rejectInvite::onFailedRejectInvite);
            }
        });
    }

    public static void addParticipantsToBubble(Room room, List<IRainbowContact> contactList, RainbowBubbleListener.AddParticipants addParticipants) {
        RainbowSdk.instance().bubbles().addParticipantsToBubble(room, contactList, new IRoomProxy.IAddParticipantsListener() {
            @Override
            public void onAddParticipantsSuccess() {
                new Handler(Looper.getMainLooper()).post(addParticipants::onAddParticipantsSuccess);
            }

            @Override
            public void onMaxParticipantsReached() {
                new Handler(Looper.getMainLooper()).post(addParticipants::onMaxParticipantsReached);
            }

            @Override
            public void onAddParticipantFailed(Contact contact) {
                new Handler(Looper.getMainLooper()).post(() -> addParticipants.onAddParticipantFailed(contact));
            }
        });
    }

    public static List<Room> getArchivedList() {
        return RainbowSdk.instance().bubbles().getAllList();
    }

    public static List<Room> getPendingList() {
        return RainbowSdk.instance().bubbles().getPendingList();
    }

    public static List<Room> getMyList() {
        return RainbowSdk.instance().bubbles().getMyList();
    }
}
