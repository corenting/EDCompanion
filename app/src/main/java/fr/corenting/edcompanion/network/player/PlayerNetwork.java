package fr.corenting.edcompanion.network.player;

import net.xpece.android.support.preference.EditTextPreference;

import org.greenrobot.eventbus.EventBus;

public abstract class PlayerNetwork {

    public abstract boolean needPassword();
    public abstract boolean supportFleet();
    public abstract boolean supportCredits();
    public abstract boolean supportLocation();


    public abstract void usernameSettingSetup(EditTextPreference preference);
    public abstract void passwordSettingSetup(EditTextPreference preference);

    public abstract String getErrorMessage();

    public abstract void getRanks();
    public abstract void getCommanderPosition();
    public abstract void getCredits();


    protected void sendResultMessage(Object data) {
        EventBus.getDefault().post(data);
    }
}