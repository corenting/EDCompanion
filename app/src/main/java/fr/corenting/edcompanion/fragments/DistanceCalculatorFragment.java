package fr.corenting.edcompanion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.adapters.AutoCompleteAdapter;
import fr.corenting.edcompanion.models.events.DistanceSearch;
import fr.corenting.edcompanion.network.DistanceCalculatorNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.utils.ViewUtils;
import fr.corenting.edcompanion.views.DelayAutoCompleteTextView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class DistanceCalculatorFragment extends Fragment {

    public static final String DISTANCE_CALCULATOR_FRAGMENT_TAG = "distance_calculator_fragment";


    @BindView(R.id.firstSystemInputEditText)
    public DelayAutoCompleteTextView firstSystemEditText;
    @BindView(R.id.secondSystemInputEditText)
    public DelayAutoCompleteTextView secondSystemEditText;
    @BindView(R.id.firstSystemProgressBar)
    public MaterialProgressBar firstSystemProgressBar;
    @BindView(R.id.secondSystemProgressBar)
    public MaterialProgressBar secondSystemProgressBar;
    @BindView(R.id.findButton)
    public Button findButton;
    @BindView(R.id.resultCardView)
    public CardView resultCardView;
    @BindView(R.id.progressBar)
    public MaterialProgressBar progressBar;
    @BindView(R.id.resultTextView)
    public TextView resultTextView;
    @BindView(R.id.warningTextView)
    public TextView warningTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_distance_calculator, container, false);
        ButterKnife.bind(this, v);

        // Autocomplete setup
        setAutoComplete(firstSystemEditText);
        setAutoComplete(secondSystemEditText);

        // Set loading indicators
        firstSystemEditText.setLoadingIndicator(firstSystemProgressBar);
        secondSystemEditText.setLoadingIndicator(secondSystemProgressBar);

        return v;
    }

    @Subscribe
    public void onDistanceEvent(DistanceSearch distanceSearch) {
        progressBar.setVisibility(View.GONE);

        // Error
        if (!distanceSearch.getSuccess()) {
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
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

        DistanceCalculatorNetwork.getDistance(getContext(), firstSystemEditText.getText().toString(), secondSystemEditText.getText().toString());
    }

    private void setAutoComplete(final DelayAutoCompleteTextView editText) {
        // System input
        editText.setThreshold(3);
        editText.setAdapter(new AutoCompleteAdapter(getContext(), AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SYSTEMS));

        // Listeners
        Runnable onSubmit = new Runnable() {
            @Override
            public void run() {
                onFindClick(editText);
            }
        };
        editText.setOnSubmit(onSubmit);
        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                editText.setText((String) adapterView.getItemAtPosition(position));
            }
        });
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
