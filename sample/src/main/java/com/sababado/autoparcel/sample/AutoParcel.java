package com.sababado.autoparcel.sample;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by rjszabo on 6/4/2014.
 */
public class AutoParcel {
    private static final String TAG = AutoParcel.class.getSimpleName();
    public static final byte IS_NULL = 0x01;
    public static final byte IS_NOT_NULL = 0x00;

    public static int describeContents(final Parcelable parcelable) {
        // TODO What is this even doing..?
        return 0;
    }

    public static void writeToParcel(final Parcelable parcelable, final Parcel dest, final int flags) {
        final Field fields[] = parcelable.getClass().getDeclaredFields();
        Annotation annotations[];
        for (final Field field : fields) {
            try {
                annotations = field.getAnnotations();
                for (final Annotation annotation : annotations) {
                    if (annotation instanceof ParcelMe) {
                        // ParcelMe annotation is present, get the value from this field and put it in the parcel
                        final Class cls = field.getType();
                        if (cls.isPrimitive()) {
                            if (Boolean.TYPE.equals(cls)) {
                                dest.writeByte((byte) (field.getBoolean(parcelable) ? 0x01 : 0x00));
                            } else if (Byte.TYPE.equals(cls)) {
                                dest.writeByte(field.getByte(parcelable));
                            } else if (Character.TYPE.equals(cls)) {
                                dest.writeInt((int) field.getChar(parcelable));
                            } else if (Short.TYPE.equals(cls)) {
                                final short value = field.getShort(parcelable);
                                writeShort(dest, value);
                            } else if (Integer.TYPE.equals(cls)) {
                                dest.writeInt(field.getInt(parcelable));
                            } else if (Long.TYPE.equals(cls)) {
                                dest.writeLong(field.getLong(parcelable));
                            } else if (Float.TYPE.equals(cls)) {
                                dest.writeFloat(field.getFloat(parcelable));
                            } else if (Double.TYPE.equals(cls)) {
                                dest.writeDouble(field.getDouble(parcelable));
                            } else {
                                Log.w(TAG, "Field type for " + field.getName() + " not supported and won't be written to the parcel.");
                            }
                        } else {
                            final Object object = field.get(parcelable);
                            if (object == null) {
                                dest.writeByte(IS_NULL);
                            } else {
                                dest.writeByte(IS_NOT_NULL);
                                if (String.class.equals(cls)) {
                                    dest.writeString((String) object);
                                } else if (Boolean.class.equals(cls)) {
                                    dest.writeByte((byte)((Boolean)object ? 0x01 : 0x00));
                                } else if (Byte.class.equals(cls)) {
                                    dest.writeByte((Byte)object);
                                } else if (Character.class.equals(cls)) {
                                    dest.writeInt((int)((Character)object));
                                } else if (Short.class.equals(cls)) {
                                    writeShort(dest, (Short)object);
                                } else if (Integer.class.equals(cls)) {
                                    dest.writeInt((Integer)object);
                                } else if (Long.class.equals(cls)) {
                                    dest.writeLong((Long)object);
                                } else if (Float.class.equals(cls)) {
                                    dest.writeFloat((Float)object);
                                } else if (Double.class.equals(cls)) {
                                    dest.writeDouble((Double)object);
                                } else if (object instanceof Parcelable) {
                                    dest.writeParcelable((Parcelable)object, flags);
                                } else {
                                    Log.w(TAG, "Field type for " + field.getName() + " not supported and won't be written to the parcel.");
                                }
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T extends Parcelable> T readFromParcel(final T parcelable, final Parcel source) {
        final Field fields[] = parcelable.getClass().getDeclaredFields();
        Annotation annotations[];
        for (final Field field : fields) {
            try {
                annotations = field.getAnnotations();
                for (final Annotation annotation : annotations) {
                    if (annotation instanceof ParcelMe) {
                        // ParcelMe annotation is present, get the value from this field and put it in the parcel
                        final Class cls = field.getType();
                        if (cls.isPrimitive()) {
                            if (Boolean.TYPE.equals(cls)) {
                                field.setBoolean(parcelable, source.readByte() == 0x01);
                            } else if (Byte.TYPE.equals(cls)) {
                                field.setByte(parcelable, source.readByte());
                            } else if (Character.TYPE.equals(cls)) {
                                field.setChar(parcelable, (char) source.readInt());
                            } else if (Short.TYPE.equals(cls)) {
                                field.setShort(parcelable, readShort(source));
                            } else if (Integer.TYPE.equals(cls)) {
                                field.setInt(parcelable, source.readInt());
                            } else if (Long.TYPE.equals(cls)) {
                                field.setLong(parcelable, source.readLong());
                            } else if (Float.TYPE.equals(cls)) {
                                field.setFloat(parcelable, source.readFloat());
                            } else if (Double.TYPE.equals(cls)) {
                                field.setDouble(parcelable, source.readDouble());
                            }
                        } else {
                            if (source.readByte() == IS_NOT_NULL) {
                                Object object = null;
                                if (String.class.equals(cls)) {
                                    object = source.readString();
                                } else if (Boolean.class.equals(cls)) {
                                    object = source.readByte() == 0x01;
                                } else if (Byte.class.equals(cls)) {
                                    object = source.readByte();
                                } else if (Character.class.equals(cls)) {
                                    object = (char) source.readInt();
                                } else if (Short.class.equals(cls)) {
                                    object = readShort(source);
                                } else if (Integer.class.equals(cls)) {
                                    object = source.readInt();
                                } else if (Long.class.equals(cls)) {
                                    object = source.readLong();
                                } else if (Float.class.equals(cls)) {
                                    object = source.readFloat();
                                } else if (Double.class.equals(cls)) {
                                    object = source.readDouble();
                                } else if (Parcelable.class.isAssignableFrom(cls)) {
                                    object = source.readParcelable(cls.getClassLoader());
                                }
                                if(object != null) {
                                    field.set(parcelable, object);
                                }
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return parcelable;
    }

    /**
     * Write a short value to a parcel object. Read the value back using {@link #readShort(android.os.Parcel)}.
     * @param dest Destination
     * @param value Short value to write
     */
    public static final void writeShort(final Parcel dest, final short value) {
        dest.writeByte((byte) value);
        dest.writeByte((byte) ((value >> 8) & 0xff));
    }

    /**
     * Read a short value from a parcel object. This assumes the value was written using {@link #writeShort(android.os.Parcel, short)}.
     * @param source Source to get the value from.
     * @return Returns the short value.
     */
    public static final short readShort(final Parcel source) {
        final byte byte1 = source.readByte();
        final byte byte2 = source.readByte();
        return (short) ((byte2 << 8) + (byte1 & 0xFF));
    }
}
