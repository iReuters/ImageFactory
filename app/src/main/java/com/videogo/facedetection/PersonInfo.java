package com.videogo.facedetection;

import android.graphics.Bitmap;

import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;

import java.io.Serializable;

public class PersonInfo implements Serializable {
    private String id;

    private String name;

    private int gender;

    private int age;

    private FaceInfo faceInfo;

    private FaceFeature faceFeature;


    public FaceFeature getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(FaceFeature faceFeature) {
        this.faceFeature = faceFeature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }


}
