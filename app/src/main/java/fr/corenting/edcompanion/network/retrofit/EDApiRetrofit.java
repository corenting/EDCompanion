package fr.corenting.edcompanion.network.retrofit;

import java.util.List;

import fr.corenting.edcompanion.models.apis.EDApi.CommunityGoalsResponse;
import fr.corenting.edcompanion.models.apis.EDApi.DistanceResponse;
import fr.corenting.edcompanion.models.apis.EDApi.GalnetArticleResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EDApiRetrofit {
    @GET("v2/community_goals/")
    Call<CommunityGoalsResponse>getCommunityGoals();

    @GET("galnet/")
    Call<List<GalnetArticleResponse>> getGalnetNews();

    @GET("distance/{first}/{second}")
    Call<DistanceResponse> getDistance(@Path("first") String firstSystem,
                                       @Path("second") String secondSystem);
}
