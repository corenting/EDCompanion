package fr.corenting.edcompanion.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import fr.corenting.edcompanion.R;

public class ViewUtils {

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

}
