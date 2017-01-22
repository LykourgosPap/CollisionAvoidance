package dit.anaptyksh;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {
    private Intent service;
    private Intent service2;
    public static Intent CheckServiceSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service=new Intent(this,SensorService.class);
        startService(service);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
    .replace(android.R.id.content, new MyPreferenceFragment())
            .commit();
    }

    public void onBackPressed() {
        stopService(service);
        if(RunItOffline.offline==false)
        {
            CheckServiceSettings=new Intent(this,CheckConnectionService.class);
            startService(CheckServiceSettings);
            CheckConnectionCreator.CheckCreator="Settings";
        }
        service2=new Intent(this,MainActivity.class);
        startActivity(service2);

    }
}
