package fr.corenting.edcompanion.views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import fr.corenting.edcompanion.R;

// Code from https://gist.github.com/rodrigohenriques/77398a81b5d01ac71c3b
// By rodrigohenriques

public class ClickToSelectEditText extends AppCompatEditText {

    List<String> mItems;
    String[] mListableItems;
    CharSequence mHint;
    int selectedIndex;

    OnItemSelectedListener<String> onItemSelectedListener;

    public ClickToSelectEditText(Context context) {
        super(context);

        mHint = getHint();
    }

    public ClickToSelectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHint = getHint();
    }

    public ClickToSelectEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHint = getHint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setFocusable(false);
        setClickable(true);
        setInputType(InputType.TYPE_NULL);
    }

    public void setItems(List<String> items) {
        this.mItems = items;
        this.mListableItems = new String[items.size()];
        int i = 0;

        for (String item : mItems) {
            mListableItems[i++] = item;
        }

        // Default item
        setText(mListableItems[0]);
        selectedIndex = 0;

        configureOnClickListener();
    }

    public List<String> getItems()
    {
        return mItems;
    }

    private void configureOnClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(mHint);
                builder.setSingleChoiceItems(mListableItems, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setText(mListableItems[i]);
                        selectedIndex = i;
                        if (onItemSelectedListener != null) {
                            onItemSelectedListener.onItemSelectedListener(mItems.get(i), i);
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton(R.string.close, null);
                builder.create().show();
            }
        });
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<String> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelectedListener(T item, int selectedIndex);
    }
}