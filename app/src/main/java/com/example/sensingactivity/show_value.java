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
    private TextView txt_valueY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_value);

        Log.i("[Sensor Activity]", "Inicializating sensor services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(show_value.this, acc, sensorManager.SENSOR_DELAY_NORMAL);
        Log.i("[SENSOR ACTIVITY]", " Accelerometer registered.");

        txt_valueX= (TextView) findViewById(R.id.txt_valueX);
        txt_valueY= (TextView) findViewById(R.id.txt_valueY);
    }

    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,acc,SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float value_sensorX= sensorEvent.values[0];
        float value_sensorY= sensorEvent.values[1];

        txt_valueX.setText(String.valueOf(value_sensorX));
        txt_valueY.setText(String.valueOf(value_sensorY));

        String info= "X: "+value_sensorX + " "+ "Y" +value_sensorY;
        System.out.println(info);

        try {
            PrintWriter bf= new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(udp.getOutputStream())),
                    true);
            bf.println(info);
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
    public void button_Stop_click(View v){
        if (v.getId()==R.id.button_Stop) {
            Log.i("[SENSOR ACTIVITY]", "Stopping aplication...\n");
            onStop();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    class ClientThread implements Runnable{
        @Override
        public void run() {
            InetAddress s = null;
            try {
                udp = new Socket(s, 8050);
            }catch (UnknownHostException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
}
}