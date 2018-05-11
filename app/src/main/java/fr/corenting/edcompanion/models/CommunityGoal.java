package fr.corenting.edcompanion.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;

import java.util.Date;

import fr.corenting.edcompanion.R;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

public class CommunityGoal implements Parcelable {

    private boolean ongoing;
    private String title;
    private String description;
    private String objective;
    private String reward;

    private int currentTier;
    private int totalTier;
    private long contributors;

    private String station;
    private String system;

    private Instant endDate;
    private Instant refreshDate;

    public CommunityGoal() { }

    private CommunityGoal(Parcel source) {
        ongoing = source.readByte() != 0;
        endDate = Instant.ofEpochSecond(source.readLong());
        refreshDate = Instant.ofEpochSecond(source.readLong());

        title = source.readString();
        description = source.readString();
        objective = source.readString();
        reward = source.readString();

        currentTier = source.readInt();
        totalTier = source.readInt();
        contributors = source.readLong();

        station = source.readString();
        system = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (ongoing ? 1 : 0));
        dest.writeLong(endDate.getEpochSecond());
        dest.writeLong(refreshDate.getEpochSecond());

        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(objective);
        dest.writeString(reward);

        dest.writeInt(currentTier);
        dest.writeInt(totalTier);
        dest.writeLong(contributors);

        dest.writeString(station);
        dest.writeString(system);
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

    public long getContributors() {
        return contributors;
    }

    public void setContributors(long contributors) {
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

    public void setRefreshDate(Date refreshDate) {
        this.refreshDate = DateTimeUtils.toInstant(refreshDate);
    }

    public String getEndDate(Context ctx) {
        if (!ongoing)
        {
            return ctx.getString(R.string.finished);
        }
        try {
            return android.text.format.DateUtils.getRelativeTimeSpanString(endDate.toEpochMilli(),
                    Instant.now().toEpochMilli(), 0, FORMAT_ABBREV_RELATIVE ).toString();
        }
        catch (Exception e)
        {
            return ctx.getString(R.string.unknown);
        }
    }

    public void setEndDate(Date endDate) {
        this.endDate = DateTimeUtils.toInstant(endDate);
    }

    public String getRefreshDateString(Context ctx) {
        try {
            String date = android.text.format.DateUtils.getRelativeTimeSpanString(refreshDate.toEpochMilli(),
                    Instant.now().toEpochMilli(), 0, FORMAT_ABBREV_RELATIVE).toString();
            return ctx.getString(R.string.last_update, date);
        } catch (Exception e) {
            return ctx.getString(R.string.last_update, ctx.getString(R.string.unknown));
        }
    }

    public String getTierString() {
        return String.valueOf(getCurrentTier()) + " / " + getTotalTier();
    }
}
