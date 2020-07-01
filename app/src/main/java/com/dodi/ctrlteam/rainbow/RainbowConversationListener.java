package com.dodi.ctrlteam.rainbow;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.ale.infra.proxy.conversation.IRainbowConversation;

public class RainbowConversationListener {
    public interface Conversation {
        void onSuccessConversation(IRainbowConversation conversation);

        void onFailedConversation(String error);
    }

    public interface UploadFile {
        void onUploadSuccess(RainbowFileDescriptor rainbowFileDescriptor);

        void onUploadFailed(RainbowServiceException e);

        void onUploadProgress(int progress);
    }
}
