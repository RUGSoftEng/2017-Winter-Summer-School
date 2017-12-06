package nl.rug.www.rugsummerschools.controller;

import android.os.HandlerThread;
import android.util.Log;

/**
 * Created by jk on 17. 12. 6.
 */

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";

    public ThumbnailDownloader(String name) {
        super(TAG);
    }

    @Override
    public boolean quit() {
        return super.quit();
    }

    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);
    }
}
