package com.ssl.support.downloader;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class DownloadTaskParams implements Parcelable{

    public final int type, id;

    public DownloadTaskParams(int type, int id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(id);
    }

    public static final Creator<DownloadTaskParams> CREATOR = new Creator<DownloadTaskParams>() {
        @Override
        public DownloadTaskParams createFromParcel(Parcel source) {
            int type = source.readInt();
            int id = source.readInt();
            return new DownloadTaskParams(type, id);
        }

        @Override
        public DownloadTaskParams[] newArray(int size) {
           return new DownloadTaskParams[size];
        }
    };
}
