package acceler.acceler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.R.anim.accelerate_decelerate_interpolator;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;

    float x_value,y_value,z_value;
    boolean run_flag = true, flag=true, left=false;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    AnimationDrawable smurf, smurfL;
    boolean isStarted;
    MediaPlayer mediaPlayer;
    Button button;

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
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);//register the sensor using one of the SensorManager's public methods, registerListener. This method accepts three arguments, the activity's context, a sensor, and the rate at which sensor events are delivered to us.
        if(left == false & y_value<-2)
            left=true;


        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.smurf_reset);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "start");
                ImageView smurfImage = (ImageView) findViewById(R.id.smurf_image_l);
                Animation inter = AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump);
                LinearLayoutCompat.LayoutParams layoutParams= new LinearLayoutCompat.LayoutParams(smurfImage.getWidth()+1,smurfImage.getHeight()+1);
                Animation jump = AnimationUtils.loadAnimation(MainActivity.this,R.anim.jump);
                smurfImage.setX( ((RelativeLayout) findViewById(R.id.activity_main)).getWidth()/2);


            }
        });
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        ImageView smurfImageL = (ImageView) findViewById(R.id.smurf_image_l);
        smurfImageL.setBackgroundResource(R.drawable.smurf_anim_l);
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
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float xDiff,xCurr, yDiff, yCurr, zDiff, zCurr;
        ImageView smurfImage = (ImageView) findViewById(R.id.smurf_image_l);
        TextView TV;

        xCurr=event.values[0];
        yCurr=event.values[1];
        zCurr=event.values[2];
        xDiff = xCurr - x_value;
        yDiff = yCurr - y_value;
        zDiff = zCurr - z_value;
        x_value=xCurr;
        y_value=yCurr;
        z_value=zCurr;

        if( Math.abs(xDiff) > 0.5 ){
            TV = (TextView) findViewById(R.id.x_text);
            TV.setText("x: "+ xCurr);
        }
        if( Math.abs(yDiff) > 0.5 ){
            TV = (TextView) findViewById(R.id.y_text);
            TV.setText("y: "+ yCurr);
        }
        if( Math.abs(zDiff) > 0.5 ){
            TV = (TextView) findViewById(R.id.z_text);
            TV.setText("z: "+ zCurr);
        }

        LinearLayoutCompat.LayoutParams layoutParams= new LinearLayoutCompat.LayoutParams(smurfImage.getWidth()+1,smurfImage.getHeight()+1);
        double acceleration = Math.sqrt(Math.pow(x_value, 2) + Math.pow(y_value, 2) + Math.pow(z_value, 2)) - SensorManager.GRAVITY_EARTH;
        Animation jump = AnimationUtils.loadAnimation(this,R.anim.jump);

        if(x_value > 5 & y_value > 2){
            if(left){
                //smurfImage.setImageBitmap(flipImage(BitmapFactory.decodeResource(getResources(), R.drawable.smurf_anim_l),2));
                smurfImage.setX(smurfImage.getX()+6);
                left=false;
            }
            smurfImage.setX(smurfImage.getX()+6);
        }else if(x_value > 5 & y_value < -2){
            if(left==false){

                smurfImage.setX(smurfImage.getX()-6);
                left=true;
            }
            smurfImage.setX(smurfImage.getX()-6);
        }if (x_value >10 ){               //STILL HASNT BEEN IMPLEMENTED
        smurfImage.startAnimation(jump);
        Log.d("Activity","SHAKE");
    }

        return;



    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isStarted) {
                smurfL.stop();
                isStarted = false;
            } else {
                smurfL.start();
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
    public Bitmap flipImage(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if(type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if(type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

}
