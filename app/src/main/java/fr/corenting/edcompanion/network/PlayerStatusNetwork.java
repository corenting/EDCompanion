package fr.corenting.edcompanion.network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommanderPosition;
import fr.corenting.edcompanion.models.Credits;
import fr.corenting.edcompanion.models.Ranks;
import fr.corenting.edcompanion.utils.SettingsUtils;


public class PlayerStatusNetwork {


    public static void getRanks(Context ctx) {
        String url = ctx.getString(R.string.edsm_ranks) +
                "?apiKey=" + SettingsUtils.getEdsmApiKey(ctx) +
                "&commanderName=" + SettingsUtils.getCommanderName(ctx);
        Ion.with(ctx)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }

                            JsonObject ranksObject = result.getAsJsonObject("ranks");
                            JsonObject progressObject = result.getAsJsonObject("progress");
                            JsonObject verboseObject = result.getAsJsonObject("ranksVerbose");

                            Ranks ranks = new Ranks();

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

                            // Send to bus and stop loading
                            ranks.Success = true;
                            EventBus.getDefault().post(ranks);
                        } catch (Exception ex) {
                            Ranks ranks = new Ranks();
                            ranks.Success = false;
                            EventBus.getDefault().post(ranks);
                        }
                    }
                });
    }

    public static void getPosition(Context ctx) {

        String url = ctx.getString(R.string.edsm_position) +
                "?apiKey=" + SettingsUtils.getEdsmApiKey(ctx) +
                "&commanderName=" + SettingsUtils.getCommanderName(ctx);
        Ion.with(ctx)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }

                            // Extract position from json
                            CommanderPosition res = new CommanderPosition();
                            if (result.get("system").isJsonNull() || result.get("firstDiscover").isJsonNull()) {
                                res.SystemName = null;
                                res.FirstDiscover = false;
                            } else {
                                res.SystemName = result.get("system").getAsString();
                                res.FirstDiscover = result.get("firstDiscover").getAsBoolean();
                            }

                            // Send to bus
                            res.Success = true;
                            EventBus.getDefault().post(res);
                        } catch (Exception ex) {
                            CommanderPosition pos = new CommanderPosition();
                            pos.Success = false;
                            EventBus.getDefault().post(pos);
                        }
                    }
                });
    }

    public static void getCredits(Context ctx) {

        String url = ctx.getString(R.string.edsm_credits) +
                "?apiKey=" + SettingsUtils.getEdsmApiKey(ctx) +
                "&commanderName=" + SettingsUtils.getCommanderName(ctx);
        Ion.with(ctx)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }
                            Credits res = new Credits();

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
                            EventBus.getDefault().post(res);
                        } catch (Exception ex) {
                            Credits res = new Credits();
                            res.Success = false;
                            EventBus.getDefault().post(res);
                        }
                    }
                });
    }
}
