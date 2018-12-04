package com.tautvydas.openglrenderingpractice.lesson5;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class Lesson5GLSurfaceView extends GLSurfaceView {

    private BasicRenderer5 mRenderer;

    public Lesson5GLSurfaceView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null && event.getAction() == MotionEvent.ACTION_DOWN && mRenderer != null) {

            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mRenderer.switchMode();
                }
            });

            return true;
        }
        return super.onTouchEvent(event);
    }

    public void setRenderer(BasicRenderer5 renderer) {
        mRenderer = renderer;
        super.setRenderer(renderer);
    }
}
