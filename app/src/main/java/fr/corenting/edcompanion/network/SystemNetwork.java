package fr.corenting.edcompanion.network;

import android.content.Context;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.SystemFinderResult;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystem;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;

public class SystemNetwork {
    public static void findSystem(Context ctx, String system) {
        EDSMRetrofit retrofit = RetrofitUtils.getEDSMRetrofit(ctx);

        retrofit2.Callback<List<EDSMSystem>> callback = new retrofit2.Callback<List<EDSMSystem>>() {
            @Override
            public void onResponse(@NonNull Call<List<EDSMSystem>> call,
                                   retrofit2.Response<List<EDSMSystem>> response) {

                List<EDSMSystem> body = response.body();
                ResultsList<SystemFinderResult> convertedResults;
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    List<SystemFinderResult> resultsList = new ArrayList<>();
                    try {
                        for (EDSMSystem resultItem : body) {
                            resultsList.add(
                                    SystemFinderResult.Companion.fromEDSMSystem(resultItem));
                        }
                        convertedResults = new ResultsList<>(true, resultsList);

                    } catch (Exception ex) {
                        convertedResults = new ResultsList<>(false,
                                new ArrayList<SystemFinderResult>());
                    }
                    EventBus.getDefault().post(convertedResults);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EDSMSystem>> call, @NonNull Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<SystemFinderResult>()));
            }
        };

        retrofit.getSystems(system, 1, 1, 1).enqueue(callback);
    }
}
