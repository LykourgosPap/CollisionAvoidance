package dit.anaptyksh;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SensorService extends Service {

    SoundPool mypool;
    public static String LightR;                        //4 metavlhtes gia tis rithmiseis twn threshold kai ta refresh rate
    public static String ProximityR;
    public static String LightT;
    public static String ProximityT;
    private float prox;                                //2 metavlhtes gia na pernane tis times twn sensor
    private float light;
    private ScheduledExecutorService scheduleTaskExecutor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context ctx = getApplicationContext();                                                      //fortosh twn rithmisewn *
        PreferenceManager.setDefaultValues(ctx, R.xml.pref_general, false);
        SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(ctx);
        LightR = shpref.getString("LR", null);
        ProximityR = shpref.getString("PR", null);
        LightT = shpref.getString("LS", null);
        ProximityT = shpref.getString("PS", null);                                                  // * mexri edw
        final int AlertId;
        mypool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);                                    //rithmiseis gia to alert
        AlertId = mypool.load(this, R.raw.mgs_alert, 1);
        scheduleTaskExecutor = Executors.newScheduledThreadPool(3);        //ekkinhsh tou executor gia 3 threads

        scheduleTaskExecutor.scheduleWithFixedDelay(new Runnable() {                            //1o thread xeirizetai ton proximity sensor
            private SensorManager mSensorManager;
            private Sensor mSensor;
            final SListener SListener = new SListener();
            @Override
            public void run() {
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                mSensorManager.registerListener(SListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                prox = SListener.val;
                if (SListener.Online){

                }
            }
        }, 0, Integer.parseInt(ProximityR), TimeUnit.SECONDS);

        scheduleTaskExecutor.scheduleWithFixedDelay(new Runnable() {                                //2o thread xeirizetai ton light sensor
            private SensorManager mSensorManager;
            private Sensor mSensor;
            final SListener SListener = new SListener();
            @Override
            public void run() {
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                mSensorManager.registerListener(SListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                light = SListener.val;
            }
        }, 0, Integer.parseInt(LightR), TimeUnit.SECONDS);

        scheduleTaskExecutor.scheduleWithFixedDelay(new Runnable() {                                //3o thread xeirizetai ta minimata sthn periptwsh epikeimenhs sugoushs
            @Override
            public void run() {                                         //3o thread xeirizetai ta optikoakoustika mhnymata se periptwsh epikeimenhs sugroushs
                MainActivity.mHandler.post(new Runnable(){
                    public void run(){
                        if (light < Float.parseFloat(LightT) || prox == 0){
                            mypool.play(AlertId, 1, 1, 1, 0, 1);
                            if (prox == 0)
                                Toast.makeText(SensorService.this, "DANGER!!! proximity sensor value is: " + String.valueOf(prox), Toast.LENGTH_SHORT).show();
                            if (light < Float.parseFloat(LightT))
                                Toast.makeText(SensorService.this, "DANGER!!! light sensor value is: " + String.valueOf(light), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, 0, 2000, TimeUnit.MILLISECONDS);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        scheduleTaskExecutor.shutdown();
        super.onDestroy();
    }
}