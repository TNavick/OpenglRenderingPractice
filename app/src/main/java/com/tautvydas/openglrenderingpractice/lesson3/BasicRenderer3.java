package com.tautvydas.openglrenderingpractice.lesson3;

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

class BasicRenderer3 implements GLSurfaceView.Renderer {

    private Context mContext;

    private final int mBytesPerFloat = 4;
    private final int mPositionDataSize = 3;             // Size of the position data in elements.
    private final int mColorDataSize = 4;                // Size of the color data in elements.
    private final int mNormalDataSize = 3;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mLightModelMatrix = new float[16];

    private final float[] mLightPosInModelSpace = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] mLightPosInWorldSpace = new float[4];
    private final float[] mLightPosInEyeSpace = new float[4];

    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mNormalHandle;
    private int mLightPosHandle;

    private int mPerFragProgramHandle;
    private int mPointProgramHandle;

    private Cube cube1;
    private Cube cube2;
    private Cube cube3;
    private Cube cube4;
    private Cube cube5;

    public BasicRenderer3(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // View matrix
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

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

        mPerFragProgramHandle = ShaderHelper.createProgram(mContext, R.string.shader_perFragment_vertex, R.string.shader_perFragment_fragment, new String[]{"a_Position", "a_Color", "a_Normal"});
        mPointProgramHandle = ShaderHelper.createProgram(mContext, R.string.shader_point_vertex, R.string.shader_point_fragment, new String[]{"a_Position"});

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

        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees1 = (360.0f / 10000.0f) * ((int) time);
        float angleInDegrees2 = (360.0f / 5000.0f) * ((int) time);

        double angleInRads = Math.toRadians(angleInDegrees1);
        float sin = (float) Math.sin(angleInRads);
        float cos = (float) Math.cos(angleInRads);

        GLES20.glUseProgram(mPerFragProgramHandle);

        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerFragProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mPerFragProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mPerFragProgramHandle, "u_LightPos");
        mPositionHandle = GLES20.glGetAttribLocation(mPerFragProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mPerFragProgramHandle, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mPerFragProgramHandle, "a_Normal");

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees2, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        // Draw some cubes.
        cube1.setPosition(4.0f * sin, 4.0f * cos, -7.0f);
        cube1.setRotation(angleInDegrees1, 0.0f, 0.0f);
        updateUniforms(cube1);
        cube1.draw(mPositionHandle, mColorHandle, mNormalHandle);

        cube2.setPosition(-4.0f * sin, -4.0f * cos, -7.0f);
        cube2.setRotation(0.0f, angleInDegrees1, 0.0f);
        updateUniforms(cube2);
        cube2.draw(mPositionHandle, mColorHandle, mNormalHandle);

        cube3.setPosition(0.0f, 4.0f * sin, 4.0f * cos - 7.0f);
        cube3.setRotation(0.0f, 0.0f, angleInDegrees1);
        updateUniforms(cube3);
        cube3.draw(mPositionHandle, mColorHandle, mNormalHandle);

        cube4.setPosition(0.0f, -4.0f * sin, -4.0f * cos - 7.0f);
        updateUniforms(cube4);
        cube4.draw(mPositionHandle, mColorHandle, mNormalHandle);

        cube5.setPosition(0.0f, 0.0f, -5.0f);
        cube5.setRotation(angleInDegrees1, angleInDegrees1, 0.0f);
        updateUniforms(cube5);
        cube5.draw(mPositionHandle, mColorHandle, mNormalHandle);

        // Draw a point to indicate the light.
        GLES20.glUseProgram(mPointProgramHandle);
        drawLight();
    }

    private void updateUniforms(Cube cube) {
        float[] modelMatrix = cube.getTransformation();

        // multiply view by the model (model * view) and pass it in
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // multiply projection by the viewModel (model * view * projection) and pass it in
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // pass in light position
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
    }

    private void drawLight() {
        final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
        final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position");

        GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle);

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }
}
