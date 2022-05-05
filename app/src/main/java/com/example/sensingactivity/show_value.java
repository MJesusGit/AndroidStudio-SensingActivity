package com.example.sensingactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Activity for showing the values of the sensor in the x-axis and y-axis
 *
 * This activity is used to display different  values
 *
 * @author Maria Jesús Dueñas
 * @version 1.2
 *
 */
public class show_value extends Activity implements SensorEventListener {
    //puerto del servidor
    final int PUERTO_SERVIDOR = 5000;
    //buffer donde se almacenara los mensajes
    byte[] buffer = new byte[1024];
    private float valueX_Socket;
    private float valueY_Socket;

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

        Log.i("[Sensor Activity]", "Starting sensor ");
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
    /**
     * Method that show the values in some labels
     *
     * @param sensorEvent which obtain the values of the axis' accelerometer
     *
     */
    public void onSensorChanged(SensorEvent sensorEvent) {
        valueX_Socket= sensorEvent.values[0];
        valueY_Socket= sensorEvent.values[1];

        txt_valueX.setText(String.valueOf(valueX_Socket));
        txt_valueY.setText(String.valueOf(valueY_Socket));

        String info= "X: "+valueX_Socket + " "+ "Y" +valueY_Socket;
        System.out.println(info);

        new Thread (new Client_udp()).start();
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this, acc);
        super.onStop();
    }
    /**
     * Method that stop the activity.
     *
     * Show a message in the terminal that finish the app.
     *
     * Return to the main activity.
     */
    public void button_Stop_click(View v){
        if (v.getId()==R.id.button_Stop) {
            Log.i("[SENSOR ACTIVITY]", "Stopping aplication...\n");
            onStop();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    /**
     * this class will do the role of client in our client/server conection
     *
     *
     *
     * @author from https://www.discoduroderoer.es/ejemplo-conexion-udp-clienteservidor-en-java/
     * @version 1.2
     *
     */

     class Client_udp implements Runnable {
        public void run(){

            try {
                //Obtengo la localizacion de localhost
                InetAddress ip_address = InetAddress.getByName("INSERTAR IP DE LA WIFI QUE SE ESTA CONECTADO");
                //Creo el socket de UDP
                DatagramSocket socketUDP = new DatagramSocket();
                String message ="X= "+valueX_Socket + " "+ "Y=" +valueY_Socket ;
                //Convierto el mensaje a bytes
                buffer = message.getBytes();
                //Creo un datagrama
                DatagramPacket petition = new DatagramPacket(buffer, buffer.length, ip_address, PUERTO_SERVIDOR);
                //Lo envio con send
                System.out.println("Envio el datagrama");
                socketUDP.send(petition);
                //Preparo la respuesta
                DatagramPacket petition2 = new DatagramPacket(buffer, buffer.length);
                //Recibo la respuesta
                socketUDP.receive(petition2);
                System.out.println("Recibo la peticion");
                //Cojo los datos y lo muestro
                message = new String(petition2.getData());
                System.out.println(message);
                //cierro el socket
                socketUDP.close();

            } catch (SocketException ex) {
                Logger.getLogger(show_value.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(show_value.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(show_value.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}