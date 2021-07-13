package fr.corenting.edcompanion.models.apis.Inara;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InaraProfileResponse {

    @SerializedName("events")
    public List<InaraProfileInnerResponse> events;

    public class InaraProfileInnerResponse {

        @SerializedName("eventData")
        public InaraProfileResponseEventData EventData;

        public class InaraProfileResponseEventData {

            @SerializedName("commanderName")
            public String CommanderName;

            @SerializedName("commanderRanksPilot")
            public List<InaraProfileResponseRanks> CommanderRanksPilot;

        }

        public class InaraProfileResponseRanks {
            @SerializedName("rankName")
            public String RankName;

            @SerializedName("rankValue")
            public int RankValue;

            @SerializedName("rankProgress")
            public float RankProgress;
        }
    }
}
