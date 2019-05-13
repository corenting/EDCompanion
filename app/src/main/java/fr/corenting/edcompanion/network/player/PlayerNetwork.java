package fr.corenting.edcompanion.network.player;

import androidx.preference.EditTextPreference;

import org.greenrobot.eventbus.EventBus;

public abstract class PlayerNetwork {

    public abstract boolean useFrontierAuth();
    public abstract boolean usePassword();
    public abstract boolean supportFleet();
    public abstract boolean supportCredits();
    public abstract boolean supportLocation();


    public abstract void usernameSettingSetup(EditTextPreference preference);
    public abstract void passwordSettingSetup(EditTextPreference preference);

    public abstract String getErrorMessage();

    // Method to get all informations in one go for commander status
    public void getCommanderStatus() {
        getRanks();
        getCommanderPosition();
        getCredits();
    }

    public abstract void getRanks();
    public abstract void getCommanderPosition();
    public abstract void getCredits();
    public abstract void getFleet();


    protected void sendResultMessage(Object data) {
        EventBus.getDefault().post(data);
    }
}
