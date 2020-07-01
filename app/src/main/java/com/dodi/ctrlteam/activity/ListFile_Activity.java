package com.dodi.ctrlteam.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.http.GetFileResponse;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.ale.infra.manager.room.Room;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.adapter.FileAdapter;
import com.dodi.ctrlteam.rainbow.RainbowFile;
import com.dodi.ctrlteam.rainbow.RainbowFileListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFile_Activity extends AppCompatActivity implements FileAdapter.FileAdapterCallback,
        RainbowFileListener.DownloadFile, RainbowFileListener.DeleteFile {

    private List<RainbowFileDescriptor> fileList = new ArrayList<>();
    private FileAdapter mAdapter;
    private boolean isBubble = true;
    private boolean isContact = false;
    private boolean isHome = false;
    private Room room;
    private IRainbowConversation conversation;
    private int deletePosition;

    @BindView(R.id.list_item)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bg_modal_progress)
    RelativeLayout bg_modal_progress;
    @BindView(R.id.progress_percent_text)
    TextView progress_percent_text;
    @BindView(R.id.label_progress_text)
    TextView label_progress_text;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_file);
        ButterKnife.bind(this);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this::updateList);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        isBubble = getIntent().getStringExtra("typeNav").equals("bubble");
        isContact = getIntent().getStringExtra("typeNav").equals("contact");
        isHome = getIntent().getStringExtra("typeNav").equals("home");
        if (isBubble) {
            room = RainbowSdk.instance().bubbles().findBubbleById(getIntent().getStringExtra("roomId"));
        } else if (isHome) {
            getAllFile();
        } else {
            conversation = RainbowSdk.instance().conversations().getConversationFromId(getIntent().getStringExtra("conversationId"));
        }

        toolbar.setTitle(R.string.list_files);
        toolbar.setNavigationOnClickListener(v -> finish());

        // recycle view init
        mAdapter = new FileAdapter(fileList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareList();
    }

    private void prepareList() {
        swipeRefreshLayout.setRefreshing(true);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        if (RainbowSdk.instance().connection().isConnected()) {
            updateList();
        }
    }

    private void getAllFile() {
        ArrayItemList<RainbowFileDescriptor> filesReceived = RainbowSdk.instance().fileStorage().getAllFilesReceived();
        ArrayItemList<RainbowFileDescriptor> fileSent = RainbowSdk.instance().fileStorage().getAllFilesSent();
        List<RainbowFileDescriptor> fileDescriptors = new ArrayList<>();
        for (int pos = 0; pos < filesReceived.getCount(); pos++) {
            fileDescriptors.add(filesReceived.get(pos));
        }
        for (int pos = 0; pos < fileSent.getCount(); pos++) {
            fileDescriptors.add(filesReceived.get(pos));
        }
        fileList = new ArrayList<>();
        fileList.addAll(fileDescriptors);
    }

    private void updateList() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        if (isBubble) {
            if (room != null) {
                fileList = RainbowSdk.instance().fileStorage().getFilesSentInBubble(room);
                fileList.addAll(RainbowSdk.instance().fileStorage().getFilesReceivedInBubble(room));
            } else {
                Toast.makeText(this, "File in group not found", Toast.LENGTH_SHORT).show();
            }
        } else if (isContact){
            if (conversation != null) {
                fileList = RainbowSdk.instance().fileStorage().getFilesSentInConversation(conversation);
                fileList.addAll(RainbowSdk.instance().fileStorage().getFilesReceivedInConversation(conversation));
            } else {
                Toast.makeText(this, "Chat in conversation not found", Toast.LENGTH_SHORT).show();
            }
        } else if (isHome) {
            getAllFile();
        }

        if (fileList != null) {
            mAdapter.updateList(fileList);
            swipeRefreshLayout.setRefreshing(false);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            if (fileList.size() == 0) {
                Toast.makeText(this, "File empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemDownloadClicked(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to download this file ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    showProgress(0, R.string.progress_download);
                    RainbowFile.download(fileList.get(position), this);
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onItemDeleteClicked(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to delete this file ?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    deletePosition = position;
                    showProgress(0, R.string.progress_deleted);
                    RainbowFile.delete(fileList.get(position), this);
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onItemImageClicked(int position) {
        Intent intent = new Intent(ListFile_Activity.this, ZoomImage.class);
        intent.putExtra("image", fileList.get(position).getImage());
        startActivity(intent);
    }

    @Override
    public void onDownloadSuccess(GetFileResponse getFileResponse) {
        runOnUiThread(() -> {
            Toast.makeText(ListFile_Activity.this, "Download Success", Toast.LENGTH_SHORT).show();
            hideProgress();
        });
    }

    @Override
    public void onDownloadFailed(boolean b) {
        runOnUiThread(() -> {
            Toast.makeText(ListFile_Activity.this, "Download Failed", Toast.LENGTH_SHORT).show();
            hideProgress();
        });
    }

    @Override
    public void onDownloadInProgress(GetFileResponse getFileResponse) {
        runOnUiThread(() -> showProgress(getFileResponse.getPercentDownloaded(), R.string.progress_download));
    }

    AlertDialog alertDialog;
    @SuppressLint("SetTextI18n")
    private void showProgress(int progress, int label) {
        Log.d("TAG", "" + progress);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(label);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void hideProgress() {
        alertDialog.hide();
    }

    @Override
    public void onDeleteSuccess() {
        runOnUiThread(() -> {
            Toast.makeText(ListFile_Activity.this, "Delete File Success", Toast.LENGTH_SHORT).show();
            hideProgress();
            if (fileList.get(deletePosition) != null) {
                fileList.remove(deletePosition);
                mAdapter.updateList(fileList);
            }
        });
    }

    @Override
    public void onDeleteFailed() {
        runOnUiThread(() -> {
            Toast.makeText(ListFile_Activity.this, "Delete File Failed", Toast.LENGTH_SHORT).show();
            hideProgress();
        });
    }
}
