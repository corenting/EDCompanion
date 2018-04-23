package fr.corenting.edcompanion.models.apis.EDSM;

import com.google.gson.annotations.SerializedName;

public class EDSMServerStatus {
    @SerializedName("type")
    public String Type;
    @SerializedName("message")
    public String Message;
    @SerializedName("status")
    public int Status;
}
