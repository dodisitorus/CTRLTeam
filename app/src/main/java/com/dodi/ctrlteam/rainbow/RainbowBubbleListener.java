package com.dodi.ctrlteam.rainbow;

import com.ale.infra.contact.Contact;
import com.ale.infra.manager.room.RoomParticipant;

public class RainbowBubbleListener {
    public interface CreateBubble {
        void onSuccessCreateBubble();

        void onFailedCreateBubble();
    }

    public interface AcceptInvite {
        void onSucessAcceptInvite(RoomParticipant roomParticipant);

        void onFailedAcceptInvite();
    }

    public interface RejectInvite {
        void onSucessRejectInvite(RoomParticipant roomParticipant);

        void onFailedRejectInvite();
    }

    public interface AddParticipants {
        void onAddParticipantsSuccess();

        void onMaxParticipantsReached();

        void onAddParticipantFailed(Contact contact);
    }
}
