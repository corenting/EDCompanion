package fr.corenting.edcompanion.activities;

import android.os.Bundle;

import com.lb.material_preferences_library.PreferenceActivity;

import fr.corenting.edcompanion.R;


public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getPreferencesXmlId() {
        return R.xml.settings;
    }
}
