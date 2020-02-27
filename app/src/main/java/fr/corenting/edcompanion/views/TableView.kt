package fr.corenting.edcompanion.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.models.CommunityGoalReward
import kotlinx.android.synthetic.main.view_table.view.*

class TableView : RelativeLayout {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.view_table, this)
    }

    fun setHeaders(first: String, second: String, third: String) {
        headerRow.setRowContent(first, second, third, true)
    }

    fun setContent(content: List<CommunityGoalReward>) {
        loop@ for (i in content.indices) {
            val (contributors, rewards, tier) = content[i]
            val rowView: RowView = when (i) {
                0 -> firstRow
                1 -> secondRow
                2 -> thirdRow
                3 -> fourthRow
                4 -> fifthRow
                5 -> sixthRow
                else -> continue@loop
            }
            rowView.setRowContent(tier, contributors, rewards,
                    false)
        }
    }
}