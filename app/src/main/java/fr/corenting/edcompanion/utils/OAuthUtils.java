package fr.corenting.edcompanion.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import fr.corenting.edcompanion.BuildConfig;
import fr.corenting.edcompanion.R;
import fr.corenting.edcompanion.models.apis.FrontierAuth.FrontierAccessTokenRequestBody;
import fr.corenting.edcompanion.models.apis.FrontierAuth.FrontierAccessTokenResponse;
import fr.corenting.edcompanion.singletons.RetrofitSingleton;
import okhttp3.Interceptor;
import okhttp3.Request;

public class OAuthUtils {
    public static void storeUpdatedTokens(Context context, String accessToken, String refreshToken) {

        if (BuildConfig.DEBUG) {
            Log.d("Tokens", "Access token: " + accessToken);
            Log.d("Tokens", "Refresh token: " + refreshToken);
        }

        SettingsUtils.setString(context, context.getString(R.string.access_token_key),
                accessToken);
        SettingsUtils.setString(context, context.getString(R.string.refresh_token_key),
                refreshToken);
    }

    public static String getAccessToken(Context context) {
        return SettingsUtils.getString(context, context.getString(R.string.access_token_key));
    }

    public static String getRefreshToken(Context context) {
        return SettingsUtils.getString(context, context.getString(R.string.refresh_token_key));
    }

    private static FrontierAccessTokenRequestBody getRefreshTokenRequestBody(Context ctx) {
        FrontierAccessTokenRequestBody requestBody = new FrontierAccessTokenRequestBody();
        requestBody.GrantType = "refresh_token";
        requestBody.ClientId = BuildConfig.FRONTIER_AUTH_CLIENT_ID;
        requestBody.RefreshToken = OAuthUtils.getRefreshToken(ctx);

        return requestBody;
    }

    public static FrontierAccessTokenRequestBody getAuthorizationCodeRequestBody(String codeVerifier,
                                                                                 String authCode) {
        FrontierAccessTokenRequestBody requestBody = new FrontierAccessTokenRequestBody();
        requestBody.CodeVerifier = codeVerifier;
        requestBody.GrantType = "authorization_code";
        requestBody.ClientId = BuildConfig.FRONTIER_AUTH_CLIENT_ID;
        requestBody.Code = authCode;
        requestBody.RedirectUri = "edcompanion://oauth";

        return requestBody;
    }

    public static FrontierAccessTokenResponse makeRefreshRequest(Context ctx) throws IOException {
        FrontierAccessTokenRequestBody requestBody = OAuthUtils
                .getRefreshTokenRequestBody(ctx);

        retrofit2.Response<FrontierAccessTokenResponse> authResponse =
                RetrofitSingleton.getInstance().getFrontierAuthRetrofit(ctx)
                        .getAccessToken(requestBody)
                        .execute();
        return authResponse.body();
    }

    public static Request getRequestWithFrontierAuthorization(Context ctx, Interceptor.Chain chain) {
        // Add access token header to request
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder()
                .header("Authorization", "Bearer " +
                        OAuthUtils.getAccessToken(ctx));

        return requestBuilder.build();
    }
}
