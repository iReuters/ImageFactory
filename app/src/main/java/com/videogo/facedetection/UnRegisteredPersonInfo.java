package com.videogo.facedetection;

import com.arcsoft.face.FaceFeature;

public class UnRegisteredPersonInfo {
    public int count;

    public int noOperationCount;

    public boolean isModified = false;

    public int getNoOperationCount() {
        return noOperationCount;
    }

    private FaceFeature faceFeature;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public FaceFeature getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(FaceFeature faceFeature) {
        this.faceFeature = faceFeature;
    }
}
