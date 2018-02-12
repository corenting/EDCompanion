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
import fr.corenting.edcompanion.models.Distance;
import fr.corenting.edcompanion.network.DistanceCalculatorNetwork;
import fr.corenting.edcompanion.utils.NotificationsUtils;
import fr.corenting.edcompanion.views.DelayAutoCompleteTextView;

public class DistanceCalculatorFragment extends Fragment{

    public static final String DISTANCE_CALCULATOR_FRAGMENT_TAG = "distance_calculator_fragment";


    @BindView(R.id.firstSystemInputEditText)
    public DelayAutoCompleteTextView firstSystemEditText;
    @BindView(R.id.secondSystemInputEditText)
    public DelayAutoCompleteTextView secondSystemEditText;
    @BindView(R.id.findButton)
    public Button findButton;
    @BindView(R.id.resultCardView)
    public CardView resultCardView;
    @BindView(R.id.resultTextView)
    public TextView resultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_distance_calculator, container, false);
        ButterKnife.bind(this, v);

        // Autocomplete setup
        setAutoComplete(firstSystemEditText);
        setAutoComplete(secondSystemEditText);

        resultCardView.setVisibility(View.GONE);
        return v;
    }

    @Subscribe
    public void onDistanceEvent(Distance distance) {
        resultCardView.setVisibility(View.VISIBLE);

        // Error
        if (!distance.Success) {
            NotificationsUtils.displayDownloadErrorSnackbar(getActivity());
            return;
        }

        resultTextView.setText(getContext().getString(R.string.distance_result, distance.Distance));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register event
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.findButton)
    public void onFindClick(View view) {
        resultCardView.setVisibility(View.GONE);
        DistanceCalculatorNetwork.getDistance(
                getContext(),
                firstSystemEditText.getText().toString(),
                secondSystemEditText.getText().toString());
    }

    private void setAutoComplete(final DelayAutoCompleteTextView editText)
    {
        // System input
        editText.setThreshold(3);
        editText.setAdapter(new AutoCompleteAdapter(getContext(), AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SYSTEMS));
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
