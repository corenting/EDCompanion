package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.CommodityFinderResults;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityResponse;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityFinderResponse;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommodityFinderNetwork {
    public static void findCommodity(Context ctx, String system, final String commodity,
                                     String landingPad, int minStock) {

        // Init retrofit instance
        final EDApiRetrofit edApiRetrofit = RetrofitUtils.getEdApiRetrofit(ctx);

        final retrofit2.Callback<List<CommodityFinderResponse>> callback = new retrofit2.Callback<List<CommodityFinderResponse>>() {
            @Override
            public void onResponse(Call<List<CommodityFinderResponse>> call, retrofit2.Response<List<CommodityFinderResponse>> response) {

                // Check response
                final List<CommodityFinderResponse> sellersResponseBody = response.body();
                if (!response.isSuccessful() || sellersResponseBody == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {

                    // Then get the commodities list to get average price
                    Callback<List<CommodityResponse>> commodtitiesCallback = new Callback<List<CommodityResponse>>() {
                        @Override
                        public void onResponse(Call<List<CommodityResponse>> call, Response<List<CommodityResponse>> response) {
                            final List<CommodityResponse> commoditiesResponseBody = response.body();
                            if (!response.isSuccessful() || commoditiesResponseBody == null) {
                                onFailure(call, new Exception("Invalid response"));
                            } else {
                                processResults(sellersResponseBody, commoditiesResponseBody);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CommodityResponse>> call, Throwable t) {
                            CommodityFinderResults results = new CommodityFinderResults(false,
                                    null, null);
                            EventBus.getDefault().post(results);
                        }
                    };

                    edApiRetrofit.getCommodities(commodity).enqueue(commodtitiesCallback);
                }
            }

            @Override
            public void onFailure(Call<List<CommodityFinderResponse>> call, Throwable t) {
                CommodityFinderResults results = new CommodityFinderResults(false,
                        null, null);
                EventBus.getDefault().post(results);
            }
        };

        edApiRetrofit.findCommodity(system, commodity, landingPad, minStock).enqueue(callback);
    }

    private static void processResults(List<CommodityFinderResponse> responseBody,
                                       List<CommodityResponse> edApiResponseBody) {


        CommodityFinderResults convertedResults;
        List<CommodityFinderResult> resultsList = new ArrayList<>();
        try {
            for (CommodityFinderResponse seller : responseBody) {
                CommodityFinderResult newResult = new CommodityFinderResult();
                newResult.BuyPrice = seller.BuyPrice;
                newResult.LandingPad = seller.Station.MaxLandingPad;
                newResult.Station = seller.Station.Name;
                newResult.Stock = seller.Stock;
                newResult.System = seller.Station.System.Name;
                newResult.PermitRequired = seller.Station.System.PermitRequired;
                newResult.Distance = seller.Distance;
                newResult.DistanceToStar = seller.DistanceToStar;
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
