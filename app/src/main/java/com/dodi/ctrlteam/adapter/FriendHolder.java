package com.dodi.ctrlteam.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ale.infra.contact.IRainbowContact;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.common.textdrawable.TextAvatar;
import com.dodi.ctrlteam.common.utils.ColorGenerator;

class FriendHolder extends RecyclerView.ViewHolder {

    private TextView name, email, time;
    private ImageView image;
    RelativeLayout itemLayout;

    FriendHolder(View itemView) {
        super(itemView);

        itemLayout = itemView.findViewById(R.id.item_layout_view);
        name = itemView.findViewById(R.id.title);
        email = itemView.findViewById(R.id.genre);
        time = itemView.findViewById(R.id.year);
        image = itemView.findViewById(R.id.imageViewProfile);
    }

    @SuppressLint({"SetTextI18n"})
    void bindContent(IRainbowContact contact, Context context) {
        name.setText(contact.getFirstName() + " " + contact.getLastName());
        email.setText(contact.getFirstEmailAddress());

        // set presence contact
        changePresenceUI(contact.getPresence().toString(), time);

        // set avatar contact
        createAvatarUI(contact);

        Log.d("Context", context.toString());
    }

    private void createAvatarUI(IRainbowContact contact) {
        Bitmap photo = contact.getPhoto();
        if (photo == null) {
            String firstLetter;
            String secondLetter;
            TextAvatar drawable = null;

            if (contact.getFirstName() != null) {

                firstLetter = getCharAt(contact.getFirstName());
                ColorDrawable cd = new ColorDrawable(ColorGenerator.MATERIAL.getColor(contact.getFirstName()));

                drawable = TextAvatar.builder().buildRect(firstLetter, cd.getColor());

                if (contact.getLastName() != null) {
                    secondLetter = getCharAt(contact.getLastName());
                    drawable = TextAvatar.builder().buildRect(firstLetter + secondLetter, cd.getColor());
                }
            }

            if (drawable != null) {
                image.setImageDrawable(drawable);
            }
        } else {
            image.setImageBitmap(photo);
        }
    }

    private String getCharAt(String text) {
        if (text.length() > 0) {
            return "" + text.charAt(0);
        } else {
            return "AA";
        }
    }

    @SuppressLint("ResourceAsColor")
    private void changePresenceUI(String presence, TextView tv) {
        switch (presence) {
            case "online":
                tv.setText(R.string.online);
                tv.setTextColor(R.color.primaryTextColor);
                break;
            case "offline":
                tv.setTextColor(R.color.profilePrimaryDark);
                tv.setText(R.string.offline);
                break;
            case "mobile_online":
                tv.setText(R.string.mobile_online);
                break;
            case "away":
                tv.setText(R.string.away);
                break;
            case "manual_away":
                tv.setText(R.string.away);
                break;
            case "busy":
                tv.setText(R.string.busy);
                break;
            case "dnd":
                tv.setText(R.string.do_not_disturb);
                break;
            case "dnd_presentation":
                tv.setText(R.string.do_not_disturb_on_presentation);
                break;
        }
    }
}
