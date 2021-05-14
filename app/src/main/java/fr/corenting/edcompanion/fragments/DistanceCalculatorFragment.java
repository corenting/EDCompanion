package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.databinding.FragmentDistanceCalculatorBinding;
import fr.corenting.edcompanion.models.events.DistanceSearch;
import fr.corenting.edcompanion.network.DistanceCalculatorNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.ViewUtils;
import fr.corenting.edcompanion.views.SystemInputView;

public class DistanceCalculatorFragment extends Fragment {

    public static final String DISTANCE_CALCULATOR_FRAGMENT_TAG = "distance_calculator_fragment";

    private FragmentDistanceCalculatorBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDistanceCalculatorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Autocomplete setup
        setAutocompleteListeners(binding.firstSystemInputView);
        setAutocompleteListeners(binding.secondSystemInputView);

        // Button event
        binding.findButton.setOnClickListener(this::onFindClick);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDistanceEvent(DistanceSearch distanceSearch) {
        binding.progressBar.setVisibility(View.GONE);

        // Error
        if (!distanceSearch.getSuccess()) {
            NotificationsUtils.displayGenericDownloadErrorSnackbar(getActivity());
            return;
        }

        binding.resultCardView.setVisibility(View.VISIBLE);

        // Display warning if permits are required
        if (distanceSearch.getStartPermitRequired() && distanceSearch.getEndPermitRequired()) {
            binding.warningTextView.setVisibility(View.VISIBLE);
            binding.warningTextView.setText(getContext().getString(R.string.permit_required_both,
                    distanceSearch.getStartSystemName(), distanceSearch.getEndSystemName()));
        } else if (distanceSearch.getStartPermitRequired()) {
            binding.warningTextView.setVisibility(View.VISIBLE);
            binding.warningTextView.setText(getContext().getString(R.string.permit_required,
                    distanceSearch.getStartSystemName()));
        } else if (distanceSearch.getEndPermitRequired()) {
            binding.warningTextView.setVisibility(View.VISIBLE);
            binding.warningTextView.setText(getContext().getString(R.string.permit_required,
                    distanceSearch.getEndSystemName()));
        }

        binding.resultTextView.setText(getContext().getString(R.string.distance_result,
                distanceSearch.getDistance()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onFindClick(View view) {
        ViewUtils.hideSoftKeyboard(view.getRootView());
        binding.resultCardView.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        DistanceCalculatorNetwork.getDistance(
                getContext(),
                binding.firstSystemInputView.getText().toString(),
                binding.secondSystemInputView.getText().toString()
        );
    }

    private void setAutocompleteListeners(final SystemInputView editText) {
        editText.setOnSubmit(() -> onFindClick(editText));
    }
}
