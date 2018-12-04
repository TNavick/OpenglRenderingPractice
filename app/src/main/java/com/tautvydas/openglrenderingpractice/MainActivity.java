package com.tautvydas.openglrenderingpractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tautvydas.openglrenderingpractice.lesson1.Lesson1Activity;
import com.tautvydas.openglrenderingpractice.lesson2.Lesson2Activity;
import com.tautvydas.openglrenderingpractice.lesson3.Lesson3Activity;
import com.tautvydas.openglrenderingpractice.lesson4.Lesson4Activity;
import com.tautvydas.openglrenderingpractice.lesson5.Lesson5Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleClick(View view) {
        Button button = (Button) view;

        switch (button.getId()) {
            case R.id.btn1:
                goToLesson(Lesson1Activity.class);
                break;
            case R.id.btn2:
                goToLesson(Lesson2Activity.class);
                break;
            case R.id.btn3:
                goToLesson(Lesson3Activity.class);
                break;
            case R.id.btn4:
                goToLesson(Lesson4Activity.class);
                break;
            case R.id.btn5:
                goToLesson(Lesson5Activity.class);
                break;
        }
    }

    private void goToLesson(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
