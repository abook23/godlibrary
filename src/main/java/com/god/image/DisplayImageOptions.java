package com.god.image;

import android.widget.ImageView;

/**
 * Created by abook23 on 2016/11/2.
 */

public class DisplayImageOptions {

    public String uri;
    public ImageView imageView;
    public int width, height, inSampleSize, loadImage, errorImage;
    public boolean isRound;
    public String cacheDirName;

    public static DisplayImageOptions builder() {
        return new DisplayImageOptions();
    }

    public DisplayImageOptions uri(String uri) {
        this.uri = uri;
        return this;
    }

    public DisplayImageOptions imageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public DisplayImageOptions width(int width) {
        this.width = width;
        return this;
    }

    public DisplayImageOptions height(int height) {
        this.height = height;
        return this;
    }

    public DisplayImageOptions inSampleSize(int inSampleSize) {
        this.inSampleSize = inSampleSize;
        return this;
    }

    public DisplayImageOptions loadImage(int loadImage) {
        this.loadImage = loadImage;
        return this;
    }

    public DisplayImageOptions errorImage(int errorImage) {
        this.errorImage = errorImage;
        return this;
    }

    public DisplayImageOptions round(boolean round) {
        isRound = round;
        return this;
    }

    public DisplayImageOptions cacheDirName(String cacheDirName) {
        this.cacheDirName = cacheDirName;
        return this;
    }
}
