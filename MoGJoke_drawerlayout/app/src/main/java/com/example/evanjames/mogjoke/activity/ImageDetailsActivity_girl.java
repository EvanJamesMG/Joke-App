package com.example.evanjames.mogjoke.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.evanjames.mogjoke.R;

/**
 * Created by EvanJames on 2015/9/17.
 */
public class ImageDetailsActivity_girl extends Activity{

    private ImageView zoomImageView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_details_girl);
        zoomImageView = (ImageView) findViewById(R.id.zoom_image_view);
        String imagePath = getIntent().getStringExtra("image_path");
        showProgressDialog();
        Glide.with(this).load(imagePath).into((new GlideDrawableImageViewTarget(zoomImageView) {
            @Override
            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                super.onResourceReady(drawable, anim);
                closeProgressDialog();
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Glide.clear(zoomImageView);
        finish();
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        try {
            progressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
