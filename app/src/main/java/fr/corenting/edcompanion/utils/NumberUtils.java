package fr.corenting.edcompanion.utils;

import android.content.Context;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {
    private static NumberFormat numberFormat = null;

    public static NumberFormat getNumberFormat(Context context) {
        if (numberFormat != null) {
            return numberFormat;
        }

        Locale userLocale = DateUtils.getCurrentLocale(context);
        numberFormat =  NumberFormat.getIntegerInstance(userLocale);
        return numberFormat;
    }
}
