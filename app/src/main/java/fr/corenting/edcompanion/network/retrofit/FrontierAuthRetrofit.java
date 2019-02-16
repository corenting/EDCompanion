package fr.corenting.edcompanion.network.retrofit;

import fr.corenting.edcompanion.models.apis.FrontierAuth.FrontierAccessTokenRequestBody;
import fr.corenting.edcompanion.models.apis.FrontierAuth.FrontierAccessTokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FrontierAuthRetrofit {
    @POST("token")
    Call<FrontierAccessTokenResponse> getAccessToken(@Body FrontierAccessTokenRequestBody body);
}
