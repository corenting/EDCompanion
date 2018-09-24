package fr.corenting.edcompanion.network.player;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.preference.EditTextPreference;

import java.util.ArrayList;

import fr.corenting.edcompanion.BuildConfig;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.events.Ranks;
import fr.corenting.edcompanion.models.apis.Inara.InaraProfileRequestBody;
import fr.corenting.edcompanion.models.apis.Inara.InaraProfileResponse;
import fr.corenting.edcompanion.network.retrofit.InaraRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import fr.corenting.edcompanion.utils.DateUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;
import retrofit2.Call;


public class InaraPlayer extends PlayerNetwork {

    private Context context;
    private InaraRetrofit inaraRetrofit;

    private String commanderName;

    public InaraPlayer(Context context) {

        this.context = context;
        commanderName = SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_username));

        inaraRetrofit = RetrofitSingleton.getInstance()
                .getInaraRetrofit(context.getApplicationContext());
    }

    private InaraProfileRequestBody buildRequestBody() {
        InaraProfileRequestBody res = new InaraProfileRequestBody();

        // Build header
        res.header = new InaraProfileRequestBody.InaraRequestBodyHeader();
        res.header.ApiKey = BuildConfig.INARA_API_KEY;
        res.header.ApplicationName = context.getString(R.string.app_name);
        res.header.ApplicationVersion = BuildConfig.VERSION_NAME;
        res.header.IsDeveloped = BuildConfig.DEBUG;

        // Build event
        InaraProfileRequestBody.InaraRequestBodyEvent profileEvent = new InaraProfileRequestBody.InaraRequestBodyEvent();
        profileEvent.EventName = "getCommanderProfile";
        profileEvent.EventTimestamp = DateUtils.getUtcIsoDate();
        profileEvent.EventData = new InaraProfileRequestBody.InaraRequestBodyEvent.InaraRequestBodyEventData();
        profileEvent.EventData.SearchName = commanderName;
        res.events = new ArrayList<>();
        res.events.add(profileEvent);

        return res;
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

        retrofit2.Callback<InaraProfileResponse> callback = new retrofit2.Callback<InaraProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<InaraProfileResponse> call,
                                   retrofit2.Response<InaraProfileResponse> response) {
                InaraProfileResponse body = response.body();
                if (!response.isSuccessful() || body == null || body.events == null ||
                        body.events.size() != 1) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    Ranks ranks;
                    try {
                        Ranks.Rank combatRank = null;
                        Ranks.Rank tradeRank = null;
                        Ranks.Rank exploreRank = null;
                        Ranks.Rank cqcRank = null;
                        Ranks.Rank federationRank = null;
                        Ranks.Rank empireRank = null;

                        for (InaraProfileResponse.InaraProfileInnerResponse.InaraProfileResponseRanks rank :
                                body.events.get(0).EventData.CommanderRanksPilot) {
                            switch (rank.RankName) {
                                case "combat":
                                    combatRank = new Ranks.Rank(
                                            context.getResources()
                                                    .getStringArray(R.array.ranks_combat)
                                                    [rank.RankValue],
                                            rank.RankValue,
                                            (int) (rank.RankProgress * 100)
                                    );
                                    break;
                                case "trade":
                                    tradeRank = new Ranks.Rank(
                                            context.getResources()
                                                    .getStringArray(R.array.ranks_trade)
                                                    [rank.RankValue],
                                            rank.RankValue,
                                            (int) (rank.RankProgress * 100)
                                    );
                                    break;
                                case "exploration":
                                    exploreRank = new Ranks.Rank(
                                            context.getResources()
                                                    .getStringArray(R.array.ranks_explorer)
                                                    [rank.RankValue],
                                            rank.RankValue,
                                            (int) (rank.RankProgress * 100)
                                    );
                                    break;
                                case "cqc":
                                    cqcRank = new Ranks.Rank(
                                            context.getResources()
                                                    .getStringArray(R.array.ranks_cqc)
                                                    [rank.RankValue],
                                            rank.RankValue,
                                            (int) (rank.RankProgress * 100)
                                    );
                                    break;
                                case "empire":
                                    empireRank = new Ranks.Rank(
                                            context.getResources()
                                                    .getStringArray(R.array.ranks_empire)
                                                    [rank.RankValue],
                                            rank.RankValue,
                                            (int) (rank.RankProgress * 100)
                                    );
                                    break;
                                case "federation":
                                    federationRank = new Ranks.Rank(
                                            context.getResources()
                                                    .getStringArray(R.array.ranks_federation)
                                                    [rank.RankValue],
                                            rank.RankValue,
                                            (int) (rank.RankProgress * 100)
                                    );
                                    break;
                            }
                        }

                        ranks = new Ranks(true, combatRank, tradeRank, exploreRank,
                                cqcRank, federationRank, empireRank);
                    } catch (Exception e) {
                        ranks = new Ranks(false);
                    }
                    sendResultMessage(ranks);
                }
            }

            @Override
            public void onFailure(@NonNull Call<InaraProfileResponse> call, @NonNull Throwable t) {
                Ranks ranks = new Ranks(false);
                sendResultMessage(ranks);
            }
        };

        inaraRetrofit.getProfile(buildRequestBody()).enqueue(callback);
    }

    @Override
    public void getCommanderPosition() {

    }

    @Override
    public void getCredits() {

    }
}
