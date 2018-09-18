package fr.corenting.edcompanion.network.retrofit;

import java.util.List;

import fr.corenting.edcompanion.models.apis.EDApi.CommodityFinderResponse;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityResponse;
import fr.corenting.edcompanion.models.apis.EDApi.CommunityGoalsResponse;
import fr.corenting.edcompanion.models.apis.EDApi.DistanceResponse;
import fr.corenting.edcompanion.models.apis.EDApi.GalnetArticleResponse;
import fr.corenting.edcompanion.models.apis.EDApi.ShipFinderResponse;
import fr.corenting.edcompanion.models.apis.EDApi.ShipResponse;
import fr.corenting.edcompanion.models.apis.EDApi.SystemHistoryResponse;
import fr.corenting.edcompanion.models.apis.EDApi.SystemResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EDApiRetrofit {
    @GET("v2/community_goals/")
    Call<CommunityGoalsResponse> getCommunityGoals();

    @GET("galnet/")
    Call<List<GalnetArticleResponse>> getGalnetNews();

    @GET("distance/{first}/{second}")
    Call<DistanceResponse> getDistance(@Path("first") String firstSystem,
                                       @Path("second") String secondSystem);

    @GET("ships")
    Call<List<ShipResponse>> getShips(@Query("name") String shipName);

    @GET("commodities")
    Call<List<CommodityResponse>> getCommodities(@Query("name") String filter);

    @GET("{system}/stations/ships/{ship}")
    Call<List<ShipFinderResponse>> findShip(@Path("system") String system,
                                            @Path("ship") String ship);

    @GET("system/{system}")
    Call<SystemResponse> getSystemDetails(@Path("system") String system);

    @GET("system/{system}/history")
    Call<List<SystemHistoryResponse>> getSystemHistory(@Path("system") String system);

    @GET("{system}/stations/commodities/{commodity}")
    Call<List<CommodityFinderResponse>> findCommodity(@Path("system") String system,
                                                      @Path("commodity") String commodity,
                                                      @Query("pad") String minLandingPad,
                                                      @Query("stock") int stock);
}
