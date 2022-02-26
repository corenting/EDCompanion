package fr.corenting.edcompanion.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.SystemDetailsActivity;
import fr.corenting.edcompanion.databinding.FragmentSystemDetailsBinding;
import fr.corenting.edcompanion.models.System;
import fr.corenting.edcompanion.models.events.SystemDetails;
import fr.corenting.edcompanion.utils.MathUtils;
import fr.corenting.edcompanion.utils.ThemeUtils;

public class SystemDetailsFragment extends Fragment {

    public static final String SYSTEM_DETAILS_FRAGMENT = "system_details_fragment";

    private FragmentSystemDetailsBinding binding;
    private NumberFormat numberFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSystemDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //Swipe to refresh setup
        SwipeRefreshLayout.OnRefreshListener listener = () -> {
            binding.swipeContainer.setRefreshing(true);
            if (getActivity() != null) {
                ((SystemDetailsActivity) getActivity()).getData();
            }
        };
        binding.swipeContainer.setOnRefreshListener(listener);

        // Init number format
        numberFormat = MathUtils.getNumberFormat(getContext());

        // Setup views
        binding.swipeContainer.setVisibility(View.VISIBLE);
        binding.swipeContainer.setRefreshing(true);

        // Fix icon on black theme
        if (ThemeUtils.isDarkThemeEnabled(getContext())) {
            ImageViewCompat.setImageTintList(binding.logoImageView, ColorStateList.valueOf(
                    ContextCompat.getColor(getContext(), android.R.color.white)));
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSystemEvent(SystemDetails systemDetails) {
        endLoading();

        if (systemDetails.getSuccess() && systemDetails.getSystem() != null) {
            bindInformations(systemDetails.getSystem());
        }
    }

    private void bindInformations(System system) {

        // Text
        binding.systemNameTextView.setText(system.getName());
        binding.permitRequiredTextView.setVisibility(system.isPermitRequired() ? View.VISIBLE : View.GONE);
        binding.coordsTextView.setText(getString(R.string.coordinates_num, system.getX(),
                system.getY(), system.getZ()));
        binding.allegianceTextView.setText(system.getAllegiance() != null ? system.getAllegiance() : getString(R.string.unknown));
        binding.powerTextView.setText(system.getPower() != null ? String.format("%s (%s)", system.getPower(), system.getPowerState()): getString(R.string.unknown));
        binding.securityTextView.setText(system.getSecurity() != null ? system.getSecurity() : getString(R.string.unknown));
        binding.governmentTextView.setText(system.getGovernment() != null ? system.getGovernment() : getString(R.string.unknown));
        binding.controllingFactionTextView.setText(system.getControllingFaction() != null ? system.getControllingFaction() : getString(R.string.unknown));
        binding.economyTextView.setText(system.getPrimaryEconomy() != null ? system.getPrimaryEconomy() : getString(R.string.unknown));
        binding.stateTextView.setText(system.getState() != null ? system.getState() : getString(R.string.unknown));
        binding.populationTextView.setText(system.getPopulation() != null ? numberFormat.format(system.getPopulation()) : getString(R.string.unknown));

        // Logo
        if (system.getAllegiance() != null) {
            switch (system.getAllegiance()) {
                case "Federation":
                    binding.logoImageView.setImageResource(R.drawable.elite_federation);
                    break;
                case "Empire":
                    binding.logoImageView.setImageResource(R.drawable.elite_empire);
                    break;
                case "Alliance":
                    binding.logoImageView.setImageResource(R.drawable.elite_alliance);
                    break;
            }
        }
    }

    public void endLoading() {
        binding.swipeContainer.setRefreshing(false);
    }
}
