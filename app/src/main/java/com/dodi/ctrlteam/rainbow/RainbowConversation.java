package com.dodi.ctrlteam.rainbow;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.fileserver.IFileProxy;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.ale.infra.manager.room.Room;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.listener.IRainbowGetConversationListener;
import com.ale.rainbowsdk.RainbowSdk;

public class RainbowConversation {
    public static void getConversationFromRoom(Room room, RainbowConversationListener.Conversation conversation) {
        RainbowSdk.instance().conversations().getConversationFromRoom(room, new IRainbowGetConversationListener() {
            @Override
            public void onGetConversationSuccess(IRainbowConversation iRainbowConversation) {
                new Handler(Looper.getMainLooper()).post(() -> conversation.onSuccessConversation(iRainbowConversation));
            }

            @Override
            public void onGetConversationError() {
                new Handler(Looper.getMainLooper()).post(() ->conversation.onFailedConversation("error get conversation"));
            }
        });
    }

    public static void getConversationFromContact(String contactId, RainbowConversationListener.Conversation conversation) {
        RainbowSdk.instance().conversations().getConversationFromContact(contactId, new IRainbowGetConversationListener() {
            @Override
            public void onGetConversationSuccess(IRainbowConversation iRainbowConversation) {
                new Handler(Looper.getMainLooper()).post(() -> conversation.onSuccessConversation(iRainbowConversation));
            }

            @Override
            public void onGetConversationError() {
                new Handler(Looper.getMainLooper()).post(() ->conversation.onFailedConversation("error get conversation"));
            }
        });
    }

    public static void uploadFileToConversation(IRainbowConversation iRainbowConversation, Uri fileUri, String messageText, RainbowConversationListener.UploadFile listener) {
        RainbowSdk.instance().fileStorage().uploadFileToConversation(iRainbowConversation, fileUri, messageText, new IFileProxy.IUploadFileListener() {
            @Override
            public void onUploadSuccess(RainbowFileDescriptor rainbowFileDescriptor) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onUploadSuccess(rainbowFileDescriptor));
            }

            @Override
            public void onUploadInProgress(int i) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onUploadProgress(i));
            }

            @Override
            public void onUploadFailed(RainbowServiceException e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onUploadFailed(e));
            }
        });
    }

    public static void uploadFileToBubble(Room room, Uri fileUri, String messageText, RainbowConversationListener.UploadFile listener) {
        RainbowSdk.instance().fileStorage().uploadFileToBubble(room, fileUri, messageText, new IFileProxy.IUploadFileListener() {
            @Override
            public void onUploadSuccess(RainbowFileDescriptor rainbowFileDescriptor) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onUploadSuccess(rainbowFileDescriptor));
            }

            @Override
            public void onUploadInProgress(int i) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onUploadProgress(i));
            }

            @Override
            public void onUploadFailed(RainbowServiceException e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onUploadFailed(e));
            }
        });
    }
}
