package fr.corenting.edcompanion.network.player;

import org.greenrobot.eventbus.EventBus;

public abstract class PlayerNetworkBase {

    public abstract boolean canBeUsed();
    public abstract String getErrorMessage();

    public abstract void getRanks();
    public abstract void getCommanderPosition();
    public abstract void getCredits();


    protected void sendResultMessage(Object data) {
        EventBus.getDefault().post(data);
    }
}
