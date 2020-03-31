package fr.corenting.edcompanion.network.player;

import androidx.preference.EditTextPreference;

import org.greenrobot.eventbus.EventBus;

public abstract class PlayerNetwork {

    public abstract boolean useFrontierAuth();

    public abstract boolean useUsername();

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

    public void getCommanderPosition() {
        getCommanderPosition(EventBus.getDefault());
    }

    public abstract void getRanks();

    public abstract void getCredits();

    public abstract void getFleet();

    public abstract void getCommanderPosition(EventBus bus); // Special method for SystemInputView


    protected void sendResultMessage(Object data) {
        EventBus.getDefault().post(data);
    }

    protected void sendResultMessage(EventBus bus, Object data) {
        bus.post(data);
    }
}
