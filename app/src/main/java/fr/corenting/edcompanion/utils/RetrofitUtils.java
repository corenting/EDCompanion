package fr.corenting.edcompanion.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystemInformation;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.network.retrofit.InaraRetrofit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static EDSMRetrofit edsmRetrofit = null;
    private static EDApiRetrofit edApiRetrofit = null;
    private static InaraRetrofit inaraRetrofit = null;

    private static Retrofit.Builder retrofitBuilder = null;

    public static EDSMRetrofit getEDSMRetrofit(Context ctx)
    {
        if (edsmRetrofit != null) {
            return edsmRetrofit;
        }

        edsmRetrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.edsm_base))
                .build()
                .create(EDSMRetrofit.class);

        return edsmRetrofit;
    }

    public static EDApiRetrofit getEdApiRetrofit(Context ctx)
    {
        if (edApiRetrofit != null) {
            return edApiRetrofit;
        }

        edApiRetrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.edapi_base))
                .build()
                .create(EDApiRetrofit.class);
        return  edApiRetrofit;
    }

    public static InaraRetrofit getInaraRetrofit(Context ctx)
    {
        if (inaraRetrofit != null) {
            return inaraRetrofit;
        }

        inaraRetrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.inara_api_base))
                .build()
                .create(InaraRetrofit.class);
        return inaraRetrofit;
    }

    private static Retrofit.Builder getRetrofitInstance()
    {
        if (retrofitBuilder != null) {
            return retrofitBuilder;
        }

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(EDSMSystemInformation.class, new EDSMDeserializer())
                .create();

        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson));
        return retrofitBuilder;
    }
}
