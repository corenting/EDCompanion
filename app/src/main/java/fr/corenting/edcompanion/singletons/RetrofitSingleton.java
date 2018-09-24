package fr.corenting.edcompanion.singletons;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystemInformation;
import fr.corenting.edcompanion.network.retrofit.EDApiRetrofit;
import fr.corenting.edcompanion.network.retrofit.EDSMRetrofit;
import fr.corenting.edcompanion.network.retrofit.InaraRetrofit;
import fr.corenting.edcompanion.utils.EDSMDeserializer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Singleton safe from serialization/reflection...
// From https://medium.com/exploring-code/how-to-make-the-perfect-singleton-de6b951dfdb0
public class RetrofitSingleton implements Serializable {
    private static volatile RetrofitSingleton instance;

    private EDSMRetrofit edsmRetrofit;
    private EDApiRetrofit edApiRetrofit;
    private InaraRetrofit inaraRetrofit;
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

    private Retrofit.Builder getRetrofitInstance() {
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
