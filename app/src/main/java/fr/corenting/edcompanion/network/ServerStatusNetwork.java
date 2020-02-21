package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.models.apis.EDSM.EDSMServerStatusResponse;
import fr.corenting.edcompanion.models.events.ServerStatus;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;


public class ServerStatusNetwork {
    public static void getStatus(Context ctx) {
        EDSMRetrofit edsmRetrofit = RetrofitSingleton.getInstance()
                .getEDSMRetrofit(ctx.getApplicationContext());
        retrofit2.Callback<EDSMServerStatusResponse> callback = new retrofit2.Callback<EDSMServerStatusResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<EDSMServerStatusResponse> call,
                                   retrofit2.Response<EDSMServerStatusResponse> response) {
                EDSMServerStatusResponse edsmStatus = response.body();
                if (!response.isSuccessful() || edsmStatus == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    ServerStatus serverStatus = new ServerStatus(true, edsmStatus.Message);
                    EventBus.getDefault().post(serverStatus);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<EDSMServerStatusResponse> call, Throwable t) {
                EventBus.getDefault().post(new ServerStatus(false, ""));
            }
        };
        edsmRetrofit.getServerStatus().enqueue(callback);
    }
}
