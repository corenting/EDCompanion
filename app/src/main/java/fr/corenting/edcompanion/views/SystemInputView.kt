package fr.corenting.edcompanion.views

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import fr.corenting.edcompanion.adapters.AutoCompleteAdapter
import fr.corenting.edcompanion.models.events.CommanderPosition
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.utils.PlayerNetworkUtils
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

            systemInputLayout.isEndIconVisible = true
            systemInputLayout.setEndIconOnClickListener {
                PlayerNetworkUtils.getCurrentPlayerNetwork(context).getCommanderPosition(bus)
            }
        } else {
            systemInputLayout.isEndIconVisible = false
        }

        // Set autocomplete view
        systemInputEditText.threshold = 3
        systemInputEditText.setAdapter(AutoCompleteAdapter(context,
                AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SYSTEMS))
        systemInputEditText.setOnItemClickListener { adapterView, _, position, _ ->
            systemInputEditText.setText(adapterView.getItemAtPosition(position) as String)
        }
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
