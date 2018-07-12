package fr.corenting.edcompanion.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;

public class CommunityGoalRewardAdapter extends AbstractTableAdapter<String, String, String> {

    public CommunityGoalRewardAdapter(Context context) {
        super(context);
    }

    class CellViewHolder extends AbstractViewHolder {

        @BindView(R.id.cellTextView)
        TextView cellTextView;

        public CellViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        return onCreateCellCommon(parent, viewType);
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        return onCreateCellCommon(parent, viewType);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        return onCreateCellCommon(parent, viewType);
    }

    private AbstractViewHolder onCreateCellCommon(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.community_goal_reward_cell,
                parent, false);
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int
            columnPosition, int rowPosition) {
        String cellContent = (String) cellItemModel;
        CellViewHolder viewHolder = (CellViewHolder) holder;
        viewHolder.cellTextView.setText(cellContent);
    }


    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int
            position) {
        String cellContent = (String) columnHeaderItemModel;

        // Get the holder to update cell item text
        CellViewHolder columnHeaderViewHolder = (CellViewHolder) holder;
        columnHeaderViewHolder.cellTextView.setText(cellContent);
        columnHeaderViewHolder.cellTextView.setTypeface(
                columnHeaderViewHolder.cellTextView.getTypeface(),
                Typeface.BOLD);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int
            position) {
        String cellContent = (String) rowHeaderItemModel;

        // Get the holder to update row header item text
        CellViewHolder rowHeaderViewHolder = (CellViewHolder) holder;
        rowHeaderViewHolder.cellTextView.setText(cellContent);
    }

    @Override
    public View onCreateCornerView() {
        View v = new View(mContext);
        return v;
    }

    @Override
    public int getColumnHeaderItemViewType(int columnPosition) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int rowPosition) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int columnPosition) {
        return 0;
    }
}