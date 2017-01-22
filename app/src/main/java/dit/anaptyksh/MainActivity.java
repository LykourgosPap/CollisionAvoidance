package dit.anaptyksh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;



public class MainActivity extends AppCompatActivity {
    public static Handler mHandler;
    private Intent service;
    public static Intent Sensorservice;
    public static String DeviceId;
    private Intent CheckserviceMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("devid", "notnull")=="notnull") {
            // <---- run your one time code here
            TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            String tmDevice = tm.getDeviceId();

            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("devid", tmDevice);
            editor.commit();
        }
        DeviceId = prefs.getString("devid", "ads");
        mHandler= new Handler();
        setContentView(R.layout.activity_main);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_launcher);
        if(RunItOffline.offline==true)
        {
            Sensorservice=new Intent(this,SensorService.class);
            startService(Sensorservice);
        }
        else {
            if (CheckConnection.isOnline()) {
                service = new Intent(this, Online_Activity.class);
                startActivity(service);
            } else {
                Sensorservice = new Intent(this, SensorService.class);
                startService(Sensorservice);
                if(isCheckConnectionServiceCreated.isCheckCreated==false) {
                    CheckserviceMain = new Intent(this, CheckConnectionService.class);
                    startService(CheckserviceMain);
                    isCheckConnectionServiceCreated.isCheckCreated = true;
                    CheckConnectionCreator.CheckCreator="Main";
                }
                nameOfRunningActivity.currentActivity = "MainActivity";
            }
        }
        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
            stopService(Sensorservice);
            stopService(CheckserviceMain);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent( this, SettingsActivity.class);
                if(RunItOffline.offline==true)
                {
                    stopService(Sensorservice);
                    startActivity(intent);

                }
                else {
                    stopService(Sensorservice);
                    if(CheckConnectionCreator.CheckCreator=="Main")
                        stopService(CheckserviceMain);
                    else if(CheckConnectionCreator.CheckCreator=="Online")
                        stopService(Online_Activity.CheckserviceOnline);
                    else
                        stopService(SettingsActivity.CheckServiceSettings);
                    startActivity(intent);
                }


                return true;
            case R.id.exit:
                stopService(Sensorservice);
                stopService(CheckserviceMain);
                System.exit(0);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        stopService(Sensorservice);
                        stopService(CheckserviceMain);
                        System.exit(0);

                    }
                }).create().show();
    }



}
