package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.CommoditiesListResult;
import fr.corenting.edcompanion.models.CommodityBestPricesStationResult;
import fr.corenting.edcompanion.models.CommodityDetailsResult;
import fr.corenting.edcompanion.models.apis.EDAPIV4.CommodityBestPricesResponse;
import fr.corenting.edcompanion.models.apis.EDAPIV4.CommodityWithPriceResponse;
import fr.corenting.edcompanion.models.events.CommodityBestPrices;
import fr.corenting.edcompanion.models.events.CommodityDetails;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.network.retrofit.EDApiV4Retrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;


public class CommoditiesNetwork {
    public static void getCommoditiesPrices(Context ctx, String commodityName) {
        // Init retrofit instance
        final EDApiV4Retrofit edApiRetrofit = RetrofitSingleton.getInstance()
                .getEdApiV4Retrofit(ctx.getApplicationContext());

        final retrofit2.Callback<List<CommodityWithPriceResponse>> callback = new retrofit2.Callback<>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<CommodityWithPriceResponse>> call,
                                   retrofit2.Response<List<CommodityWithPriceResponse>> response) {

                List<CommodityWithPriceResponse> body = response.body();
                ResultsList<CommoditiesListResult> convertedResults;
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    List<CommoditiesListResult> resultsList = new ArrayList<>();
                    try {
                        for (CommodityWithPriceResponse resultItem : body) {
                            resultsList.add(
                                    CommoditiesListResult.Companion.fromEDApiCommodityPrice(resultItem)
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
            public void onFailure(Call<List<CommodityWithPriceResponse>> call, Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<CommoditiesListResult>()));
            }
        };

        edApiRetrofit.getCommoditiesWithPrice(commodityName).enqueue(callback);
    }

    public static void getCommodityDetails(Context ctx, String commodityName) {

        // Init retrofit instance
        final EDApiV4Retrofit edApiRetrofit = RetrofitSingleton.getInstance()
                .getEdApiV4Retrofit(ctx.getApplicationContext());

        final retrofit2.Callback<CommodityWithPriceResponse> callback = new retrofit2.Callback<>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<CommodityWithPriceResponse> call,
                                   retrofit2.Response<CommodityWithPriceResponse> response) {

                CommodityWithPriceResponse body = response.body();
                CommodityDetails resultEvent;
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    try {
                        CommodityDetailsResult convertedResult = CommodityDetailsResult.Companion
                                .fromEDApiCommodityDetails(body);
                        resultEvent = new CommodityDetails(true, convertedResult);

                    } catch (Exception ex) {
                        resultEvent = new CommodityDetails(false, null);

                    }
                    EventBus.getDefault().post(resultEvent);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<CommodityWithPriceResponse> call, Throwable t) {
                EventBus.getDefault().post(new CommodityDetails(false, null));

            }
        };

        edApiRetrofit.getCommodityPrice(commodityName).enqueue(callback);
    }

    public static void getCommoditiesBestPrices(Context ctx, String commodityName) {

        // Init retrofit instance
        final EDApiV4Retrofit edApiRetrofit = RetrofitSingleton.getInstance()
                .getEdApiV4Retrofit(ctx.getApplicationContext());

        final retrofit2.Callback<CommodityBestPricesResponse> callback = new retrofit2.Callback<>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<CommodityBestPricesResponse> call,
                                   retrofit2.Response<CommodityBestPricesResponse> response) {

                CommodityBestPricesResponse body = response.body();

                // Failure case
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {

                    try {
                        // Get stations for buy
                        List<CommodityBestPricesStationResult> stationsToBuy = new ArrayList<>();
                        for (CommodityBestPricesResponse.CommodityBestPricesStationResponse resultItem : body.BestStationsToBuy) {
                            stationsToBuy.add(
                                    CommodityBestPricesStationResult.Companion.fromEDApiCommodityBestPricesStation(resultItem)
                            );
                        }

                        // Get stations for sell
                        List<CommodityBestPricesStationResult> stationsToSell = new ArrayList<>();
                        for (CommodityBestPricesResponse.CommodityBestPricesStationResponse resultItem : body.BestStationsToSell) {
                            stationsToSell.add(
                                    CommodityBestPricesStationResult.Companion.fromEDApiCommodityBestPricesStation(resultItem)
                            );
                        }

                        EventBus.getDefault().post(new CommodityBestPrices(true, stationsToBuy, stationsToSell));
                    } catch (Exception ex) {
                        EventBus.getDefault().post(new CommodityBestPrices(false, new ArrayList<>(), new ArrayList<>()));
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<CommodityBestPricesResponse> call, Throwable t) {
                EventBus.getDefault().post(new CommodityBestPrices(false, new ArrayList<>(), new ArrayList<>()));
            }
        };

        edApiRetrofit.getCommodityBestPrices(commodityName).enqueue(callback);
    }
}
