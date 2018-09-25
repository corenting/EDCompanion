package fr.corenting.edcompanion.network;

import android.content.Context;
import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.ShipFinderResult;
import fr.corenting.edcompanion.models.apis.EDApi.ShipFinderResponse;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;


public class ShipFinderNetwork {
    public static void findShip(Context ctx, String system, String ship) {
        EDApiRetrofit retrofit = RetrofitSingleton.getInstance()
                .getEdApiRetrofit(ctx.getApplicationContext());

        retrofit2.Callback<List<ShipFinderResponse>> callback = new retrofit2.Callback<List<ShipFinderResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ShipFinderResponse>> call,
                                   retrofit2.Response<List<ShipFinderResponse>> response) {
                List<ShipFinderResponse> body = response.body();
                ResultsList<ShipFinderResult> convertedResults;
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    List<ShipFinderResult> resultsList = new ArrayList<>();
                    try {
                        for (ShipFinderResponse resultItem : body) {
                            resultsList.add(
                                    ShipFinderResult.Companion.fromShipFinderResponse(resultItem));
                        }
                        convertedResults = new ResultsList<>(true, resultsList);

                    } catch (Exception ex) {
                        convertedResults = new ResultsList<>(false,
                                new ArrayList<ShipFinderResult>());
                    }
                    EventBus.getDefault().post(convertedResults);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ShipFinderResponse>> call,
                                  @NonNull Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<ShipFinderResult>()));
            }
        };

        retrofit.findShip(system, ship).enqueue(callback);
    }
}
