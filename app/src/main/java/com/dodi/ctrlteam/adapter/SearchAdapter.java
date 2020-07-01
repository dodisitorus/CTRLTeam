package com.dodi.ctrlteam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ale.infra.contact.IRainbowContact;
import com.dodi.ctrlteam.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> implements Filterable {

    private List<IRainbowContact> friendList;
    private List<IRainbowContact> contactListFiltered;
    private List<IRainbowContact> contactListBackup;
    private List<IRainbowContact> contactListChoosen;
    private SearchAdapterCallback mAdapterCallback;
    private Context context;

    public SearchAdapter(Context context, List<IRainbowContact> friendList, SearchAdapter.SearchAdapterCallback callback, List<IRainbowContact> choosen) {
        this.context = context;
        this.friendList = friendList;
        this.contactListBackup = friendList;
        this.mAdapterCallback = callback;
        this.contactListChoosen = choosen;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);
        return new SearchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        holder.bindContent(friendList.get(position), context, contactListChoosen);
        holder.itemLayout.setOnClickListener(view -> mAdapterCallback.onItemListClicked(position, friendList.get(position)));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void updateContact(List<IRainbowContact> friend, List<IRainbowContact> choosen) {
        this.friendList = new ArrayList<>();
        this.friendList.addAll(friend);
        this.contactListBackup = new ArrayList<>();
        this.contactListBackup.addAll(friend);
        this.contactListChoosen = choosen;

        notifyDataSetChanged();
    }

    public void updateCheck(List<IRainbowContact> contactListChoosen) {
        this.contactListChoosen = contactListChoosen;
        notifyDataSetChanged();
    }

    public interface SearchAdapterCallback {
        void onItemListClicked(int position, IRainbowContact contact);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactListBackup;
                } else {
                    List<IRainbowContact> filteredList = new ArrayList<>();
                    for (IRainbowContact row : contactListBackup) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String name = row.getFirstName() + " " + row.getLastName();
                        if (name.toLowerCase().contains(charString.toLowerCase()) || row.getMainEmailAddress().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                friendList = new ArrayList<>();
                friendList.addAll(contactListFiltered);
                notifyDataSetChanged();
            }
        };
    }
}
