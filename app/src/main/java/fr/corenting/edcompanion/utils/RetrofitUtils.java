package fr.corenting.edcompanion.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDMRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.network.retrofit.InaraRetrofit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    public static EDSMRetrofit getEDSMRetrofit(Context ctx)
    {
        Retrofit retrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.edsm_base))
                .build();

        return retrofit.create(EDSMRetrofit.class);
    }

    public static EDApiRetrofit getEdApiRetrofit(Context ctx)
    {
        Retrofit retrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.edapi_base))
                .build();

        return retrofit.create(EDApiRetrofit.class);
    }

    public static InaraRetrofit getInaraRetrofit(Context ctx)
    {
        Retrofit retrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.inara_api_base))
                .build();

        return retrofit.create(InaraRetrofit.class);
    }

    public static EDMRetrofit getEDMRetrofit(Context ctx)
    {
        Retrofit retrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.edm_api_base))
                .build();

        return retrofit.create(EDMRetrofit.class);
    }

    private static Retrofit.Builder getRetrofitInstance()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson));
    }
}
