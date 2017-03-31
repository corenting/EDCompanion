package fr.corenting.edcompanion.network;

import android.support.design.widget.Snackbar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.fragments.CommunityGoalsFragment;
import fr.corenting.edcompanion.models.CommunityGoal;

public class CommunityGoalsNetwork {
    public static void getCommunityGoals(final CommunityGoalsFragment fragment) {
        Ion.with(fragment)
                .load(fragment.getString(R.string.api_url))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                            try {
                                if (e != null || result == null)
                                {
                                    throw new Exception();
                                }
                                int count = result.get("goals").getAsJsonArray().size();
                                for (JsonElement elt : result.get("goals").getAsJsonArray()) {
                                    JsonObject currentGoal = elt.getAsJsonObject();
                                    CommunityGoal newCg = new CommunityGoal();

                                    newCg.setTitle(currentGoal.get("title").getAsString());

                                    EventBus.getDefault().post(newCg);
                                }
                                fragment.endLoading(count);
                            }
                            catch (Exception ex)
                            {
                                fragment.endLoading(0);
                                Snackbar snackbar = Snackbar
                                        .make(fragment.getActivity().findViewById(android.R.id.content),
                                                R.string.download_error, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                    }
                });
    }
}
