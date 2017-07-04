package fr.corenting.edcompanion.network;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.activities.MainActivity;


public class ServerStatusNetwork {
    public static void getStatus(final MainActivity activity) {
        String url = activity.getString(R.string.edsm_server);
        Ion.with(activity)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (e != null || result == null) {
                                throw new Exception();
                            }

                            String status = result.get("message").getAsString();

                            EventBus.getDefault().post(status);
                        } catch (Exception ex) {
                            EventBus.getDefault().post(activity.getString(R.string.unknown));
                        }
                    }
                });
    }
}
