package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.events.DistanceSearch;
import fr.corenting.edcompanion.network.DistanceCalculatorNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.ViewUtils;
import fr.corenting.edcompanion.views.SystemInputView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class DistanceCalculatorFragment extends Fragment {

    public static final String DISTANCE_CALCULATOR_FRAGMENT_TAG = "distance_calculator_fragment";


    @BindView(R.id.firstSystemInputView)
    public SystemInputView firstSystemInputView;
    @BindView(R.id.secondSystemInputView)
    public SystemInputView secondSystemInputView;
    @BindView(R.id.findButton)
    public Button findButton;
    @BindView(R.id.resultCardView)
    public MaterialCardView resultCardView;
    @BindView(R.id.progressBar)
    public MaterialProgressBar progressBar;
    @BindView(R.id.resultTextView)
    public TextView resultTextView;
    @BindView(R.id.warningTextView)
    public TextView warningTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_distance_calculator, container,
                false);
        ButterKnife.bind(this, v);

        // Autocomplete setup
        setAutocompleteListeners(firstSystemInputView);
        setAutocompleteListeners(secondSystemInputView);

        return v;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDistanceEvent(DistanceSearch distanceSearch) {
        progressBar.setVisibility(View.GONE);

        // Error
        if (!distanceSearch.getSuccess()) {
            NotificationsUtils.displayGenericDownloadErrorSnackbar(getActivity());
            return;
        }

        resultCardView.setVisibility(View.VISIBLE);

        // Display warning if permits are required
        if (distanceSearch.getStartPermitRequired() && distanceSearch.getEndPermitRequired()) {
            warningTextView.setVisibility(View.VISIBLE);
            warningTextView.setText(getContext().getString(R.string.permit_required_both,
                    distanceSearch.getStartSystemName(), distanceSearch.getEndSystemName()));
        } else if (distanceSearch.getStartPermitRequired()) {
            warningTextView.setVisibility(View.VISIBLE);
            warningTextView.setText(getContext().getString(R.string.permit_required,
                    distanceSearch.getStartSystemName()));
        } else if (distanceSearch.getEndPermitRequired()) {
            warningTextView.setVisibility(View.VISIBLE);
            warningTextView.setText(getContext().getString(R.string.permit_required,
                    distanceSearch.getEndSystemName()));
        }

        resultTextView.setText(getContext().getString(R.string.distance_result,
                distanceSearch.getDistance()));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.findButton)
    public void onFindClick(View view) {
        ViewUtils.hideSoftKeyboard(view.getRootView());
        resultCardView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        DistanceCalculatorNetwork.getDistance(getContext(),
                firstSystemInputView.getText().toString(),
                secondSystemInputView.getText().toString());
    }

    private void setAutocompleteListeners(final SystemInputView editText) {
        editText.setOnSubmit(() -> onFindClick(editText));
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
}
