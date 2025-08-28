package com.tudominio.semaforosthreads;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private ImageView ivUno;
    private Thread tFase1;
    private volatile boolean runningFase1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivUno = findViewById(R.id.ivUno);
    }

    // onClick FASE 1
    public void startFase1(View v) {
        if (tFase1 != null && tFase1.isAlive()) return; // evita hilos duplicados
        runningFase1 = true;

        tFase1 = new Thread(new Runnable() {
            @Override public void run() {
                boolean on = false;
                while (runningFase1) {
                    on = !on;
                    final boolean finalOn = on;
                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            ivUno.setImageResource(finalOn ? R.drawable.light_red_on : R.drawable.light_off);
                        }
                    });
                    sleepMs(5000); // 5 seg encendido, 5 seg apagado
                }
            }
        });
        tFase1.start();
    }

    // Fase 2
    private Thread tFase2;
    private volatile boolean runningFase2 = false;

    public void startFase2(View v) {
        if (tFase2 != null && tFase2.isAlive()) return;
        runningFase2 = true;

        tFase2 = new Thread(new Runnable() {
            @Override public void run() {
                while (runningFase2) {
                    setSingleColor(R.drawable.light_red_on);    sleepMs(5000);
                    setSingleColor(R.drawable.light_yellow_on); sleepMs(5000);
                    setSingleColor(R.drawable.light_green_on);  sleepMs(5000);
                }
            }
        });
        tFase2.start();
    }

    private void setSingleColor(final int drawableId) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                ivUno.setImageResource(drawableId);
            }
        });
    }


    private static void sleepMs(long ms) { try { Thread.sleep(ms); } catch (InterruptedException ignored) {} }

    @Override
    protected void onDestroy() {
        runningFase1 = false;
        super.onDestroy();
    }
}


