package fr.corenting.edcompanion.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.*
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.activities.LoginActivity
import fr.corenting.edcompanion.utils.NotificationsUtils

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_settings)

        initPushPreferences()
        initCmdrPreferences()
    }

    private fun initCmdrPreferences() {
        // Bind help preference
        val helpPreference = findPreference<Preference>(getString(R.string.settings_cmdr_help))
        if (helpPreference != null) {
            helpPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.data = Uri.parse(getString(R.string.commander_help_url))
                startActivity(browserIntent)
                true
            }
        }

        // Bind preferences summary to values
        bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_cmdr_edsm_username)))

        val frontierPreference =
            findPreference<Preference>(getString(R.string.settings_cmdr_frontier_oauth))
        frontierPreference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val i = Intent(context, LoginActivity::class.java)
            activity?.startActivityForResult(i, FRONTIER_LOGIN_REQUEST_CODE)
            true
        }
    }

    private fun initPushPreferences() { // Get preferences
        val newGoalPreference =
            findPreference<Preference>(getString(R.string.settings_notifications_new_goal))
        val newTierPreference =
            findPreference<Preference>(getString(R.string.settings_notifications_new_tier))
        val finishedGoalPreference =
            findPreference<Preference>(getString(R.string.settings_notifications_finished_goal))

        // Disable push notifications settings if Google Play Services are not available
        if (NotificationsUtils.pushNotificationsNotWorking(context)) {
            disablePushPreference(newGoalPreference)
            disablePushPreference(newTierPreference)
            disablePushPreference(finishedGoalPreference)
        }

        // Change Firebase subscriptions on preference change
        val context = context
        val notificationsChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference, newValue: Any? ->
                NotificationsUtils.refreshPushSubscription(
                    context,
                    preference.key,
                    (newValue as Boolean?)!!
                )
                true
            }
        if (newGoalPreference != null) {
            newGoalPreference.onPreferenceChangeListener = notificationsChangeListener
        }
        if (newTierPreference != null) {
            newTierPreference.onPreferenceChangeListener = notificationsChangeListener
        }
        if (finishedGoalPreference != null) {
            finishedGoalPreference.onPreferenceChangeListener = notificationsChangeListener
        }
    }

    private fun disablePushPreference(preference: Preference?) {
        preference?.isEnabled = false
        preference?.summary = getString(R.string.settings_notifications_error_summary)
    }

    companion object {
        private const val FRONTIER_LOGIN_REQUEST_CODE = 999

        private val preferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference, value: Any ->
                val stringValue = value.toString()
                when (preference) {
                    // For list preferences, look up the correct display value in the preference's 'entries' list.
                    is ListPreference -> {
                        val index = preference.findIndexOfValue(stringValue)

                        // Set the summary to reflect the new value.
                        preference.setSummary(if (index >= 0) preference.entries[index] else preference.summary)
                    }
                    // For all other preferences, set the summary to the value's simple string representation.
                    else -> {
                        preference.summary = stringValue
                    }
                }
                true
            }

        private fun bindPreferenceSummaryToValue(preference: Preference?) { // Set the listener to watch for value changes.
            preference?.onPreferenceChangeListener = preferenceChangeListener

            // Trigger the listener immediately with the preference's current value.
            preferenceChangeListener.onPreferenceChange(
                preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference?.context)
                    .getString(preference?.key, "")
            )
        }
    }
}