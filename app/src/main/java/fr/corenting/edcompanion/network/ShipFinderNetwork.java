package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.ShipFinderResult;
import fr.corenting.edcompanion.models.apis.EDAPIV4.ShipFinderResponse;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.network.retrofit.EDApiV4Retrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;


public class ShipFinderNetwork {
    public static void findShip(Context ctx, String system, String ship) {
        EDApiV4Retrofit retrofit = RetrofitSingleton.getInstance()
                .getEdApiV4Retrofit(ctx.getApplicationContext());

        retrofit2.Callback<List<ShipFinderResponse>> callback = new retrofit2.Callback<List<ShipFinderResponse>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<ShipFinderResponse>> call,
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
                                new ArrayList<>());
                    }
                    EventBus.getDefault().post(convertedResults);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<ShipFinderResponse>> call,
                                  Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<ShipFinderResult>()));
            }
        };

        retrofit.findShip(system, ship).enqueue(callback);
    }
}
