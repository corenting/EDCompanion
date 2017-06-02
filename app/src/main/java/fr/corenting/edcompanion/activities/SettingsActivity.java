package fr.corenting.edcompanion.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import com.lb.material_preferences_library.PreferenceActivity;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.utils.ThemeUtils;


public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        // Set theme first before parent call
        AppCompatDelegate.setDefaultNightMode(ThemeUtils.getDarkThemeValue(this));

        super.onCreate(savedInstanceState);

        ThemeUtils.setToolbarColor(this, getToolbar());
    }

    @Override
    protected int getPreferencesXmlId() {
        return R.xml.settings;
    }
}
