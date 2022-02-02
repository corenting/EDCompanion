package fr.corenting.edcompanion.network.retrofit;

import java.util.List;

import fr.corenting.edcompanion.models.apis.EDApi.CommodityDetailsResponse;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityFinderResponse;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityResponse;
import fr.corenting.edcompanion.models.apis.EDApi.ShipFinderResponse;
import fr.corenting.edcompanion.models.apis.EDApi.ShipResponse;
import fr.corenting.edcompanion.models.apis.EDApi.SystemHistoryResponse;
import fr.corenting.edcompanion.models.apis.EDApi.SystemResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EDApiRetrofit {


    @GET("ships/")
    Call<List<ShipResponse>> getShips(@Query("name") String shipName);

    @GET("commodities/")
    Call<List<CommodityResponse>> getCommodities(@Query("name") String filter);

    @GET("commodities/{name}")
    Call<CommodityDetailsResponse> getCommodityDetails(@Path("name") String name);

    @GET("ship_finder/")
    Call<List<ShipFinderResponse>> findShip(@Query("referenceSystem") String system,
                                            @Query("ship") String ship);

    @GET("system/{system}")
    Call<SystemResponse> getSystemDetails(@Path("system") String system);

    @GET("system/{system}/history")
    Call<List<SystemHistoryResponse>> getSystemHistory(@Path("system") String system);

    @GET("commodity_finder/")
    Call<List<CommodityFinderResponse>> findCommodity(@Query("referenceSystem") String system,
                                                      @Query("commodityName") String commodity,
                                                      @Query("pad") String minLandingPad,
                                                      @Query("stock") int stock,
                                                      @Query("demand") int demand,
                                                      @Query("selling") boolean sellingMode);
}
