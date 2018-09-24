package fr.corenting.edcompanion.network;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.NameId;
import fr.corenting.edcompanion.models.apis.EDApi.CommodityResponse;
import fr.corenting.edcompanion.models.apis.EDApi.ShipResponse;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystem;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Response;

public class AutoCompleteNetwork {
    private static int MAX_RESULTS = 10;

    public static List<NameId> searchSystems(Context context, String filter) {
        try {
            EDSMRetrofit edsmRetrofit = RetrofitSingleton.getInstance()
                    .getEDSMRetrofit(context.getApplicationContext());
            Response<List<EDSMSystem>> response = edsmRetrofit.getSystems(filter, 1,
                    0, 0).execute();
            List<EDSMSystem> systems = response.body();
            if (!response.isSuccessful() || systems == null) {
                return new ArrayList<>();
            } else {
                List<NameId> results = new ArrayList<>();

                for (EDSMSystem sys : systems) {
                    NameId newItem = new NameId(sys.Name, 0);
                    results.add(newItem);
                    if (results.size() >= MAX_RESULTS) {
                        break;
                    }
                }
                return results;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<NameId> searchShips(Context context, String filter) {
        try {
            EDApiRetrofit edApiRetrofit = RetrofitSingleton.getInstance()
                    .getEdApiRetrofit(context.getApplicationContext());
            Response<List<ShipResponse>> response = edApiRetrofit.getShips(filter).execute();
            List<ShipResponse> ships = response.body();

            if (!response.isSuccessful() || ships == null) {
                return new ArrayList<>();
            } else {
                List<NameId> results = new ArrayList<>();

                for (ShipResponse ship : ships) {
                    NameId newItem = new NameId(ship.Name, 0);
                    results.add(newItem);
                    if (results.size() >= MAX_RESULTS) {
                        break;
                    }
                }
                return results;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<NameId> searchCommodities(Context context, String filter) {
        try {
            EDApiRetrofit edApiRetrofit = RetrofitSingleton.getInstance()
                    .getEdApiRetrofit(context.getApplicationContext());
            Response<List<CommodityResponse>> response = edApiRetrofit.getCommodities(filter).execute();
            List<CommodityResponse> commodities = response.body();

            if (!response.isSuccessful() || commodities == null) {
                return new ArrayList<>();
            } else {
                List<NameId> results = new ArrayList<>();

                for (CommodityResponse commodity : commodities) {
                    NameId newItem = new NameId(commodity.Name, 0);
                    results.add(newItem);
                    if (results.size() >= MAX_RESULTS) {
                        break;
                    }
                }
                return results;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
