package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.SystemDetailsActivity;
import fr.corenting.edcompanion.models.System;
import fr.corenting.edcompanion.models.events.SystemDetails;
import fr.corenting.edcompanion.utils.SettingsUtils;

public class SystemFactionsFragment extends Fragment {

    public static final String SYSTEM_FACTIONS_FRAGMENT = "system_factions_fragment";

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.systemNameTextView)
    public TextView systemNameTextView;
    @BindView(R.id.logoImageView)
    public ImageView logoImageView;
    @BindView(R.id.permitRequiredTextView)
    public TextView permitRequiredTextView;
    @BindView(R.id.coordsTextView)
    public TextView coordinatesTextView;
    @BindView(R.id.allegianceTextView)
    public TextView allegianceTextView;
    @BindView(R.id.powerTextView)
    public TextView powerTextView;
    @BindView(R.id.securityTextView)
    public TextView securityTextView;
    @BindView(R.id.governmentTextView)
    public TextView governmentTextView;
    @BindView(R.id.controllingFactionTextView)
    public TextView controllingFactionTextView;
    @BindView(R.id.economyTextView)
    public TextView economyTextView;
    @BindView(R.id.stateTextView)
    public TextView stateTextView;
    @BindView(R.id.populationTextView)
    public TextView populationTextView;

    private NumberFormat numberFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system_details, container, false);
        ButterKnife.bind(this, v);

        //Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (getActivity() != null) {
                    ((SystemDetailsActivity) getActivity()).getData();
                }
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);

        // Setup views
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Init number format
        Locale currentLocale = SettingsUtils.getUserLocale(getContext());
        numberFormat = NumberFormat.getIntegerInstance(currentLocale);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Register event and get the news
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onSystemEvent(SystemDetails systemDetails) {
        endLoading();

        if (systemDetails.getSuccess() && systemDetails.getSystem() != null) {
            bindInformations(systemDetails.getSystem());
        }
    }

    private void bindInformations(System system) {

        // Text
        systemNameTextView.setText(system.getName());
        permitRequiredTextView.setVisibility(system.isPermitRequired() ? View.VISIBLE : View.GONE);
        coordinatesTextView.setText(getString(R.string.coordinates_num, system.getX(),
                system.getY(), system.getZ()));
        allegianceTextView.setText(system.getAllegiance());
        powerTextView.setText(String.format("%s (%s)", system.getPower(), system.getPowerState()));
        securityTextView.setText(system.getSecurity());
        governmentTextView.setText(system.getGovernment());
        controllingFactionTextView.setText(system.getControllingFaction());
        economyTextView.setText(system.getPrimaryEconomy());
        stateTextView.setText(system.getState());
        populationTextView.setText(numberFormat.format(system.getPopulation()));

        // Logo
        if (system.getAllegiance().equals("Federation")) {
            logoImageView.setImageResource(R.drawable.elite_federation);
        }
        else if (system.getAllegiance().equals("Empire")) {
            logoImageView.setImageResource(R.drawable.elite_empire);
        }
        else if (system.getAllegiance().equals("Alliance")) {
            logoImageView.setImageResource(R.drawable.elite_alliance);
        }
    }

    public void endLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
