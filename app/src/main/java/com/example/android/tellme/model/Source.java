package com.example.android.tellme.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shafy on 08/02/2018.
 */

public class Source implements Parcelable {

    private String mId;
    private String mName;
    private String mDescription;
    private String mUrl;

    public Source(String mId, String mName, String mDescription, String mUrl) {
        this.mId = mId;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mUrl = mUrl;
    }

    public Source(Parcel in) {
        mId=in.readString();
        mName = in.readString();
        mDescription = in.readString();
        mUrl = in.readString();
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeString(mUrl);
    }
    static final Parcelable.Creator<Source> CREATOR
            = new Parcelable.Creator<Source>() {

        @Override
        public Source createFromParcel(Parcel source) {
            return new Source(source);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

}
