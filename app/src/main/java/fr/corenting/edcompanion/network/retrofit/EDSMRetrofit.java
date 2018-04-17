package fr.corenting.edcompanion.network.retrofit;

import fr.corenting.edcompanion.models.apis.EDSM.EDSMCredits;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMPosition;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EDSMRetrofit {
    @GET("api-logs-v1/get-position")
    Call<EDSMPosition> getPosition(@Query("apiKey") String apiKey,
                                   @Query("commanderName") String commanderName);

    @GET("api-commander-v1/get-credits")
    Call<EDSMCredits> getCredits(@Query("apiKey") String apiKey,
                                 @Query("commanderName") String commanderName);
}
