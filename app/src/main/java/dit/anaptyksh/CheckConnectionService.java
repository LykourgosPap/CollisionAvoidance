package dit.anaptyksh;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CheckConnectionService extends Service {
    private ScheduledExecutorService scheduleTaskExecutor;
    private Intent service1;
    private Intent service2;

    public int onStartCommand(Intent intent, int flags, int startId) {
        //service1=new Intent(this,Online_Activity.class);
        //service2=new Intent(this,MainActivity.class);



        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);        //ekkinhsh tou executor gia 1 thread
        scheduleTaskExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run(){
            if((CheckConnection.isOnline())&&(nameOfRunningActivity.currentActivity=="MainActivity")) {
                stopService(MainActivity.Sensorservice);
                service1 = new Intent(CheckConnectionService.this,Online_Activity.class);
                service1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(service1);
            }
            else if((!CheckConnection.isOnline())&&(nameOfRunningActivity.currentActivity=="Online_Activity"))
            {
                stopService(Online_Activity.yolo);
                service2=new Intent(CheckConnectionService.this,MainActivity.class);
                service2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(service2);
            }
            }
        },0,3, TimeUnit.SECONDS);

            return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        scheduleTaskExecutor.shutdown();
        super.onDestroy();
    }
}
