package fr.corenting.edcompanion.models;

import android.os.Parcel;
import android.os.Parcelable;

public class GalnetNews implements Parcelable {

    private String title;
    private String content;
    private long date;

    public GalnetNews() {}

    public GalnetNews(Parcel source) {
        title = source.readString();
        content = source.readString();
        date = source.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(date);
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
