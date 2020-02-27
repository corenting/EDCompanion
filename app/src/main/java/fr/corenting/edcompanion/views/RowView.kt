package fr.corenting.edcompanion.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.ButterKnife
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.utils.MiscUtils
import kotlinx.android.synthetic.main.view_row.view.*

class RowView : RelativeLayout {
    constructor(context: Context?) :
            super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) :
            super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.view_row, this)
        ButterKnife.bind(this)
        setListeners(firstCellTextView)
        setListeners(secondCellTextView)
        setListeners(thirdCellTextView)
    }

    private fun setListeners(textView: TextView) {
        textView.setOnLongClickListener(null)
        textView.setOnLongClickListener {
            MiscUtils.putTextInClipboard(context, "",
                    textView.text.toString(), true)
            true
        }
    }

    fun setRowContent(firstCell: String, secondCell: String, thirdCell: String,
                      isHeader: Boolean) {
        if (isHeader) {
            firstCellTextView.setTypeface(null, Typeface.BOLD)
            secondCellTextView.setTypeface(null, Typeface.BOLD)
            thirdCellTextView.setTypeface(null, Typeface.BOLD)
        }
        firstCellTextView.text = firstCell
        secondCellTextView.text = secondCell
        thirdCellTextView.text = thirdCell
    }
}