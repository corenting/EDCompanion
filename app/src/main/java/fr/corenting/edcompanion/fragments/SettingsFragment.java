package fr.corenting.edcompanion.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.XpPreferenceFragment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import net.xpece.android.support.preference.ListPreference;
import net.xpece.android.support.preference.MultiSelectListPreference;
import net.xpece.android.support.preference.PreferenceDividerDecoration;
import net.xpece.android.support.preference.PreferenceIconHelper;
import net.xpece.android.support.preference.PreferenceScreenNavigationStrategy;
import net.xpece.android.support.preference.SharedPreferencesCompat;

import java.util.HashSet;
import java.util.Set;

import fr.corenting.edcompanion.BuildConfig;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.ViewUtils;

public class SettingsFragment extends XpPreferenceFragment {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
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
            } else if (preference instanceof MultiSelectListPreference) {
                String summary = stringValue.trim().substring(1, stringValue.length() - 1); // strip []
                preference.setSummary(summary);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    public static SettingsFragment newInstance(String rootKey) {
        Bundle args = new Bundle();
        args.putString(SettingsFragment.ARG_PREFERENCE_ROOT, rootKey);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String[] getCustomDefaultPackages() {
        return new String[]{BuildConfig.APPLICATION_ID};
    }

    @Override
    public void onCreatePreferences2(final Bundle savedInstanceState, final String rootKey) {
        // Set an empty screen so getPreferenceScreen doesn't return null -
        // so we can create fake headers from the get-go.
        setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getPreferenceManager().getContext()));

        // Add preferences from fill
        addPreferencesFromResource(R.xml.settings);

        //Tint icons
        tintSubscreenIcon("player_subscreen", R.drawable.ic_person_black_24dp);
        tintSubscreenIcon(getString(R.string.settings_notifications_subscreen), R.drawable.settings_notifications);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_edsm_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_cmdr)));

        getPreferenceScreen().setTitle(getActivity().getTitle());

        // Setup root preference.
        // Use with ReplaceFragment strategy.
        PreferenceScreenNavigationStrategy.ReplaceFragment.onCreatePreferences(this, rootKey);

        // Remove push notifications subscreen if Google Play Services are not available
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext()) != ConnectionResult.SUCCESS)
        {
            Preference notificationsScreen = findPreference(getString(R.string.settings_notifications_subscreen));
            notificationsScreen.setVisible(false);
        }

        // Change Firebase subscriptions on preference change
        final Context context = getContext();
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

    private void tintSubscreenIcon(String key, int drawableId)
    {
        Preference playerSubScreen = findPreference(key);
        PreferenceIconHelper.setup(playerSubScreen,drawableId,
                ViewUtils.resolveResourceId(playerSubScreen.getContext(),
                        R.attr.asp_preferenceIconTint, R.color.colorAccent), true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Change activity title to preference title. Used with ReplaceFragment strategy.
        getActivity().setTitle(getPreferenceScreen().getTitle());
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
        final String key = preference.getKey();
        if (preference instanceof MultiSelectListPreference) {
            Set<String> summary = SharedPreferencesCompat.getStringSet(
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()),
                    key,
                    new HashSet<String>());
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, summary);
        } else {
            String value = PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(key, "");
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, value);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView listView = getListView();

        final int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        listView.setPadding(0, padding, 0, padding);

        // We're using alternative divider.
        getListView().addItemDecoration(new PreferenceDividerDecoration(getContext()).drawBottom(false));
        setDivider(null);

        // We don't want this. The children are still focusable.
        listView.setFocusable(false);
    }
}
