package dit.anaptyksh;

import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.os.Bundle;


public class MyPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);
        ListPreference pref = (ListPreference) findPreference("LR");
        if(pref.getValue() == null){
            pref.setValueIndex(0); //set to index of your default value
        }
        SensorService.LightR = pref.getValue();
        pref = (ListPreference) findPreference("PR");
        if(pref.getValue() == null){
            pref.setValueIndex(0); //set to index of your default value
        }
        SensorService.ProximityR = pref.getValue();
        pref = (ListPreference) findPreference("LS");
        if(pref.getValue() == null){
            pref.setValueIndex(0); //set to index of your default value
        }
        SensorService.LightT = pref.getValue();
        pref = (ListPreference) findPreference("PS");
        if(pref.getValue() == null){
            pref.setValueIndex(0); //set to index of your default value
        }
        SensorService.ProximityT = pref.getValue();
    }
}