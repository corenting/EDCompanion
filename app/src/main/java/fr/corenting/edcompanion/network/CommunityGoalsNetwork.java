package fr.corenting.edcompanion.network;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommunityGoal;

public class CommunityGoalsNetwork {
    public static void getCommunityGoals(final View v) {
        Ion.with(v.getContext())
                .load(v.getContext().getString(R.string.api_url))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null && result != null && result.get("success").getAsBoolean()) {
                            for(JsonElement elt : result.get("goals").getAsJsonArray()) {
                                JsonObject currentGoal = elt.getAsJsonObject();
                                CommunityGoal newCg = new CommunityGoal();

                                newCg.setTitle(currentGoal.get("title").getAsString());

                                EventBus.getDefault().post(newCg);
                            }
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(v, R.string.download_error, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }
}
