package fr.corenting.edcompanion.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import fr.corenting.edcompanion.utils.ThemeUtils

class LightDarkImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {
        commonInit()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        commonInit()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        commonInit()
    }

    private fun commonInit() {
        if (ThemeUtils.isDarkThemeEnabled(context)) {
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(
                    ContextCompat.getColor(context, android.R.color.white)))
        }
        else {
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(
                    ContextCompat.getColor(context, android.R.color.black)))
        }
    }
}