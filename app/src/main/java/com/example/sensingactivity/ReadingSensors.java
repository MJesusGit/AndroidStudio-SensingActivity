package com.example.sensingactivity;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.util.Log;


public class ReadingSensors extends IntentService  implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor macc;
   @Override
   public void onSensorChanged(SensorEvent sensorEvent) {
       

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class LocalBinder extends Binder {
        ReadingSensors getServices(){ return ReadingSensors.this;}

    }


    public ReadingSensors() {
        super("ReadingSensors");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Log.w("[READING SENSORS]","Reading Sensor is started!\n");
        sensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
         macc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(macc!=null){
            Log.w("[READING SENSORS]","THERE IS ACCELEROMETER!!\n");
            sensorManager.registerListener(this,macc,SensorManager.SENSOR_DELAY_NORMAL);
            
        }else{
            Log.w("[READING SENSORS]","ERROR: THERE IS ACCELEROMETER!!\n");
        }
        if (intent != null) {
            final String action = intent.getAction();

        }
    }


}