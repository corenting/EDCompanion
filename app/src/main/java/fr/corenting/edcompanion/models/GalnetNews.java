package fr.corenting.edcompanion.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.Date;

public class GalnetNews implements Parcelable {

    private String title;
    private String content;
    private long dateTimestamp;

    public GalnetNews() {}

    public GalnetNews(Parcel source) {
        title = source.readString();
        content = source.readString();
        dateTimestamp = source.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(dateTimestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GalnetNews> CREATOR = new Creator<GalnetNews>()
    {
        @Override
        public GalnetNews createFromParcel(Parcel source)
        {
            return new GalnetNews(source);
        }

        @Override
        public GalnetNews[] newArray(int size)
        {
            return new GalnetNews[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(long dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }
}
