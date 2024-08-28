package com.alek.peacebooster.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ImagePart implements Serializable {

    Bitmap partImage;
    boolean status;

    public ImagePart(Bitmap partImage, boolean status) {
        this.partImage = partImage;
        this.status = status;
    }

    public Bitmap getPartImage() {
        return partImage;
    }

    public void setPartImage(Bitmap partImage) {
        this.partImage = partImage;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
