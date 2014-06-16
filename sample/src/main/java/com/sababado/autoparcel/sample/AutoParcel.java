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
    /**
     * Constant to denote when a field is null.
     */
    public static final int IS_NULL = 0x01;
    /**
     * Constant to denote when a field is not null.
     */
    public static final int IS_NOT_NULL = 0x00;

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
                            // End of "isPrimitive"
                        } else {
                            final Object object = field.get(parcelable);
                            if (object == null) {
                                dest.writeInt(IS_NULL);
                            } else if (cls.isArray()) {
                                writePrimitiveArrayToParcel(cls, dest, object);
                            } // End of "primitive array"
                            else {
                                if (String.class.equals(cls)) {
                                    dest.writeInt(IS_NOT_NULL);
                                    dest.writeString((String) object);
                                } else if (Boolean.class.equals(cls)) {
                                    dest.writeInt(IS_NOT_NULL);
                                    dest.writeByte((byte) ((Boolean) object ? 0x01 : 0x00));
                                } else if (Byte.class.equals(cls)) {
                                    dest.writeInt(IS_NOT_NULL);
                                    dest.writeByte((Byte) object);
                                } else if (Character.class.equals(cls)) {
                                    dest.writeInt(IS_NOT_NULL);
                                    dest.writeInt((int) ((Character) object));
                                } else if (Short.class.equals(cls)) {
                                    dest.writeInt(IS_NOT_NULL);
                                    writeShort(dest, (Short) object);
                                } else if (Integer.class.equals(cls)) {
                                    dest.writeInt(IS_NOT_NULL);
                                    dest.writeInt((Integer) object);
                                } else if (Long.class.equals(cls)) {
                                    dest.writeInt(IS_NOT_NULL);
                                    dest.writeLong((Long) object);
                                } else if (Float.class.equals(cls)) {
                                    dest.writeInt(IS_NOT_NULL);
                                    dest.writeFloat((Float) object);
                                } else if (Double.class.equals(cls)) {
                                    dest.writeInt(IS_NOT_NULL);
                                    dest.writeDouble((Double) object);
                                } else if (object instanceof Parcelable) {
                                    dest.writeInt(IS_NOT_NULL);
                                    dest.writeParcelable((Parcelable) object, flags);
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
                            // End of "isPrimitive"
                        } else {
                            if (source.readInt() == IS_NOT_NULL) {
                                if (cls.isArray()) {
                                    readPrimitiveArrayFromParcel(cls, source, field, parcelable);
                                } // End of "primitive array"
                                else {
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
                                    if (object != null) {
                                        field.set(parcelable, object);
                                    }
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

    private static final void writePrimitiveArrayToParcel(final Class cls, final Parcel dest, final Object value) {
        if (boolean[].class == cls) {
            dest.writeInt(IS_NOT_NULL);
            final boolean arr[] = (boolean[]) value;
            dest.writeInt(arr.length);
            dest.writeBooleanArray(arr);
        } else if (byte[].class == cls) {
            dest.writeInt(IS_NOT_NULL);
            final byte arr[] = (byte[]) value;
            dest.writeInt(arr.length);
            dest.writeByteArray(arr);
        } else if (char[].class == cls) {
            dest.writeInt(IS_NOT_NULL);
            final char arr[] = (char[]) value;
            dest.writeInt(arr.length);
            dest.writeCharArray(arr);
        } else if (short[].class == cls) {
            dest.writeInt(IS_NOT_NULL);
            final short arr[] = (short[]) value;
            dest.writeInt(arr.length);
            for(final short s : arr) {
                writeShort(dest, s);
            }
        } else if (int[].class == cls) {
            dest.writeInt(IS_NOT_NULL);
            final int arr[] = (int[]) value;
            dest.writeInt(arr.length);
            dest.writeIntArray(arr);
        } else if (long[].class == cls) {
            dest.writeInt(IS_NOT_NULL);
            final long arr[] = (long[]) value;
            dest.writeInt(arr.length);
            dest.writeLongArray(arr);
        } else if (float[].class == cls) {
            dest.writeInt(IS_NOT_NULL);
            final float arr[] = (float[]) value;
            dest.writeInt(arr.length);
            dest.writeFloatArray(arr);
        } else if (double[].class == cls) {
            dest.writeInt(IS_NOT_NULL);
            final double arr[] = (double[]) value;
            dest.writeInt(arr.length);
            dest.writeDoubleArray(arr);
        }
    }

    private static final void readPrimitiveArrayFromParcel(final Class cls, final Parcel source, final Field field, final Parcelable parcelable) throws IllegalAccessException{
        if (boolean[].class == cls) {
            final boolean[] arr = new boolean[source.readInt()];
            source.readBooleanArray(arr);
            field.set(parcelable, arr);
        } else if (byte[].class == cls) {
            final byte[] arr = new byte[source.readInt()];
            source.readByteArray(arr);
            field.set(parcelable, arr);
        } else if (char[].class == cls) {
            final char[] arr = new char[source.readInt()];
            source.readCharArray(arr);
            field.set(parcelable, arr);
        } else if (short[].class == cls) {
            final short[] arr = new short[source.readInt()];
            for(int i=0; i<arr.length; i++) {
                arr[i] = readShort(source);
            }
            field.set(parcelable, arr);
        } else if (int[].class == cls) {
            final int[] arr = new int[source.readInt()];
            source.readIntArray(arr);
            field.set(parcelable, arr);
        } else if (long[].class == cls) {
            final long[] arr = new long[source.readInt()];
            source.readLongArray(arr);
            field.set(parcelable, arr);
        } else if (float[].class == cls) {
            final float[] arr = new float[source.readInt()];
            source.readFloatArray(arr);
            field.set(parcelable, arr);
        } else if (double[].class == cls) {
            final double[] arr = new double[source.readInt()];
            source.readDoubleArray(arr);
            field.set(parcelable, arr);
        }
    }

    /**
     * Write a short value to a parcel object. Read the value back using {@link #readShort(android.os.Parcel)}.
     *
     * @param dest  Destination
     * @param value Short value to write
     */
    public static final void writeShort(final Parcel dest, final short value) {
        dest.writeByte((byte) value);
        dest.writeByte((byte) ((value >> 8) & 0xff));
    }

    /**
     * Read a short value from a parcel object. This assumes the value was written using {@link #writeShort(android.os.Parcel, short)}.
     *
     * @param source Source to get the value from.
     * @return Returns the short value.
     */
    public static final short readShort(final Parcel source) {
        final byte byte1 = source.readByte();
        final byte byte2 = source.readByte();
        return (short) ((byte2 << 8) + (byte1 & 0xFF));
    }
}
