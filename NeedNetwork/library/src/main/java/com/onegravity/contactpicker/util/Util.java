package com.onegravity.contactpicker.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
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

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName == null ? phoneNumber : contactName;
    }
}
