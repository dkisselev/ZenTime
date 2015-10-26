package net.dkisselev.zentime;

import java.util.Arrays;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.setStaticIntField;
import static de.robv.android.xposed.XposedHelpers.setStaticObjectField;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Xposed implements IXposedHookLoadPackage {
    private XSharedPreferences prefs;
    private static final String LOG_TAG = "ZenTIme: ";

    private static String MORE_TIMES = "5,10,15,20,25,30,35,40,45,50,55,60,75,90,120,150,180,210,240,270,300,330,360,390,420,450,480,510,540,570,600";

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui"))
            return;

        prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID);

        // Start with an empty bucketString, if a value has been set by the user, apply that as the bucketstring
        String bucketString = "";

        switch (prefs.getString("pref_time_options", "")) {
            case "more":
                bucketString = MORE_TIMES;
                break;
            case "custom":
                bucketString = prefs.getString("pref_custom_times", "");
                break;
        }

        /*
         * Only hook the UI if the bucketString isn't empty. If it is, we couldn't resolve a valid
         * user-chosen value so the most logical course of action is to leave the default behavior
         */
        if (!bucketString.isEmpty()) {
            int[] newBuckets = stringToInts(bucketString);

            Class zenModeConfig = XposedHelpers.findClass("android.service.notification.ZenModeConfig", lpparam.classLoader);
            setStaticObjectField(zenModeConfig, "MINUTE_BUCKETS", (Object) newBuckets);
        }
    }


    /**
     * Converts a comma-separated list of numbers to an array of ints
     * Refresh the preferences.
     */
    private int[] stringToInts(String bucketString) {
        String[] strArray = bucketString.split("\\s*,\\s*");
        int[] intArray = new int[strArray.length];

        for(int i = 0; i < strArray.length; i++) {
            intArray[i] = Integer.parseInt(strArray[i]);
        }

        return intArray;
    }
}