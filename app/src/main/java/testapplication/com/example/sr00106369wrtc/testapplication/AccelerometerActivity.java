package testapplication.com.example.sr00106369wrtc.testapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accelerometerSensor;

    TextView xAxis, yAxis, zAxis;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    boolean accelerometerExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            accelerometerExist = true;
        }
        else{
            accelerometerExist = false;
        }

        xAxis = (TextView) findViewById(R.id.currentX);
        yAxis = (TextView) findViewById(R.id.currentY);
        zAxis = (TextView) findViewById(R.id.currentZ);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(accelerometerExist){
            sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            Toast.makeText(this,"Device does not have Accelerometer",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this,accelerometerSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        deltaX = event.values[0];
        deltaY = event.values[1];
        deltaZ = event.values[2];

        xAxis.setText(Float.toString(deltaX));
        yAxis.setText(Float.toString(deltaY));
        zAxis.setText(Float.toString(deltaZ));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
