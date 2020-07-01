package com.dodi.ctrlteam.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.rainbow.RainbowBubble;
import com.dodi.ctrlteam.rainbow.RainbowBubbleListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormProjectActivity extends AppCompatActivity implements RainbowBubbleListener.CreateBubble {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edittext_name)
    EditText ETname;
    @BindView(R.id.edittext_desc)
    EditText ETdesc;
    @BindView(R.id.bg_modal_progress)
    RelativeLayout bg_modal_progress;
    @BindView(R.id.progress_percent_text)
    TextView progress_percent_text;
    @BindView(R.id.label_progress_text)
    TextView label_progress_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_project);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(R.string.form_new_project);
    }

    public void chooseImage(View view) {

    }

    public void onFinishAction(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // set icon image accept ask
        builder.setMessage("Are you want create new project ?");

        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    createProject(true);
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> {
                    dialog.cancel();
                    createProject(false);
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createProject(boolean notif) {
        showProgress();
        RainbowBubble.createNew(ETname.getText().toString(), ETdesc.getText().toString(), notif, this, this);
    }

    AlertDialog alertDialog;
    @SuppressLint("SetTextI18n")
    private void showProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.progress_creating);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void hideProgress() {
        alertDialog.hide();
    }

    @Override
    public void onSuccessCreateBubble() {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(FormProjectActivity.this, "Success Create Project", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void onFailedCreateBubble() {
        runOnUiThread(() -> {
            hideProgress();
            Toast.makeText(FormProjectActivity.this, "Failed Create Project", Toast.LENGTH_SHORT).show();
        });
    }
}
