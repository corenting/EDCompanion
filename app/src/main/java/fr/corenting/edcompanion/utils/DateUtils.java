package fr.corenting.edcompanion.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import androidx.core.os.ConfigurationCompat;

import org.threeten.bp.Instant;
import org.threeten.bp.Year;

import java.util.Locale;

public class DateUtils {

    public static Locale getCurrentLocale(Context ctx) {
        return ConfigurationCompat.getLocales(ctx.getResources().getConfiguration()).get(0);
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
