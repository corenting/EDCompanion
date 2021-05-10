package fr.corenting.edcompanion.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.databinding.ViewRowBinding
import fr.corenting.edcompanion.utils.MiscUtils

class RowView : RelativeLayout {

    private var binding: ViewRowBinding = ViewRowBinding.inflate(LayoutInflater.from(context), this)

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
        setListeners(binding.firstCellTextView)
        setListeners(binding.secondCellTextView)
        setListeners(binding.thirdCellTextView)
    }

    private fun setListeners(textView: TextView) {
        textView.setOnLongClickListener(null)
        textView.setOnLongClickListener {
            MiscUtils.putTextInClipboard(
                context, "",
                textView.text.toString(), true
            )
            true
        }
    }

    fun setRowContent(
        firstCell: String, secondCell: String, thirdCell: String,
        isHeader: Boolean
    ) {
        if (isHeader) {
            binding.firstCellTextView.setTypeface(null, Typeface.BOLD)
            binding.secondCellTextView.setTypeface(null, Typeface.BOLD)
            binding.thirdCellTextView.setTypeface(null, Typeface.BOLD)
        }
        binding.firstCellTextView.text = firstCell
        binding.secondCellTextView.text = secondCell
        binding.thirdCellTextView.text = thirdCell
    }
}