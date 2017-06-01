package fr.corenting.edcompanion.network;

import android.support.design.widget.Snackbar;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.StatusFragment;
import fr.corenting.edcompanion.models.Credits;
import fr.corenting.edcompanion.utils.SettingsUtils;


public class StatusNetwork {
    public static  void getAll(final StatusFragment fragment)
    {
        getCredits(fragment);
/*
        if (!SettingsUtils.hasValidCmdrParameters(fragment.getActivity())) {
            Snackbar snackbar = Snackbar
                    .make(fragment.getActivity().findViewById(android.R.id.content),
                            R.string.commander_error, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else {
            getCredits(fragment);
            //TODO getRanks(fragment);
        }
        */
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
                            fragment.endLoading();
                        } catch (Exception ex) {
                            fragment.endLoading();
                            Snackbar snackbar = Snackbar
                                    .make(fragment.getActivity().findViewById(android.R.id.content),
                                            R.string.download_error, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }
}
