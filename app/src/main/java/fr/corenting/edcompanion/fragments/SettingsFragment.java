package fr.corenting.edcompanion.fragments;

import android.content.Context;
import android.os.Bundle;

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

public class SettingsFragment extends PreferenceFragmentCompat {
    private static Preference.OnPreferenceChangeListener preferenceChangeListener =
            new Preference.OnPreferenceChangeListener() {
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

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_settings);

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
        initPushPreferences();
        initCmdrPreferences(null);

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

    private void initPushPreferences() {

        // Remove push notifications subscreen if Google Play Services are not available
        if (NotificationsUtils.pushNotificationsNotWorking(getActivity())) {
            // TODO : disable notifications catefories
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
