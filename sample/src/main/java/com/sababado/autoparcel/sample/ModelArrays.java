package com.sababado.autoparcel.sample;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by rjszabo on 6/4/2014.
 */
public class ModelArrays implements Parcelable {

    @ParcelMe
    protected boolean[] flag;
    @ParcelMe
    protected boolean[] flagNull;
    @ParcelMe
    byte[] myByte;
    @ParcelMe
    char[] myChar;
    @ParcelMe
    short[] myShort;
    @ParcelMe
    protected int[] number;
    @ParcelMe
    long[] myLong;
    @ParcelMe
    public float[] floatNumber;
    @ParcelMe
    double[] myDouble;
    @ParcelMe
    String[] string;
    @ParcelMe
    WrapperModel[] wrapperModel;
    @ParcelMe
    PointF[] pointF;

    public ModelArrays() {
    }

    public ModelArrays(final boolean f) {
        flag = new boolean[]{false, true, true};
        flagNull = null;
        myByte = new byte[]{0x04, 0x54};
        myChar = new char[]{'h', 'e', 'l', 'l', 'o'};
        myShort = new short[]{7, 3, 4};
        number = new int[]{8, 2, 3};
        myLong = new long[]{98798798798798798L};
        floatNumber = new float[]{2.234f, 234.0f};
        myDouble = new double[]{567.876876876876876, 2.234234};
        string = new String[]{"Hello", "World!"};

        wrapperModel = new WrapperModel[2];
        for(int i=0; i<wrapperModel.length; i++) {
            wrapperModel[i] = new WrapperModel(f);
            wrapperModel[i].number = i;
            wrapperModel[i].myShort = (short)i;
        }
        pointF = new PointF[]{new PointF(1,1), new PointF(20, 24)};
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        AutoParcel.writeToParcel(this, dest, flags);
    }

    public static final Creator<ModelArrays> CREATOR = new Creator<ModelArrays>() {
        @Override
        public ModelArrays createFromParcel(Parcel source) {
            return AutoParcel.readFromParcel(new ModelArrays(), source);
        }

        @Override
        public ModelArrays[] newArray(int size) {
            return new ModelArrays[size];
        }
    };

    @Override
    public String toString() {
        return "ModelArrays{" +
                "flag=" + Arrays.toString(flag) +
                ", flagNull=" + Arrays.toString(flagNull) +
                ", myByte=" + Arrays.toString(myByte) +
                ", myChar=" + Arrays.toString(myChar) +
                ", myShort=" + Arrays.toString(myShort) +
                ", number=" + Arrays.toString(number) +
                ", myLong=" + Arrays.toString(myLong) +
                ", floatNumber=" + Arrays.toString(floatNumber) +
                ", myDouble=" + Arrays.toString(myDouble) +
                ", string=" + Arrays.toString(string) +
                ", wrapperModel=" + Arrays.toString(wrapperModel) +
                ", pointF=" + Arrays.toString(pointF) +
                '}';
    }
}
