package fr.corenting.edcompanion.models.apis.EDSM;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class EDSMCredits extends EDSMBaseResponse {

    @SerializedName("credits")
    public EDSMInnerCredits credits;

    public class EDSMInnerCredits {
        @SerializedName("balance")
        public int balance;

        @SerializedName("loan")
        public int loan;

        @SerializedName("date")
        public Date lastUpdate;
    }
}
