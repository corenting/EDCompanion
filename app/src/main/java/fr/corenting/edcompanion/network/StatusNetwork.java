package fr.corenting.edcompanion.network;

import android.support.design.widget.Snackbar;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.StatusFragment;
import fr.corenting.edcompanion.models.CommanderPosition;
import fr.corenting.edcompanion.models.Credits;
import fr.corenting.edcompanion.models.Ranks;
import fr.corenting.edcompanion.utils.SettingsUtils;


public class StatusNetwork {
    public static void getAll(final StatusFragment fragment) {
        if (!SettingsUtils.hasValidCmdrParameters(fragment.getActivity())) {
            Snackbar snackbar = Snackbar
                    .make(fragment.getActivity().findViewById(android.R.id.content),
                            R.string.commander_error, Snackbar.LENGTH_SHORT);
            snackbar.show();
            fragment.endLoading();
        } else {
            getCredits(fragment);
            getRanks(fragment);
            getPosition(fragment);
        }
    }

    private static void getRanks(final StatusFragment fragment) {
        String url = fragment.getString(R.string.edsm_ranks) +
                "?apiKey=" + SettingsUtils.getEdsmApiKey(fragment.getContext()) +
                "&commanderName=" + SettingsUtils.getCommanderName(fragment.getContext());
        Ion.with(fragment)
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
                            EventBus.getDefault().post(ranks);
                            fragment.endLoading();
                        } catch (Exception ex) {
                            Snackbar snackbar = Snackbar
                                    .make(fragment.getActivity().findViewById(android.R.id.content),
                                            R.string.download_error, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }

    private static void getPosition(final StatusFragment fragment) {

        String url = fragment.getString(R.string.edsm_position) +
                "?apiKey=" + SettingsUtils.getEdsmApiKey(fragment.getContext()) +
                "&commanderName=" + SettingsUtils.getCommanderName(fragment.getContext());
        Ion.with(fragment)
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
                            res.SystemName = result.get("system").getAsString();
                            res.FirstDiscover = result.get("firstDiscover").getAsBoolean();

                            // Send to bus and stop loading
                            EventBus.getDefault().post(res);
                            fragment.endLoading();
                        } catch (Exception ex) {
                            Snackbar snackbar = Snackbar
                                    .make(fragment.getActivity().findViewById(android.R.id.content),
                                            R.string.download_error, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            fragment.endLoading();
                        }
                    }
                });
    }

    private static void getCredits(final StatusFragment fragment) {

        String url = fragment.getString(R.string.edsm_credits) +
                "?apiKey=" + SettingsUtils.getEdsmApiKey(fragment.getContext()) +
                "&commanderName=" + SettingsUtils.getCommanderName(fragment.getContext());
        Ion.with(fragment)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }

                            JsonObject creditsObject = result.getAsJsonArray("credits").get(0).getAsJsonObject();

                            // Extract balance from json
                            Credits res = new Credits();
                            res.balance = creditsObject.get("balance").getAsInt();
                            res.loan = creditsObject.get("loan").getAsInt();

                            // Send to bus and stop loading
                            EventBus.getDefault().post(res);
                        } catch (Exception ex) {
                            Snackbar snackbar = Snackbar
                                    .make(fragment.getActivity().findViewById(android.R.id.content),
                                            R.string.download_error, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }
}
