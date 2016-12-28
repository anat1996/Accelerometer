package acceler.acceler;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor mSensor;
    AnimationDrawable smurf;
    boolean isStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER ) != null)
        {// Success! There's a accelerometer.
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        else {// Failure! No accelerometer.
        }
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);//register the sensor using one of the SensorManager's public methods, registerListener. This method accepts three arguments, the activity's context, a sensor, and the rate at which sensor events are delivered to us.

        setContentView(R.layout.activity_main);
        ImageView smurfImage = (ImageView) findViewById(R.id.smurf_image);
        smurfImage.setBackgroundResource(R.drawable.smurf_anim);
        smurf = (AnimationDrawable) smurfImage.getBackground();



    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

//        final float alpha = 0.8;
//
//        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
//        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
//        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
//
//        linear_acceleration[0] = event.values[0] - gravity[0];
//        linear_acceleration[1] = event.values[1] - gravity[1];
//        linear_acceleration[2] = event.values[2] - gravity[2];
        TextView TV = (TextView) findViewById(R.id.x);
        TV.setText("x: "+event.values[0]);
        TV = (TextView) findViewById(R.id.y);
        TV.setText("y: "+event.values[1]);
        TV = (TextView) findViewById(R.id.z);
        TV.setText("z: "+event.values[2]);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isStarted) {
                smurf.stop();
                isStarted = false;
            } else {
                smurf.start();
                isStarted = true;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}
