package fr.corenting.edcompanion.network;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.NameId;
import fr.corenting.edcompanion.models.apis.EDApi.ShipResponse;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystem;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.utils.RetrofitUtils;
import retrofit2.Response;

public class AutoCompleteNetwork {
    private static int MAX_RESULTS = 10;

    public static List<NameId> searchSystems(Context context, String filter) {
        try {
            EDSMRetrofit edsmRetrofit = RetrofitUtils.getEDSMRetrofit(context);
            Response<List<EDSMSystem>> response = edsmRetrofit.getSystems(filter, 1).execute();
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
            EDApiRetrofit edApiRetrofit = RetrofitUtils.getEdApiRetrofit(context);
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
}
