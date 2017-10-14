package fr.corenting.edcompanion.network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.ServerStatus;


public class ServerStatusNetwork {
    public static void getStatus(Context ctx) {
        String url = ctx.getString(R.string.edsm_server);
        Ion.with(ctx)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }
                            ServerStatus status = new ServerStatus(true, result.get("message").getAsString());
                            EventBus.getDefault().post(status);
                        } catch (Exception ex) {
                            EventBus.getDefault().post(new ServerStatus(false, null));
                        }
                    }
                });
    }
}
