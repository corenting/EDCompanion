package fr.corenting.edcompanion.network;

import android.content.Context;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Callback;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.ServerStatus;


public class ServerStatusNetwork {
    public static void getStatus(Context ctx) {
        Bridge.get(ctx.getString(R.string.edsm_base) + ctx.getString(R.string.edsm_server))
                .request(new Callback() {
                    @Override
                    public void response(Request request, Response response, BridgeException e) {
                        try {
                            if (e != null) {
                                throw new Exception();
                            }
                            JsonObject json = new JsonParser().parse(response.asString()).getAsJsonObject();

                            ServerStatus status = new ServerStatus(true, json.get("message").getAsString());
                            EventBus.getDefault().post(status);

                        } catch (Exception ex) {
                            EventBus.getDefault().post(new ServerStatus(false, null));

                        }
                    }
                });
    }
}
