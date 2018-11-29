package com.tautvydas.openglrenderingpractice.lesson1;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Lesson1Activity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);

        mGLSurfaceView.setEGLContextClientVersion(2);

        BasicRenderer1 renderer = new BasicRenderer1();
        renderer.setVertexShader(readFromFile("BasicShader.vert"));
        renderer.setFragmentShader(readFromFile("BasicShader.frag"));

        mGLSurfaceView.setRenderer(renderer);

        setContentView(mGLSurfaceView);
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

    private String readFromFile(String file) {
        String ret = "";

        try {
            InputStream stream = getAssets().open(file);

            if (stream != null) {
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
            }
        } catch (FileNotFoundException e) {
            Log.e("File Read", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("File Read", "Can't read file: " + e.toString());
        }

        return ret;
    }
}
