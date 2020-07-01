package com.dodi.ctrlteam.rainbow;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.channel.Channel;

import java.util.List;

public class RainbowChannelListener {
    public interface Find {
        void onSearchSuccess(List<Channel> list);

        void onSearchFailed(RainbowServiceException e);
    }

    public interface Subsribe {
        void onSubscribeSuccess(Channel channel);

        void onSubscribeFailed(RainbowServiceException e);
    }

    public interface Unsubsribe {
        void onUnsubscribeSuccess();

        void onUnsubscribeFailed(RainbowServiceException e);
    }
}
