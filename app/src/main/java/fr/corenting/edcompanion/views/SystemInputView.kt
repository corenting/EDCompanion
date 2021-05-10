package fr.corenting.edcompanion.views

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import fr.corenting.edcompanion.R
import fr.corenting.edcompanion.adapters.AutoCompleteAdapter
import fr.corenting.edcompanion.databinding.ViewSystemInputBinding
import fr.corenting.edcompanion.models.events.CommanderPosition
import fr.corenting.edcompanion.utils.NotificationsUtils
import fr.corenting.edcompanion.utils.PlayerNetworkUtils
import fr.corenting.edcompanion.utils.SettingsUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class SystemInputView : RelativeLayout {

    private lateinit var binding: ViewSystemInputBinding

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
        binding = ViewSystemInputBinding.inflate(LayoutInflater.from(context), this)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.SystemInputView, 0, 0
            )

            try {
                // Set hint from attributes
                val hintRef = a.getResourceId(
                    R.styleable.SystemInputView_hint,
                    -1
                )
                if (hintRef != -1) {
                    binding.systemInputEditText.setHint(hintRef)
                    binding.systemInputLayout.hint = context.getString(hintRef)
                }

                // Cache system name to display on different screens
                val cacheKeyRef = a.getResourceId(
                    R.styleable.SystemInputView_cacheKey, -1
                )
                val cacheKey = context.getString(cacheKeyRef)
                setCache(cacheKey)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                a.recycle()
            }
        }

        // Check if position can be get from commander
        if (PlayerNetworkUtils.setupOk(context) &&
            PlayerNetworkUtils.getCurrentPlayerNetwork(context).supportLocation()
        ) {

            binding.systemInputLayout.isEndIconVisible = true
            binding.systemInputLayout.setEndIconOnClickListener {
                PlayerNetworkUtils.getCurrentPlayerNetwork(context).getCommanderPosition(bus)
            }
        } else {
            binding.systemInputLayout.isEndIconVisible = false
        }

        // Set autocomplete view
        binding.systemInputEditText.threshold = 3
        binding.systemInputEditText.setAdapter(
            AutoCompleteAdapter(
                context,
                AutoCompleteAdapter.TYPE_AUTOCOMPLETE_SYSTEMS
            )
        )
        binding.systemInputEditText.setOnItemClickListener { adapterView, _, position, _ ->
            binding.systemInputEditText.setText(adapterView.getItemAtPosition(position) as String)
        }
    }

    private fun setCache(cacheKey: String) {
        // Set listener
        binding.systemInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                SettingsUtils.setString(context, cacheKey, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // Fetch and set existing value if any
        val existingValue = SettingsUtils.getString(context, cacheKey)
        if (!existingValue.isNullOrEmpty()) {
            binding.systemInputEditText.setText(existingValue)
        }
    }

    fun setOnSubmit(runnable: Runnable) {
        binding.systemInputEditText.setOnSubmit(runnable)
    }

    fun getText(): Editable {
        return binding.systemInputEditText.text
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
            binding.systemInputEditText.setText(event.systemName)
        } else {
            val activity = this.findViewById<View>(android.R.id.content).context as Activity
            NotificationsUtils.displaySnackbar(
                activity,
                context.getString(fr.corenting.edcompanion.R.string.my_location_error)
            )

        }
    }
}
