package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.models.apis.EDApi.DistanceResponse;
import fr.corenting.edcompanion.models.events.DistanceSearch;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;


public class DistanceCalculatorNetwork {
    public static void getDistance(Context ctx, String firstSystem, String secondSystem) {
        EDApiRetrofit retrofit = RetrofitSingleton.getInstance()
                .getEdApiRetrofit(ctx.getApplicationContext());

        retrofit2.Callback<DistanceResponse> callback = new retrofit2.Callback<DistanceResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<DistanceResponse> call,
                                   retrofit2.Response<DistanceResponse> response) {
                DistanceResponse body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    DistanceSearch distanceSearch;
                    try {
                        distanceSearch = new DistanceSearch(true, body.Distance, body.FromSystem.Name,
                                body.ToSystem.Name, body.FromSystem.PermitRequired, body.ToSystem.PermitRequired);
                    } catch (Exception ex) {
                        distanceSearch = new DistanceSearch(false, 0, "",
                                "", false, false);
                    }
                    EventBus.getDefault().post(distanceSearch);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<DistanceResponse> call, Throwable t) {
                DistanceSearch distanceSearch = new DistanceSearch(false, 0, "",
                        "", false, false);
                EventBus.getDefault().post(distanceSearch);
            }
        };
        retrofit.getDistance(firstSystem, secondSystem).enqueue(callback);
    }
}
