package fr.corenting.edcompanion.utils;


import android.content.Context;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.network.player.EDSMPlayer;
import fr.corenting.edcompanion.network.player.FrontierPlayer;
import fr.corenting.edcompanion.network.player.InaraPlayer;
import fr.corenting.edcompanion.network.player.PlayerNetwork;

public class PlayerNetworkUtils {

    private static final String frontier = "Frontier API (directly with your game account)";
    private static final String edsm = "EDSM";
    private static final String inara = "Inara";

    public static PlayerNetwork getCurrentPlayerNetwork(Context context) {
        String current = SettingsUtils.getString(context, context.getResources().getString(R.string.settings_cmdr_source));

        return getCurrentPlayerNetwork(context, current);
    }

    public static PlayerNetwork getCurrentPlayerNetwork(Context context, String value) {
        if (value.equals("")) {
            return new FrontierPlayer(context);
        }

        switch (value) {
            case edsm:
                return new EDSMPlayer(context);
            case inara:
                return new InaraPlayer(context);
            default:
                return new FrontierPlayer(context);
        }
    }

    public static String[] getSourcesList() {
        return new String[]{frontier, edsm, inara};
    }


    public static boolean setupOk(Context context) {
        String password = SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_password));
        String username = SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_username));
        PlayerNetwork network = getCurrentPlayerNetwork(context);

        if (network.useFrontierAuth()) {
            return !OAuthUtils.getAccessToken(context).equals("");
        } else if (network.useUsername() && network.usePassword()) {
            return !username.equals("") && !password.equals("");
        } else if (network.usePassword()) {
            return !password.equals("");
        }
        return false;
    }
}
