package fr.corenting.edcompanion.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.activities.LoginActivity
import fr.corenting.edcompanion.utils.NotificationsUtils

class SettingsFragment : PreferenceFragmentCompat() {

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            onPermissionRefused()
        }
    }


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
        val pref: Preference? = findPreference(getString(R.string.settings_cmdr_edsm_username))
        if (pref != null) {
            bindPreferenceSummaryToValue(pref)
        }

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
                val newBooleanValue = (newValue as Boolean?)!!

                if (newBooleanValue) {
                    askNotificationPermission(preference as SwitchPreference)
                    createNotificationChannelsIfNeeded()
                }

                NotificationsUtils.refreshPushSubscription(
                    context,
                    preference.key,
                    newBooleanValue
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

    private fun createNotificationChannelsIfNeeded() {
        NotificationsUtils.createNotificationsChannelsIfNeeded(requireContext())
    }

    private fun askNotificationPermission(preference: SwitchPreference) {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showRationaleForPushPermission(preference)
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showRationaleForPushPermission(preference: SwitchPreference) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.permission_needed_for_push_notifications_title)
            .setMessage(requireContext().getString(R.string.permission_needed_for_push_notifications_message))
            .setPositiveButton(android.R.string.ok) { dialogInterface, _ ->
                dialogInterface.dismiss()
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton(R.string.no_thanks) { dialogInterface, _ ->
                dialogInterface.dismiss()
                preference.isChecked = false
            }
            .create()
        dialog.show()
    }

    private fun onPermissionRefused() {
        // First disable all the permissions
        (findPreference<Preference>(getString(R.string.settings_notifications_new_goal)) as SwitchPreference).isChecked =
            false
        (findPreference<Preference>(getString(R.string.settings_notifications_new_tier)) as SwitchPreference).isChecked =
            false
        (findPreference<Preference>(getString(R.string.settings_notifications_finished_goal)) as SwitchPreference).isChecked =
            false

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.permission_refused_for_push_notifications_title)
            .setMessage(requireContext().getString(R.string.permission_refused_for_push_notifications_message))
            .setPositiveButton(android.R.string.ok) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
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

        private fun bindPreferenceSummaryToValue(preference: Preference) { // Set the listener to watch for value changes.
            preference?.onPreferenceChangeListener = preferenceChangeListener

            // Trigger the listener immediately with the preference's current value.
            preferenceChangeListener.onPreferenceChange(
                preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference.context)
                    .getString(preference.key, "")
            )
        }
    }
}