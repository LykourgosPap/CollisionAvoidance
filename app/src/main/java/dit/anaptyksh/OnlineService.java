package dit.anaptyksh;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;


public class OnlineService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    private volatile Location mLastLocation;
    private ScheduledExecutorService scheduleTaskExecutor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        Thread subscribe = new Thread(){
            @Override
            public void run(){
                while (Online_Activity.MQTT_IP==null){
                    ;
                }
                if (Online_Activity.first==false)
                    Subscribe.sub(MainActivity.DeviceId);
                Online_Activity.first=true;
                mGoogleApiClient.connect();

            }
        };
        subscribe.start();

        return Service.START_STICKY;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);


        //To thread pou stelnei ta dedomena ston mqtt broker
        scheduleTaskExecutor.scheduleWithFixedDelay(new Runnable() {
            private SensorManager mSensorManager;
            private Sensor mSensor;
            final SListener SListener = new SListener();
            final SListener SListener2 = new SListener();

            @Override
            public void run() {
                double mLatitude=0;
                double mLongitude=0;
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                mSensorManager.registerListener(SListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                float prox = SListener.val;
                mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                mSensorManager.registerListener(SListener2, mSensor, SensorManager.SENSOR_DELAY_UI);
                float light = SListener2.val;
                mLatitude=mLastLocation.getLatitude();
                mLongitude=mLastLocation.getLongitude();
                Publish.pub(MainActivity.DeviceId, prox, light, mLatitude, mLongitude);

            }
        }, 500, 500, TimeUnit.MILLISECONDS);
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
        Context context = getApplicationContext();
        CharSequence text = "Please enable your GPS and restart application";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onDestroy() {
        scheduleTaskExecutor.shutdown();
        super.onDestroy();
    }
}
