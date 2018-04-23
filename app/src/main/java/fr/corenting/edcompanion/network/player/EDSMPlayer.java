package fr.corenting.edcompanion.network.player;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommanderPosition;
import fr.corenting.edcompanion.models.Credits;
import fr.corenting.edcompanion.models.Ranks;
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
                    Ranks ranks = new Ranks();
                    try {
                        // Combat
                        ranks.combat.name = body.ranksNames.combat;
                        ranks.combat.progress = body.progress.combat;
                        ranks.combat.value = body.ranks.combat;

                        // Trade
                        ranks.trade.name = body.ranksNames.trade;
                        ranks.trade.progress = body.progress.trade;
                        ranks.trade.value = body.ranks.trade;

                        // Explore
                        ranks.explore.name = body.ranksNames.explore;
                        ranks.explore.progress = body.progress.explore;
                        ranks.explore.value = body.ranks.explore;

                        // CQC
                        ranks.cqc.name = body.ranksNames.cqc;
                        ranks.cqc.progress = body.progress.cqc;
                        ranks.cqc.value = body.ranks.cqc;

                        // Federation
                        ranks.federation.name = body.ranksNames.federation;
                        ranks.federation.progress = body.progress.federation;
                        ranks.federation.value = body.ranks.federation;

                        // Empire
                        ranks.empire.name = body.ranksNames.empire;
                        ranks.empire.progress = body.progress.empire;
                        ranks.empire.value = body.ranks.empire;

                        ranks.Success = true;

                    } catch (Exception ex) {
                        ranks.Success = false;
                    }
                    sendResultMessage(ranks);
                }
            }

            @Override
            public void onFailure(Call<EDSMRanks> call, Throwable t) {
                Ranks ranks = new Ranks();
                ranks.Success = false;

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
                    CommanderPosition pos = new CommanderPosition();
                    try {
                        pos.SystemName = body.system;
                        pos.FirstDiscover = body.firstDiscover;

                        // Send to bus
                        pos.Success = true;
                    } catch (Exception ex) {
                        pos.Success = false;
                    }
                    sendResultMessage(pos);
                }
            }

            @Override
            public void onFailure(Call<EDSMPosition> call, Throwable t) {
                CommanderPosition pos = new CommanderPosition();
                pos.Success = false;
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
                    Credits res = new Credits();
                    try {
                        res.Balance = body.credits.get(0).balance;
                        res.Loan = body.credits.get(0).loan;
                        res.Success = true;
                    } catch (Exception e) {
                        res.Success = false;
                    }
                    sendResultMessage(res);
                }
            }

            @Override
            public void onFailure(Call<EDSMCredits> call, Throwable t) {
                Credits res = new Credits();
                res.Success = false;
                sendResultMessage(res);
            }
        };

        edsmRetrofit.getCredits(apiKey, commanderName).enqueue(callback);
    }
}
