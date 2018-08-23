package fr.corenting.edcompanion.network.player;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.events.CommanderPosition;
import fr.corenting.edcompanion.models.events.Credits;
import fr.corenting.edcompanion.models.events.Ranks;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMCredits;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMPosition;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMRanks;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import fr.corenting.edcompanion.utils.SettingsUtils;
import retrofit2.Call;


public class EDSMPlayer extends PlayerNetwork {

    private Context context;
    private EDSMRetrofit edsmRetrofit;

    private String apiKey;
    private String commanderName;

    public EDSMPlayer(Context context) {

        this.context = context;
        apiKey = SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_password));
        commanderName = SettingsUtils.getString(context, context.getString(R.string.settings_cmdr_username));
        edsmRetrofit = RetrofitUtils.getEDSMRetrofit(context);
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
        retrofit2.Callback<EDSMRanks> callback = new retrofit2.Callback<EDSMRanks>() {
            @Override
            public void onResponse(Call<EDSMRanks> call, retrofit2.Response<EDSMRanks> response) {
                EDSMRanks body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    Ranks ranks;
                    try {
                        // Combat
                        Ranks.Rank combatRank = new Ranks.Rank(body.ranksNames.combat,
                                body.ranks.combat, body.progress.combat);

                        // Trade
                        Ranks.Rank tradeRank = new Ranks.Rank(body.ranksNames.trade,
                                body.ranks.trade, body.progress.trade);

                        // Explore
                        Ranks.Rank exploreRank = new Ranks.Rank(body.ranksNames.explore,
                                body.ranks.explore, body.progress.explore);

                        // CQC
                        Ranks.Rank cqcRank = new Ranks.Rank(body.ranksNames.cqc,
                                body.ranks.cqc, body.progress.cqc);

                        // Federation
                        Ranks.Rank federationRank = new Ranks.Rank(body.ranksNames.federation,
                                body.ranks.federation, body.progress.federation);

                        // Empire
                        Ranks.Rank empireRank = new Ranks.Rank(body.ranksNames.empire,
                                body.ranks.empire, body.progress.empire);

                        ranks = new Ranks(true, combatRank, tradeRank, exploreRank,
                                cqcRank, federationRank, empireRank);

                    } catch (Exception ex) {
                        ranks = new Ranks(false, null, null,
                                null, null, null, null);
                    }
                    sendResultMessage(ranks);
                }
            }

            @Override
            public void onFailure(Call<EDSMRanks> call, Throwable t) {
                Ranks ranks = new Ranks(false, null, null,
                        null, null, null, null);

                sendResultMessage(ranks);
            }
        };
        edsmRetrofit.getRanks(apiKey, commanderName).enqueue(callback);
    }

    @Override
    public void getCommanderPosition() {
        retrofit2.Callback<EDSMPosition> callback = new retrofit2.Callback<EDSMPosition>() {
            @Override
            public void onResponse(Call<EDSMPosition> call, retrofit2.Response<EDSMPosition> response) {
                EDSMPosition body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    CommanderPosition pos;
                    try {
                        pos = new CommanderPosition(true, body.system, body.firstDiscover);
                    } catch (Exception ex) {
                        pos = new CommanderPosition(false, "",
                                false);
                    }
                    sendResultMessage(pos);
                }
            }

            @Override
            public void onFailure(Call<EDSMPosition> call, Throwable t) {
                CommanderPosition pos = new CommanderPosition(false,
                        "", false);
                sendResultMessage(pos);
            }
        };
        edsmRetrofit.getPosition(apiKey, commanderName).enqueue(callback);
    }

    @Override
    public void getCredits() {
        retrofit2.Callback<EDSMCredits> callback = new retrofit2.Callback<EDSMCredits>() {
            @Override
            public void onResponse(Call<EDSMCredits> call, retrofit2.Response<EDSMCredits> response) {
                EDSMCredits body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    Credits res;
                    try {
                        res = new Credits(true, body.credits.get(0).balance,
                                body.credits.get(0).loan);
                    } catch (Exception e) {
                        res = new Credits(false, 0, 0);
                    }
                    sendResultMessage(res);
                }
            }

            @Override
            public void onFailure(Call<EDSMCredits> call, Throwable t) {
                Credits res = new Credits(false, 0, 0);
                sendResultMessage(res);
            }
        };

        edsmRetrofit.getCredits(apiKey, commanderName).enqueue(callback);
    }
}
