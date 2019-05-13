package fr.corenting.edcompanion.network.player;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.apis.Frontier.FrontierProfileResponse;
import fr.corenting.edcompanion.models.events.CommanderPosition;
import fr.corenting.edcompanion.models.events.Credits;
import fr.corenting.edcompanion.models.events.Ranks;
import fr.corenting.edcompanion.network.retrofit.FrontierRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;


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
    public boolean usePassword() {
        return false;
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

    @Override
    public void getCommanderStatus() {
        retrofit2.Callback<FrontierProfileResponse> callback = new retrofit2.Callback<FrontierProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<FrontierProfileResponse> call,
                                   retrofit2.Response<FrontierProfileResponse> response) {
                FrontierProfileResponse body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    CommanderPosition pos;
                    Credits credits;
                    Ranks ranks;
                    try {
                        pos = new CommanderPosition(true, body.LastSystem.Name,
                                false);
                        sendResultMessage(pos);

                        credits = new Credits(true, body.Commander.Credits,
                                body.Commander.Debt);
                        sendResultMessage(credits);

                        ranks = getRanksFromApiBody(body);
                        sendResultMessage(ranks);
                    } catch (Exception ex) {
                        onFailure(call, new Exception("Invalid response"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FrontierProfileResponse> call, @NonNull Throwable t) {
                Credits credits = new Credits(false, 0, 0);
                CommanderPosition pos = new CommanderPosition(false,
                        "", false);
                Ranks ranks = new Ranks(false, null, null,
                        null, null, null, null);

                sendResultMessage(credits);
                sendResultMessage(pos);
                sendResultMessage(ranks);
            }
        };
        frontierRetrofit.getProfile().enqueue(callback);
    }

    @Override
    public void getRanks() {
        // TODO
    }

    @Override
    public void getCommanderPosition() {
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
                        sendResultMessage(pos);
                    } catch (Exception ex) {
                        onFailure(call, new Exception("Invalid response"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FrontierProfileResponse> call, @NonNull Throwable t) {
                CommanderPosition pos = new CommanderPosition(false,
                        "", false);
                sendResultMessage(pos);
            }
        };
        frontierRetrofit.getProfile().enqueue(callback);
    }

    @Override
    public void getCredits() {
        // TODO
    }

    @Override
    public void getFleet() {
        retrofit2.Callback<FrontierProfileResponse> callback = new retrofit2.Callback<FrontierProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<FrontierProfileResponse> call,
                                   retrofit2.Response<FrontierProfileResponse> response) {
                FrontierProfileResponse body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    CommanderPosition pos;
                    Credits credits;
                    Ranks ranks;
                    try {
                        pos = new CommanderPosition(true, body.LastSystem.Name,
                                false);
                        sendResultMessage(pos);

                        credits = new Credits(true, body.Commander.Credits,
                                body.Commander.Debt);
                        sendResultMessage(credits);

                        ranks = getRanksFromApiBody(body);
                        sendResultMessage(ranks);
                    } catch (Exception ex) {
                        onFailure(call, new Exception("Invalid response"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FrontierProfileResponse> call, @NonNull Throwable t) {
                Credits credits = new Credits(false, 0, 0);
                CommanderPosition pos = new CommanderPosition(false,
                        "", false);
                Ranks ranks = new Ranks(false, null, null,
                        null, null, null, null);

                sendResultMessage(credits);
                sendResultMessage(pos);
                sendResultMessage(ranks);
            }
        };
        frontierRetrofit.getProfile().enqueue(callback);
    }
}
