package fr.corenting.edcompanion.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommunityGoalReward;

public class TableView extends RelativeLayout {

    @BindView(R.id.headerRow)
    public RowView headerRow;
    @BindView(R.id.firstRow)
    public RowView firstRow;
    @BindView(R.id.secondRow)
    public RowView secondRow;
    @BindView(R.id.thirdRow)
    public RowView thirdRow;
    @BindView(R.id.fourthRow)
    public RowView fourthRow;
    @BindView(R.id.fifthRow)
    public RowView fifthRow;
    @BindView(R.id.sixthRow)
    public RowView sixthRow;

    public TableView(Context context) {
        super(context);
        init();
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_table, this);
        ButterKnife.bind(this);
    }

    public void setHeaders(String first, String second, String third) {
        headerRow.setRowContent(first, second, third, true);
    }

    public void setContent(List<CommunityGoalReward> content) {
        for (int i = 0; i < content.size(); i++) {
            CommunityGoalReward reward = content.get(i);
            RowView rowView;
            switch(i){
                case 0:
                    rowView = firstRow;
                    break;
                case 1:
                    rowView = secondRow;
                    break;
                case 2:
                    rowView = thirdRow;
                    break;
                case 3:
                    rowView = fourthRow;
                    break;
                case 4:
                    rowView = fifthRow;
                    break;
                case 5:
                    rowView = sixthRow;
                    break;
                default:
                    continue;
            }
            rowView.setRowContent(reward.getTier(), reward.getContributors(), reward.getRewards(),
                    false);
        }
    }
}
