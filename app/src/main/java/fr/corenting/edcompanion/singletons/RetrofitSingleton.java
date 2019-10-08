package fr.corenting.edcompanion.singletons;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystemInformation;
import fr.corenting.edcompanion.models.apis.FrontierAuth.FrontierAccessTokenResponse;
import fr.corenting.edcompanion.models.events.FrontierAuthNeeded;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.network.retrofit.FrontierAuthRetrofit;
import fr.corenting.edcompanion.network.retrofit.FrontierRetrofit;
import fr.corenting.edcompanion.network.retrofit.InaraRetrofit;
import fr.corenting.edcompanion.utils.EDSMDeserializer;
import fr.corenting.edcompanion.utils.OAuthUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Singleton safe from serialization/reflection...
// From https://medium.com/exploring-code/how-to-make-the-perfect-singleton-de6b951dfdb0
public class RetrofitSingleton implements Serializable {
    private static volatile RetrofitSingleton instance;

    private EDSMRetrofit edsmRetrofit;
    private EDApiRetrofit edApiRetrofit;
    private InaraRetrofit inaraRetrofit;
    private FrontierAuthRetrofit frontierAuthRetrofit;
    private FrontierRetrofit frontierRetrofit;

    private Retrofit.Builder retrofitBuilder;

    // Private constructor.
    private RetrofitSingleton() {

        // Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get an instance of this class.");
        }
    }

    public static RetrofitSingleton getInstance() {
        if (instance == null) {
            synchronized (RetrofitSingleton.class) {
                if (instance == null) instance = new RetrofitSingleton();
            }
        }

        return instance;
    }

    //Make singleton from serialize and deserialize operation.
    protected RetrofitSingleton readResolve() {
        return getInstance();
    }

    // Retrofit stuff
    public EDSMRetrofit getEDSMRetrofit(Context ctx) {
        if (edsmRetrofit != null) {
            return edsmRetrofit;
        }

        edsmRetrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.edsm_base))
                .build()
                .create(EDSMRetrofit.class);

        return edsmRetrofit;
    }

    public EDApiRetrofit getEdApiRetrofit(Context ctx) {
        if (edApiRetrofit != null) {
            return edApiRetrofit;
        }

        edApiRetrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.edapi_base))
                .build()
                .create(EDApiRetrofit.class);
        return edApiRetrofit;
    }

    public InaraRetrofit getInaraRetrofit(Context ctx) {
        if (inaraRetrofit != null) {
            return inaraRetrofit;
        }

        inaraRetrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.inara_api_base))
                .build()
                .create(InaraRetrofit.class);
        return inaraRetrofit;
    }

    public FrontierAuthRetrofit getFrontierAuthRetrofit(Context ctx) {
        if (frontierAuthRetrofit != null) {
            return frontierAuthRetrofit;
        }

        frontierAuthRetrofit = getRetrofitInstance()
                .baseUrl(ctx.getString(R.string.frontier_auth_base))
                .build()
                .create(FrontierAuthRetrofit.class);
        return frontierAuthRetrofit;
    }

    public FrontierRetrofit getFrontierRetrofit(Context ctx) {
        if (frontierRetrofit != null) {
            return frontierRetrofit;
        }

        OkHttpClient.Builder httpClient = getCommonOkHttpClientBuilder();

        // Add interceptor for tokens in response
        httpClient.addInterceptor(chain -> {

            Request request = OAuthUtils.getRequestWithFrontierAuthorization(ctx, chain);
            Response response = chain.proceed(request);

            // Check if access token expired and renew it if needed
            if (response.code() == 403 || response.code() == 422 || response.code() == 401) {

                FrontierAccessTokenResponse responseBody = OAuthUtils.makeRefreshRequest(ctx);
                if (responseBody == null) {
                    // Send event to renew login
                    EventBus.getDefault().post(new FrontierAuthNeeded(true));
                    return response;
                }

                // Retry request
                OAuthUtils.storeUpdatedTokens(ctx, responseBody.AccessToken,
                        responseBody.RefreshToken);
                request = OAuthUtils.getRequestWithFrontierAuthorization(ctx, chain);
                response = chain.proceed(request);
            }

            // If still not ok, need login
            if (!response.isSuccessful()) {
                EventBus.getDefault().post(new FrontierAuthNeeded(true));
            }

            return response;
        });

        Retrofit.Builder customRetrofitBuilder = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(getCommonGsonConverterFactory());

        frontierRetrofit = customRetrofitBuilder
                .baseUrl(ctx.getString(R.string.frontier_api_base))
                .build()
                .create(FrontierRetrofit.class);
        return frontierRetrofit;
    }

    private Retrofit.Builder getRetrofitInstance() {
        if (retrofitBuilder != null) {
            return retrofitBuilder;
        }

        retrofitBuilder = new Retrofit.Builder()
                .client(getCommonOkHttpClientBuilder().build())
                .addConverterFactory(getCommonGsonConverterFactory());
        return retrofitBuilder;
    }

    private GsonConverterFactory getCommonGsonConverterFactory() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(EDSMSystemInformation.class, new EDSMDeserializer())
                .create();
        return GsonConverterFactory.create(gson);
    }

    private OkHttpClient.Builder getCommonOkHttpClientBuilder() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);
    }
}
