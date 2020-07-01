package com.dodi.ctrlteam.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.dodi.ctrlteam.R;

import java.util.Calendar;

class FileHolder extends RecyclerView.ViewHolder {

    private TextView name, deskipsi, date;
    ImageView image;
    ImageView ic_download;
    ImageView ic_delete;

    FileHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.title);
        deskipsi = itemView.findViewById(R.id.deskipsi);
        date = itemView.findViewById(R.id.year);
        image = itemView.findViewById(R.id.imageViewProfile);

        ic_download = itemView.findViewById(R.id.ic_download);
        ic_delete = itemView.findViewById(R.id.ic_delete);
    }

    @SuppressLint("SetTextI18n")
    void bindContent(RainbowFileDescriptor fileDescriptor, IRainbowContact owner) {
        name.setText(fileDescriptor.getFileName());
        if (owner != null) {
            deskipsi.setText(owner.getFirstName() + " " + owner.getLastName());
        }

        image.setImageBitmap(fileDescriptor.getImagePreview());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fileDescriptor.getUploadedDate());
        String dateToDisplay = calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        date.setText(dateToDisplay);
    }
}
