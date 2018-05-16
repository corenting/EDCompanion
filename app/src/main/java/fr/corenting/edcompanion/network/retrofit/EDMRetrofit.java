package fr.corenting.edcompanion.network.retrofit;

import fr.corenting.edcompanion.models.apis.EDM.CommodityFinderResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EDMRetrofit {

    @GET("v1/buy-near/byname/{system}/{commodity}")
    Call<CommodityFinderResponse> findCommodity(@Path("system") String system,
                                                @Path("commodity") String commodity,
                                                @Query("pad") String minLandingPad,
                                                @Query("stock") int stock);
}
