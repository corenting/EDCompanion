package fr.corenting.edcompanion.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.util.TypedValue;

public class ViewUtils {
    private static final int[] TEMP_ARRAY = new int[1];

    public static float dpToPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int dpToPxOffset(Context context, int dp) {
        return (int) (dpToPx(context, dp));
    }


    public static int resolveResourceId(Context context, @AttrRes int attr, int fallback) {
        TEMP_ARRAY[0] = attr;
        TypedArray ta = context.obtainStyledAttributes(TEMP_ARRAY);
        try {
            return ta.getResourceId(0, fallback);
        } finally {
            ta.recycle();
        }
    }
}
