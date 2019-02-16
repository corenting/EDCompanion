package fr.corenting.edcompanion.models.apis.FrontierAuth;

import com.google.gson.annotations.SerializedName;


public class FrontierAccessTokenResponse {

    @SerializedName("access_token")
    public String AccessToken;

    @SerializedName("refresh_token")
    public String RefreshToken;
}