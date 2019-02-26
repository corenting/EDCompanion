package fr.corenting.edcompanion.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.network.player.PlayerNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.PlayerNetworkUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;
import fr.corenting.edcompanion.utils.ThemeUtils;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static Preference.OnPreferenceChangeListener preferenceChangeListener =
            (preference, value) -> {
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
            };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_settings);

        // Init status source list
        ListPreference statusListPreference = (ListPreference)
                findPreference(getString(R.string.settings_cmdr_source));
        if (statusListPreference != null) {
            statusListPreference.setEntries(PlayerNetworkUtils.getSourcesList());
            statusListPreference.setEntryValues(PlayerNetworkUtils.getSourcesList());
            statusListPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                initCmdrPreferences((String) newValue);
                return true;
            });
        }
        initPushPreferences();
        initCmdrPreferences(null);

        // Fix icons colors
        if (ThemeUtils.isDarkThemeEnabled(getActivity())) {
            fixIconColor(findPreference(getString(R.string.settings_cmdr_help)));
        }
    }

    private void fixIconColor(Preference preference) {
        if (preference != null) {
            DrawableCompat.setTint(preference.getIcon(),
                    ContextCompat.getColor(getActivity(), android.R.color.white));
        }
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(preferenceChangeListener);

        // Trigger the listener immediately with the preference's
        // current value.
        preferenceChangeListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private void initCmdrPreferences(String newValue) {
        // Bind help preference
        Preference helpPreference = findPreference(getString(R.string.settings_cmdr_help));
        if (helpPreference != null) {
            helpPreference.setOnPreferenceClickListener(preference -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(getString(R.string.commander_help_url)));
                startActivity(browserIntent);
                return true;
            });
        }

        // If invoked with a specific value get network for it else get current one
        PlayerNetwork playerNetwork = newValue != null ?
                PlayerNetworkUtils.getCurrentPlayerNetwork(getActivity(), newValue) :
                PlayerNetworkUtils.getCurrentPlayerNetwork(getActivity());

        EditTextPreference passwordPreference = (EditTextPreference) findPreference(getString(R.string.settings_cmdr_password));
        EditTextPreference usernamePreference = (EditTextPreference) findPreference(getString(R.string.settings_cmdr_username));
        Preference frontierPreference = findPreference(getString(R.string.settings_cmdr_oauth));

        // If preferences are null return immediatly
        if (passwordPreference == null || usernamePreference == null ||
                frontierPreference == null) {
            return;
        }

        if (playerNetwork.useFrontierAuth()) {
            usernamePreference.setVisible(false);
            passwordPreference.setVisible(false);
            frontierPreference.setVisible(true);
        } else {
            frontierPreference.setVisible(false);
            usernamePreference.setVisible(true);
            passwordPreference.setVisible(playerNetwork.usePassword());

            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_cmdr_username)));

            passwordPreference.setText(SettingsUtils.getString(getActivity(), getActivity()
                    .getString(R.string.settings_cmdr_password)));
            playerNetwork.passwordSettingSetup(passwordPreference);
            playerNetwork.usernameSettingSetup(usernamePreference);
        }
    }

    private void initPushPreferences() {

        // Get preferences
        Preference newGoalPreference = findPreference(getString(R.string.settings_notifications_new_goal));
        Preference newTierPreference = findPreference(getString(R.string.settings_notifications_new_tier));
        Preference finishedGoalPreference = findPreference(getString(R.string.settings_notifications_finished_goal));

        // Disable push notifications settings if Google Play Services are not available
        if (NotificationsUtils.pushNotificationsNotWorking(getActivity())) {
            disablePushPreference(newGoalPreference);
            disablePushPreference(newTierPreference);
            disablePushPreference(finishedGoalPreference);
        }

        // Change Firebase subscriptions on preference change
        final Context context = getActivity();
        Preference.OnPreferenceChangeListener notificationsChangeListener = (preference, newValue) -> {
            NotificationsUtils.refreshPushSubscription(context, preference.getKey(), (Boolean) newValue);
            return true;
        };

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

    private void disablePushPreference(Preference preference) {
        preference.setEnabled(false);
        preference.setSummary(getString(R.string.settings_notifications_error_summary));
    }
}
