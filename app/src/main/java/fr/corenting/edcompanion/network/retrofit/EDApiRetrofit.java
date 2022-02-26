package fr.corenting.edcompanion.network.retrofit;

import java.util.List;

import fr.corenting.edcompanion.models.apis.EDApi.CommodityDetailsResponse;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityFinderResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EDApiRetrofit {
    @GET("commodities/{name}")
    Call<CommodityDetailsResponse> getCommodityDetails(@Path("name") String name);

    @GET("commodity_finder/")
    Call<List<CommodityFinderResponse>> findCommodity(@Query("referenceSystem") String system,
                                                      @Query("commodityName") String commodity,
                                                      @Query("pad") String minLandingPad,
                                                      @Query("stock") int stock,
                                                      @Query("demand") int demand,
                                                      @Query("selling") boolean sellingMode);
}
