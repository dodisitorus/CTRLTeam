package com.dodi.ctrlteam.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.manager.IMMessage;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.common.textdrawable.TextAvatar;
import com.dodi.ctrlteam.common.utils.ColorGenerator;

import java.util.Calendar;

class MessageListHolder extends RecyclerView.ViewHolder {
    private TextView messageText;
    private TextView timeText;
    ImageView messageImage;

    MessageListHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_body);
        timeText = itemView.findViewById(R.id.text_message_time);
        messageImage = itemView.findViewById(R.id.message_image);
    }

    @SuppressLint("SetTextI18n")
    void bind(IMMessage message, int typeView, IRainbowContact user) {
        String messageFinalString;
        messageFinalString = message.getMessageContent();
        // Format the stored timestamp into a readable String using method.

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(message.getMessageDate());
        String timeToDisplay = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        timeText.setText(timeToDisplay);

        // set image message
        messageImage.setVisibility(View.GONE);
        if (message.isFileDescriptorAvailable() && message.getFileDescriptor().isImageType()) {
            messageImage.setVisibility(View.VISIBLE);
            messageImage.setImageBitmap(message.getFileDescriptor().getImagePreview());
            if (message.getMessageContent().equals("-")) {
                messageFinalString = message.getFileDescriptor().getFileName();
            } else {
                messageFinalString = message.getFileDescriptor().getFileName() + "\n" + messageFinalString;
            }

            if (message.getFileDescriptor().isDeleted()) {
                messageImage.setVisibility(View.GONE);
                messageFinalString = "This file has been deleted." + "\n" + messageFinalString;
            }
        }
        messageText.setText(messageFinalString);

//        if (message.isFileDescriptorAvailable() && message.getFileDescriptor().isPdfFileType()) {
//
//        }

        if (typeView == 2) {
            TextView nameText = itemView.findViewById(R.id.text_message_name);
            ImageView profileImage = itemView.findViewById(R.id.image_message_profile);

            nameText.setText("@"+user.getFirstName());

            Bitmap photo = user.getPhoto();
            if (photo == null) {
                String firstLetter;
                String secondLetter;
                TextAvatar drawable = null;

                if (user.getFirstName() != null) {
                    firstLetter = getCharAt(user.getFirstName());
                    ColorDrawable cd = new ColorDrawable(ColorGenerator.MATERIAL.getColor(user.getFirstName()));
                    drawable = TextAvatar.builder().buildRect(firstLetter, cd.getColor());

                    if (user.getLastName() != null) {
                        secondLetter = getCharAt(user.getLastName());
                        drawable = TextAvatar.builder().buildRect(firstLetter + secondLetter, cd.getColor());
                    }
                }

                if (drawable != null) {
                    profileImage.setImageDrawable(drawable);
                }
            } else {
                profileImage.setImageBitmap(photo);
            }
        }
    }

    private String getCharAt(String text) {
        if (text.length() > 0) {
            return "" + text.charAt(0);
        } else {
            return "AA";
        }
    }
}
