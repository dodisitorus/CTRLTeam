package com.dodi.ctrlteam.rainbow;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;

import java.util.List;

public class RainbowContactListener {
    public interface Invite {
        void onInvitationSentSuccess();

        void onInvitationSentError(RainbowServiceException error);

        void onInvitationError();
    }

    public interface RemoveRoster {
        void onSuccess();

        void onFailure(RainbowServiceException error);
    }

    public interface Accept {
        void onAcceptSuccess();

        void onDeclineSuccess();

        void onError();

        void onNewReceivedInvitation(List<IRainbowContact> list);
    }

    public interface Decline {
        void onAcceptSuccess();

        void onDeclineSuccess();

        void onError();

        void onNewReceivedInvitation(List<IRainbowContact> list);
    }
}
