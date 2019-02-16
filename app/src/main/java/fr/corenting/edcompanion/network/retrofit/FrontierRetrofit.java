package fr.corenting.edcompanion.network.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FrontierRetrofit {
    @GET("profile")
    Call<ResponseBody> getProfile();
}
