package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.models.events.ServerStatus;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMServerStatus;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Call;


public class ServerStatusNetwork {
    public static void getStatus(Context ctx) {
        EDSMRetrofit edsmRetrofit = RetrofitUtils.getEDSMRetrofit(ctx);
        retrofit2.Callback<EDSMServerStatus> callback = new retrofit2.Callback<EDSMServerStatus>() {
            @Override
            public void onResponse(Call<EDSMServerStatus> call, retrofit2.Response<EDSMServerStatus> response) {
                EDSMServerStatus edsmStatus = response.body();
                if (!response.isSuccessful() || edsmStatus == null)
                {
                    onFailure(call, new Exception("Invalid response"));
                }
                else
                {
                    ServerStatus serverStatus = new ServerStatus(true, edsmStatus.Message);
                    EventBus.getDefault().post(serverStatus);
                }
            }

            @Override
            public void onFailure(Call<EDSMServerStatus> call, Throwable t) {
                EventBus.getDefault().post(new ServerStatus(false, null));
            }
        };
        edsmRetrofit.getServerStatus().enqueue(callback);
    }
}
