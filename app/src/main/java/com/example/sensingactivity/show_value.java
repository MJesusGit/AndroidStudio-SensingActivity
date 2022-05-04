package com.example.sensingactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class show_value extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor acc;
    private Socket udp;
    public String ip;
    Thread sensor;
    private TextView txt_valueX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_value);



        Log.i("[Sensor Activity]", "Inicializating Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(show_value.this, acc, sensorManager.SENSOR_DELAY_NORMAL);
        Log.i("[SENSOR ACTIVITY]", "Registered accelerometer listener.");

        txt_valueX= (TextView) findViewById(R.id.txt_valueX);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 11)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            new Thread (new ClientThread()).start();
        }
    }

    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,acc,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorchanged(SensorEvent sensorEvent){

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float value_sensor= sensorEvent.values[0];
        txt_valueX.setText(String.valueOf(value_sensor));
        String info= "X: "+value_sensor;
        System.out.println(info);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(udp.getOutputStream())),
                    true);
            out.println(info);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this, acc);
        super.onStop();
    }
    public void onClickStart(View v){
        if (v.getId()==R.id.button_Stop) {
            Log.i("[SENSOR ACTIVITY]", "Stopping Sensor Activity...\n");
            onStop();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    public void button_Stop_click(View v){
        sensorManager.unregisterListener(this, acc);
        super.onStop();
    }

    class ClientThread implements Runnable{

        @Override
        public void run() {
            InetAddress server = null;
            try {

                udp = new Socket(server, 8050);

            }catch (UnknownHostException e) {
                e.printStackTrace();

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
}
}