package fr.corenting.edcompanion.utils;


import android.content.Context;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.network.player.EDSMPlayer;
import fr.corenting.edcompanion.network.player.InaraPlayer;
import fr.corenting.edcompanion.network.player.PlayerNetwork;

public class PlayerNetworkUtils {

    private static final String edsm = "EDSM";
    private static final String inara = "Inara";
    private static final String frontier = "Frontier cAPI";

    public static PlayerNetwork getCurrentPlayerNetwork(Context context) {
        String current = SettingsUtils.getString(context, context.getResources().getString(R.string.settings_cmdr_source));

        return getCurrentPlayerNetwork(context, current);
    }

    public static PlayerNetwork getCurrentPlayerNetwork(Context context, String value) {
        if (value.equals("")) {
            return new EDSMPlayer(context);
        }

        switch (value) {
            case edsm:
                return new EDSMPlayer(context);
            case inara:
                return new InaraPlayer(context);
            default:
                return new EDSMPlayer(context);
        }
    }

    public static String[] getSourcesList() {
        return new String[]{edsm, inara};
    }


    public static boolean setupOk(Context context) {
        String apiKey = SettingsUtils.getSecureString(context, context.getString(R.string.settings_cmdr_password));
        String commanderName = SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_username));

        PlayerNetwork network = getCurrentPlayerNetwork(context);

        boolean res = false;
        if (!commanderName.equals("")) {
            res = true;
        }
        return !(network.needPassword() && apiKey.equals("")) && res;
    }
}
