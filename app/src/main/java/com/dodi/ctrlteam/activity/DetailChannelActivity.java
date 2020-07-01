package com.dodi.ctrlteam.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.channel.Channel;
import com.ale.infra.proxy.channel.IChannelProxy;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.common.textdrawable.TextAvatar;
import com.dodi.ctrlteam.common.utils.ColorGenerator;
import com.dodi.ctrlteam.rainbow.RainbowChannel;
import com.dodi.ctrlteam.rainbow.RainbowChannelListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailChannelActivity extends AppCompatActivity implements RainbowChannelListener.Subsribe, RainbowChannelListener.Unsubsribe {

    private Menu menu;
    private Channel rChannel;
    private boolean isCancelAction = false;
    private boolean isSubscribed = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_channel)
    ImageView image_channel;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.channel_name)
    TextView channel_name;
    @BindView(R.id.channel_topic)
    TextView channel_topic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        ButterKnife.bind(this);

        collapsingToolbarLayout.setCollapsedTitleTextColor(
                ContextCompat.getColor(this, R.color.whiteCardColor));
        collapsingToolbarLayout.setExpandedTitleColor(
                ContextCompat.getColor(this, R.color.colorPrimary));

        fab.setOnClickListener(view -> subscribeChannel());

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    showOption();
                } else if (isShow) {
                    isShow = false;
                    hideOption();
                }
            }
        });
        // get detail channel;
        getDetailChannel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_channel, menu);
        hideOption();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_subscribe) {
            subscribeChannel();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideOption() {
        runOnUiThread(() -> {
            MenuItem item = menu.findItem(R.id.action_subscribe);
            if (item != null) {
                if (isSubscribed) {
                    item.setIcon(R.drawable.star_off);
                } else {
                    item.setIcon(R.drawable.star);
                }
                item.setVisible(false);
            }
        });
    }

    private void showOption() {
        runOnUiThread(() -> {
            MenuItem item = menu.findItem(R.id.action_subscribe);
            if (item != null) {
                if (isSubscribed) {
                    item.setIcon(R.drawable.star_off);
                } else {
                    item.setIcon(R.drawable.star);
                }
                item.setVisible(true);
            }
        });
    }

    private void getDetailChannel() {
        RainbowSdk.instance().channels().getChannelById(getIntent().getStringExtra("idChannel"), new IChannelProxy.IChannelGetListener() {
            @Override
            public void onGetSuccess(Channel channel) {
                runOnUiThread(() -> setDetailChannel(channel));
            }

            @Override
            public void onGetFailed(RainbowServiceException e) {
                runOnUiThread(() -> Toast.makeText(DetailChannelActivity.this, "" + e.getMessage() + " " + e.getDetailsMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setDetailChannel(Channel channel) {
        rChannel = channel;
        isSubscribed = rChannel.isSubscribed();

        ColorDrawable cd = new ColorDrawable(ColorGenerator.MATERIAL.getRandomColor());
        String letter = channel.getName();
        image_channel.setImageDrawable(TextAvatar.builder().buildRect(letter, cd.getColor()));
        channel_name.setText(channel.getName());
        channel_topic.setText(channel.getTopic());

        if (channel.isSubscribed()) {
            fab.setImageDrawable(getDrawable(R.drawable.star_off));
        }

        toolbar.setTitle(channel.getName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void subscribeChannel() {
        isCancelAction = false;
        if (isSubscribed) {
            fab.setImageDrawable(getDrawable(R.drawable.star));
            RainbowChannel.unsubscribeFromChannel(rChannel, this);
        } else {
            fab.setImageDrawable(getDrawable(R.drawable.star_off));
            RainbowChannel.subscribeToChannel(rChannel, this);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onSubscribeSuccess(Channel channel) {
        runOnUiThread(() -> {
            isSubscribed = true;
            if (!isCancelAction) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "This channel subscribed", Snackbar.LENGTH_LONG)
                        .setAction("Cancel", view -> {
                            isCancelAction = true;
                            RainbowChannel.unsubscribeFromChannel(rChannel, this);
                            fab.setImageDrawable(getDrawable(R.drawable.star));
                        }).show();
            }
        });
    }

    @Override
    public void onSubscribeFailed(RainbowServiceException e) {
        runOnUiThread(() -> {
            Toast.makeText(DetailChannelActivity.this, "" + e.getMessage() + " " + e.getDetailsMessage(), Toast.LENGTH_SHORT).show();
            fab.setImageDrawable(getDrawable(R.drawable.star));
        });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onUnsubscribeSuccess() {
        runOnUiThread(() -> {
            isSubscribed = false;
            if (!isCancelAction) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "This channel unsubscribe", Snackbar.LENGTH_LONG)
                        .setAction("Cancel", view -> {
                            isCancelAction = true;
                            RainbowChannel.subscribeToChannel(rChannel, this);
                            fab.setImageDrawable(getDrawable(R.drawable.star_off));
                        }).show();
            }
        });
    }

    @Override
    public void onUnsubscribeFailed(RainbowServiceException e) {
        runOnUiThread(() -> {
            Toast.makeText(DetailChannelActivity.this, "" + e.getMessage() + " " + e.getDetailsMessage(), Toast.LENGTH_SHORT).show();
            fab.setImageDrawable(getDrawable(R.drawable.star_off));
        });
    }
}
