package com.tautvydas.openglrenderingpractice;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);

        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new BasicRenderer());

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
