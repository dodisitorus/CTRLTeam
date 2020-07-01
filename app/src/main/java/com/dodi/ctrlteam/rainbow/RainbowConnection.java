package com.dodi.ctrlteam.rainbow;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.listener.IRainbowSentInvitationListener;
import com.ale.listener.SigninResponseListener;
import com.ale.listener.SignoutResponseListener;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;

import java.util.List;

public class RainbowConnection {

    private static final String AppID = "0a832a70588e11eabf7e77d14e87b936";
    private static final String AppSecret = "KZzFxVFtmC6rr92dpZEL6crVX1ccN1CDx2zYMh8dyOqgPSRKH1xeEj4hhUCehP0y";
    private static final String urlServer = "sandbox.openrainbow.com";

    public static void startConnection(RainbowConnectionListener.Connection connection, Context context) {
        RainbowSdk.instance().initialize(context, AppID,AppSecret);

        RainbowSdk.instance().connection().start(new StartResponseListener() {
            @Override
            public void onStartSucceeded() {
                new Handler(Looper.getMainLooper()).post(connection::onConnectionSuccess);
            }

            @Override
            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                new Handler(Looper.getMainLooper()).post(() -> connection.onConnectionFailed(s));
            }
        });
    }

    public static void startSignIn(String email, String password, RainbowConnectionListener.Login login) {
        RainbowSdk.instance().connection().signin(email, password, urlServer, new SigninResponseListener() {
            @Override
            public void onSigninSucceeded() {
                new Handler(Looper.getMainLooper()).post(login::onSignInSuccess);
            }

            @Override
            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                new Handler(Looper.getMainLooper()).post(() -> login.onSignInFailed(s));
            }
        });
    }

    public static void signOut(RainbowConnectionListener.Logout listener) {
        RainbowSdk.instance().connection().signout(new SignoutResponseListener() {
            @Override
            public void onSignoutSucceeded() {
                new Handler(Looper.getMainLooper()).post(listener::onSignoutSucceeded);
            }
        });
    }

    public static void registerAllContact(Contact.ContactListener listener, Context context) {
        for (IRainbowContact contact : getRainbowContacts(context)) {
            contact.registerChangeListener(listener);
        }
    }

    public static List<IRainbowContact> getRainbowContacts(Context context){
        Log.d("TAG", context.toString());
        return RainbowSdk.instance().contacts().getRainbowContacts().getCopyOfDataList();
    }

    public static String getUserLoginInCache(){
        return RainbowSdk.instance().myProfile().getUserLoginInCache();
    }

    public static String getUserPasswordInCache(){
        return RainbowSdk.instance().myProfile().getUserPasswordInCache();
    }

    public static List<IRainbowContact> getSentInvitations(Context context) {
        return RainbowSdk.instance().contacts().getSentInvitations().getCopyOfDataList();
    }

    public static List<IRainbowContact> getReceivedInvitations(Context context) {
        return RainbowSdk.instance().contacts().getReceivedInvitations().getCopyOfDataList();
    }

    public static void registerInvitationsListener(Contact.ContactListener listener, Context context) {
        for (IRainbowContact contact : getReceivedInvitations(context)) {
            contact.registerChangeListener(listener);
        }
    }

    public static void unregisInvitationsterListener(Contact.ContactListener listener, Context context) {
        for (IRainbowContact contact : getReceivedInvitations(context)) {
            contact.registerChangeListener(listener);
        }
    }
}
