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
import fr.corenting.edcompanion.models.Distance;


public class DistanceCalculatorNetwork {
    public static void getDistance(Context ctx, String firstSystem, String secondSystem) {
        String url = ctx.getString(R.string.edapi_base) + ctx.getString(R.string.edapi_distance);
        url += firstSystem + "/" + secondSystem;
        Bridge.get(url)
                .request(new Callback() {
                    @Override
                    public void response(Request request, Response response, BridgeException e) {
                        try {
                            if (e != null) {
                                throw new Exception();
                            }
                            JsonObject json = new JsonParser().parse(response.asString()).getAsJsonObject();
                            JsonObject from = json.get("from").getAsJsonObject();
                            JsonObject to = json.get("to").getAsJsonObject();

                            Distance distance = new Distance(
                                    true,
                                    json.get("distance").getAsFloat(),
                                    from.get("name").getAsString(),
                                    to.get("name").getAsString(),
                                    from.get("permit_required").getAsBoolean(),
                                    to.get("permit_required").getAsBoolean()
                            );

                            EventBus.getDefault().post(distance);

                        } catch (Exception ex) {
                            Distance distance = new Distance(false, 0, null, null, false, false);
                            EventBus.getDefault().post(distance);
                        }
                    }
                });
    }
}
