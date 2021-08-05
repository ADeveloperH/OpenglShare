package com.noxgroup.app.learnopengl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.noxgroup.app.learnopengl.base.BaseActivity;
import com.noxgroup.app.learnopengl.glsurfaceview.GLSurfaceViewActivity;
import com.noxgroup.app.learnopengl.image.ImageActivity;
import com.noxgroup.app.learnopengl.mirror.MirrorActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void glsurfaceview(View view) {
        startActivity(new Intent(this, GLSurfaceViewActivity.class));
    }

    public void loadImage(View view) {
        startActivity(new Intent(this, ImageActivity.class));
    }

    public void showMirror(View view) {
        startActivity(new Intent(this, MirrorActivity.class));
    }
}