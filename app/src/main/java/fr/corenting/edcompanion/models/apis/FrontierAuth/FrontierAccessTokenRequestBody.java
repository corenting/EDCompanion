package fr.corenting.edcompanion.models.apis.FrontierAuth;

import com.google.gson.annotations.SerializedName;


public class FrontierAccessTokenRequestBody {

    @SerializedName("grant_type")
    public String GrantType;

    @SerializedName("client_id")
    public String ClientId;

    @SerializedName("code_verifier")
    public String CodeVerifier;

    @SerializedName("code")
    public String Code;

    @SerializedName("redirect_uri")
    public String RedirectUri;
}