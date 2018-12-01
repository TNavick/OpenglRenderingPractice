package com.tautvydas.openglrenderingpractice.common;

import android.opengl.Matrix;

public class Transform {
    private float[] mPosition;
    private float mRotationAngle;
    private float[] mRotationAxis;
    private float[] mScale;

    public Transform() {
        mPosition = new float[]{0.0f, 0.0f, 0.0f};
        mRotationAngle = 0.0f;
        mRotationAxis = new float[]{1.0f, 0.0f, 0.0f};
        mScale = new float[]{1.0f, 1.0f, 1.0f};
    }

    public float[] getTransformation() {
        float[] modelMatrix = new float[16];

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, mPosition[0], mPosition[1], mPosition[2]);
        Matrix.rotateM(modelMatrix, 0, mRotationAngle, mRotationAxis[0], mRotationAxis[1], mRotationAxis[2]);
        Matrix.scaleM(modelMatrix, 0, mScale[0], mScale[1], mScale[2]);

        return modelMatrix;
    }

    public void setPosition(float[] mPosition) {
        this.mPosition = mPosition;
    }

    public void setRotation(float rotationAngle, float[] rotationAxis) {
        mRotationAngle = rotationAngle;
        mRotationAxis = rotationAxis;
    }

    public void setScale(float[] mScale) {
        this.mScale = mScale;
    }
}
