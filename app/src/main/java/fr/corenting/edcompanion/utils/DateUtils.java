package fr.corenting.edcompanion.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import org.threeten.bp.Instant;

import java.util.Locale;

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
        return Instant.now().toString();
    }
}
