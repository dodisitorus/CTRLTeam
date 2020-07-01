package com.dodi.ctrlteam.activity.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.channel.Channel;
import com.ale.infra.manager.room.Room;
import com.ale.rainbowsdk.Bubbles;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.activity.DetailChannelActivity;
import com.dodi.ctrlteam.activity.ListChannelActivity;
import com.dodi.ctrlteam.activity.ListFile_Activity;
import com.dodi.ctrlteam.adapter.ChannelAdapter;
import com.dodi.ctrlteam.common.UserPreferences;
import com.dodi.ctrlteam.rainbow.RainbowBubble;
import com.dodi.ctrlteam.rainbow.RainbowChannel;
import com.dodi.ctrlteam.rainbow.RainbowChannelListener;
import com.dodi.ctrlteam.rainbow.RainbowConnection;
import com.dodi.ctrlteam.rainbow.RainbowConnectionListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WhatsNewFragment extends Fragment implements ChannelAdapter.ChannelAdapterCallback,
        RainbowChannelListener.Find, RainbowConnectionListener.Logout {

    private List<Channel> channelList = new ArrayList<>();
    private List<Channel> channelSubsList = new ArrayList<>();
    private ChannelAdapter mAdapter;
    private ChannelAdapter mAdapterSubs;

    @BindView(R.id.title_menu)
    TextView title_menu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_subs_news)
    RecyclerView list_subs_news;
    @BindView(R.id.list_news)
    RecyclerView list_news;
    @BindView(R.id.swipe_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.bg_modal_progress)
    RelativeLayout bg_modal_progress;
    @BindView(R.id.progress_percent_text)
    TextView progress_percent_text;
    @BindView(R.id.label_progress_text)
    TextView label_progress_text;
    @BindView(R.id.see_more)
    TextView see_more;
    @BindView(R.id.label_list_news_subs)
    LinearLayout label_list_news_subs;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.shimmer_view_container2)
    ShimmerFrameLayout shimmerFrameLayout2;
    @BindView(R.id.LLMenuAttendance)
    LinearLayout LLMenuAttendance;
    @BindView(R.id.LLMenuSchedule)
    LinearLayout LLMenuSchedule;
    @BindView(R.id.LLMenuFiles)
    LinearLayout LLMenuFiles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whats_new, container, false);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setRefreshing(true);

        // recycle view news channel
        mAdapter = new ChannelAdapter(channelList, this, false, false, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        list_news.setLayoutManager(mLayoutManager);
        list_news.setItemAnimator(new DefaultItemAnimator());
        list_news.setAdapter(mAdapter);

        // recycle view subscribe channel
        mAdapterSubs = new ChannelAdapter(channelSubsList, this, true, true, false);
        RecyclerView.LayoutManager mLayoutManagerHorizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        list_subs_news.setLayoutManager(mLayoutManagerHorizontal);
        list_subs_news.setItemAnimator(new DefaultItemAnimator());
        list_subs_news.setAdapter(mAdapterSubs);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout2.startShimmer();

        // set toolbar
        onSetToolbar();

        swipeRefreshLayout.setOnRefreshListener(this::getChannelList);

        // set see more action
        see_more.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), ListChannelActivity.class);
            startActivity(intent);
        });

        // get news feed channel list
        getChannelList();

        // update list news feed channel list
        updateList();

        // set title menu
        title_menu.setText(getString(R.string.hai) + " " + RainbowSdk.instance().myProfile().getConnectedUser().getFirstName());

        // add click listener on menu
        setOnclickListenerOnMenu();
    }

    @SuppressLint("SimpleDateFormat")
    private String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate;
        strDate = dateFormat.format(date);
        String[] strings = strDate.split("/");
        StringBuilder newDate = new StringBuilder();
        for (String string: strings) {
            newDate.append(string);
        }
        return newDate.toString();
    }

    private void setOnclickListenerOnMenu() {
        // action attendance menu
        LLMenuAttendance.setOnClickListener(view -> {
            // this dummy check attendance local, i know this dummy will break if user uninstalling app.
            String keyUser = "Att3" + convertDateToString(new Date());
            String checkAttendance = UserPreferences.getData(keyUser, getContext());

            //List<Room> projectList = RainbowSdk.instance().bubbles().getMyList();

            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle(R.string.absent_team);

            if (checkAttendance.equals("")) {
                builder.setMessage("Did you come to work today?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        (dialog, id) -> {
                            UserPreferences.setData(keyUser, "true", getContext());
                            dialog.cancel();
                        });

                builder.setNegativeButton(
                        "No",
                        (dialog, id) -> {
                            UserPreferences.setData(keyUser, "false", getContext());
                            dialog.cancel();
                        });
            } else {
                builder.setMessage("You already do attendance today.");

                builder.setPositiveButton(
                        "OK",
                        (dialog, id) -> dialog.cancel());
                builder.setCancelable(true);
            }

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        // this dummy scheduling
        // BOAA ---> action scheduling menu
        LLMenuSchedule.setOnClickListener(view -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builderSingle.setTitle("Select One Project:");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
            List<Room> projecyList = RainbowBubble.getMyList();
            int imm = 0;
            for (Room room: projecyList) {
                imm++;
                if (imm < 4) {
                    arrayAdapter.add(room.getName());
                }
            }

            if (imm == 0) {
                builderSingle.setMessage("Project not yet found, try again.");
            } else {
                builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                    String strName = arrayAdapter.getItem(which);
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                    builderInner.setTitle(strName);

                    final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
                    arrayAdapter2.add("10.00 - Discuss development with developer");
                    arrayAdapter2.add("14.00 - Meeting Marketing strategy");
                    arrayAdapter2.add("15.00 - Report to boss");

                    builderInner.setAdapter(arrayAdapter2, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });

                    builderInner.setPositiveButton("OK", (dialog1, which1) -> dialog1.dismiss());
                    builderInner.show();
                });
            }
            builderSingle.show();
        });


        // BOAA ---> action FILES menu
        LLMenuFiles.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ListFile_Activity.class);
            intent.putExtra("typeNav", "home");
            startActivity(intent);
        });
    }

    private void getChannelList() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout2.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout2.setVisibility(View.VISIBLE);

        // get news feed channel list
        String frequentWords = UserPreferences.getData(UserPreferences.userFavWord, getContext());
        RainbowChannel.findChannelsByName(frequentWords, this);

        channelList = new ArrayList<>();

        // get news feed channel subs list
        getSubsChannelList();
    }

    private void updateList() {
        if (channelList.size() == 0) {
            shimmerFrameLayout2.startShimmer();
            shimmerFrameLayout2.setVisibility(View.VISIBLE);
        } else {
            shimmerFrameLayout2.stopShimmer();
            shimmerFrameLayout2.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setRefreshing(true);
        if (channelList != null) {
            mAdapter.updateAdapter(channelList);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void getSubsChannelList() {
        // get favorite channel
        channelSubsList = RainbowSdk.instance().channels().getAllSubscribedChannels();

        updateListChannelSubs();
    }

    private void updateListChannelSubs() {

        if (channelSubsList.size() == 0) {
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }

        if (channelSubsList != null) {
            mAdapterSubs.updateAdapter(channelSubsList);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onSetToolbar() {
        toolbar.setTitle(R.string.what_s_new);

        // set option on room chat
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_logout:
                    showProgress();
                    RainbowConnection.signOut(this);
                    return true;
                default:
                    return false;
            }
        });
    }

    private AlertDialog alertDialog;
    @SuppressLint("SetTextI18n")
    private void showProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage("Loading...");
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void hideProgress() {
        alertDialog.hide();
    }

    @Override
    public void onItemListClicked(int position) {
        Intent intent = new Intent(getContext(), DetailChannelActivity.class);
        intent.putExtra("idChannel", "" + channelList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onButtomFollowClicked(int position) {

    }

    @Override
    public void onSearchSuccess(List<Channel> list) {
        if (getActivity() != null) {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                channelList.addAll(list);
                List<Channel> channelListSus = new ArrayList<>();
                for (Channel channel: list) {
                    if (channel.isSubscribed()) {
                        channelListSus.add(channel);
                    }
                }
                channelSubsList.addAll(channelListSus);
                if (channelListSus.size() > 0) {
                    updateListChannelSubs();
                }
                updateList();
            });
        }
    }

    @Override
    public void onSearchFailed(RainbowServiceException e) {
        if (getActivity() != null) {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), "" + e.getMessage() + ". " + e.getDetailsMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onSignoutSucceeded() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            UserPreferences.setDataBool(UserPreferences.loginKey, false, getContext());
            Objects.requireNonNull(getActivity()).finish();
            hideProgress();
        });
    }
}
