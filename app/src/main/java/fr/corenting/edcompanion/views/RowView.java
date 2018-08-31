package fr.corenting.edcompanion.views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.utils.MiscUtils;

public class RowView extends RelativeLayout {

    @BindView(R.id.firstCellTextView)
    public TextView firstCellTextView;
    @BindView(R.id.secondCellTextView)
    public TextView secondCellTextView;
    @BindView(R.id.thirdCellTextView)
    public TextView thirdCellTextView;

    public RowView(Context context) {
        super(context);
        init();
    }

    public RowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.row_view, this);
        ButterKnife.bind(this);

        setListeners(firstCellTextView);
        setListeners(secondCellTextView);
        setListeners(thirdCellTextView);
    }

    private void setListeners(final TextView textView) {
        textView.setOnLongClickListener(null);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MiscUtils.putTextInClipboard(getContext(), "",
                        textView.getText().toString(), true);
                return true;
            }
        });
    }

    public void setRowContent(String firstCell, String secondCell, String thirdCell,
                               boolean isHeader) {
        if (isHeader) {
            firstCellTextView.setTypeface(null, Typeface.BOLD);
            secondCellTextView.setTypeface(null, Typeface.BOLD);
            thirdCellTextView.setTypeface(null, Typeface.BOLD);
        }

        firstCellTextView.setText(firstCell);
        secondCellTextView.setText(secondCell);
        thirdCellTextView.setText(thirdCell);
    }
}
