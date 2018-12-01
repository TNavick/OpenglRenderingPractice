package com.tautvydas.openglrenderingpractice.common;

import android.opengl.Matrix;

public class Transform {
    private float[] mPosition;
    private float[] mRotation;
    private float[] mScale;

    public Transform() {
        mPosition = new float[]{0, 0, 0};
        mRotation = new float[]{0, 0, 0};
        mScale = new float[]{1, 1, 1};
    }

    public float[] getTransformation() {
        float[] modelMatrix = new float[16];

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, mPosition[0], mPosition[1], mPosition[2]);
        Matrix.rotateM(modelMatrix, 0, mRotation[0], 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, mRotation[1], 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, mRotation[2], 0.0f, 0.0f, 1.0f);
        Matrix.scaleM(modelMatrix, 0, mScale[0], mScale[1], mScale[2]);

        return modelMatrix;
    }

    public void setPosition(float[] mPosition) {
        this.mPosition = mPosition;
    }

    public void setRotation(float[] mRotation) {
        this.mRotation = mRotation;
    }

    public void setScale(float[] mScale) {
        this.mScale = mScale;
    }
}
