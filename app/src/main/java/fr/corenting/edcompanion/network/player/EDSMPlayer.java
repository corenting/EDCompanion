package fr.corenting.edcompanion.network.player;

import android.content.Context;
import android.net.Uri;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Callback;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.xpece.android.support.preference.EditTextPreference;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommanderPosition;
import fr.corenting.edcompanion.models.Credits;
import fr.corenting.edcompanion.models.Ranks;
import fr.corenting.edcompanion.utils.SettingsUtils;


public class EDSMPlayer extends PlayerNetwork {

    private Context context;

    private String apiKey;
    private String commanderName;

    public EDSMPlayer(Context context) {

        this.context = context;
        apiKey = SettingsUtils.getSecureString(context, context.getString(R.string.settings_cmdr_password));
        commanderName = SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_username));
    }

    private String buildUrlParameters(String urlBase) {
        urlBase = context.getString(R.string.edsm_base) + urlBase;
        return Uri.parse(urlBase)
                .buildUpon()
                .appendQueryParameter("apiKey", apiKey)
                .appendQueryParameter("commanderName", commanderName)
                .build().toString();
    }

    @Override
    public boolean needPassword() {
        return true;
    }

    @Override
    public boolean supportFleet() {
        return false;
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
        preference.setTitle(context.getString(R.string.settings_cmdr_username_title));
        preference.setSummary(context.getString(R.string.settings_cmdr_username_summary));
        preference.setDialogTitle(context.getString(R.string.settings_cmdr_username_title));
    }

    @Override
    public void passwordSettingSetup(EditTextPreference preference) {
        preference.setTitle(context.getString(R.string.settings_cmdr_edsm_password_title));
        preference.setSummary(context.getString(R.string.settings_cmdr_edsm_password_summary));
        preference.setDialogTitle(context.getString(R.string.settings_cmdr_edsm_password_title));
    }


    @Override
    public String getErrorMessage() {
        return context.getString(R.string.edsm_error);
    }

    @Override
    public void getRanks() {
        String url = buildUrlParameters(context.getString(R.string.edsm_ranks));
        Bridge.get(url)
                .request(new Callback() {
                    @Override
                    public void response(Request request, Response response, BridgeException e) {
                        Ranks ranks = new Ranks();
                        try {
                            if (e != null) {
                                throw new Exception();
                            }

                            JsonObject json = new JsonParser().parse(response.asString()).getAsJsonObject();
                            JsonObject ranksObject = json.getAsJsonObject("ranks");
                            JsonObject progressObject = json.getAsJsonObject("progress");
                            JsonObject verboseObject = json.getAsJsonObject("ranksVerbose");

                            // Combat
                            ranks.combat.name = verboseObject.get("Combat").getAsString();
                            ranks.combat.progress = progressObject.get("Combat").getAsInt();
                            ranks.combat.value = ranksObject.get("Combat").getAsInt();

                            // Trade
                            ranks.trade.name = verboseObject.get("Trade").getAsString();
                            ranks.trade.progress = progressObject.get("Trade").getAsInt();
                            ranks.trade.value = ranksObject.get("Trade").getAsInt();

                            // Explore
                            ranks.explore.name = verboseObject.get("Explore").getAsString();
                            ranks.explore.progress = progressObject.get("Explore").getAsInt();
                            ranks.explore.value = ranksObject.get("Explore").getAsInt();

                            // CQC
                            ranks.cqc.name = verboseObject.get("CQC").getAsString();
                            ranks.cqc.progress = progressObject.get("CQC").getAsInt();
                            ranks.cqc.value = ranksObject.get("CQC").getAsInt();

                            // Federation
                            ranks.federation.name = verboseObject.get("Federation").getAsString();
                            ranks.federation.progress = progressObject.get("Federation").getAsInt();
                            ranks.federation.value = ranksObject.get("Federation").getAsInt();

                            // Empire
                            ranks.empire.name = verboseObject.get("Empire").getAsString();
                            ranks.empire.progress = progressObject.get("Empire").getAsInt();
                            ranks.empire.value = ranksObject.get("Empire").getAsInt();

                            ranks.Success = true;

                        } catch (Exception ex) {
                            ranks.Success = false;
                        }
                        sendResultMessage(ranks);
                    }
                });
    }

    @Override
    public void getCommanderPosition() {
        String url = buildUrlParameters(context.getString(R.string.edsm_position));
        Bridge.get(url)
                .request(new Callback() {
                    @Override
                    public void response(Request request, Response response, BridgeException e) {
                        CommanderPosition pos = new CommanderPosition();
                        try {
                            if (e != null) {
                                throw new Exception();
                            }
                            JsonObject json = new JsonParser().parse(response.asString()).getAsJsonObject();

                            // Extract position from json
                            if (json.get("system").isJsonNull() || json.get("firstDiscover").isJsonNull()) {
                                pos.SystemName = null;
                                pos.FirstDiscover = false;
                            } else {
                                pos.SystemName = json.get("system").getAsString();
                                pos.FirstDiscover = json.get("firstDiscover").getAsBoolean();
                            }

                            // Send to bus
                            pos.Success = true;
                        } catch (Exception ex) {
                            pos.Success = false;
                        }
                        sendResultMessage(pos);
                    }
                });
    }

    @Override
    public void getCredits() {
        String url = buildUrlParameters(context.getString(R.string.edsm_credits));
        Bridge.get(url)
                .request(new Callback() {
                    @Override
                    public void response(Request request, Response response, BridgeException e) {
                        Credits res = new Credits();
                        try {
                            if (e != null) {
                                throw new Exception();
                            }
                            JsonObject json = new JsonParser().parse(response.asString()).getAsJsonObject();

                            if (!json.has("credits")) {
                                res.Balance = -1;
                                res.Loan = -1;
                            } else {
                                // Extract Balance from json
                                JsonObject creditsObject = json.getAsJsonArray("credits").get(0).getAsJsonObject();
                                res.Balance = creditsObject.get("balance").getAsInt();
                                res.Loan = creditsObject.get("loan").getAsInt();
                            }

                            // Send to bus
                            res.Success = true;
                        } catch (Exception ex) {
                            res.Success = false;
                        }
                        sendResultMessage(res);
                    }
                });
    }
}
