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
        ivDos = findViewById(R.id.ivDos);


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
    // en onCreate, después de ivUno = ...


    // Fase 3
    private ImageView ivDos;
    private Thread tFase3;
    private volatile boolean runningFase3 = false;

    public void startFase3(View v) {
        if (tFase3 != null && tFase3.isAlive()) return;
        runningFase3 = true;

        tFase3 = new Thread(new Runnable() {
            @Override public void run() {
                while (runningFase3) {
                    setColor(ivDos, R.drawable.light_red_on);    sleepMs(5000);
                    setColor(ivDos, R.drawable.light_yellow_on); sleepMs(5000);
                    setColor(ivDos, R.drawable.light_green_on);  sleepMs(5000);
                }
            }
        });
        tFase3.start();
    }

    private void setColor(final ImageView iv, final int drawableId) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                iv.setImageResource(drawableId);
            }
        });
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


