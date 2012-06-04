/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.andro;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 *
 * @author noone
 */
public class PrefActivity extends PreferenceActivity {

    public PrefActivity() {
        super ();
        addPreferencesFromResource(R.xml.settings);
    }

    
}
