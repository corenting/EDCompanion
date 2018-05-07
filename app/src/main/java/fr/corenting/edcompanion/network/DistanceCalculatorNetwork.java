package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.models.Distance;
import fr.corenting.edcompanion.models.apis.EDApi.DistanceResponse;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;


public class DistanceCalculatorNetwork {
    public static void getDistance(Context ctx, String firstSystem, String secondSystem) {
        EDApiRetrofit retrofit = RetrofitUtils.getEdApiRetrofit(ctx);

        retrofit2.Callback<DistanceResponse> callback = new retrofit2.Callback<DistanceResponse>() {
            @Override
            public void onResponse(Call<DistanceResponse> call, retrofit2.Response<DistanceResponse> response) {
                DistanceResponse body = response.body();
                if (!response.isSuccessful() || body == null)
                {
                    onFailure(call, new Exception("Invalid response"));
                }
                else
                {
                    Distance distance;
                    try {
                        distance = new Distance(true, body.Distance, body.FromSystem.Name,
                                body.ToSystem.Name, body.FromSystem.PermitRequired, body.ToSystem.PermitRequired);
                    } catch (Exception ex) {
                        distance = new Distance(false, 0, null,
                                null, false, false);
                    }
                    EventBus.getDefault().post(distance);
                }
            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {
                Distance distance = new Distance(false, 0, null,
                        null, false, false);
                EventBus.getDefault().post(distance);
            }
        };
        retrofit.getDistance(firstSystem, secondSystem).enqueue(callback);
    }
}
