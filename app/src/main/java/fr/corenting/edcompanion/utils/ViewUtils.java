package fr.corenting.edcompanion.utils;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.AppBarLayout;

import fr.corenting.edcompanion.R;

public class ViewUtils {

    public static float dpToPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void switchFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {
        if (fragmentManager != null && fragment != null && tag != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContent, fragment, tag)
                    .commit();
        } else {
            Log.w("switchFragment", "Error when switching fragment");
        }
    }

    public static void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void setToolbarElevation(AppBarLayout appBarLayout, float elevation) {
        appBarLayout.setStateListAnimator(null);
        ViewCompat.setElevation(appBarLayout, elevation);
    }
}
