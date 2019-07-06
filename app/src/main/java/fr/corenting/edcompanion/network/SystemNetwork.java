package fr.corenting.edcompanion.network;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.corenting.edcompanion.models.Station;
import fr.corenting.edcompanion.models.System;
import fr.corenting.edcompanion.models.SystemFinderResult;
import fr.corenting.edcompanion.models.SystemHistoryResult;
import fr.corenting.edcompanion.models.apis.EDApi.StationResponse;
import fr.corenting.edcompanion.models.apis.EDApi.SystemHistoryResponse;
import fr.corenting.edcompanion.models.apis.EDApi.SystemResponse;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystem;
import fr.corenting.edcompanion.models.events.ResultsList;
import fr.corenting.edcompanion.models.events.SystemDetails;
import fr.corenting.edcompanion.models.events.SystemHistory;
import fr.corenting.edcompanion.models.events.SystemStations;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;

public class SystemNetwork {
    public static void findSystem(Context ctx, String system) {
        EDSMRetrofit retrofit = RetrofitSingleton.getInstance()
                .getEDSMRetrofit(ctx.getApplicationContext());

        retrofit2.Callback<List<EDSMSystem>> callback = new retrofit2.Callback<List<EDSMSystem>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<EDSMSystem>> call,
                                   retrofit2.Response<List<EDSMSystem>> response) {

                List<EDSMSystem> body = response.body();
                ResultsList<SystemFinderResult> convertedResults;
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    List<SystemFinderResult> resultsList = new ArrayList<>();
                    try {
                        for (EDSMSystem resultItem : body) {
                            resultsList.add(
                                    SystemFinderResult.Companion.fromEDSMSystem(resultItem));
                        }
                        convertedResults = new ResultsList<>(true, resultsList);

                    } catch (Exception ex) {
                        convertedResults = new ResultsList<>(false,
                                new ArrayList<>());
                    }
                    EventBus.getDefault().post(convertedResults);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<EDSMSystem>> call, Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<SystemFinderResult>()));
            }
        };

        retrofit.getSystems(system, 1, 1, 1).enqueue(callback);
    }

    public static void getSystemDetails(Context ctx, String system) {
        EDApiRetrofit retrofit = RetrofitSingleton.getInstance()
                .getEdApiRetrofit(ctx.getApplicationContext());

        retrofit2.Callback<SystemResponse> callback = new retrofit2.Callback<SystemResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<SystemResponse> call,
                                   retrofit2.Response<SystemResponse> response) {

                SystemResponse body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    SystemDetails event;
                    try {
                        System convertedSystem = System.Companion.fromSystemResponse(body);
                        event = new SystemDetails(true, convertedSystem);
                    } catch (Exception ex) {
                        event = new SystemDetails(false, null);
                    }
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<SystemResponse> call, Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<SystemFinderResult>()));
            }
        };

        retrofit.getSystemDetails(system).enqueue(callback);
    }

    public static void getSystemHistory(Context ctx, String system) {
        EDApiRetrofit retrofit = RetrofitSingleton.getInstance()
                .getEdApiRetrofit(ctx.getApplicationContext());

        retrofit2.Callback<List<SystemHistoryResponse>> callback =
                new retrofit2.Callback<List<SystemHistoryResponse>>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call<List<SystemHistoryResponse>> call,
                                           retrofit2.Response<List<SystemHistoryResponse>> response) {

                        List<SystemHistoryResponse> body = response.body();
                        if (!response.isSuccessful() || body == null) {
                            onFailure(call, new Exception("Invalid response"));
                        } else {
                            SystemHistory event;
                            try {
                                List<SystemHistoryResult> results = new ArrayList<>();
                                for (SystemHistoryResponse historyResult : body) {
                                    results.add(SystemHistoryResult.Companion
                                            .fromSystemHistoryResponse(historyResult));
                                }

                                event = new SystemHistory(true, results);
                            } catch (Exception ex) {
                                event = new SystemHistory(false,
                                        new ArrayList<SystemHistoryResult>());
                            }
                            EventBus.getDefault().post(event);
                        }
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<List<SystemHistoryResponse>> call,
                                          Throwable t) {
                        EventBus.getDefault().post(new SystemHistory(false,
                                new ArrayList<SystemHistoryResult>()));

                    }
                };

        retrofit.getSystemHistory(system).enqueue(callback);
    }

    public static void getSystemStations(Context ctx, String systemName) {
        EDApiRetrofit retrofit = RetrofitSingleton.getInstance()
                .getEdApiRetrofit(ctx.getApplicationContext());

        retrofit2.Callback<List<StationResponse>> callback = new retrofit2.Callback<List<StationResponse>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<StationResponse>> call,
                                   retrofit2.Response<List<StationResponse>> response) {

                List<StationResponse> body = response.body();
                if (!response.isSuccessful() || body == null) {
                    onFailure(call, new Exception("Invalid response"));
                } else {
                    SystemStations event;
                    try {
                        List<Station> results = new ArrayList<>();
                        for (StationResponse responseItem : body) {
                            results.add(Station.Companion
                                    .fromStationResponse(responseItem));
                        }
                        event = new SystemStations(true, results);
                    } catch (Exception ex) {
                        event = new SystemStations(false, new ArrayList<Station>());
                    }
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<StationResponse>> call, Throwable t) {
                EventBus.getDefault().post(new ResultsList<>(false,
                        new ArrayList<SystemFinderResult>()));
            }
        };

        retrofit.getSystemStations(systemName).enqueue(callback);
    }
}
