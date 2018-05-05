package fr.corenting.edcompanion.network.player;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;

import java.util.LinkedList;

import fr.corenting.edcompanion.BuildConfig;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.Ranks;
import fr.corenting.edcompanion.models.apis.Inara.InaraProfileResponse;
import fr.corenting.edcompanion.models.apis.Inara.InaraProfileRequestBody;
import fr.corenting.edcompanion.network.retrofit.InaraRetrofit;
import fr.corenting.edcompanion.utils.DateUtils;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;
import retrofit2.Call;


public class InaraPlayer extends PlayerNetwork {

    private Context context;
    private InaraRetrofit inaraRetrofit;

    private String commanderName;

    public InaraPlayer(Context context) {

        this.context = context;
        commanderName = SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_username));

        inaraRetrofit = RetrofitUtils.getInaraRetrofit(context);
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
        res.events = new LinkedList<>();
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
            public void onResponse(Call<InaraProfileResponse> call, retrofit2.Response<InaraProfileResponse> response) {
                InaraProfileResponse body = response.body();
                if (!response.isSuccessful() || body == null || body.events == null ||
                        body.events.size() != 1) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    Ranks ranks = new Ranks();
                    try {

                        for (InaraProfileResponse.InaraProfileInnerResponse.InaraProfileResponseRanks rank :
                                body.events.get(0).EventData.CommanderRanksPilot) {
                            switch (rank.RankName) {
                                case "combat":
                                    ranks.combat.progress = (int) (rank.RankProgress * 100);
                                    ranks.combat.value = rank.RankValue;
                                    ranks.combat.name = context.getResources().getStringArray(R.array.ranks_combat)[ranks.combat.value];
                                    break;
                                case "trade":
                                    ranks.trade.progress = (int) (rank.RankProgress * 100);
                                    ranks.trade.value = rank.RankValue;
                                    ranks.trade.name = context.getResources().getStringArray(R.array.ranks_trade)[ranks.trade.value];
                                    break;
                                case "exploration":
                                    ranks.explore.progress = (int) (rank.RankProgress * 100);
                                    ranks.explore.value = rank.RankValue;
                                    ranks.explore.name = context.getResources().getStringArray(R.array.ranks_explorer)[ranks.explore.value];
                                    break;
                                case "cqc":
                                    ranks.cqc.progress = (int) (rank.RankProgress * 100);
                                    ranks.cqc.value = rank.RankValue;
                                    ranks.cqc.name = context.getResources().getStringArray(R.array.ranks_cqc)[ranks.cqc.value];
                                    break;
                                case "empire":
                                    ranks.empire.progress = (int) (rank.RankProgress * 100);
                                    ranks.empire.value = rank.RankValue;
                                    ranks.empire.name = context.getResources().getStringArray(R.array.ranks_empire)[ranks.empire.value];
                                    break;
                                case "federation":
                                    ranks.federation.progress = (int) (rank.RankProgress * 100);
                                    ranks.federation.value = rank.RankValue;
                                    ranks.federation.name = context.getResources().getStringArray(R.array.ranks_federation)[ranks.federation.value];
                                    break;
                            }
                        }

                        ranks.Success = true;
                    } catch (Exception e) {
                        ranks.Success = false;
                    }
                    sendResultMessage(ranks);
                }
            }

            @Override
            public void onFailure(Call<InaraProfileResponse> call, Throwable t) {
                Ranks ranks = new Ranks();
                ranks.Success = false;
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
