package fr.corenting.edcompanion.network;

import android.content.Context;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Callback;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.models.CommunityGoals;

public class CommunityGoalsNetwork {
    public static void getCommunityGoals(Context ctx) {
        String url = ctx.getString(R.string.edapi_base) + ctx.getString(R.string.edapi_cg);
        Bridge.get(url)
                .request(new Callback() {
                    @Override
                    public void response(Request request, Response response, BridgeException e) {
                        try {
                            if (e != null) {
                                throw new Exception();
                            }
                            JsonObject json = new JsonParser().parse(response.asString()).getAsJsonObject();

                            List<CommunityGoal> goalsList = new LinkedList<>();
                            for (JsonElement elt : json.get("goals").getAsJsonArray()) {
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
                                newCg.setEndDate(datesObject.get("end").getAsString());
                                newCg.setRefreshDate(datesObject.get("last_update").getAsString());

                                // Location
                                JsonObject locationSystem = goal.get("location").getAsJsonObject();
                                newCg.setStation(locationSystem.get("station").getAsString());
                                newCg.setSystem(locationSystem.get("system").getAsString());

                                goalsList.add(newCg);
                            }
                            CommunityGoals goals = new CommunityGoals(true, goalsList);
                            EventBus.getDefault().post(goals);

                        } catch (Exception ex) {
                            CommunityGoals goals = new CommunityGoals(false, null);
                            EventBus.getDefault().post(goals);
                        }
                    }
                });
    }
}
