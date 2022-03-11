package com.example.sensingactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ReadingSensors mBoundService;
    private boolean mIsBound;
    Intent message;

    private ServiceConnection mconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBoundService =((ReadingSensors.LocalBinder)iBinder).getServices();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBoundService=null;

        }
    };

    void doUnbindService(){
        if(mIsBound){
            unbindService(mconnection);
            mIsBound=false;

        }
    }

    void doBindService(){
        message= new Intent(this,ReadingSensors.class);
        mIsBound=true;
    }


    public void button_Start_click(View v){
        Log.i("SensingActivity","Launching teh service ReadingSensors\n");
        doBindService();
        this.startService(message);
    }



    public void button_Stop_click(View v){
        doUnbindService();
        finish();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}