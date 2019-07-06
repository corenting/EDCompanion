package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.models.apis.EDApi.CommunityGoalsResponse;
import fr.corenting.edcompanion.models.events.CommunityGoals;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;

public class CommunityGoalsNetwork {
    public static void getCommunityGoals(Context ctx) {

        EDApiRetrofit retrofit = RetrofitSingleton.getInstance()
                .getEdApiRetrofit(ctx.getApplicationContext());
        retrofit2.Callback<CommunityGoalsResponse> callback = new retrofit2.Callback<CommunityGoalsResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<CommunityGoalsResponse> call,
                                   retrofit2.Response<CommunityGoalsResponse> response) {
                CommunityGoalsResponse body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    CommunityGoals goals;
                    List<CommunityGoal> goalsList = new ArrayList<>();
                    try {
                        for (CommunityGoalsResponse.CommunityGoalsItemResponse goal : body.Goals) {
                            goalsList.add(
                                    CommunityGoal.Companion.fromCommunityGoalsItemResponse(goal));
                        }
                        goals = new CommunityGoals(true, goalsList);
                    } catch (Exception e) {
                        goals = new CommunityGoals(false, new ArrayList<>());
                    }
                    EventBus.getDefault().post(goals);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<CommunityGoalsResponse> call, Throwable t) {
                CommunityGoals goals = new CommunityGoals(false,
                        new ArrayList<CommunityGoal>());
                EventBus.getDefault().post(goals);
            }
        };

        retrofit.getCommunityGoals().enqueue(callback);
    }
}
