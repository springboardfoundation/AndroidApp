package com.onegravity.contactpicker.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.Arrays;
import java.util.Collection;

public class Util {

    public static String join(String[] list, String delimiter) {
        return join(Arrays.asList(list), delimiter);
    }

    public static String join(Collection<String> list, String delimiter) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        for (String item : list) {
            result.append(item);

            if (++i < list.size())
                result.append(delimiter);
        }

        return result.toString();
    }

    public static String join(long[] list, String delimeter) {
        StringBuilder sb = new StringBuilder();

        for (int j=0;j<list.length;j++) {
            if (j != 0) sb.append(delimeter);
            sb.append(list[j]);
        }

        return sb.toString();
    }

    public static String getSimCountryIso(Context context) {
        String simCountryIso = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso();
        return simCountryIso != null ? simCountryIso.toUpperCase() : "US";
    }
}
