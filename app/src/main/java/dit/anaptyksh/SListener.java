package dit.anaptyksh;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;




public class SListener implements SensorEventListener {
    public float val;
    public boolean Online;

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        val = event.values[0];
    }
}
