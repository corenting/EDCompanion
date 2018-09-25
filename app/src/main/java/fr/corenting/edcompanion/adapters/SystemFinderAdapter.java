package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.SystemDetailsActivity;
import fr.corenting.edcompanion.models.SystemFinderResult;
import fr.corenting.edcompanion.models.events.SystemFinderSearch;
import fr.corenting.edcompanion.utils.MiscUtils;
import fr.corenting.edcompanion.utils.ViewUtils;
import fr.corenting.edcompanion.views.DelayAutoCompleteTextView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class SystemFinderAdapter extends FinderAdapter<SystemFinderAdapter.HeaderViewHolder,
        SystemFinderAdapter.ResultViewHolder, SystemFinderResult> {

    public SystemFinderAdapter(Context context) {
        super(context);
    }

    @Override
    protected RecyclerView.ViewHolder getNewHeaderViewHolder(View v) {
        return new HeaderViewHolder(v);
    }

    @Override
    protected RecyclerView.ViewHolder getResultViewHolder(View v) {
        return new ResultViewHolder(v);
    }

    @Override
    public TextView getEmptyTextView() {
        return getHeader().emptyText;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.fragment_find_system_header;
    }

    @Override
    protected int getResultResId() {
        return R.layout.list_item_system_finder_result;
    }

    @Override
    protected void bindHeaderViewHolder(final HeaderViewHolder holder) {
        // System autocomplete
        holder.systemInputEditText.setThreshold(3);
        holder.systemInputEditText.setLoadingIndicator(holder.systemProgressBar);

        holder.systemInputEditText.setAdapter(new AutoCompleteAdapter(context,
                AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SYSTEMS));
        holder.systemInputEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                holder.systemInputEditText
                        .setText((String) adapterView.getItemAtPosition(position));
            }
        });

        // Find button
        final Runnable onSubmit = new Runnable() {
            @Override
            public void run() {
                ViewUtils.hideSoftKeyboard(holder.findButton.getRootView());

                SystemFinderSearch result = new SystemFinderSearch(
                        holder.systemInputEditText.getText().toString());
                EventBus.getDefault().post(result);
            }
        };

        holder.findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit.run();
            }
        });
        holder.systemInputEditText.setOnSubmit(onSubmit);
    }

    @Override
    protected void bindResultViewHolder(ResultViewHolder holder, int position) {
        final SystemFinderResult currentResult = results.get(position - 1);

        holder.titleTextView.setText(currentResult.getName());
        holder.permitRequiredTextView.setVisibility(
                currentResult.isPermitRequired() ? View.VISIBLE : View.GONE);

        holder.allegianceTextView.setText(getContentOrUnknown(currentResult.getAllegiance()));
        holder.securityTextView.setText(getContentOrUnknown(currentResult.getSecurity()));
        holder.governmentTextView.setText(getContentOrUnknown(currentResult.getGovernment()));
        holder.economyTextView.setText(getContentOrUnknown(currentResult.getEconomy()));

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SystemDetailsActivity.class);
                i.putExtra(context.getString(R.string.system), currentResult.getName());
                MiscUtils.startIntentWithFadeAnimation(context, i);
            }
        });
    }

    private String getContentOrUnknown(String content) {
        return content == null || content.length() == 0 ?
                context.getString(R.string.unknown) : content;
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemLayout)
        RelativeLayout itemLayout;

        @BindView(R.id.titleTextView)
        TextView titleTextView;

        @BindView(R.id.permitRequiredTextView)
        TextView permitRequiredTextView;

        @BindView(R.id.allegianceTextView)
        TextView allegianceTextView;

        @BindView(R.id.securityTextView)
        TextView securityTextView;

        @BindView(R.id.governmentTextView)
        TextView governmentTextView;

        @BindView(R.id.economyTextView)
        TextView economyTextView;

        public ResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.systemInputEditText)
        DelayAutoCompleteTextView systemInputEditText;

        @BindView(R.id.systemProgressBar)
        MaterialProgressBar systemProgressBar;

        @BindView(R.id.findButton)
        Button findButton;

        @BindView(R.id.emptyText)
        TextView emptyText;

        public HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
