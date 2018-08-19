package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.threeten.bp.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.ResultsList;
import fr.corenting.edcompanion.models.ShipFinderResult;
import fr.corenting.edcompanion.models.apis.EDApi.ShipFinderResponse;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;


public class ShipFinderNetwork {
    public static void findShip(Context ctx, String system, String ship) {
        EDApiRetrofit retrofit = RetrofitUtils.getEdApiRetrofit(ctx);

        retrofit2.Callback<List<ShipFinderResponse>> callback = new retrofit2.Callback<List<ShipFinderResponse>>() {
            @Override
            public void onResponse(Call<List<ShipFinderResponse>> call, retrofit2.Response<List<ShipFinderResponse>> response) {
                List<ShipFinderResponse> body = response.body();
                ResultsList<ShipFinderResult> convertedResults;
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    List<ShipFinderResult> resultsList = new ArrayList<>();
                    try {
                        for (ShipFinderResponse resultItem : body) {
                            ShipFinderResult convertedItem = new ShipFinderResult();
                            convertedItem.Distance = resultItem.Distance;
                            convertedItem.DistanceToStar = resultItem.Station.DistanceToStar;
                            convertedItem.LastShipyardUpdate = DateTimeUtils.toInstant(resultItem.Station.LastShipyardUpdate);
                            convertedItem.MaxLandingPad = resultItem.Station.MaxLandingPad;
                            convertedItem.StationName = resultItem.Station.Name;
                            convertedItem.SystemName = resultItem.Station.System.Name;
                            convertedItem.SystemPermitRequired = resultItem.Station.System.PermitRequired;
                            convertedItem.IsPlanetary = resultItem.Station.IsPlanetary;

                            resultsList.add(convertedItem);
                        }
                        convertedResults = new ResultsList<>(true, resultsList);

                    } catch (Exception ex) {
                        convertedResults = new ResultsList<>(false, new ArrayList<ShipFinderResult>());
                    }
                    EventBus.getDefault().post(convertedResults);
                }
            }

            @Override
            public void onFailure(Call<List<ShipFinderResponse>> call, Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<ShipFinderResult>()));
            }
        };

        retrofit.findShip(system, ship).enqueue(callback);
    }
}
