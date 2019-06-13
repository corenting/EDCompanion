package fr.corenting.edcompanion.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import org.threeten.bp.Instant;
import org.threeten.bp.Year;
import org.threeten.bp.format.FormatStyle;

import java.util.Locale;

public class DateUtils {

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getCurrentLocale(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ctx.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return ctx.getResources().getConfiguration().locale;
        }
    }

    public static String getUtcIsoDate() {
        return Instant.now().toString();
    }

    // Ugly hack to remove year from FormatStyle.SHORT
    public static String removeYearFromDate(String dateString) {
        String year = String.valueOf(Year.now().getValue());
        String lastYear = String.valueOf(Year.now().getValue());

        return dateString
                .replace(String.format("/%s", lastYear), "")
                .replace(String.format("/%s", year), "");
    }
}
