package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.CommodityFinderResults;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityResponse;
import fr.corenting.edcompanion.models.apis.EDM.CommodityFinderResponse;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDMRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommodityFinderNetwork {
    public static void findCommodity(Context ctx, String system, final String commodity,
                                     String landingPad, int minStock) {

        // Init retrofit instances
        final EDMRetrofit edmRetrofit = RetrofitUtils.getEDMRetrofit(ctx);
        final EDApiRetrofit edApiRetrofit = RetrofitUtils.getEdApiRetrofit(ctx);

        final retrofit2.Callback<CommodityFinderResponse> edmCallback = new retrofit2.Callback<CommodityFinderResponse>() {
            @Override
            public void onResponse(Call<CommodityFinderResponse> call, retrofit2.Response<CommodityFinderResponse> response) {

                // Check response
                final CommodityFinderResponse edmResponseBody = response.body();
                if (!response.isSuccessful() || edmResponseBody == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    // Then get the commodities network list
                    Callback<List<CommodityResponse>> edApiCallback = new Callback<List<CommodityResponse>>() {
                        @Override
                        public void onResponse(Call<List<CommodityResponse>> call, Response<List<CommodityResponse>> response) {
                            final List<CommodityResponse> edApiResponseBody = response.body();
                            if (!response.isSuccessful() || edApiResponseBody == null) {
                                onFailure(call, new Exception("Invalid response"));
                            } else {
                                processResults(edmResponseBody, edApiResponseBody);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CommodityResponse>> call, Throwable t) {
                            CommodityFinderResults results = new CommodityFinderResults(false,
                                    null, null);
                            EventBus.getDefault().post(results);
                        }
                    };

                    edApiRetrofit.getCommodities(commodity).enqueue(edApiCallback);
                }
            }

            @Override
            public void onFailure(Call<CommodityFinderResponse> call, Throwable t) {
                CommodityFinderResults results = new CommodityFinderResults(false,
                        null, null);
                EventBus.getDefault().post(results);
            }
        };

        edmRetrofit.findCommodity(system, commodity, landingPad, minStock).enqueue(edmCallback);
    }

    private static void processResults(CommodityFinderResponse responseBody,
                                       List<CommodityResponse> edApiResponseBody) {


        CommodityFinderResults convertedResults;
        List<CommodityFinderResult> resultsList = new LinkedList<>();
        try {
            for (CommodityFinderResponse.CommodityFinderItem seller : responseBody.Sellers) {
                CommodityFinderResult newResult = new CommodityFinderResult();
                newResult.BuyPrice = seller.BuyPrice;
                newResult.LandingPad = seller.LandingPad;
                newResult.Station = seller.Station;
                newResult.Stock = seller.Stock;
                newResult.System = seller.System;
                resultsList.add(newResult);
            }
            convertedResults = new CommodityFinderResults(true, resultsList,
                    edApiResponseBody.get(0));

        } catch (Exception ex) {
            convertedResults = new CommodityFinderResults(false, null, null);
        }
        EventBus.getDefault().post(convertedResults);
    }
}
