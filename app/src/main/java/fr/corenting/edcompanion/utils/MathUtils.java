package fr.corenting.edcompanion.utils;

import android.content.Context;

import java.text.NumberFormat;
import java.util.Locale;

public class MathUtils {

    public static NumberFormat getNumberFormat(Context context) {
        Locale userLocale = DateUtils.getCurrentLocale(context);
        return NumberFormat.getIntegerInstance(userLocale);
    }
}
