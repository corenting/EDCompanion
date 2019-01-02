package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.models.apis.EDSM.EDSMServerStatus;
import fr.corenting.edcompanion.models.events.ServerStatus;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;


public class ServerStatusNetwork {
    public static void getStatus(Context ctx) {
        EDSMRetrofit edsmRetrofit = RetrofitSingleton.getInstance()
                .getEDSMRetrofit(ctx.getApplicationContext());
        retrofit2.Callback<EDSMServerStatus> callback = new retrofit2.Callback<EDSMServerStatus>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<EDSMServerStatus> call,
                                   retrofit2.Response<EDSMServerStatus> response) {
                EDSMServerStatus edsmStatus = response.body();
                if (!response.isSuccessful() || edsmStatus == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    ServerStatus serverStatus = new ServerStatus(true, edsmStatus.Message);
                    EventBus.getDefault().post(serverStatus);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<EDSMServerStatus> call, Throwable t) {
                EventBus.getDefault().post(new ServerStatus(false, ""));
            }
        };
        edsmRetrofit.getServerStatus().enqueue(callback);
    }
}
