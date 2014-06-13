package com.sababado.autoparcel.sample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rjszabo on 6/4/2014.
 */
public class WrapperModel implements Parcelable {

    @ParcelMe
    protected Boolean flag;
    @ParcelMe
    Byte myByte;
    @ParcelMe
    Character myChar;
    @ParcelMe
    Short myShort;
    @ParcelMe
    protected Integer number;
    @ParcelMe
    Long myLong;
    @ParcelMe
    public Float floatNumber;
    @ParcelMe
    Double myDouble;

    public WrapperModel() {
    }

    public WrapperModel(final boolean f) {
        flag = true;
        myByte = 0x4;
        myChar = 'h';
        myShort = 78;
        myDouble = 567.876876876876876;
    }

    // Lists

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        AutoParcel.writeToParcel(this, dest, flags);
    }

    public static final Creator<WrapperModel> CREATOR = new Creator<WrapperModel>() {
        @Override
        public WrapperModel createFromParcel(Parcel source) {
            return AutoParcel.readFromParcel(new WrapperModel(), source);
        }

        @Override
        public WrapperModel[] newArray(int size) {
            return new WrapperModel[size];
        }
    };

    @Override
    public String toString() {
        return "ModelWrapperTest{" +
                "flag=" + flag +
                ", myByte=" + myByte +
                ", myChar=" + myChar +
                ", myShort=" + myShort +
                ", number=" + number +
                ", myLong=" + myLong +
                ", floatNumber=" + floatNumber +
                ", myDouble=" + myDouble +
                '}';
    }
}
