package fr.corenting.edcompanion.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getCurrentLocale(Context ctx){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return ctx.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return ctx.getResources().getConfiguration().locale;
        }
    }

    public static String getUtcIsoDate(){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US);
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static Date getDateFromIsoDate(String isoDate) {
        try {
            Instant date = ZonedDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME).toInstant();
            return DateTimeUtils.toDate(date);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
