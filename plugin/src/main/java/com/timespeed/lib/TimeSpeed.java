package com.timespeed.lib;

import android.content.Context;
/* compiled from: TimeSpeed.kt */
public final class TimeSpeed {
    public static final TimeSpeed INSTANCE = new TimeSpeed();

    private final static native void speedClockTime(String str, float f);

    private final static native void speedUTCTime(String str, float f);

    public final static native long getRealClockTime();

    public final static native long getRealUTCTime();



    private TimeSpeed() {
    }

    public static void speedUTCTime(Context context, float f) {

        speedUTCTime(context.getApplicationInfo().nativeLibraryDir + "/libsubstrate.so", f);
    }

    public static void speedClockTime(Context context, float f) {

        speedClockTime(context.getApplicationInfo().nativeLibraryDir + "/libsubstrate.so", f);
    }
}