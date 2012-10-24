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

    public final Uri remoteUri, localUri, updateUri, notifyUri;

    public DownloadTaskParams(Uri remoteUri, Uri localUri, Uri updateUri, Uri notifyUri) {
        this.remoteUri = remoteUri;
        this.localUri = localUri;
        this.updateUri = updateUri;
        this.notifyUri = notifyUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(remoteUri, flags);
        dest.writeParcelable(localUri, flags);
        dest.writeParcelable(updateUri, flags);
        dest.writeParcelable(notifyUri, flags);
    }

    public static final Creator<DownloadTaskParams> CREATOR = new Creator<DownloadTaskParams>() {
        @Override
        public DownloadTaskParams createFromParcel(Parcel source) {
            Uri remoteUri = (Uri) source.readParcelable(ClassLoader.getSystemClassLoader());
            Uri localUri = (Uri) source.readParcelable(ClassLoader.getSystemClassLoader());
            Uri updateUri = (Uri) source.readParcelable(ClassLoader.getSystemClassLoader());
            Uri notifyUri = (Uri) source.readParcelable(ClassLoader.getSystemClassLoader());
            return new DownloadTaskParams(remoteUri, localUri, updateUri, notifyUri);
        }

        @Override
        public DownloadTaskParams[] newArray(int size) {
           return new DownloadTaskParams[size];
        }
    };
}
