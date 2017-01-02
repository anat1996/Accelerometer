package acceler.acceler;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import static android.R.anim.accelerate_decelerate_interpolator;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    float x_value,y_value,z_value;
    boolean run_flag = true, flag=true;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    AnimationDrawable smurf;
    boolean isStarted;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER ) != null)
        {// Success! There's a accelerometer.
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        else {// Failure! No accelerometer.
            onDestroy();
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
        mSensorManager.registerListener(this,mSensor,600);
    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        ImageView smurfImage = (ImageView) findViewById(R.id.smurf_image);
        Animation inter = AnimationUtils.loadAnimation(this,R.anim.jump);
        TextView TV = (TextView) findViewById(R.id.x_text);
        TV.setText("x: "+event.values[0]);
        TV = (TextView) findViewById(R.id.y_text);
        TV.setText("y: "+event.values[1]);
        TV = (TextView) findViewById(R.id.z_text);
        TV.setText("z: "+event.values[2]);
        x_value=event.values[0];
        y_value=event.values[1];
        z_value=event.values[2];
        LinearLayoutCompat.LayoutParams layoutParams= new LinearLayoutCompat.LayoutParams(smurfImage.getWidth()+1,smurfImage.getHeight()+1);
        double acceleration = Math.sqrt(Math.pow(x_value, 2) + Math.pow(y_value, 2) + Math.pow(z_value, 2)) - SensorManager.GRAVITY_EARTH;
        Animation jump = AnimationUtils.loadAnimation(this,R.anim.jump);
        if(smurfImage.getBottom() > 10)
            flag=false;
        if(x_value > 5 & y_value > 2){
            smurfImage.setX(smurfImage.getX()+2);
        }else if(x_value > 5 & y_value < 2){
            smurfImage.setX(smurfImage.getX()-2);
        }if (x_value >10 & flag){               //STILL HASNT BEEN IMPLEMENTED
            smurfImage.startAnimation(jump);
            Log.d("Activity","SHAKE");
        }
        flag=true;


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
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer!=null) mediaPlayer.release();
    }
}
