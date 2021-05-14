package fr.corenting.edcompanion.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

public class HideKeyboardDrawerListener implements DrawerLayout.DrawerListener {

    private final View rootView;

    public HideKeyboardDrawerListener(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {
        ViewUtils.hideSoftKeyboard(rootView);
    }
}
