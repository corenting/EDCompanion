package fr.corenting.edcompanion.singletons;

import android.content.Context;
import android.util.Base64;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

import fr.corenting.edcompanion.BuildConfig;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.apis.FrontierAuth.FrontierAccessTokenRequestBody;
import fr.corenting.edcompanion.models.apis.FrontierAuth.FrontierAccessTokenResponse;
import fr.corenting.edcompanion.models.events.FrontierTokensEvent;
import fr.corenting.edcompanion.network.retrofit.FrontierAuthRetrofit;
import fr.corenting.edcompanion.utils.OAuthUtils;
import retrofit2.Call;
import retrofit2.internal.EverythingIsNonNull;

// Singleton safe from serialization/reflection...
// From https://medium.com/exploring-code/how-to-make-the-perfect-singleton-de6b951dfdb0
public class FrontierAuthSingleton implements Serializable {
    private static volatile FrontierAuthSingleton instance;

    private String codeVerifier;
    private String codeChallenge;
    private String requestState;


    // Private constructor.
    private FrontierAuthSingleton() {

        // Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get an instance of this class.");
        }
    }

    public static FrontierAuthSingleton getInstance() {
        if (instance == null) {
            synchronized (FrontierAuthSingleton.class) {
                if (instance == null) instance = new FrontierAuthSingleton();
            }
        }

        return instance;
    }

    //Make singleton from serialize and deserialize operation.
    protected FrontierAuthSingleton readResolve() {
        return getInstance();
    }

    private void generateCodeVerifierAndChallenge() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        codeVerifier = Base64.encodeToString(code,
                Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);

        try {
            byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes, 0, bytes.length);
            byte[] digest = md.digest();
            codeChallenge = Base64.encodeToString(digest,
                    Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
        } catch (Exception e) {
            codeChallenge = null;
        }
    }

    private void generateState() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[8];
        sr.nextBytes(code);
        requestState = Base64.encodeToString(code,
                Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
    }

    public String getAuthorizationUrl(Context ctx) {
        generateCodeVerifierAndChallenge();
        generateState();

        return ctx.getString(R.string.frontier_auth_base) + "auth" +
                "?audience=frontier,epic,steam" +
                "&scope=auth%20capi" +
                "&response_type=code" +
                "&state=" + requestState +
                "&client_id=" + BuildConfig.FRONTIER_AUTH_CLIENT_ID +
                "&code_challenge=" + codeChallenge +
                "&code_challenge_method=S256" +
                "&redirect_uri=edcompanion://oauth";
    }

    public void sendTokensRequest(Context ctx, String authCode, String state) {
        // Check if same state
        if (!state.equals(requestState)) {
            EventBus.getDefault().post(new FrontierTokensEvent(false,
                    "", ""));
        }

        FrontierAuthRetrofit retrofit = RetrofitSingleton.getInstance()
                .getFrontierAuthRetrofit(ctx);

        retrofit2.Callback<FrontierAccessTokenResponse> callback =
                new retrofit2.Callback<FrontierAccessTokenResponse>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call<FrontierAccessTokenResponse> call,
                                           retrofit2.Response<FrontierAccessTokenResponse> response) {

                        FrontierAccessTokenResponse body = response.body();
                        if (!response.isSuccessful() || body == null) {
                            onFailure(call, new Exception("Invalid response"));
                        } else {
                            OAuthUtils.storeUpdatedTokens(ctx, body.AccessToken, body.RefreshToken);

                            EventBus.getDefault().post(new FrontierTokensEvent(true,
                                    body.AccessToken, body.RefreshToken));
                        }
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<FrontierAccessTokenResponse> call, Throwable t) {
                        EventBus.getDefault().post(new FrontierTokensEvent(false,
                                "", ""));
                    }
                };

        FrontierAccessTokenRequestBody requestBody = OAuthUtils.getAuthorizationCodeRequestBody(
                codeVerifier, authCode);

        retrofit.getAccessToken(requestBody).enqueue(callback);
    }
}
