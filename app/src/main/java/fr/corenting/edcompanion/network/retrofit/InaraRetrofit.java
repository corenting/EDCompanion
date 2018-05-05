package fr.corenting.edcompanion.network.retrofit;

import fr.corenting.edcompanion.models.apis.Inara.InaraProfileResponse;
import fr.corenting.edcompanion.models.apis.Inara.InaraProfileRequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InaraRetrofit {
    @POST("inapi/v1/")
    Call<InaraProfileResponse> getProfile(@Body InaraProfileRequestBody body);
}
