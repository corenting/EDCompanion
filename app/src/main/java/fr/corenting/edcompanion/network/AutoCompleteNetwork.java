package fr.corenting.edcompanion.network;

import android.content.Context;

import com.afollestad.bridge.Bridge;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.NameId;

public class AutoCompleteNetwork {
    private static int MAX_RESULTS = 10;

    public static List<NameId> searchCommodities(Context context, String filter) {
        String url = context.getResources().getString(R.string.edapi_commodities) + "?name=" + filter;
        try {
            JsonArray array = new JsonParser().parse(Bridge.get(url).asString()).getAsJsonArray();
            List<NameId> results = new ArrayList<>();
            for (JsonElement element : array) {
                JsonObject jsonObject = element.getAsJsonObject();
                NameId newItem = new NameId(jsonObject.get("name").getAsString(), jsonObject.get("id").getAsInt());
                results.add(newItem);
                if (results.size() >= MAX_RESULTS) {
                    break;
                }
            }
            return results;

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<NameId> searchSystems(Context context, String filter) {
        String url = context.getResources().getString(R.string.edsm_systems) + "?systemName=" + filter;
        try {
            JsonArray array = new JsonParser().parse(Bridge.get(url).asString()).getAsJsonArray();
            List<NameId> results = new ArrayList<>();
            for (JsonElement element : array) {
                JsonObject jsonObject = element.getAsJsonObject();
                NameId newItem = new NameId(jsonObject.get("name").getAsString(), 0);
                results.add(newItem);
                if (results.size() >= MAX_RESULTS) {
                    break;
                }
            }
            return results;

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
