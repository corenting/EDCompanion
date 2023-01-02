package fr.corenting.edcompanion.views

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

// Code from http://makovkastar.github.io/blog/2014/04/12/android-autocompletetextview-with-suggestions-from-a-web-service/
class DelayAutoCompleteTextView(context: Context, attrs: AttributeSet) :
    AppCompatAutoCompleteTextView(context, attrs) {

    private val mHandler: Handler = AutoCompleteHandler(this)

    init {
        this.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        this.imeOptions = EditorInfo.IME_ACTION_DONE
    }

    private fun performFiltering(msg: Message) {
        super.performFiltering(msg.obj as CharSequence, msg.arg1)
    }

    fun setOnSubmit(onSubmit: Runnable) {
        setOnEditorActionListener { _: TextView?, i: Int, _: KeyEvent? ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                onSubmit.run()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun performFiltering(text: CharSequence, keyCode: Int) {
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED)
        mHandler.sendMessageDelayed(
            mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text),
            DEFAULT_AUTOCOMPLETE_DELAY.toLong()
        )
    }

    private class AutoCompleteHandler(private val view: DelayAutoCompleteTextView) :
        Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            view.performFiltering(msg)
        }

    }

    companion object {
        private const val MESSAGE_TEXT_CHANGED = 100
        private const val DEFAULT_AUTOCOMPLETE_DELAY = 250
    }
}