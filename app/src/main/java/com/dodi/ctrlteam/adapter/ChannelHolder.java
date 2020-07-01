package com.dodi.ctrlteam.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ale.infra.manager.channel.Channel;

import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.common.textdrawable.TextAvatar;
import com.dodi.ctrlteam.common.utils.ColorGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class ChannelHolder extends RecyclerView.ViewHolder {

    private TextView name, deskipsi, date;
    RelativeLayout itemLayout;
    private ImageView image, image_fav_star;
    Button buttonFollow;

    ChannelHolder(View itemView) {
        super(itemView);

        itemLayout = itemView.findViewById(R.id.item_layout_view);
        name = itemView.findViewById(R.id.title);
        deskipsi = itemView.findViewById(R.id.deskipsi);
        image = itemView.findViewById(R.id.image_channel);
        date = itemView.findViewById(R.id.date);
        buttonFollow = itemView.findViewById(R.id.button_follow);
        image_fav_star = itemView.findViewById(R.id.image_fav_star);
    }

    void bindContent(Channel channel, boolean isSearch) {
        name.setText(channel.getName());
        deskipsi.setText(channel.getTopic());

        boolean isSubscribe = channel.isSubscribed();
        if (isSubscribe) {
            image_fav_star.setVisibility(View.VISIBLE);
        } else {
            image_fav_star.setVisibility(View.GONE);
        }

        // create image
        Bitmap imageChannel = channel.getChannelAvatar();
        if (imageChannel != null) {
            image.setImageBitmap(imageChannel);
        } else {
            String letter = channel.getName();
            ColorDrawable cd = new ColorDrawable(ColorGenerator.MATERIAL.getColor(letter));
            image.setImageDrawable(TextAvatar.builder().buildRoundRect(letter, cd.getColor(), 5));
        }

        if (channel.getCreationDate() != null) {
            String hashMapKey = convertDateToString(channel.getCreationDate());
            date.setText(hashMapKey);
        }

        if (isSearch && !isSubscribe) {
            buttonFollow.setVisibility(View.VISIBLE);
        } else {
            buttonFollow.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String convertDateToString(Date date) {
        DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");

        String strDate;
        strDate = dateFormat1.format(date);
        return strDate;
    }
}


