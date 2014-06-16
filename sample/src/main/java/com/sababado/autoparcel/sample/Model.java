package com.sababado.autoparcel.sample;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rjszabo on 6/4/2014.
 */
public class Model implements Parcelable {

    @ParcelMe
    protected boolean flag;
    @ParcelMe
    byte myByte;
    @ParcelMe
    char myChar;
    @ParcelMe
    short myShort;
    @ParcelMe
    protected int number;
    @ParcelMe
    long myLong;
    @ParcelMe
    public float floatNumber;
    @ParcelMe
    double myDouble;
    @ParcelMe
    String nullString;
    @ParcelMe
    String string;
    @ParcelMe
    WrapperModel wrapperModel;
    @ParcelMe
    PointF pointF;
    @ParcelMe
    ModelArrays modelArrays;

    public Model() {
    }

    public Model(final boolean f) {
        flag = true;
        myByte = 0x4;
        myChar = 'h';
        myShort = 78;
        number = 345678;
        myLong = 98798798798798798L;
        floatNumber = 987.876f;
        myDouble = 567.876876876876876;
        string = "Hello World!";
        wrapperModel = new WrapperModel(f);
        pointF = new PointF(23,24);
        modelArrays = new ModelArrays(f);
    }

    // String
    // Wrapper primitives
    // Parcelable Object
    // Lists

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        AutoParcel.writeToParcel(this, dest, flags);
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel source) {
            return AutoParcel.readFromParcel(new Model(), source);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    @Override
    public String toString() {
        return "Model{" +
                "flag=" + flag +
                ", myByte=" + myByte +
                ", myChar=" + myChar +
                ", myShort=" + myShort +
                ", number=" + number +
                ", myLong=" + myLong +
                ", floatNumber=" + floatNumber +
                ", myDouble=" + myDouble +
                ", nullString='" + nullString + '\'' +
                ", string='" + string + '\'' +
                ", wrapperModel=" + wrapperModel +
                ", pointF=" + pointF +
                ", modelArrays=" + modelArrays +
                '}';
    }
}