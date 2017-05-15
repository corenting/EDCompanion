package fr.corenting.edcompanion.models;

import android.os.Parcel;
import android.os.Parcelable;

public class GalnetNews implements Parcelable {

    private String title;
    private String content;

    public GalnetNews() {}

    public GalnetNews(Parcel source) {
        title = source.readString();
        content = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
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
}
