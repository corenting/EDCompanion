package fr.corenting.edcompanion.network.player;

import android.content.Context;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Callback;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.xpece.android.support.preference.EditTextPreference;

import fr.corenting.edcompanion.BuildConfig;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.Ranks;
import fr.corenting.edcompanion.utils.DateUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;


public class InaraPlayer extends PlayerNetwork {

    private Context context;

    private String commanderName;

    public InaraPlayer(Context context) {

        this.context = context;
        commanderName = SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_username));
    }

    private JsonObject getJsonBody(String eventName, String eventDataKey, String eventDataValue) {

        JsonObject json = new JsonObject();

        JsonObject header = new JsonObject();
        header.addProperty("appName", context.getString(R.string.app_name));
        header.addProperty("appVersion", BuildConfig.VERSION_NAME);
        header.addProperty("isDeveloped", BuildConfig.DEBUG);
        header.addProperty("APIkey", BuildConfig.INARA_API_KEY);

        JsonArray eventsArray = new JsonArray();
        JsonObject event = new JsonObject();
        event.addProperty("eventName", eventName);
        event.addProperty("eventTimestamp", DateUtils.getUtcIsoDate());
        JsonObject eventData = new JsonObject();
        eventData.addProperty(eventDataKey, eventDataValue);

        event.add("eventData", eventData);
        eventsArray.add(event);
        json.add("events", eventsArray);
        json.add("header", header);

        return json;
    }

    private boolean isValidInaraResponse(JsonObject res) {
        int header = res.getAsJsonObject("header").get("eventStatus").getAsInt();
        int event = res.getAsJsonArray("events").get(0).getAsJsonObject().get("eventStatus").getAsInt();

        return header == 200 & event == 200;
    }

    @Override
    public boolean needPassword() {
        return false;
    }

    @Override
    public boolean supportFleet() {
        return false;
    }

    @Override
    public boolean supportCredits() {
        return false;
    }

    @Override
    public boolean supportLocation() {
        return false;
    }

    @Override
    public void usernameSettingSetup(EditTextPreference preference) {
        preference.setTitle(context.getString(R.string.settings_cmdr_inara_username_title));
        preference.setSummary(context.getString(R.string.settings_cmdr_inara_username_summary));
        preference.setDialogTitle(context.getString(R.string.settings_cmdr_inara_username_title));
    }

    @Override
    public void passwordSettingSetup(EditTextPreference preference) {

    }

    @Override
    public String getErrorMessage() {
        return context.getString(R.string.inara_error);
    }

    @Override
    public void getRanks() {
        JsonObject body = getJsonBody("getCommanderProfile", "searchName", commanderName);

        Bridge.post(context.getString(R.string.inara_api))
                .body(body.toString())
                .request(new Callback() {
                    @Override
                    public void response(Request request, Response response, BridgeException e) {
                        Ranks ranks = new Ranks();
                        try {
                            if (e != null) {
                                throw new Exception();
                            }
                            JsonObject json = new JsonParser().parse(response.asString()).getAsJsonObject();
                            if (!isValidInaraResponse(json)) {
                                throw new Exception();
                            }

                            json = json.get("events").getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("eventData");

                            JsonArray ranksObject = json.getAsJsonArray("commanderRanksPilot");

                            for (JsonElement rank : ranksObject) {
                                String name = rank.getAsJsonObject().get("rankName").getAsString();
                                int value = rank.getAsJsonObject().get("rankValue").getAsInt();
                                int progress = (int) (rank.getAsJsonObject().get("rankProgress").getAsDouble() * 100);

                                switch (name) {
                                    case "combat":
                                        ranks.combat.progress = progress;
                                        ranks.combat.value = value;
                                        ranks.combat.name = context.getResources().getStringArray(R.array.ranks_combat)[ranks.combat.value];
                                        break;
                                    case "trade":
                                        ranks.trade.progress = progress;
                                        ranks.trade.value = value;
                                        ranks.trade.name = context.getResources().getStringArray(R.array.ranks_trade)[ranks.trade.value];
                                        break;
                                    case "exploration":
                                        ranks.explore.progress = progress;
                                        ranks.explore.value = value;
                                        ranks.explore.name = context.getResources().getStringArray(R.array.ranks_explorer)[ranks.explore.value];
                                        break;
                                    case "cqc":
                                        ranks.cqc.progress = progress;
                                        ranks.cqc.value = value;
                                        ranks.cqc.name = context.getResources().getStringArray(R.array.ranks_cqc)[ranks.cqc.value];
                                        break;
                                    case "empire":
                                        ranks.empire.progress = progress;
                                        ranks.empire.value = value;
                                        ranks.empire.name = context.getResources().getStringArray(R.array.ranks_empire)[ranks.empire.value];
                                        break;
                                    case "federation":
                                        ranks.federation.progress = progress;
                                        ranks.federation.value = value;
                                        ranks.federation.name = context.getResources().getStringArray(R.array.ranks_federation)[ranks.federation.value];
                                        break;
                                }
                            }
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

    }

    @Override
    public void getCredits() {

    }
}
