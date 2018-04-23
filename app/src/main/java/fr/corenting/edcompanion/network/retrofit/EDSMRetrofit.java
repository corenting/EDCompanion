package fr.corenting.edcompanion.network.retrofit;

import java.util.List;

import fr.corenting.edcompanion.models.apis.EDSM.EDSMCredits;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMPosition;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMRanks;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMServerStatus;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystem;
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

    @GET("api-commander-v1/get-ranks")
    Call<EDSMRanks> getRanks(@Query("apiKey") String apiKey,
                               @Query("commanderName") String commanderName);

    @GET("api-status-v1/elite-server")
    Call<EDSMServerStatus> getServerStatus();

    @GET("api-v1/systems")
    Call<List<EDSMSystem>> getSystems(@Query("systemName") String systemName, @Query("showId") int showId);
}
