package com.dodi.ctrlteam.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.dodi.ctrlteam.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZoomImage extends AppCompatActivity {

    @BindView(R.id.message_image_zoomed)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        ButterKnife.bind(this);

        Bitmap bitmap = getIntent().getParcelableExtra("image");
        imageView.setImageBitmap(bitmap);
    }
}
