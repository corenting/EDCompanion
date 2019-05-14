package fr.corenting.edcompanion.network.retrofit;

import fr.corenting.edcompanion.models.apis.Frontier.FrontierProfileResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FrontierRetrofit {
    @GET("profile")
    Call<ResponseBody> getProfileRaw();

    @GET("profile")
    Call<FrontierProfileResponse> getProfile();
}
