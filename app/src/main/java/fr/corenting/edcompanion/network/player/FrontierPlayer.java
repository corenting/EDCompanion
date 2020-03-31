package fr.corenting.edcompanion.network.player;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.Ship;
import fr.corenting.edcompanion.models.apis.Frontier.FrontierProfileResponse;
import fr.corenting.edcompanion.models.events.CommanderPosition;
import fr.corenting.edcompanion.models.events.Credits;
import fr.corenting.edcompanion.models.events.Fleet;
import fr.corenting.edcompanion.models.events.Ranks;
import fr.corenting.edcompanion.network.retrofit.FrontierRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import fr.corenting.edcompanion.utils.InternalNamingUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class FrontierPlayer extends PlayerNetwork {

    private Context context;
    private FrontierRetrofit frontierRetrofit;

    public FrontierPlayer(Context context) {

        this.context = context;
        frontierRetrofit = RetrofitSingleton.getInstance()
                .getFrontierRetrofit(context.getApplicationContext());
    }

    @Override
    public boolean useFrontierAuth() {
        return true;
    }

    @Override
    public boolean useUsername() {
        return false;
    }

    @Override
    public boolean usePassword() {
        return false;
    }

    @Override
    public boolean supportFleet() {
        return true;
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
    }

    @Override
    public void passwordSettingSetup(EditTextPreference preference) {
    }


    @Override
    public String getErrorMessage() {
        return context.getString(R.string.frontier_error);
    }


    private Ranks getRanksFromApiBody(FrontierProfileResponse apiResponse) {

        FrontierProfileResponse.FrontierProfileCommanderRankResponse apiRanks =
                apiResponse.Commander.Rank;

        // Combat
        Ranks.Rank combatRank = new Ranks.Rank(
                context.getResources()
                        .getStringArray(R.array.ranks_combat)
                        [apiRanks.Combat],
                apiRanks.Combat, -1);

        // Trade
        Ranks.Rank tradeRank = new Ranks.Rank(
                context.getResources()
                        .getStringArray(R.array.ranks_trade)
                        [apiRanks.Trade],
                apiRanks.Trade, -1);

        // Explore
        Ranks.Rank exploreRank = new Ranks.Rank(
                context.getResources()
                        .getStringArray(R.array.ranks_explorer)
                        [apiRanks.Explore],
                apiRanks.Explore, -1);

        // CQC
        Ranks.Rank cqcRank = new Ranks.Rank(
                context.getResources()
                        .getStringArray(R.array.ranks_cqc)
                        [apiRanks.Cqc],
                apiRanks.Cqc, -1);

        // Federation
        Ranks.Rank federationRank = new Ranks.Rank(
                context.getResources()
                        .getStringArray(R.array.ranks_federation)
                        [apiRanks.Federation],
                apiRanks.Federation, -1);

        // Empire
        Ranks.Rank empireRank = new Ranks.Rank(
                context.getResources()
                        .getStringArray(R.array.ranks_empire)
                        [apiRanks.Empire],
                apiRanks.Empire, -1);

        return new Ranks(true, combatRank, tradeRank, exploreRank,
                cqcRank, federationRank, empireRank);
    }

    private void handleFleetParsing(JsonObject rawProfileResponse) {
        int currentShipId = rawProfileResponse.get("commander")
                .getAsJsonObject()
                .get("currentShipId")
                .getAsInt();


        // Sometimes the cAPI return an array, sometimes an object with indexes
        List<JsonElement> responseList = new ArrayList<>();
        if (rawProfileResponse.get("ships").isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry :
                    rawProfileResponse.get("ships").getAsJsonObject().entrySet()) {
                responseList.add(entry.getValue());
            }
        } else {
            for (JsonElement ship : rawProfileResponse.get("ships").getAsJsonArray()) {
                responseList.add(ship);
            }
        }

        List<Ship> shipsList = new ArrayList<>();

        for (JsonElement entry : responseList) {
            JsonObject rawShip = entry.getAsJsonObject();

            String shipName = null;
            if (rawShip.has("shipName")) {
                shipName = rawShip.get("shipName").getAsString();
            }

            JsonObject value = rawShip.get("value").getAsJsonObject();
            boolean isCurrentShip = rawShip.get("id").getAsInt() == currentShipId;

            Ship newShip = new Ship(
                    rawShip.get("id").getAsInt(),
                    InternalNamingUtils.getShipName(rawShip.get("name").getAsString()),
                    shipName,
                    rawShip.get("starsystem").getAsJsonObject().get("name").getAsString(),
                    rawShip.get("station").getAsJsonObject().get("name").getAsString(),
                    value.get("hull").getAsLong(),
                    value.get("modules").getAsLong(),
                    value.get("cargo").getAsLong(),
                    value.get("total").getAsLong(),
                    isCurrentShip);

            if (isCurrentShip) {
                shipsList.add(0, newShip);
            } else {
                shipsList.add(newShip);
            }
        }

        sendResultMessage(new Fleet(true, shipsList));
    }

    @Override
    public void getCommanderStatus() {
        retrofit2.Callback<ResponseBody> callback = new
                retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NotNull Response<ResponseBody> response) {

                        // Parse as string and as body
                        FrontierProfileResponse profileResponse = null;
                        JsonObject rawResponse = null;
                        try {
                            String responseString = response.body().string();

                            rawResponse = new JsonParser()
                                    .parse(responseString).getAsJsonObject();
                            profileResponse = new Gson()
                                    .fromJson(rawResponse, FrontierProfileResponse.class);
                        } catch (Exception e) {
                            onFailure(call, new Exception("Invalid response"));
                        }

                        if (!response.isSuccessful() || profileResponse == null) {
                            onFailure(call, new Exception("Invalid response"));
                        } else {
                            CommanderPosition pos;
                            Credits credits;
                            Ranks ranks;
                            try {

                                // Position
                                pos = new CommanderPosition(true, profileResponse.LastSystem.Name,
                                        false);
                                sendResultMessage(pos);

                                // Credits
                                credits = new Credits(true, profileResponse.Commander.Credits,
                                        profileResponse.Commander.Debt);
                                sendResultMessage(credits);

                                // Ranks
                                ranks = getRanksFromApiBody(profileResponse);
                                sendResultMessage(ranks);

                                // Fleet
                                handleFleetParsing(rawResponse);

                            } catch (Exception ex) {
                                onFailure(call, new Exception("Invalid response"));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Credits credits = new Credits(false, 0, 0);
                        CommanderPosition pos = new CommanderPosition(false,
                                "", false);
                        Ranks ranks = new Ranks(false, null, null,
                                null, null, null, null);
                        Fleet fleet = new Fleet(false, new ArrayList<>());

                        sendResultMessage(credits);
                        sendResultMessage(pos);
                        sendResultMessage(ranks);
                        sendResultMessage(fleet);
                    }
                };
        frontierRetrofit.getProfileRaw().enqueue(callback);
    }

    @Override
    public void getRanks() {
        // Not implemented for now, not needed as getCommanderStatus does it all at once
    }

    @Override
    public void getCommanderPosition(EventBus bus) {
        retrofit2.Callback<FrontierProfileResponse> callback = new retrofit2.Callback<FrontierProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<FrontierProfileResponse> call,
                                   retrofit2.Response<FrontierProfileResponse> response) {
                FrontierProfileResponse body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    CommanderPosition pos;
                    try {
                        pos = new CommanderPosition(true, body.LastSystem.Name,
                                false);
                        sendResultMessage(bus, pos);
                    } catch (Exception ex) {
                        onFailure(call, new Exception("Invalid response"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FrontierProfileResponse> call, @NonNull Throwable t) {
                CommanderPosition pos = new CommanderPosition(false,
                        "", false);
                sendResultMessage(bus, pos);
            }
        };
        frontierRetrofit.getProfile().enqueue(callback);
    }

    @Override
    public void getCredits() {
        // Not implemented for now, not needed as getCommanderStatus does it all at once
    }

    @Override
    public void getFleet() {
        getCommanderStatus(); // getCommanderStatus is already fetching all the informations for this
    }
}
