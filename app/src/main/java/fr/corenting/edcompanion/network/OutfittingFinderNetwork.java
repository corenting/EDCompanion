package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.CommodityFinderResult;
import fr.corenting.edcompanion.models.OutfittingFinderResult;
import fr.corenting.edcompanion.models.apis.EDAPIV4.OutfittingFinderResponse;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.network.retrofit.EDApiV4Retrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;


public class OutfittingFinderNetwork {
    public static void findOutfitting(Context ctx, String system, final String outfitting,
                                      String landingPad) {

        // Init retrofit instance
        final EDApiV4Retrofit edApiRetrofit = RetrofitSingleton.getInstance()
                .getEdApiV4Retrofit(ctx.getApplicationContext());

        final retrofit2.Callback<List<OutfittingFinderResponse>> callback = new retrofit2.Callback<>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<OutfittingFinderResponse>> call,
                                   retrofit2.Response<List<OutfittingFinderResponse>> response) {

                // Check response
                final List<OutfittingFinderResponse> responseBody = response.body();
                if (!response.isSuccessful() || responseBody == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    processResults(responseBody);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<OutfittingFinderResponse>> call,
                                  Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<CommodityFinderResult>()));
            }
        };

        edApiRetrofit.findOutfitting(
                system,
                outfitting,
                landingPad
        ).enqueue(callback);
    }

    private static void processResults(List<OutfittingFinderResponse> responseBody) {


        ResultsList<OutfittingFinderResult> convertedResults;
        List<OutfittingFinderResult> resultsList = new ArrayList<>();
        try {
            for (OutfittingFinderResponse item : responseBody) {
                resultsList.add(OutfittingFinderResult.Companion.fromOutfittingFinderResponse(item));
            }
            convertedResults = new ResultsList<>(true, resultsList);

        } catch (Exception ex) {
            convertedResults = new ResultsList<>(false, new ArrayList<>());
        }
        EventBus.getDefault().post(convertedResults);
    }
}
