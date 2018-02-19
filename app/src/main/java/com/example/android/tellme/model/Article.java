package com.example.android.tellme.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shafy on 08/02/2018.
 */

public class Article implements Parcelable{
    private String mSourceName;
    private String mAuthor;
    private String mTitle;
    private String mDescription;
    private String mImageUrl;
    private String mUrl;
    private String mDate;

    public Article(String mSourceName, String mAuthor, String mTitle, String mDescription, String mImageUrl, String mUrl, String mDate) {
        this.mSourceName = mSourceName;
        this.mAuthor = mAuthor;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mImageUrl = mImageUrl;
        this.mUrl = mUrl;
        this.mDate = mDate;
    }



    public String getSourceName() {
        return mSourceName;
    }

    public void setSourceName(String mSourceName) {
        this.mSourceName = mSourceName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public Article(Parcel in) {
        mSourceName=in.readString();
        mAuthor=in.readString();
        mTitle=in.readString();
        mDescription=in.readString();
        mImageUrl=in.readString();
        mUrl=in.readString();
        mDate=in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSourceName);
        dest.writeString(mAuthor);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mImageUrl);
        dest.writeString(mUrl);
        dest.writeString(mDate);
    }
    static public final Parcelable.Creator<Article> CREATOR
            = new Parcelable.Creator<Article>() {

        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

}
