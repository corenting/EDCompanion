package fr.corenting.edcompanion.network.player;

import android.content.Context;

import androidx.preference.EditTextPreference;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.network.retrofit.FrontierRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;


public class FrontierPlayer extends PlayerNetwork {

    private Context context;
    private FrontierRetrofit frontierRetrofit;

    public FrontierPlayer(Context context) {

        this.context = context;
        frontierRetrofit = RetrofitSingleton.getInstance()
                .getFrontierRetrofit(context.getApplicationContext());
    }

    @Override
    public boolean useFrontierAuth() {
        return true;
    }

    @Override
    public boolean usePassword() {
        return false;
    }

    @Override
    public boolean supportFleet() {
        return true;
    }

    @Override
    public boolean supportCredits() {
        return true;
    }

    @Override
    public boolean supportLocation() {
        return true;
    }

    @Override
    public void usernameSettingSetup(EditTextPreference preference) {
    }

    @Override
    public void passwordSettingSetup(EditTextPreference preference) {
    }


    @Override
    public String getErrorMessage() {
        return context.getString(R.string.frontier_error);
    }

    @Override
    public void getRanks() {
        // TODO
    }

    @Override
    public void getCommanderPosition() {
        // TODO
    }

    @Override
    public void getCredits() {
        // TODO
    }
}
