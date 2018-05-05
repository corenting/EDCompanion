package fr.corenting.edcompanion.network.retrofit;

import fr.corenting.edcompanion.models.apis.EDApi.CommunityGoalsResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EDApiRetrofit {
    @GET("v2/community_goals/")
    Call<CommunityGoalsResponse> getCommunityGoals();
}
