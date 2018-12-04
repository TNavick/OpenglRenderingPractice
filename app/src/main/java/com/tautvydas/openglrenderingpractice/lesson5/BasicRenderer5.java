package com.tautvydas.openglrenderingpractice.lesson5;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.tautvydas.openglrenderingpractice.R;
import com.tautvydas.openglrenderingpractice.common.ShaderHelper;
import com.tautvydas.openglrenderingpractice.common.shapes.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class BasicRenderer5 implements GLSurfaceView.Renderer {

    private Context mContext;

    private final int mBytesPerFloat = 4;
    private final int mPositionDataSize = 3;             // Size of the position data in elements.
    private final int mColorDataSize = 4;                // Size of the color data in elements.

    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    private int mBlendingProgramHandle;

    private Cube cube1;
    private Cube cube2;
    private Cube cube3;
    private Cube cube4;
    private Cube cube5;

    private boolean mBlending;

    public BasicRenderer5(Context context) {
        mContext = context;
        mBlending = true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // View matrix
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);

        // No culling of back faces
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        // No depth testing
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        // Enable blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);

        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        mBlendingProgramHandle = ShaderHelper.createProgram(mContext,
                R.string.shader_blending_vertex,
                R.string.shader_blending_fragment,
                new String[]{"a_Position", "a_Color"});

        cube1 = new Cube();
        cube2 = new Cube();
        cube3 = new Cube();
        cube4 = new Cube();
        cube5 = new Cube();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Projection matrix
        GLES20.glViewport(0, 0, width, height);

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 20.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        long time = SystemClock.uptimeMillis() % 20000L;
        float angleInDegrees1 = (360.0f / 20000.0f) * ((int) time);
        float angleInDegrees2 = (360.0f / 5000.0f) * ((int) time);

        double angleInRads = Math.toRadians(angleInDegrees1);
        double angleInRads2 = Math.toRadians(angleInDegrees2);
        float sin = (float) Math.sin(angleInRads);
        float cos = (float) Math.cos(angleInRads);
        float sin2 = (float) Math.sin(angleInRads2);
        float cos2 = (float) Math.cos(angleInRads2);

        //Matrix.setLookAtM(mViewMatrix, 0,
        //        cos * 7.5f, 0.0f, Math.abs(sin * 7.5f) - 5.0f,
        //        0.0f, 0.0f, -5.0f,
        //        0.0f, 1.0f, 0.0f);

        GLES20.glUseProgram(mBlendingProgramHandle);

        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mBlendingProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mBlendingProgramHandle, "u_MVMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mBlendingProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mBlendingProgramHandle, "a_Color");

        // Draw some cubes.
        //cube1.setPosition(4.0f * sin, 4.0f * cos, -5.0f);
        cube1.setPosition(4.0f, 0.0f, -7.0f);
        cube1.setRotation(angleInDegrees1, 1.0f, 0.0f, 0.0f);
        updateUniforms(cube1);
        cube1.draw(mPositionHandle, mColorHandle);

        //cube2.setPosition(-4.0f * sin, -4.0f * cos, -5.0f);
        cube2.setPosition(-4.0f, 0.0f, -7.0f);
        cube2.setRotation(angleInDegrees1, 0.0f, 1.0f, 0.0f);
        updateUniforms(cube2);
        cube2.draw(mPositionHandle, mColorHandle);

        //cube3.setPosition(0.0f, 4.0f * sin, 4.0f * cos - 5.0f);
        cube3.setPosition(0.0f, 4.0f, -7.0f);
        cube3.setRotation(angleInDegrees1, 0.0f, 0.0f, 1.0f);
        updateUniforms(cube3);
        cube3.draw(mPositionHandle, mColorHandle);

        //cube4.setPosition(0.0f, -4.0f * sin, -4.0f * cos - 5.0f);
        cube4.setPosition(0.0f, -4.0f, -7.0f);
        cube4.setRotation(45.0f * sin, 0.0f, 1.0f, 0.0f);
        updateUniforms(cube4);
        cube4.draw(mPositionHandle, mColorHandle);

        cube5.setPosition(0.0f, 0.0f, -5.0f);
        cube5.setRotation(angleInDegrees1, 1.0f, 1.0f, 0.0f);
        updateUniforms(cube5);
        cube5.draw(mPositionHandle, mColorHandle);
    }

    public void switchMode() {
        mBlending = !mBlending;
        
        if (mBlending) {
            // No culling of back faces
            GLES20.glDisable(GLES20.GL_CULL_FACE);

            // No depth testing
            GLES20.glDisable(GLES20.GL_DEPTH_TEST);

            // Enable blending
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
        } else {
            // Cull back faces
            GLES20.glEnable(GLES20.GL_CULL_FACE);

            // Enable depth testing
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            // Disable blending
            GLES20.glDisable(GLES20.GL_BLEND);
        }
    }

    private void updateUniforms(Cube cube) {
        float[] modelMatrix = cube.getTransformation();

        // multiply view by the model (model * view) and pass it in
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // multiply projection by the viewModel (model * view * projection) and pass it in
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
    }
}
