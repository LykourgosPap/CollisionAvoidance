package dit.anaptyksh;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;


public class Online_Activity extends AppCompatActivity{
    public static Context context;
    public static Intent yolo;
    private Intent service;
    public static Intent CheckserviceOnline;
    public static boolean first=false;
    public static SoundPool mypool2;
    public static int AlertId;
    public static String MQTT_IP;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_activity);
        context = getApplicationContext();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Broker's IP");
        alert.setMessage("Please Set MQTT Broker's IP");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MQTT_IP = input.getText().toString();
            }
        });

        alert.show();
        mypool2 = new SoundPool(1, AudioManager.STREAM_ALARM, 0);                                    //rithmiseis gia to alert
        AlertId = mypool2.load(this, R.raw.pistol_shot, 1);
        yolo = new Intent(this, OnlineService.class);
        startService(yolo);
        if(isCheckConnectionServiceCreated.isCheckCreated==false) {
            CheckserviceOnline = new Intent(this, CheckConnectionService.class);
            startService(CheckserviceOnline);
            isCheckConnectionServiceCreated.isCheckCreated = true;
            CheckConnectionCreator.CheckCreator="Online";
        }
        nameOfRunningActivity.currentActivity="Online_Activity";
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.online_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId()==R.id.exit)
        {
            stopService(yolo);
            stopService(CheckserviceOnline);
            System.exit(0);
        }
        else if(item.getItemId()==R.id.GoOff)
        {
            RunItOffline.offline=true;
            stopService(yolo);
            service = new Intent(this, MainActivity.class);
            startActivity(service);

        }
            return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        stopService(yolo);
                        stopService(CheckserviceOnline);
                        finish();
                    }
                }).create().show();
    }



}

