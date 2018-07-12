package fr.corenting.edcompanion.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

// Code from http://makovkastar.github.io/blog/2014/04/12/android-autocompletetextview-with-suggestions-from-a-web-service/

public class DelayAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 250;

    private MaterialProgressBar mLoadingIndicator;

    private final Handler mHandler = new AutoCompleteHandler(this);

    private void performFiltering(Message msg) {
        DelayAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
    }

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setInputType(InputType.TYPE_CLASS_TEXT);
        this.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public void setLoadingIndicator(MaterialProgressBar progressBar) {
        mLoadingIndicator = progressBar;
    }

    public void setOnSubmit(final Runnable onSubmit)
    {
        this.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    onSubmit.run();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), DEFAULT_AUTOCOMPLETE_DELAY);
    }

    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }

    private static class AutoCompleteHandler extends Handler {
        private DelayAutoCompleteTextView view;

        public AutoCompleteHandler(DelayAutoCompleteTextView view)
        {
            this.view = view;
        }
        public void handleMessage(Message msg) {
            view.performFiltering(msg);
        }
    }
}
