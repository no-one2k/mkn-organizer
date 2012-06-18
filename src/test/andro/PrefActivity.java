/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.andro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputType;
import java.util.Map;

/**
 *
 * @author noone
 */
public class PrefActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager preferenceManager = this.getPreferenceManager();
        SharedPreferences sp = preferenceManager.getSharedPreferences();
        String sharedPreferencesName = preferenceManager.getSharedPreferencesName();
        String s = getString(R.string.send_vk);
        boolean b=sp.getBoolean(s, true);
        Map<String, ?> allprefs = sp.getAll();
        for (String pref : allprefs.keySet()) {
            Preference findPreference = this.findPreference(pref);
            if (findPreference instanceof EditTextPreference) {
                EditTextPreference editTextPreference = (EditTextPreference) findPreference;
                if (editTextPreference.getEditText().getInputType() != (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    editTextPreference.setSummary(editTextPreference.getText());
                    editTextPreference.setOnPreferenceChangeListener(this);
                }
            }
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary( (newValue==null)?null:newValue.toString());

        return true;
    }
}
