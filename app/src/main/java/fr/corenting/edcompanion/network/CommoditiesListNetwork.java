package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.CommoditiesListResult;
import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.SystemFinderResult;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityFinderResponse;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityResponse;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystem;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;


public class CommoditiesListNetwork {
    public static void findCommodity(Context ctx, String commodityName) {

        // Init retrofit instance
        final EDApiRetrofit edApiRetrofit = RetrofitSingleton.getInstance()
                .getEdApiRetrofit(ctx.getApplicationContext());

        final retrofit2.Callback<List<CommodityResponse>> callback = new retrofit2.Callback<List<CommodityResponse>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<CommodityResponse>> call,
                                   retrofit2.Response<List<CommodityResponse>> response) {

                List<CommodityResponse> body = response.body();
                ResultsList<CommoditiesListResult> convertedResults;
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    List<CommoditiesListResult> resultsList = new ArrayList<>();
                    try {
                        for (CommodityResponse resultItem : body) {
                            resultsList.add(
                                    CommoditiesListResult.Companion.fromEDApiCommodity(resultItem)
                            );
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
            public void onFailure(Call<List<CommodityResponse>> call, Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<SystemFinderResult>()));
            }
        };

        edApiRetrofit.getCommodities(commodityName).enqueue(callback);
    }
}
