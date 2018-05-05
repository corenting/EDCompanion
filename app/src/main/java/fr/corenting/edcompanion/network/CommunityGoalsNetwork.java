package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.models.CommunityGoals;
import fr.corenting.edcompanion.models.apis.EDApi.CommunityGoalsResponse;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;

public class CommunityGoalsNetwork {
    public static void getCommunityGoals(Context ctx) {

        EDApiRetrofit retrofit = RetrofitUtils.getEdApiRetrofit(ctx);
        retrofit2.Callback<CommunityGoalsResponse> callback = new retrofit2.Callback<CommunityGoalsResponse>() {
            @Override
            public void onResponse(Call<CommunityGoalsResponse> call, retrofit2.Response<CommunityGoalsResponse> response) {
                CommunityGoalsResponse body = response.body();
                if (response.body() == null)
                {
                    onFailure(call, new Exception("Invalid response"));
                }
                else
                {
                    CommunityGoals goals;
                    List<CommunityGoal> goalsList = new LinkedList<>();
                    try {
                        for (CommunityGoalsResponse.CommunityGoalsItemResponse goal : body.Goals) {
                            CommunityGoal newCg = new CommunityGoal();

                            // Main informations
                            newCg.setTitle(goal.Title);
                            newCg.setDescription(goal.Description);
                            newCg.setReward(goal.Reward);
                            newCg.setObjective(goal.Objective);
                            newCg.setContributors(goal.Contributors);
                            newCg.setOngoing(goal.Ongoing);

                            // Tiers
                            newCg.setCurrentTier(goal.TierProgress.Current);
                            newCg.setTotalTier(goal.TierProgress.Total);

                            // Date
                            newCg.setEndDate(goal.Date.End);
                            newCg.setRefreshDate(goal.Date.LastUpdate);

                            // Location
                            newCg.setStation(goal.Location.Station);
                            newCg.setSystem(goal.Location.System);

                            goalsList.add(newCg);
                        }
                        goals = new CommunityGoals(true, goalsList);
                    }
                    catch (Exception e)
                    {
                        goals = new CommunityGoals(false, null);
                    }
                    EventBus.getDefault().post(goals);
                }
            }

            @Override
            public void onFailure(Call<CommunityGoalsResponse> call, Throwable t) {
                CommunityGoals goals = new CommunityGoals(false, null);
                EventBus.getDefault().post(goals);
            }
        };

        retrofit.getCommunityGoals().enqueue(callback);
    }
}
