package fr.corenting.edcompanion.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.corenting.edcompanion.R;

public class CommunityGoal implements Parcelable {

    private boolean ongoing;
    private String title;
    private String description;
    private String objective;
    private String reward;

    private int currentTier;
    private int totalTier;
    private int contributors;

    private String station;
    private String system;

    private String endDate;
    private String refreshDate;

    // For the string function
    private PrettyTime prettyTime;

    public CommunityGoal() {
        prettyTime = new PrettyTime(Locale.US);
    }

    public CommunityGoal(Parcel source) {
        ongoing = source.readByte() != 0;
        title = source.readString();
        description = source.readString();
        objective = source.readString();
        reward = source.readString();

        currentTier = source.readInt();
        totalTier = source.readInt();
        contributors = source.readInt();

        station = source.readString();
        system = source.readString();

        endDate = source.readString();
        refreshDate = source.readString();

        prettyTime = new PrettyTime(Locale.US);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (ongoing ? 1 : 0));
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(objective);
        dest.writeString(reward);

        dest.writeInt(currentTier);
        dest.writeInt(totalTier);
        dest.writeInt(contributors);

        dest.writeString(station);
        dest.writeString(system);

        dest.writeString(endDate);
        dest.writeString(refreshDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CommunityGoal> CREATOR = new Parcelable.Creator<CommunityGoal>()
    {
        @Override
        public CommunityGoal createFromParcel(Parcel source)
        {
            return new CommunityGoal(source);
        }

        @Override
        public CommunityGoal[] newArray(int size)
        {
            return new CommunityGoal[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalTier() {
        return totalTier;
    }

    public void setTotalTier(int totalTier) {
        this.totalTier = totalTier;
    }

    public int getCurrentTier() {
        return currentTier;
    }

    public void setCurrentTier(int currentTier) {
        this.currentTier = currentTier;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public int getContributors() {
        return contributors;
    }

    public void setContributors(int contributors) {
        this.contributors = contributors;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(String refreshDate) {
        this.refreshDate = refreshDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean refreshIsFinished() {
        return refreshDate.equals("finished");
    }

    public String getRefreshDateString(Context ctx) {
        // Last update
        if (refreshIsFinished()) {
            return ctx.getString(R.string.last_update, getRefreshDate());
        }
        else {
            try {
                DateFormat sourceFormat = new SimpleDateFormat("dd MMM yyyy, hh:mma", Locale.US);
                Date date = sourceFormat.parse(getRefreshDate());
                return ctx.getString(R.string.last_update, prettyTime.format(date));
            }
            catch (Exception e)
            {
                return ctx.getString(R.string.last_update, ctx.getString(R.string.unknown));
            }
        }
    }

    public String getRemainingString() {
        return isOngoing() ? getEndDate() : "Finished";
    }

    public String getTierString() {
        return String.valueOf(getCurrentTier()) + " / " + getTotalTier();
    }
}
