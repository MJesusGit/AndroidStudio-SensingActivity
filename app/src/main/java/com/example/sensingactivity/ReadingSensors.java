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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ReadingSensors extends IntentService  implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor macc;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) { //FALTA ALGO!!!1 copiar lo que queda de las diapos
        float value_sensor= sensorEvent.values[0];
        Log.i("On sensor changed",value_sensor+"\n");

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
        Log.w("ReadingSensor Service","ReadingSensor service started!\n");
        sensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
         macc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(macc!=null){
            Log.w("ReadingSensor Service","hay acelerometro!\n");
            sensorManager.registerListener(this,macc,SensorManager.SENSOR_DELAY_NORMAL);
            
        }else{
            Log.w("ReadingSensor Service","ERROR: acc not available!\n");
        }
        if (intent != null) {
            final String action = intent.getAction();

        }
    }


}