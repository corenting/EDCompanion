package fr.corenting.edcompanion.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.network.player.PlayerNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.PlayerNetworkUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ViewGroup rootView = findViewById(R.id.action_bar_root); //id from appcompat

        if (rootView != null) {
            View view = getLayoutInflater().inflate(R.layout.include_app_bar, rootView, false);
            rootView.addView(view, 0);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || CommanderPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationsPreferenceFragment.class.getName().equals(fragmentName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationsPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_notifications);
            setHasOptionsMenu(true);

            initPushPreferences();
        }

        private void initPushPreferences() {

            // Remove push notifications subscreen if Google Play Services are not available
            if (NotificationsUtils.pushNotificationsNotWorking(getActivity())) {
               // TODO : disable preferences when there is no FCM
            }

            // Change Firebase subscriptions on preference change
            final Context context = getActivity();
            Preference.OnPreferenceChangeListener notificationsChangeListener = new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    NotificationsUtils.refreshPushSubscription(context, preference.getKey(), (Boolean) newValue);
                    return true;
                }
            };

            Preference newGoalPreference = findPreference(getString(R.string.settings_notifications_new_goal));
            Preference newTierPreference = findPreference(getString(R.string.settings_notifications_new_tier));
            Preference finishedGoalPreference = findPreference(getString(R.string.settings_notifications_finished_goal));
            if (newGoalPreference != null) {
                newGoalPreference.setOnPreferenceChangeListener(notificationsChangeListener);
            }
            if (newTierPreference != null) {
                newTierPreference.setOnPreferenceChangeListener(notificationsChangeListener);
            }
            if (finishedGoalPreference != null) {
                finishedGoalPreference.setOnPreferenceChangeListener(notificationsChangeListener);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CommanderPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_commander);
            setHasOptionsMenu(true);

            initCmdrPreferences(null);

            // Init status source list
            ListPreference statusListPreference = (ListPreference) findPreference(getString(R.string.settings_cmdr_source));
            if (statusListPreference != null) {
                statusListPreference.setEntries(PlayerNetworkUtils.getSourcesList());
                statusListPreference.setEntryValues(PlayerNetworkUtils.getSourcesList());
                statusListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        initCmdrPreferences((String) newValue);
                        return true;
                    }
                });
            }
        }

        private void initCmdrPreferences(String newValue) {
            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_cmdr_username)));

            // If invoked with a specific value get network for it else get current one
            PlayerNetwork playerNetwork = newValue != null ?
                    PlayerNetworkUtils.getCurrentPlayerNetwork(getActivity(), newValue) :
                    PlayerNetworkUtils.getCurrentPlayerNetwork(getActivity());

            EditTextPreference passwordPreference = (EditTextPreference) findPreference(getString(R.string.settings_cmdr_password));
            EditTextPreference usernamePreference = (EditTextPreference) findPreference(getString(R.string.settings_cmdr_username));

            if (passwordPreference != null) {
                passwordPreference.setVisible(playerNetwork.needPassword());
                playerNetwork.passwordSettingSetup(passwordPreference);

                passwordPreference.setText(SettingsUtils.getString(getActivity(), getActivity().getString(R.string.settings_cmdr_password)));
            }
            if (usernamePreference != null) {
                playerNetwork.usernameSettingSetup(usernamePreference);
            }
        }
    }
}
