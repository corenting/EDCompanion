package fr.corenting.edcompanion.network.player;

import android.content.Context;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommanderPosition;
import fr.corenting.edcompanion.models.Credits;
import fr.corenting.edcompanion.models.Ranks;


public class EDSMPlayer extends PlayerNetworkBase {

    private Context context;

    private String apiKey;
    private String commanderName;

    public EDSMPlayer(Context context) {

        this.context = context;
        apiKey = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.settings_edsm_key), "");
        commanderName = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.settings_cmdr), "");
    }

    private String buildUrlParameters(String urlBase) {
        return Uri.parse(urlBase)
                .buildUpon()
                .appendQueryParameter("apiKey", apiKey)
                .appendQueryParameter("commanderName", commanderName)
                .build().toString();
    }

    @Override
    public boolean canBeUsed() {
        return !apiKey.equals("") && !commanderName.equals("");
    }

    @Override
    public String getErrorMessage() {
        return context.getString(R.string.edsm_error);
    }

    @Override
    public void getRanks() {
        String url = buildUrlParameters(context.getString(R.string.edsm_ranks));
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Ranks ranks = new Ranks();
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }

                            JsonObject ranksObject = result.getAsJsonObject("ranks");
                            JsonObject progressObject = result.getAsJsonObject("progress");
                            JsonObject verboseObject = result.getAsJsonObject("ranksVerbose");

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
                        } catch (Exception ignored) {
                            ranks.Success = false;
                        }
                        sendResultMessage(ranks);
                    }
                });
    }

    @Override
    public void getCommanderPosition() {

        String url = buildUrlParameters(context.getString(R.string.edsm_position));
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        CommanderPosition pos = new CommanderPosition();
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }

                            // Extract position from json
                            if (result.get("system").isJsonNull() || result.get("firstDiscover").isJsonNull()) {
                                pos.SystemName = null;
                                pos.FirstDiscover = false;
                            } else {
                                pos.SystemName = result.get("system").getAsString();
                                pos.FirstDiscover = result.get("firstDiscover").getAsBoolean();
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
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Credits res = new Credits();
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }

                            if (!result.has("credits")) {
                                res.Balance = -1;
                                res.Loan = -1;
                            } else {
                                // Extract Balance from json
                                JsonObject creditsObject = result.getAsJsonArray("credits").get(0).getAsJsonObject();
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
