package com.dodi.ctrlteam.rainbow;

import com.ale.infra.http.GetFileResponse;

public class RainbowFileListener {
    public interface DownloadFile {
        void onDownloadSuccess(GetFileResponse getFileResponse);

        void onDownloadFailed(boolean b);

        void onDownloadInProgress(GetFileResponse getFileResponse);
    }

    public interface DeleteFile {
        void onDeleteSuccess();

        void onDeleteFailed();
    }
}
