package com.dodi.ctrlteam.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.channel.Channel;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.adapter.ChannelAdapter;
import com.dodi.ctrlteam.rainbow.RainbowChannel;
import com.dodi.ctrlteam.rainbow.RainbowChannelListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListChannelActivity extends AppCompatActivity implements RainbowChannelListener.Find,
        ChannelAdapter.ChannelAdapterCallback, RainbowChannelListener.Subsribe {

    private List<Channel> channelList = new ArrayList<>();
    private ChannelAdapter mAdapter;
    private String searchNameFinal = "h";

    @BindView(R.id.list_item)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_channel);
        ButterKnife.bind(this);

        // set toolbar
        setToolbar();

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this::prepareList);

        // recycle view init
        mAdapter = new ChannelAdapter(channelList, this, false, false, true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        updateList();

        prepareList();
    }

    private void prepareList() {
        channelList = new ArrayList<>();
        // this is dummy
        RainbowChannel.findChannelsByName(searchNameFinal, this);
    }

    private void updateList() {
        swipeRefreshLayout.setRefreshing(true);
        if (channelList != null) {
            mAdapter.updateAdapter(channelList);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setToolbar() {
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(R.string.list_new_channel);
        toolbar.inflateMenu(R.menu.menu_seach);

        Menu menu = toolbar.getMenu();
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchChannelBy(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchChannelBy(String query) {
        searchNameFinal = query;
        swipeRefreshLayout.setRefreshing(true);
        RainbowChannel.findChannelsByName(query, this);
    }

    @Override
    public void onSearchSuccess(List<Channel> list) {
        runOnUiThread(() -> {
            channelList = new ArrayList<>();
            channelList.addAll(list);
            updateList();
            if (list.size() == 0) {
                Toast.makeText(this, "News Channel Not Found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSearchFailed(RainbowServiceException e) {
        runOnUiThread(() -> Toast.makeText(this, "" + e.getMessage() + ". " + e.getDetailsMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onItemListClicked(int position) {
        Intent intent = new Intent(this, DetailChannelActivity.class);
        intent.putExtra("idChannel", "" + channelList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onButtomFollowClicked(int position) {
        RainbowChannel.subscribeToChannel(channelList.get(position), this);
    }

    @Override
    public void onSubscribeSuccess(Channel channel) {
        runOnUiThread(() -> {
            Toast.makeText(this, channel.getName() + " subscribed", Toast.LENGTH_SHORT).show();
            updateList();
        });
    }

    @Override
    public void onSubscribeFailed(RainbowServiceException e) {
        runOnUiThread(() -> Toast.makeText(ListChannelActivity.this, "Subscribe Failed", Toast.LENGTH_SHORT).show());
    }
}
