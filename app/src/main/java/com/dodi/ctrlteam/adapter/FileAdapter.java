package com.dodi.ctrlteam.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.ale.infra.proxy.users.IUserProxy;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileHolder> {

    private List<RainbowFileDescriptor> fileDescriptorList;
    private FileAdapterCallback mAdapterCallback;
    private IRainbowContact owner;

    public FileAdapter(List<RainbowFileDescriptor> files, FileAdapterCallback fileAdapterCallback) {
        this.fileDescriptorList = files;
        this.mAdapterCallback = fileAdapterCallback;
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);
        return new FileHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, @SuppressLint("RecyclerView") int position) {
        RainbowSdk.instance().contacts().getUserDataFromId(fileDescriptorList.get(position).getOwnerId(), new IUserProxy.IGetUserDataListener() {
            @Override
            public void onSuccess(Contact contact) {
                owner = contact;
                holder.bindContent(fileDescriptorList.get(position), owner);
            }

            @Override
            public void onFailure(RainbowServiceException e) {

            }
        });


        holder.ic_download.setOnClickListener(view -> mAdapterCallback.onItemDownloadClicked(position));

        holder.ic_delete.setOnClickListener(view -> mAdapterCallback.onItemDeleteClicked(position));

        holder.image.setOnClickListener(View -> mAdapterCallback.onItemImageClicked(position));
    }

    @Override
    public int getItemCount() {
        return fileDescriptorList.size();
    }

    public void updateList(List<RainbowFileDescriptor> files){
        this.fileDescriptorList = new ArrayList<>();
        this.fileDescriptorList.addAll(files);

        notifyDataSetChanged();
    }

    public interface FileAdapterCallback {
        void onItemDownloadClicked(int position);
        void onItemDeleteClicked(int position);
        void onItemImageClicked(int position);
    }
}
