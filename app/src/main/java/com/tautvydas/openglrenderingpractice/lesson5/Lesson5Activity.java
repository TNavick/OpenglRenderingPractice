package com.tautvydas.openglrenderingpractice.lesson5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tautvydas.openglrenderingpractice.R;

public class Lesson5Activity extends AppCompatActivity {

    private Lesson5GLSurfaceView mGLSurfaceView;

    private static final String SHOWED_TOAST = "showed_toast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new Lesson5GLSurfaceView(this);

        mGLSurfaceView.setEGLContextClientVersion(2);

        mGLSurfaceView.setRenderer(new BasicRenderer5(this));

        setContentView(mGLSurfaceView);

        if (savedInstanceState == null || !savedInstanceState.getBoolean(SHOWED_TOAST, false)) {
            Toast.makeText(this, R.string.lesson_5_startup_toast, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SHOWED_TOAST, true);
        super.onSaveInstanceState(outState);
    }
}
