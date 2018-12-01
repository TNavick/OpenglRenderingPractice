package com.tautvydas.openglrenderingpractice.common;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.ContentValues.TAG;

public class ShaderHelper {


    public static int createProgram(Context context, int vertexShaderResourceID, int fragmentShaderResourceID, String[] attributes) {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        String vertexShaderFile = context.getResources().getString(vertexShaderResourceID);
        String fragmentShaderFile = context.getResources().getString(fragmentShaderResourceID);

        addShaderToProgram(programHandle, loadTextFromRawShaderFile(context, vertexShaderFile), GLES20.GL_VERTEX_SHADER);
        addShaderToProgram(programHandle, loadTextFromRawShaderFile(context, fragmentShaderFile), GLES20.GL_FRAGMENT_SHADER);

        addAttributes(programHandle, attributes);

        return linkProgram(programHandle);
    }

    private static void addAttributes(int programHandle, String[] attributes) {
        if (attributes != null) {
            for (int i = 0; i < attributes.length; i++) {
                GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
            }
        }
    }

    private static int linkProgram(int programHandle) {
        // Link the two shaders together into a program.
        GLES20.glLinkProgram(programHandle);

        // Get the link status.
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

        // If the link failed, delete the program.
        if (linkStatus[0] == 0) {
            Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
            GLES20.glDeleteProgram(programHandle);
            return programHandle = 0;
        }

        return programHandle;
    }

    private static void addShaderToProgram(int programHandle, String shaderText, int shaderType) {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderText);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0) {
            throw new RuntimeException("Error creating vertex shader.");
        }

        GLES20.glAttachShader(programHandle, shaderHandle);
    }

    private static String loadTextFromRawShaderFile(Context context, String fileName) {
        String ret = "";

        try {
            InputStream stream = context.getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
                stringBuilder.append("\n");
            }

            stream.close();
            ret = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e("File Read", "File not found: " + e.toString());
            return null;
        } catch (IOException e) {
            Log.e("File Read", "Can't read file: " + e.toString());
            return null;
        }

        return ret;
    }
}
