package fr.corenting.edcompanion.network.retrofit;

import java.util.List;

import fr.corenting.edcompanion.models.apis.EDSM.EDSMCreditsResponse;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMPositionResponse;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMRanksResponse;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMServerStatusResponse;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystemResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EDSMRetrofit {
    @GET("api-logs-v1/get-position")
    Call<EDSMPositionResponse> getPosition(@Query("apiKey") String apiKey,
                                           @Query("commanderName") String commanderName);

    @GET("api-commander-v1/get-credits")
    Call<EDSMCreditsResponse> getCredits(@Query("apiKey") String apiKey,
                                         @Query("commanderName") String commanderName);

    @GET("api-commander-v1/get-ranks")
    Call<EDSMRanksResponse> getRanks(@Query("apiKey") String apiKey,
                                     @Query("commanderName") String commanderName);

    @GET("api-status-v1/elite-server")
    Call<EDSMServerStatusResponse> getServerStatus();

    @GET("api-v1/systems")
    Call<List<EDSMSystemResponse>> getSystems(@Query("systemName") String systemName,
                                              @Query("showId") int showId,
                                              @Query("showInformation") int showInformation,
                                              @Query("showPermit") int showPermit);
}
