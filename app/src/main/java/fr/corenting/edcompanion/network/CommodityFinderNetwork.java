package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.CommodityFinderResults;
import fr.corenting.edcompanion.models.apis.EDM.CommodityFinderResponse;
import fr.corenting.edcompanion.network.retrofit.EDMRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;


public class CommodityFinderNetwork {
    public static void findCommodity(Context ctx, String system, String commodity,
                                     String landingPad, int minStock) {
        EDMRetrofit retrofit = RetrofitUtils.getEDMRetrofit(ctx);

        retrofit2.Callback<CommodityFinderResponse> callback = new retrofit2.Callback<CommodityFinderResponse>() {
            @Override
            public void onResponse(Call<CommodityFinderResponse> call, retrofit2.Response<CommodityFinderResponse> response) {
                CommodityFinderResponse body = response.body();
                CommodityFinderResults convertedResults;
                if (!response.isSuccessful() || body == null)
                {
                    onFailure(call, new Exception("Invalid response"));
                }
                else
                {
                    List<CommodityFinderResult> resultsList = new LinkedList<>();
                    try {
                        for (CommodityFinderResponse.CommodityFinderItem seller : body.Sellers) {
                            CommodityFinderResult newResult = new CommodityFinderResult();
                            newResult.BuyPrice = seller.BuyPrice;
                            newResult.LandingPad = seller.LandingPad;
                            newResult.Station = seller.Station;
                            newResult.Stock = seller.Stock;
                            newResult.System = seller.System;
                            resultsList.add(newResult);
                        }
                        convertedResults = new CommodityFinderResults(true, resultsList );

                    } catch (Exception ex) {
                        convertedResults = new CommodityFinderResults(false, null );
                    }
                    EventBus.getDefault().post(convertedResults);
                }
            }

            @Override
            public void onFailure(Call<CommodityFinderResponse> call, Throwable t) {
                CommodityFinderResults results =  new CommodityFinderResults(false, null );
                EventBus.getDefault().post(results);
            }
        };

        retrofit.findCommodity(system, commodity, landingPad, minStock).enqueue(callback);
    }
}
