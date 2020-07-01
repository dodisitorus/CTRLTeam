package com.dodi.ctrlteam.rainbow;

import android.os.Handler;
import android.os.Looper;

import com.ale.infra.http.GetFileResponse;
import com.ale.infra.manager.fileserver.IFileProxy;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.ale.rainbowsdk.RainbowSdk;

public class RainbowFile {
    public static void download(RainbowFileDescriptor descriptor, RainbowFileListener.DownloadFile listener) {
        RainbowSdk.instance().fileStorage().downloadFile(descriptor, new IFileProxy.IDownloadFileListener() {
            @Override
            public void onDownloadSuccess(GetFileResponse getFileResponse) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onDownloadSuccess(getFileResponse));
            }

            @Override
            public void onDownloadInProgress(GetFileResponse getFileResponse) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onDownloadInProgress(getFileResponse));
            }

            @Override
            public void onDownloadFailed(boolean b) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onDownloadFailed(b));
            }
        });
    }

    public static void delete(RainbowFileDescriptor descriptor, RainbowFileListener.DeleteFile listener) {
        RainbowSdk.instance().fileStorage().removeFile(descriptor, new IFileProxy.IDeleteFileListener() {
            @Override
            public void onDeletionSuccess() {
                new Handler(Looper.getMainLooper()).post(listener::onDeleteSuccess);
            }

            @Override
            public void onDeletionError() {
                new Handler(Looper.getMainLooper()).post(listener::onDeleteFailed);
            }
        });
    }
}
