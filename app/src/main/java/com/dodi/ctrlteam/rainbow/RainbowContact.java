package com.dodi.ctrlteam.rainbow;

import android.os.Handler;
import android.os.Looper;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.proxy.users.IUserProxy;
import com.ale.listener.IRainbowInvitationManagementListener;
import com.ale.listener.IRainbowSentInvitationListener;
import com.ale.rainbowsdk.RainbowSdk;

import java.util.List;

public class RainbowContact {

    public static void inviteUser(String contactCorporateId, String contactFirstEmail, String contactMainEmail, RainbowContactListener.Invite listener) {
        RainbowSdk.instance().contacts().inviteUserNotRegisteredToRainbow(contactCorporateId, contactFirstEmail, contactMainEmail, new IRainbowSentInvitationListener() {
            @Override
            public void onInvitationSentSuccess() {
                new Handler(Looper.getMainLooper()).post(listener::onInvitationSentSuccess);
            }

            @Override
            public void onInvitationSentError(RainbowServiceException e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onInvitationSentError(e));
            }

            @Override
            public void onInvitationError() {
                new Handler(Looper.getMainLooper()).post(listener::onInvitationError);
            }
        });
    }

    public static void addRoster(String corporateId, RainbowContactListener.Invite listener) {
        RainbowSdk.instance().contacts().addRainbowContactToRoster(corporateId, new IRainbowSentInvitationListener() {
            @Override
            public void onInvitationSentSuccess() {
                new Handler(Looper.getMainLooper()).post(listener::onInvitationSentSuccess);
            }

            @Override
            public void onInvitationSentError(RainbowServiceException e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onInvitationSentError(e));
            }

            @Override
            public void onInvitationError() {
                new Handler(Looper.getMainLooper()).post(listener::onInvitationError);
            }
        });
    }

    public static void removeRoster(String corporateId, RainbowContactListener.RemoveRoster listener) {
        RainbowSdk.instance().contacts().removeContactFromRoster(corporateId, new IUserProxy.IContactRemovedFromRosterListener() {
            @Override
            public void onSuccess() {
                new Handler(Looper.getMainLooper()).post(listener::onSuccess);
            }

            @Override
            public void onFailure(RainbowServiceException e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onFailure(e));
            }
        });
    }

    public static void acceptInvitation(String id, RainbowContactListener.Accept listener) {
        RainbowSdk.instance().contacts().acceptInvitation(id, new IRainbowInvitationManagementListener() {
            @Override
            public void onAcceptSuccess() {
                new Handler(Looper.getMainLooper()).post(listener::onAcceptSuccess);
            }

            @Override
            public void onDeclineSuccess() {
                new Handler(Looper.getMainLooper()).post(listener::onDeclineSuccess);
            }

            @Override
            public void onError() {
                new Handler(Looper.getMainLooper()).post(listener::onError);
            }

            @Override
            public void onNewReceivedInvitation(List<IRainbowContact> list) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onNewReceivedInvitation(list));
            }
        });
    }

    public static void declineInvitation(String id, RainbowContactListener.Accept listener) {
        RainbowSdk.instance().contacts().declineInvitation(id, new IRainbowInvitationManagementListener() {
            @Override
            public void onAcceptSuccess() {
                new Handler(Looper.getMainLooper()).post(listener::onAcceptSuccess);
            }

            @Override
            public void onDeclineSuccess() {
                new Handler(Looper.getMainLooper()).post(listener::onDeclineSuccess);
            }

            @Override
            public void onError() {
                new Handler(Looper.getMainLooper()).post(listener::onError);
            }

            @Override
            public void onNewReceivedInvitation(List<IRainbowContact> list) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onNewReceivedInvitation(list));
            }
        });
    }
}
