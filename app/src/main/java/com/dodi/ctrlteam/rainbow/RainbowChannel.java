package com.dodi.ctrlteam.rainbow;

import android.os.Handler;
import android.os.Looper;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.channel.Channel;
import com.ale.infra.proxy.channel.IChannelProxy;
import com.ale.rainbowsdk.RainbowSdk;

import java.util.List;

public class RainbowChannel {
    public static void findChannelsByName(String string, RainbowChannelListener.Find listener) {
        RainbowSdk.instance().channels().findChannelsByName(string, new IChannelProxy.IChannelSearchListener() {
            @Override
            public void onSearchSuccess(List<Channel> list) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onSearchSuccess(list));
            }

            @Override
            public void onSearchFailed(RainbowServiceException e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onSearchFailed(e));
            }
        });
    }

    public static void findChannelsByTopic(String string, RainbowChannelListener.Find listener) {
        RainbowSdk.instance().channels().findChannelsByTopic(string, new IChannelProxy.IChannelSearchListener() {
            @Override
            public void onSearchSuccess(List<Channel> list) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onSearchSuccess(list));
            }

            @Override
            public void onSearchFailed(RainbowServiceException e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onSearchFailed(e));
            }
        });
    }

    public static void subscribeToChannel(Channel rChannel, RainbowChannelListener.Subsribe listener) {
        RainbowSdk.instance().channels().subscribeToChannel(rChannel, new IChannelProxy.IChannelSubscribeListener() {
            @Override
            public void onSubscribeSuccess(Channel channel) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onSubscribeSuccess(channel));
            }

            @Override
            public void onSubscribeFailed(RainbowServiceException e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onSubscribeFailed(e));
            }
        });
    }

    public static void unsubscribeFromChannel(Channel rChannel, RainbowChannelListener.Unsubsribe listener) {
        RainbowSdk.instance().channels().unsubscribeFromChannel(rChannel, new IChannelProxy.IChannelUnsubscribeListener() {
            @Override
            public void onUnsubscribeSuccess() {
                new Handler(Looper.getMainLooper()).post(() -> listener.onUnsubscribeSuccess());
            }

            @Override
            public void onUnsubscribeFailed(RainbowServiceException e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onUnsubscribeFailed(e));
            }
        });
    }
}
