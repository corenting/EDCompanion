package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.CommunityGoal;
import fr.corenting.edcompanion.models.apis.EDAPIV4.CommunityGoalsResponse;
import fr.corenting.edcompanion.models.events.CommunityGoals;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDApiV4Retrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;

public class CommunityGoalsNetwork {
    public static void getCommunityGoals(Context ctx) {

        EDApiV4Retrofit retrofit = RetrofitSingleton.getInstance()
                .getEdApiV4Retrofit(ctx.getApplicationContext());
        retrofit2.Callback<List<CommunityGoalsResponse>> callback = new retrofit2.Callback<List<CommunityGoalsResponse>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<CommunityGoalsResponse>> call,
                                   retrofit2.Response<List<CommunityGoalsResponse>> response) {
                List<CommunityGoalsResponse> body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    CommunityGoals goalsEvent;
                    List<CommunityGoal> goalsList = new ArrayList<>();
                    try {
                        for (CommunityGoalsResponse goal : body) {
                            goalsList.add(CommunityGoal.Companion.fromCommunityGoalsItemResponse(goal));
                        }
                        goalsEvent = new CommunityGoals(true, goalsList);
                    } catch (Exception e) {
                        goalsEvent = new CommunityGoals(false, new ArrayList<>());
                    }
                    EventBus.getDefault().post(goalsEvent);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<CommunityGoalsResponse>> call, Throwable t) {
                CommunityGoals goals = new CommunityGoals(false,
                        new ArrayList<>());
                EventBus.getDefault().post(goals);
            }
        };

        retrofit.getCommunityGoals().enqueue(callback);
    }
}
