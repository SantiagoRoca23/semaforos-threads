package com.tudominio.semaforosthreads;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Semáforo 1
    private ImageView s1Red, s1Yellow, s1Green;
    // Semáforo 2
    private ImageView s2Red, s2Yellow, s2Green;

    // Hilos
    private Thread t1, t2;
    private volatile boolean running1 = false, running2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        s1Red = findViewById(R.id.s1Red);
        s1Yellow = findViewById(R.id.s1Yellow);
        s1Green = findViewById(R.id.s1Green);

        s2Red = findViewById(R.id.s2Red);
        s2Yellow = findViewById(R.id.s2Yellow);
        s2Green = findViewById(R.id.s2Green);
    }

    // ONCLICK FASE 4: cada semáforo enciende 1 color a la vez (5s) y apaga los demás
    public void startFase4(View v) {
        if (t1 == null || !t1.isAlive()) {
            running1 = true;
            t1 = new Thread(new Runnable() {
                @Override public void run() {
                    while (running1) {
                        setState(1, "RED");    sleepMs(5000);
                        setState(1, "YELLOW"); sleepMs(5000);
                        setState(1, "GREEN");  sleepMs(5000);
                    }
                }
            });
            t1.start();
        }

        if (t2 == null || !t2.isAlive()) {
            running2 = true;
            t2 = new Thread(new Runnable() {
                @Override public void run() {
                    while (running2) {
                        setState(2, "RED");    sleepMs(5000);
                        setState(2, "YELLOW"); sleepMs(5000);
                        setState(2, "GREEN");  sleepMs(5000);
                    }
                }
            });
            t2.start();
        }
    }

    private void setState(final int sem, final String color) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                ImageView r = (sem == 1) ? s1Red : s2Red;
                ImageView y = (sem == 1) ? s1Yellow : s2Yellow;
                ImageView g = (sem == 1) ? s1Green : s2Green;

                r.setImageResource(color.equals("RED")    ? R.drawable.light_red_on    : R.drawable.light_off);
                y.setImageResource(color.equals("YELLOW") ? R.drawable.light_yellow_on : R.drawable.light_off);
                g.setImageResource(color.equals("GREEN")  ? R.drawable.light_green_on  : R.drawable.light_off);
            }
        });
    }

    private static void sleepMs(long ms) { try { Thread.sleep(ms); } catch (InterruptedException ignored) {} }

    @Override
    protected void onDestroy() {
        running1 = false; running2 = false;
        super.onDestroy();
    }
}



