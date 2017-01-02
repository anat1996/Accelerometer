package acceler.acceler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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
    boolean run_flag = true, flag=true, left=false;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    AnimationDrawable smurf, smurfL;
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
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_GAME);//register the sensor using one of the SensorManager's public methods, registerListener. This method accepts three arguments, the activity's context, a sensor, and the rate at which sensor events are delivered to us.
        if(left == false & y_value<-2)
            left=true;

        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        ImageView smurfImage = (ImageView) findViewById(R.id.smurf_image);
        smurfImage.setBackgroundResource(R.drawable.smurf_anim);
        smurf = (AnimationDrawable) smurfImage.getBackground();
        ImageView smurfImageL = (ImageView) findViewById(R.id.smurf_image_l);
        smurfImage.setBackgroundResource(R.drawable.smurf_anim_l);
        smurfL = (AnimationDrawable) smurfImageL.getBackground();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        double acceleration;
        ImageView smurfImage = (ImageView) findViewById(R.id.smurf_image);
        ImageView smurfImageL = (ImageView) findViewById(R.id.smurf_image_l);
        Animation jump = AnimationUtils.loadAnimation(this,R.anim.jump);
        TextView TV = (TextView) findViewById(R.id.x_text);
        x_value=event.values[0];
        y_value=event.values[1];
        z_value=event.values[2];
        acceleration = Math.sqrt(Math.pow(x_value, 2) + Math.pow(y_value, 2) + Math.pow(z_value, 2)) - SensorManager.GRAVITY_EARTH;

        TV.setText("x: "+x_value);
        TV = (TextView) findViewById(R.id.y_text);
        TV.setText("y: "+y_value);
        TV = (TextView) findViewById(R.id.z_text);
        TV.setText("z: "+z_value);

        if(smurfImage.getBottom() > 1)
            flag=false;
        if(x_value > 5 & y_value > 2){
            if(left){
                smurfImageL.setVisibility(View.INVISIBLE);
                smurfL.setVisible(false,false);
                smurfL.stop();
                smurfImage.setX(smurfImageL.getX()+2);
                smurfImageL.setX(TV.getX()+700);
                smurfImage.setVisibility(View.VISIBLE);
                smurf.setVisible(true,true);
                smurf.start();
                left=false;
            }else {
                smurfImage.setX(smurfImage.getX() + 2);
                smurfImageL.setVisibility(View.INVISIBLE);
                smurfImage.setVisibility(View.VISIBLE);
            }
        }else if(x_value > 5 & y_value < -2){
            if(left==false) {
                smurfImage.setVisibility(View.INVISIBLE);
                smurf.setVisible(false,false);
                smurf.stop();
                smurfImageL.setX(smurfImage.getX() - 2);
                smurfImage.setX(TV.getX()+700);
                smurfImageL.setVisibility(View.VISIBLE);
                smurfL.setVisible(true,true);
                smurfL.start();
                left=true;
            }else {
                smurfImageL.setX(smurfImageL.getX() - 2);
                smurfImage.setVisibility(View.INVISIBLE);
                smurfImageL.setVisibility(View.VISIBLE);
            }
        }else if (x_value>10){               //STILL HASNT BEEN IMPLEMENTED

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
                smurfL.stop();
                smurf.stop();
                isStarted = false;
            } else {
                smurfL.start();
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
