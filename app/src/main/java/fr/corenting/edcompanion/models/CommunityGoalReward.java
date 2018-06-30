package fr.corenting.edcompanion.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CommunityGoalReward implements Parcelable {

    private String contributors;
    private String rewards;
    private String tier;

    public CommunityGoalReward() { }

    private CommunityGoalReward(Parcel source) {
        contributors = source.readString();
        rewards = source.readString();
        tier = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contributors);
        dest.writeString(rewards);
        dest.writeString(tier);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommunityGoalReward> CREATOR = new Creator<CommunityGoalReward>()
    {
        @Override
        public CommunityGoalReward createFromParcel(Parcel source)
        {
            return new CommunityGoalReward(source);
        }

        @Override
        public CommunityGoalReward[] newArray(int size)
        {
            return new CommunityGoalReward[size];
        }
    };

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}
