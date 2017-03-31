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
                                    JsonObject goal = elt.getAsJsonObject();
                                    CommunityGoal newCg = new CommunityGoal();

                                    // Main informations
                                    newCg.setTitle(goal.get("title").getAsString());
                                    newCg.setDescription(goal.get("description").getAsString());
                                    newCg.setReward(goal.get("reward").getAsString());
                                    newCg.setObjective(goal.get("objective").getAsString());
                                    newCg.setContributors(goal.get("contributors").getAsInt());
                                    newCg.setOngoing(goal.get("ongoing").getAsBoolean());

                                    // Tiers
                                    JsonObject tiersObject = goal.get("tier_progress").getAsJsonObject();
                                    newCg.setCurrentTier(tiersObject.get("current").getAsInt());
                                    newCg.setTotalTier(tiersObject.get("total").getAsInt());

                                    // Date
                                    JsonObject datesObject = goal.get("date").getAsJsonObject();
                                    newCg.setStartDate(datesObject.get("start").getAsString());
                                    newCg.setEndDate(datesObject.get("end").getAsString());
                                    newCg.setRefreshDate(datesObject.get("last_update").getAsString());

                                    // Location
                                    JsonObject locationSystem = goal.get("location").getAsJsonObject();
                                    newCg.setStation(locationSystem.get("station").getAsString());
                                    newCg.setSystem(locationSystem.get("system").getAsString());

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
