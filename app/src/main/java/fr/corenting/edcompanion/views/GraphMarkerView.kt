package fr.corenting.edcompanion.views

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.databinding.ViewMarkerBinding
import fr.corenting.edcompanion.models.FactionChartEntryData
import fr.corenting.edcompanion.utils.DateUtils
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.Locale


class GraphMarkerView(context: Context?) : MarkerView(context, R.layout.view_marker) {

    private var binding: ViewMarkerBinding =
        ViewMarkerBinding.inflate(LayoutInflater.from(context), this, true)

    private val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    private val userLocale: Locale = DateUtils.getCurrentLocale(context)

    override fun refreshContent(e: Entry, highlight: Highlight) {
        val (name, state, updateDate, influence) = e.data as FactionChartEntryData
        binding.factionNameTextView.text = name
        binding.influenceTextView.text = String.format(
            userLocale,
            "%.2f %%", influence * 100
        )
        binding.stateTextView.text = state
        val date = LocalDateTime.ofInstant(updateDate, ZoneOffset.UTC)
        binding.updateDateTextView.text = dateFormatter.format(date)
        super.refreshContent(e, highlight)
    }

    fun setChartView(chart: LineChart) {
        chartView = chart
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        super.draw(canvas, posX, posY)
        getOffsetForDrawingAtPoint(posX, posY)
    }
}