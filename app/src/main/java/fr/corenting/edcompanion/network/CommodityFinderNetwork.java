package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.threeten.bp.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.ResultsList;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityFinderResponse;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;


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
                    processResults(sellersResponseBody);
                }
            }

            @Override
            public void onFailure(Call<List<CommodityFinderResponse>> call, Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<CommodityFinderResult>()));
            }
        };

        edApiRetrofit.findCommodity(system, commodity, landingPad, minStock).enqueue(callback);
    }

    private static void processResults(List<CommodityFinderResponse> responseBody) {


        ResultsList<CommodityFinderResult> convertedResults;
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
                newResult.IsPlanetary = seller.Station.IsPlanetary;
                newResult.LastPriceUpdate = DateTimeUtils.toInstant(seller.LastPriceUpdate);
                resultsList.add(newResult);
            }
            convertedResults = new ResultsList<>(true, resultsList);

        } catch (Exception ex) {
            convertedResults = new ResultsList<>(false, new ArrayList<CommodityFinderResult>());
        }
        EventBus.getDefault().post(convertedResults);
    }
}
