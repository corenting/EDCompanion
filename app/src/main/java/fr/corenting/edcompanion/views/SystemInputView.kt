package fr.corenting.edcompanion.views

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import fr.corenting.edcompanion.adapters.AutoCompleteAdapter
import fr.corenting.edcompanion.models.events.CommanderPosition
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.utils.PlayerNetworkUtils
import fr.corenting.edcompanion.utils.ThemeUtils
import kotlinx.android.synthetic.main.view_system_input.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class SystemInputView : RelativeLayout {

    private var bus: EventBus = EventBus.builder().build()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) :
            super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {

        View.inflate(context, fr.corenting.edcompanion.R.layout.view_system_input, this)

        // Theme button according to theme
        if (ThemeUtils.isDarkThemeEnabled(context)) {
            ImageViewCompat.setImageTintList(systemMyLocationButton, ColorStateList.valueOf(
                    ContextCompat.getColor(context!!, android.R.color.white)))
        }

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs,
                    fr.corenting.edcompanion.R.styleable.SystemInputView, 0, 0)

            try {
                // Set hint from attributes
                val hintRef = a.getResourceId(
                        fr.corenting.edcompanion.R.styleable.SystemInputView_hint,
                        -1)
                systemInputEditText.setHint(hintRef)
                systemInputLayout.hint = context.getString(hintRef)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                a.recycle()
            }
        }

        // Check if position can be get from commander
        if (PlayerNetworkUtils.setupOk(context) &&
                PlayerNetworkUtils.getCurrentPlayerNetwork(context).supportLocation()) {
            systemMyLocationButton.visibility = View.VISIBLE

            systemMyLocationButton.setOnClickListener {
                if (systemInputEditText.loadingIndicator != null) {
                    systemInputEditText.loadingIndicator.visibility = View.VISIBLE
                }
                PlayerNetworkUtils.getCurrentPlayerNetwork(context).getCommanderPosition(bus)
            }

        } else {
            systemMyLocationButton.visibility = View.GONE
        }

        // Set autocomplete view
        systemInputEditText.threshold = 3
        systemInputEditText.setAdapter(AutoCompleteAdapter(context,
                AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SYSTEMS))
        systemInputEditText.setOnItemClickListener { adapterView, _, position, _ ->
            systemInputEditText.setText(adapterView.getItemAtPosition(position) as String)
        }
        systemInputEditText.loadingIndicator = progressBar
    }

    fun setOnSubmit(runnable: Runnable) {
        systemInputEditText.setOnSubmit(runnable)
    }

    fun getText(): Editable {
        return systemInputEditText.text
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bus.register(this)
    }

    override fun onDetachedFromWindow() {
        bus.unregister(this)
        super.onDetachedFromWindow()
    }

    @Subscribe
    fun onCommanderPositionEvent(event: CommanderPosition) {
        if (event.success) {
            systemInputEditText.setText(event.systemName)
        } else {
            val activity = this.findViewById<View>(android.R.id.content).context as Activity
            NotificationsUtils.displaySnackbar(activity,
                    context.getString(fr.corenting.edcompanion.R.string.my_location_error))

        }
    }
}
