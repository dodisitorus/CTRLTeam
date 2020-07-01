package com.dodi.ctrlteam.rainbow;

public class RainbowConnectionListener {
    public interface Connection {
        void onConnectionSuccess();

        void onConnectionFailed(String error);
    }

    public interface Login {
        void onSignInSuccess();

        void onSignInFailed(String error);
    }

    public interface Logout {
        void onSignoutSucceeded();
    }
}
