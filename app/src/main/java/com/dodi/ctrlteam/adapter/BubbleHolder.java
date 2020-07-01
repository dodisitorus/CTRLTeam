package com.dodi.ctrlteam.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.manager.room.Room;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.common.textdrawable.TextAvatar;
import com.dodi.ctrlteam.common.utils.ColorGenerator;

import java.util.List;

class BubbleHolder extends RecyclerView.ViewHolder {

    private TextView name, deskipsi, date, status, countMember;
    private ImageView image, member1, member2, member3, member4, member5;
    RelativeLayout itemLayout;
    private CardView cardView1, cardView2, cardView3, cardView4, cardView5;
    private Context context;

    BubbleHolder(View itemView) {
        super(itemView);

        itemLayout = itemView.findViewById(R.id.item_layout_view);
        name = itemView.findViewById(R.id.title);
        deskipsi = itemView.findViewById(R.id.deskipsi);
        status = itemView.findViewById(R.id.status);
        date = itemView.findViewById(R.id.year);
        image = itemView.findViewById(R.id.imageViewProfile);

        member1 = itemView.findViewById(R.id.avatar1);
        member2 = itemView.findViewById(R.id.avatar2);
        member3 = itemView.findViewById(R.id.avatar3);
        member4 = itemView.findViewById(R.id.avatar4);
        member5 = itemView.findViewById(R.id.avatar5);

        cardView1 = itemView.findViewById(R.id.card_avatar1);
        cardView2 = itemView.findViewById(R.id.card_avatar2);
        cardView3 = itemView.findViewById(R.id.card_avatar3);
        cardView4 = itemView.findViewById(R.id.card_avatar4);
        cardView5 = itemView.findViewById(R.id.card_avatar5);

        countMember = itemView.findViewById(R.id.count_member);
    }

    @SuppressLint("SetTextI18n")
    void bindContent(Room room, Context context) {
        this.context = context;
        name.setText(room.getName());
        deskipsi.setText(room.getTopic());

        List<Contact> contactList = room.getParticipantsAsContactList();
        int count = contactList.size();

        // create avatar bubble/group
        ColorDrawable cd = new ColorDrawable(ColorGenerator.MATERIAL.getColor(room.getName()));

        String firstLetter = getCharAt(room.getName(), 0);
        String secondLetter = getCharAt(room.getName(), 1);
        image.setImageDrawable(TextAvatar.builder().buildRect(firstLetter + secondLetter, cd.getColor()));

        // set lis avatar on item
        setListAvatarMember(count, contactList, room);

        date.setText("by. @" + room.getOwner().getFirstName() + room.getOwner().getLastName());
    }

    @SuppressLint("SetTextI18n")
    private void setListAvatarMember(int count, List<Contact> contactList, Room room) {
        countMember.setText(count + "member");
        // set photo in list member
        cardView1.setVisibility(View.VISIBLE);
        member1.setImageBitmap(contactList.get(0).getPhoto());

        if (count > 1) {
            countMember.setText(count + "members");
            cardView2.setVisibility(View.VISIBLE);
            member2.setImageDrawable(createAvatarUI(contactList.get(1)));
        }
        if (count > 2) {
            cardView3.setVisibility(View.VISIBLE);
            member3.setImageDrawable(createAvatarUI(contactList.get(2)));
        }
        if (count > 3) {
            cardView4.setVisibility(View.VISIBLE);
            member4.setImageDrawable(createAvatarUI(contactList.get(3)));
        }
        if (count > 4) {
            cardView5.setVisibility(View.VISIBLE);
            member5.setImageDrawable(createAvatarUI(contactList.get(4)));
        }

        if (!room.getUserStatus().isAccepted()) {
            status.setText(room.getUserStatus().toString());
        }
    }

    private Drawable createAvatarUI(IRainbowContact contact) {
        Bitmap photo = contact.getPhoto();
        ColorDrawable cd = new ColorDrawable(ColorGenerator.MATERIAL.getColor(contact.getFirstName()));

        if (photo == null) {
            String firstLetter;
            String secondLetter;
            TextAvatar drawable = null;

            if (contact.getFirstName() != null) {
                firstLetter = getCharAt(contact.getFirstName(), 0);
                drawable = TextAvatar.builder().buildRect(firstLetter, cd.getColor());

                if (contact.getLastName() != null) {
                    secondLetter = getCharAt(contact.getLastName(), 0);
                    drawable = TextAvatar.builder().buildRect(firstLetter + secondLetter, cd.getColor());
                }
            }

            if (drawable != null) {
                return drawable;
            } else {
                return TextAvatar.builder().buildRect("AA", cd.getColor());
            }
        } else {
            return new BitmapDrawable(context.getResources(), photo);
        }
    }

    private String getCharAt(String text, int index) {
        if (text.length() > 0) {
            return "" + text.charAt(index);
        } else {
            return "AA";
        }
    }
}
